package utils;


import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;

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
        if (project == null || project.isDisposed()) {
            return false;
        }
//        if (project == null) {
//            System.out.println("project is null");
//            return false;
//        }
//        ;
//
//
        PsiDocumentManager manager = PsiDocumentManager.getInstance(project);
//        WriteCommandAction.runWriteCommandAction(project, () -> {
//            PsiDocumentManager.getInstance(project).commitAllDocuments();
//        });


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

    public static boolean isInChineseString(Editor editor) {
        if (editor == null) {
            return false;
        }
        Project project = editor.getProject();
        if (project == null || project.isDisposed()) {
            return false;
        }
        PsiDocumentManager manager = PsiDocumentManager.getInstance(project);
        PsiFile psiFile = manager.getPsiFile(editor.getDocument());
        if (psiFile == null) {
            return false;
        }
        int offset = editor.getCaretModel().getOffset();
        PsiElement element = getElementAtOffset(psiFile, offset);
        if (element == null) {
            return false;
        }
        PsiElement current = element;
        for (int i = 0; i < 8 && current != null; i++) {
            String text = current.getText();
            if (looksLikeStringLiteral(current, text) && containsCjk(text)) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }

    private static boolean looksLikeStringLiteral(PsiElement element, String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        String trimmed = text.trim();
        if (trimmed.length() < 2) {
            return false;
        }
        char first = trimmed.charAt(0);
        char last = trimmed.charAt(trimmed.length() - 1);
        boolean hasQuotes = (first == '"' && last == '"') || (first == '\'' && last == '\'') || (first == '`' && last == '`');
        if (hasQuotes) {
            return true;
        }
        String name = element.getClass().getSimpleName();
        return name.contains("String") && name.contains("Literal");
    }

    private static boolean containsCjk(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        for (int i = 0; i < text.length(); i++) {
            Character.UnicodeScript script = Character.UnicodeScript.of(text.charAt(i));
            if (script == Character.UnicodeScript.HAN) {
                return true;
            }
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
