package Listener;

import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.PsiComment;
import org.jetbrains.annotations.NotNull;

public class CursorCommentDetector implements CaretListener {

    @Override
    public void caretPositionChanged(@NotNull CaretEvent e) {
        Editor editor = e.getEditor();
        Project project = editor.getProject();
        if (project == null) return;

        LogicalPosition newPosition = e.getNewPosition(); // 获取新的光标逻辑位置
        int offset = editor.logicalPositionToOffset(newPosition); // 转换为 offset

        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
        if (psiFile == null) return;

        PsiElement element = psiFile.findElementAt(offset);
        if (element == null) return;

        String result;
        if (PsiTreeUtil.getParentOfType(element, PsiComment.class) != null) {
            result = "Cursor is in a comment.";
        } else {
            result = "Cursor is in code.";
        }

        System.out.println(result);
    }
}