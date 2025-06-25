package Listener.test;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.util.ui.UIUtil;
import com.intellij.util.messages.MessageBusConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class EditorFocusTracker {
    
    // 用于存储每个项目的监听器连接，避免重复注册
    private static final Map<Project, MessageBusConnection> projectConnections = new ConcurrentHashMap<>();
    
    // 静态工具方法：检查当前焦点是否在编辑器内
    public static boolean isFocusInsideEditor(Project project) {
        if (project == null || project.isDisposed()) {
            return false;
        }
        
        Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        if (focusOwner == null) return false;
        
        Editor currentEditor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (currentEditor == null) return false;
        
        JComponent editorComponent = ((EditorEx) currentEditor).getContentComponent();
        return UIUtil.isDescendingFrom(focusOwner, editorComponent);
    }

    // 注册焦点变化监听器 - 改进版本
    public static void addFocusListener(Project project, Consumer<Boolean> onFocusChanged) {
        if (project == null || project.isDisposed()) {
            return;
        }
        
        // 避免重复注册
        if (projectConnections.containsKey(project)) {
            return;
        }
        
        MessageBusConnection connection = project.getMessageBus().connect();
        projectConnections.put(project, connection);
        
        // 监听编辑器切换事件
        connection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new FileEditorManagerListener() {
            @Override
            public void selectionChanged(FileEditorManagerEvent event) {
                // 当编辑器切换时，重新为新编辑器添加焦点监听
                addFocusListenerToCurrentEditor(project, onFocusChanged);
            }
        });
        
        // 为当前编辑器添加焦点监听
        addFocusListenerToCurrentEditor(project, onFocusChanged);
        
        // 当项目关闭时清理连接
        Disposer.register(project, () -> {
            connection.disconnect();
            projectConnections.remove(project);
        });
    }
    
    // 为当前编辑器添加焦点监听器
    private static void addFocusListenerToCurrentEditor(Project project, Consumer<Boolean> onFocusChanged) {
        if (project.isDisposed()) return;
        
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor instanceof EditorEx) {
            JComponent contentComponent = ((EditorEx) editor).getContentComponent();
            
            // 移除之前的监听器（如果有的话）
            FocusListener[] existingListeners = contentComponent.getFocusListeners();
            for (FocusListener listener : existingListeners) {
                if (listener instanceof EditorFocusListener) {
                    contentComponent.removeFocusListener(listener);
                }
            }
            
            // 添加新的焦点监听器
            contentComponent.addFocusListener(new EditorFocusListener(onFocusChanged));
        }
    }
    
    // 自定义焦点监听器类，便于识别和管理
    private static class EditorFocusListener extends FocusAdapter {
        private final Consumer<Boolean> onFocusChanged;
        
        public EditorFocusListener(Consumer<Boolean> onFocusChanged) {
            this.onFocusChanged = onFocusChanged;
        }
        
        @Override
        public void focusGained(FocusEvent e) {
            System.out.println("Focus在编辑器内");
            onFocusChanged.accept(true);
        }

        @Override
        public void focusLost(FocusEvent e) {
            System.out.println("Focus在编辑器外");
            onFocusChanged.accept(false);
        }
    }
    
    // 清理指定项目的监听器
    public static void removeFocusListener(Project project) {
        MessageBusConnection connection = projectConnections.remove(project);
        if (connection != null) {
            connection.disconnect();
        }
    }
}