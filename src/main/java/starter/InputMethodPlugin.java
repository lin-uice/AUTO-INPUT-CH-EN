package starter;

import listener.BaseInputMethodDetector;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;



//测试
public class InputMethodPlugin implements EditorFactoryListener {
    @Override
    public void editorCreated(@NotNull EditorFactoryEvent event) {

        try {
            Class.forName("com.maddyhome.idea.vim.VimPlugin");
        }
        catch (Exception e){
            System.out.println("现在是普通模式");
            StarterUtils starterUtils = new StarterUtils();

            Editor editor = event.getEditor();
            Project project = editor.getProject();
            System.out.println("现在无vim模式");
            BaseInputMethodDetector listener = new BaseInputMethodDetector();
            starterUtils.baseMethodFactory(editor, project, listener);
        }

    }


    @Override
    public void editorReleased(@NotNull EditorFactoryEvent event) {
    }

}