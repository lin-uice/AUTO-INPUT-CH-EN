package listener;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiDocumentManager;
import com.maddyhome.idea.vim.api.VimEditor;
import com.maddyhome.idea.vim.common.ModeChangeListener;
import com.maddyhome.idea.vim.newapi.IjVimEditorKt;
import com.maddyhome.idea.vim.state.mode.Mode;
import enums.CursorState;
import inputmethod.InputMethodChecker;
import org.jetbrains.annotations.NotNull;
import utils.CommentUtils;


/**
 * @author crl
 * @version 1.0
 * @description: TODO
 * @date 2025/7/29 19:06
 */
public class VimInputMethodDetector extends BaseInputMethodDetector implements ModeChangeListener {

    @Override
    public void modeChanged(@NotNull VimEditor vimEditor, @NotNull Mode mode) {


        Editor editor = IjVimEditorKt.getIj(vimEditor);
        //如果当前不是插入模式,则改为插入模式
        mode = vimEditor.getMode();
        if (mode instanceof Mode.INSERT) {
            ISINSERT = true;
        } else {
            ISINSERT = false;
        }
        checkAndPrint(editor);
    }
    @Override
    protected void checkAndPrint(Editor editor) {
        ApplicationManager.getApplication().invokeLater(() -> {
            WriteCommandAction.runWriteCommandAction(editor.getProject(), () -> {
                PsiDocumentManager.getInstance(editor.getProject()).commitAllDocuments();
                check( editor);
            });

        }, ModalityState.defaultModalityState());
//        Project project = editor.getProject();
//        PsiDocumentManager manager = PsiDocumentManager.getInstance(project);
//        WriteCommandAction.runWriteCommandAction(project, () -> {
//            PsiDocumentManager.getInstance(project).commitAllDocuments();
//        });

    }
    protected void check(Editor editor){
        CursorState newCursorState;
        if (ISINSERT== false) {
            newCursorState = CursorState.INCODE;
        } else {
            boolean commentType = CommentUtils.isInComment(editor);
            if (commentType) {
                newCursorState = CursorState.INCOMMENT;
            } else {
                newCursorState = CursorState.INCODE;
            }
        }
        System.out.println("旧模式" + cursorState);
        System.out.println("新模式" + newCursorState);
        if (newCursorState.equals(cursorState)) {
            //不做任何操作
        } else {
            cursorState = newCursorState;
            if (cursorState.getLanguage().equals(InputMethodChecker.getCurrentMode())) {//如果状态相等,就不需要进行切换输入法.

            } else {
                InputMethodChecker.pressShift();
            }
        }
    }
}
