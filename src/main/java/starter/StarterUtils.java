package starter;

import com.intellij.openapi.application.ApplicationActivationListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.maddyhome.idea.vim.api.VimInjector;
import com.maddyhome.idea.vim.api.VimInjectorKt;
import com.maddyhome.idea.vim.common.ModeChangeListener;
import com.maddyhome.idea.vim.common.VimListenersNotifier;
import com.maddyhome.idea.vim.newapi.IjVimInjectorKt;
import listener.BaseInputMethodDetector;
import listener.EditorFocusTracker;
import listener.VimInputMethodDetector;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author crl
 * @version 1.0
 * @description: TODO
 * @date 2025/7/30 17:18
 */
public class StarterUtils {
    private static volatile boolean vimInitialized = false;
    private static volatile boolean vimModeListenerRegistered = false;
    private static final Map<Project, BaseInputMethodDetector> baseDetectorsByProject = new ConcurrentHashMap<>();
    private static final Map<Project, VimInputMethodDetector> vimDetectorsByProject = new ConcurrentHashMap<>();
    private static final Set<Project> activationListenerRegisteredProjects = ConcurrentHashMap.newKeySet();

    public static BaseInputMethodDetector getBaseDetector(Project project) {
        if (project == null) {
            return new BaseInputMethodDetector();
        }
        return baseDetectorsByProject.computeIfAbsent(project, key -> {
            BaseInputMethodDetector detector = new BaseInputMethodDetector();
            Disposer.register(project, () -> baseDetectorsByProject.remove(project));
            return detector;
        });
    }

    public static VimInputMethodDetector getVimDetector(Project project) {
        if (project == null) {
            return new VimInputMethodDetector();
        }
        return vimDetectorsByProject.computeIfAbsent(project, key -> {
            VimInputMethodDetector detector = new VimInputMethodDetector();
            Disposer.register(project, () -> vimDetectorsByProject.remove(project));
            return detector;
        });
    }

    public void baseMethodFactory(Editor editor, Project project, BaseInputMethodDetector listener) {
        if (editor == null || project == null || project.isDisposed()) {
            return;
        }
        editor.getCaretModel().addCaretListener(listener);
        if (activationListenerRegisteredProjects.add(project)) {
            project.getMessageBus().connect(project).subscribe(ApplicationActivationListener.TOPIC, listener);
            Disposer.register(project, () -> activationListenerRegisteredProjects.remove(project));
        }
        EditorFocusTracker.addFocusListener(project, hasFocus -> {
            if (hasFocus) {
                BaseInputMethodDetector.OUTEDITOR = false;
                System.out.println("获得了注意"); // 比如启用光标监听这个时候,其实不进行操作
            } else {
                System.out.println("失去了注意");
                BaseInputMethodDetector.OUTEDITOR = true;
                listener.chekOutEditor(editor);
            }
        });
    }
    public void vimMethodFactory(ModeChangeListener listener) {
        // 避免重复添加相同的监听器
        try{

            // 只初始化一次Vim组件
            if (!vimInitialized) {
                System.out.println("初始化Vim组件");
                IjVimInjectorKt.initInjector();
                vimInitialized = true;
            }

            VimInjector vimInjector = VimInjectorKt.getInjector();
            if (vimInjector != null) {
                VimListenersNotifier listenersNotifier = vimInjector.getListenersNotifier();
                if (listenersNotifier != null) {
                    if (!vimModeListenerRegistered) {
                        synchronized (StarterUtils.class) {
                            if (!vimModeListenerRegistered) {
                                listenersNotifier.getModeChangeListeners().add(listener);
                                vimModeListenerRegistered = true;
                            }
                        }
                    }
//                registeredListeners.add(listener);
                    System.out.println("成功添加Vim模式变更监听器");
                }
            }}
        catch (Exception e){
            System.out.println( e);
        }
    }
}
