package starter;

import com.intellij.openapi.application.ApplicationActivationListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.maddyhome.idea.vim.api.VimInjector;
import com.maddyhome.idea.vim.api.VimInjectorKt;
import com.maddyhome.idea.vim.common.ModeChangeListener;
import com.maddyhome.idea.vim.common.VimListenersNotifier;
import com.maddyhome.idea.vim.newapi.IjVimInjectorKt;
import listener.BaseInputMethodDetector;
import listener.EditorFocusTracker;

/**
 * @author crl
 * @version 1.0
 * @description: TODO
 * @date 2025/7/30 17:18
 */
public class StarterUtils {
    boolean vimInitialized = false;
    public void baseMethodFactory(Editor editor, Project project, BaseInputMethodDetector listener) {
        editor.getCaretModel().addCaretListener(listener);
        ApplicationManager.getApplication().getMessageBus().connect().subscribe(ApplicationActivationListener.TOPIC, listener);
        EditorFocusTracker.addFocusListener(project, hasFocus -> {
            if (hasFocus) {
                BaseInputMethodDetector.OUTEDITOR = false;
                System.out.println("获得了注意"); // 比如启用光标监听这个时候,其实不进行操作
            } else {
                System.out.println("失去了注意");
                BaseInputMethodDetector.OUTEDITOR = true;
                listener.chekOutEditor();
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
                    listenersNotifier.getModeChangeListeners().add(listener);
//                registeredListeners.add(listener);
                    System.out.println("成功添加Vim模式变更监听器");
                }
            }}
        catch (Exception e){
            System.out.println( e);
        }
    }
}
