package utils;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;

//import com.q.q.q.q.S;

public class CommentUtils {
    /**
     * 根据编辑器和光标位置判断是否在注释中
     *
     * @param editor 当前编辑器
     * @return 如果是注释,则返回true.如果不是注释则返回 false
     */
    public static boolean isInComment(Editor editor) {
        if (editor == null) {
            System.out.println("editor is null");
            return false;
        }

        Project project = editor.getProject();
        if (project == null) {
            System.out.println("project is null");
            return false;
        }
        ;


        PsiDocumentManager manager = PsiDocumentManager.getInstance(project);
        WriteCommandAction.runWriteCommandAction(project, () -> {
            PsiDocumentManager.getInstance(project).commitAllDocuments();
        });


        PsiFile psiFile = manager.getPsiFile(editor.getDocument());

        if (psiFile == null) {
            return false;
        }

        int offset = editor.getCaretModel().getOffset();
        PsiElement element = getElementAtOffset(psiFile, offset);
        if (element == null) {
            return false;
        }

        // 检查当前元素是否是注释
        if (element instanceof PsiComment) {
            return true;
        }
        if (offset > 0) {
            PsiElement prevElement = psiFile.findElementAt(offset - 1);
            if (prevElement instanceof PsiComment) {
                return true;
            }
        }
        PsiElement parent = element.getParent();
        while (parent != null) {
            if (parent instanceof PsiComment) {
                return true;
            }
            parent = parent.getParent();
        }

        return false;
    }

    /**
     * 获取指定偏移量处的元素，处理边界情况
     */
    private static PsiElement getElementAtOffset(PsiFile file, int offset) {
        if (file == null || offset < 0 || offset > file.getTextLength()) {
//            System.out.println("Invalid offset: " + offset);
            return null;
        }

        PsiElement element = file.findElementAt(offset);
        if (element == null && offset > 0) {
            element = file.findElementAt(offset - 1);
        }

        return element;
    }




}
