//import Listener.CursorCommentDetector;

import com.maddyhome.idea.vim.common.ModeChangeListener;
import listener.BaseInputMethodDetector;
import listener.VimInputMethodDetector;
//import Listener.test.AutoFocusTracker;
import listener.EditorFocusTracker;
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


public class InputMethodPlugin implements EditorFactoryListener {

    @Override
    public void editorCreated(@NotNull EditorFactoryEvent event) {

        Editor editor = event.getEditor();
        Project project = editor.getProject();
        try{
            Class.forName("com.maddyhome.idea.vim.VimPlugin");
            VimInputMethodDetector listener = new VimInputMethodDetector(editor);
            vimMethodFactory(listener);
            baseMethodFactory( editor,project,listener);
        }catch (ClassNotFoundException e) {
            BaseInputMethodDetector listener = new BaseInputMethodDetector(editor);
            baseMethodFactory( editor,project,listener);
        }
    }

    @Override
    public void editorReleased(@NotNull EditorFactoryEvent event) {
    }
    private void baseMethodFactory(Editor editor,Project project,BaseInputMethodDetector listener){
        editor.getCaretModel().addCaretListener(listener, project);
        ApplicationManager.getApplication().getMessageBus().connect().subscribe(ApplicationActivationListener.TOPIC,listener);
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
    private void vimMethodFactory(ModeChangeListener listener){
        IjVimInjectorKt.initInjector();
        VimInjector vimInjector = VimInjectorKt.getInjector();
        VimListenersNotifier listenersNotifier = vimInjector.getListenersNotifier();
        listenersNotifier.getModeChangeListeners().add(listener);
    }



}