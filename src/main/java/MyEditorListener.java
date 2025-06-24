import Listener.CursorCommentDetector;
import Listener.CursorCommentDetectorVim;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.project.Project;
import com.maddyhome.idea.vim.VimPlugin;
import com.maddyhome.idea.vim.api.VimEditor;
import com.maddyhome.idea.vim.api.VimInjectorKt;
import com.maddyhome.idea.vim.common.ModeChangeListener;
import com.maddyhome.idea.vim.common.VimListenersNotifier;
import com.maddyhome.idea.vim.common.VimPluginListener;
import com.maddyhome.idea.vim.listener.VimListenerManager;
import com.maddyhome.idea.vim.newapi.IjVimInjectorKt;
import com.maddyhome.idea.vim.state.mode.Mode;
import org.jetbrains.annotations.NotNull;
import com.maddyhome.idea.vim.api.VimInjector;
//import com.maddyhome.idea.vim.listener.VimListenerManager.
import java.awt.*;

public class MyEditorListener implements EditorFactoryListener{
    private static boolean globalMouseListenerInstalled = false;

    @Override
    public void editorCreated(@NotNull EditorFactoryEvent event) {

        Editor editor = event.getEditor();
        Project project = editor.getProject();
//        VimModeChecker vimModeChecker = new VimModeChecker();
//        VimModeChecker vimModeChecker = new VimModeChecker(editor);
//       editor.getCaretModel().addCaretListener(new CursorCommentDetector(),project);
        try {
            Class.forName("com.maddyhome.idea.vim.VimPlugin");
            CursorCommentDetectorVim listener = new CursorCommentDetectorVim();
//        CursorCommentDetector.installGlobalMouseListener();
//        vimModeChecker.isInsertMode();
//        vimModeChecker.isInsertMode(editor);
            editor.getCaretModel().addCaretListener(listener, project);
            editor.addEditorMouseListener(listener, project);
            editor.addEditorMouseMotionListener(listener, project);
            IjVimInjectorKt.initInjector();
            VimInjector vimInjector = VimInjectorKt.getInjector();
//            VimListenerManager.INSTANCE.
            VimListenersNotifier listenersNotifier = vimInjector.getListenersNotifier();
            listenersNotifier.getModeChangeListeners().add(listener);
//            VimListenersNotifier listenersNotifier=injector.listenersNotifier;
//            listenersNotifier.getModeChangeListeners().add(listener);
//            listenersNotifier.notifyModeChanged(editor, Mode.NORMAL);


            if (!globalMouseListenerInstalled) {
                System.out.println("🔌 正在安装全局鼠标监听器...");
                CursorCommentDetectorVim.installGlobalMouseListener(project, editor);  // ✅ 调用静态方法
                globalMouseListenerInstalled = true;
            }
        } catch (ClassNotFoundException e) {
            CursorCommentDetector listener = new CursorCommentDetector();

            editor.getCaretModel().addCaretListener(listener, project);
            editor.addEditorMouseListener(listener, project);
            editor.addEditorMouseMotionListener(listener, project);
            if (!globalMouseListenerInstalled) {
                System.out.println("🔌 正在安装全局鼠标监听器...");
                CursorCommentDetector.installGlobalMouseListener(project, editor);  // ✅ 调用静态方法
                globalMouseListenerInstalled = true;
            }

        }
    }

    @Override
    public void editorReleased(@NotNull EditorFactoryEvent event) {
//        Editor editor = event.getEditor();
//        editor.getCaretModel().); // 可选清理
    }



    //注册VIM

}