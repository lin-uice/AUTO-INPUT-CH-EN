import Listener.CursorCommentDetector;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class MyEditorListener implements EditorFactoryListener {

    @Override
    public void editorCreated(@NotNull EditorFactoryEvent event) {

        Editor editor = event.getEditor();
        Project project = editor.getProject();
//       editor.getCaretModel().addCaretListener(new CursorCommentDetector(),project);
        CursorCommentDetector listener = new CursorCommentDetector();

        editor.getCaretModel().addCaretListener(listener, project);
        editor.addEditorMouseListener(listener,project);
        editor.addEditorMouseMotionListener(listener,project);
    }

    @Override
    public void editorReleased(@NotNull EditorFactoryEvent event) {
//        Editor editor = event.getEditor();
//        editor.getCaretModel().); // 可选清理
    }
}