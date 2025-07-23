//import Listener.CursorCommentDetector;

import Listener.CursorCommentDetector;
import Listener.CursorCommentDetectorVim;
//import Listener.test.AutoFocusTracker;
import Listener.EditorFocusTracker;
import com.intellij.openapi.application.ApplicationActivationListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.project.Project;
import com.maddyhome.idea.vim.api.VimInjectorKt;
import com.maddyhome.idea.vim.common.VimListenersNotifier;
import com.maddyhome.idea.vim.newapi.IjVimInjectorKt;
import org.jetbrains.annotations.NotNull;
import com.maddyhome.idea.vim.api.VimInjector;
//import com.maddyhome.idea.vim.listener.VimListenerManager.


public class MyEditorListener implements EditorFactoryListener {
    private static boolean globalMouseListenerInstalled = false;

    @Override
    public void editorCreated(@NotNull EditorFactoryEvent event) {

        Editor editor = event.getEditor();
        Project project = editor.getProject();
//        VimModeChecker vimModeChecker = new VimModeChecker();
//        VimModeChecker vimModeChecker = new VimModeChecker(editor);
//       editor.getCaretModel().addCaretListener(new CursorCommentDetector(),project);
        try {
            //VIm
            Class.forName("com.maddyhome.idea.vim.VimPlugin");
            CursorCommentDetectorVim listener = new CursorCommentDetectorVim(project);
            IjVimInjectorKt.initInjector();
            VimInjector vimInjector = VimInjectorKt.getInjector();
//            VimListenerManager.INSTANCE.
            VimListenersNotifier listenersNotifier = vimInjector.getListenersNotifier();
            listenersNotifier.getModeChangeListeners().add(listener);

            if (!globalMouseListenerInstalled) {
                System.out.println("🔌 正在安装全局鼠标监听器...");
                CursorCommentDetectorVim.installGlobalMouseListener(project, editor);  // ✅ 调用静态方法
                globalMouseListenerInstalled = true;
            }
            editor.getCaretModel().addCaretListener(listener, project);
            ApplicationManager.getApplication().getMessageBus().connect().subscribe(ApplicationActivationListener.TOPIC,listener);
            System.out.println("Vim插件已经启用了!!");
            EditorFocusTracker.addFocusListener(project, hasFocus -> {
                if (hasFocus) {
                    CursorCommentDetectorVim.OUTEDITOR = false;
                     System.out.println("获得了注意"); // 比如启用光标监听这个时候,其实不进行操作
                } else {
                    System.out.println("失去了注意");
                    CursorCommentDetectorVim.OUTEDITOR = true;
                    listener.chekOutEditor();
                }
            });
        } catch (ClassNotFoundException e) {
            System.out.println("现在是无vim的模式");
            CursorCommentDetector listener = new CursorCommentDetector(project);
            if (!globalMouseListenerInstalled) {
                System.out.println("🔌 正在安装全局鼠标监听器...");
                CursorCommentDetector.installGlobalMouseListener(project, editor);  // ✅ 调用静态方法
                globalMouseListenerInstalled = true;
            }
            editor.getCaretModel().addCaretListener(listener, project);
            ApplicationManager.getApplication().getMessageBus().connect().subscribe(ApplicationActivationListener.TOPIC,listener);
            EditorFocusTracker.addFocusListener(project, hasFocus -> {
                if (hasFocus) {
                    CursorCommentDetector.OUTEDITOR = false;
                     System.out.println("获得了注意"); // 比如启用光标监听这个时候,其实不进行操作
                } else {
                    System.out.println("失去了注意");
                    CursorCommentDetector.OUTEDITOR = true;
                    listener.chekOutEditor();
                }
            });

        }
    }

    @Override
    public void editorReleased(@NotNull EditorFactoryEvent event) {
//        Editor editor = event.getEditor();
//        editor.getCaretModel().); // 可选清理
    }


    //注册VIM

}