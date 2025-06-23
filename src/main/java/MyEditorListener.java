import Listener.CursorCommentDetector;
import Listener.VimModeChecker;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class MyEditorListener implements EditorFactoryListener {
    private static boolean globalMouseListenerInstalled = false;
    @Override
    public void editorCreated(@NotNull EditorFactoryEvent event) {

        Editor editor = event.getEditor();
        Project project = editor.getProject();
//        VimModeChecker vimModeChecker = new VimModeChecker();
//        VimModeChecker vimModeChecker = new VimModeChecker(editor);
//       editor.getCaretModel().addCaretListener(new CursorCommentDetector(),project);
        CursorCommentDetector listener = new CursorCommentDetector();
//        CursorCommentDetector.installGlobalMouseListener();
//        vimModeChecker.isInsertMode();
//        vimModeChecker.isInsertMode(editor);
        editor.getCaretModel().addCaretListener(listener, project);
        editor.addEditorMouseListener(listener,project);
        editor.addEditorMouseMotionListener(listener,project);
//        System.out.println("是插入模式吗"+vimModeChecker.isInsertMode(editor));

        if (!globalMouseListenerInstalled) {
            System.out.println("🔌 正在安装全局鼠标监听器...");
            CursorCommentDetector.installGlobalMouseListener(project,editor);  // ✅ 调用静态方法
            globalMouseListenerInstalled = true;
        }
//        vimModeChecker.isInsertMode(editor);
    }

    @Override
    public void editorReleased(@NotNull EditorFactoryEvent event) {
//        Editor editor = event.getEditor();
//        editor.getCaretModel().); // 可选清理
    }
}