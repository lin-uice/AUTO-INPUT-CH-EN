package Listener;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.q.q.q.q.S;

public class CommentUtils {
    /**
     * 根据编辑器和光标位置识别注释类型
     *
     * @param editor 当前编辑器
     * @return 注释类型枚举，如果不是注释则返回 null
     */
    public static CommentType identifyCommentType(Editor editor) {
//        System.out.println("Comment方法已经调用了!dsafdsfadsfdasf");
        if (editor == null) {
            System.out.println("editor is null");
            return null;
        }

        Project project = editor.getProject();
        if (project == null){
            System.out.println("project is null");
            return null;};


        PsiDocumentManager manager = PsiDocumentManager.getInstance(project);
        manager.commitAllDocuments(); // 强制提交所有文档变更

        PsiFile psiFile = manager.getPsiFile(editor.getDocument());








//        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
        if (psiFile == null) {
//            System.out.println("psiFile is null");
            return null;}

        int offset = editor.getCaretModel().getOffset();
        PsiElement element = getElementAtOffset(psiFile, offset);
        if (element == null) {
//            System.out.println("element is null");
            return null;}

        // 检查当前元素是否是注释
        if (element instanceof PsiComment) {
//            System.out.println("Comment type: " + CommentType.LINE);
            return getCommentType((PsiComment) element);
        }
        if(offset>0){
            PsiElement prevElement = psiFile.findElementAt(offset - 1);
            if(prevElement instanceof PsiComment){
//                System.out.println("Comment type: " + CommentType.LINE);
                return getCommentType((PsiComment) prevElement);
            }
        }
//        System.out.println("当前"+element.getText());
        // 检查父元素是否是注释
        PsiElement parent = element.getParent();
        while (parent != null) {
            if (parent instanceof PsiComment) {
                return getCommentType((PsiComment) parent);
            }
            parent = parent.getParent();
        }

//        System.out.println("当前什么都没有检查出来");
        return null;
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

    /**
     * 判断注释的具体类型
     */
    private static CommentType getCommentType(PsiComment comment) {
//        System.out.println("Checking comment type..."+comment.getText());
        if (comment instanceof PsiDocCommentBase) {
//            System.out.println("Comment type: " + CommentType.DOCUMENTATION);
            return CommentType.DOCUMENTATION;
        }

        String text = comment.getText();

        if (text.startsWith("//")) {
//            System.out.println("Comment type: " + CommentType.LINE);
            return CommentType.LINE;
        } else if (text.startsWith("/*") && !text.startsWith("/**")) {
            // 确保不是文档注释
//            System.out.println("Comment type: " + CommentType.BLOCK);
            return CommentType.BLOCK;
        }


        return null;
    }

    /**
     * 注释类型枚举
     */
    public enum CommentType {
        LINE,       // 行注释 //
        BLOCK,      // 块注释 /* */
        DOCUMENTATION // 文档注释 /** */
    }
}
