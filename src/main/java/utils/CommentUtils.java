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
//        System.out.println("Comment方法已经调用了!dsafdsfadsfdasf");
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
//            PsiDocumentManager.getInstance(project).commitDocument(editor.getDocument());
            PsiDocumentManager.getInstance(project).commitAllDocuments();
        });
        //测


        PsiFile psiFile = manager.getPsiFile(editor.getDocument());


//        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
        if (psiFile == null) {
//            System.out.println("psiFile is null");
            return false;
        }

        int offset = editor.getCaretModel().getOffset();
        PsiElement element = getElementAtOffset(psiFile, offset);
        if (element == null) {
//            System.out.println("element is null");
            return false;
        }

        // 检查当前元素是否是注释
        if (element instanceof PsiComment) {
//            System.out.println("Comment type: " + CommentType.LINE);
            return true;
//            return getCommentType((PsiComment) element);
        }
        if (offset > 0) {
            PsiElement prevElement = psiFile.findElementAt(offset - 1);
            if (prevElement instanceof PsiComment) {
//                System.out.println("Comment type: " + CommentType.LINE);
                return true;
            }
        }
//        System.out.println("当前"+element.getText());
        // 检查父元素是否是注释
        PsiElement parent = element.getParent();
        while (parent != null) {
            if (parent instanceof PsiComment) {
                return true;
            }
            parent = parent.getParent();
        }

//        System.out.println("当前什么都没有检查出来");
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
