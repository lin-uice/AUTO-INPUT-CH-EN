package starter;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.project.Project;
import listener.BaseInputMethodDetector;
import listener.VimInputMethodDetector;
import org.jetbrains.annotations.NotNull;


//测试
public class VimInputMethodPlugin implements EditorFactoryListener {
    public VimInputMethodPlugin() {
        System.out.println("VimInputMethodPlugin");
    }
    @Override
    public void editorCreated(@NotNull EditorFactoryEvent event) {
        System.out.println("现在是vim模式");
        StarterUtils starterUtils = new StarterUtils();

        Editor editor = event.getEditor();
        Project project = editor.getProject();

        VimInputMethodDetector listener = new VimInputMethodDetector();
        starterUtils.vimMethodFactory(listener);
        starterUtils.baseMethodFactory(editor, project, listener);

    }


    @Override
    public void editorReleased(@NotNull EditorFactoryEvent event) {
    }




}