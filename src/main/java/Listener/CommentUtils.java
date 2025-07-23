package Listener;

import ENUM.CommentSyntax;
import com.intellij.lang.Language;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.resolve.reference.impl.manipulators.PsiCommentManipulator;
import com.jetbrains.rd.generator.nova.Lang;
import utils.CommentSyntaxMapper;

import java.util.HashMap;
import java.util.Map;

//import com.q.q.q.q.S;

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
        if (project == null) {
            System.out.println("project is null");
            return null;
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
            return null;
        }

        int offset = editor.getCaretModel().getOffset();
        PsiElement element = getElementAtOffset(psiFile, offset);
        if (element == null) {
//            System.out.println("element is null");
            return null;
        }

        // 检查当前元素是否是注释
        if (element instanceof PsiComment) {
//            System.out.println("Comment type: " + CommentType.LINE);
            return getCommentType((PsiComment) element);
        }
        if (offset > 0) {
            PsiElement prevElement = psiFile.findElementAt(offset - 1);
            if (prevElement instanceof PsiComment) {
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

    //判断是那种语言

//        // 有待优化,Switch有点不够优雅
//        switch (id) {
//            case "java":
//                return CommentSyntax.JAVA;
//            case "python":
//                return CommentSyntax.PYTHON;
//            case "javascript":
//                return CommentSyntax.JAVASCRIPT;
//            case "kotlin":
//                return CommentSyntax.KOTLIN;
//            case "yaml":
//                return CommentSyntax.YAML;
//            case "shell":
//                return CommentSyntax.SHELL;
//            // 如果还有其他语言，可以在这里扩展
//            default:
//                return null;
//        }
//    }

    /**
     * 判断注释的具体类型
     */
    private static CommentType getCommentType(PsiComment comment) {
        String text = comment.getText();
        Language language = comment.getLanguage();
        CommentSyntax syntax = CommentSyntaxMapper.getSyntaxForLanguage(language.getID());
        if (syntax == null) {
            return CommentType.UNKNOWN;
        }

        if (syntax.docStart != null && text.startsWith(syntax.docStart)) {
            return CommentType.DOCUMENTATION;
        } else if (syntax.linePrefix != null && text.startsWith(syntax.linePrefix)) {
            return CommentType.LINE;
        } else if (syntax.blockStart != null && text.startsWith(syntax.blockStart)) {
            return CommentType.BLOCK;
        } else {
            return CommentType.UNKNOWN;
        }
    }

    /**
     * 注释类型枚举
     */
    //测试
    //侧事故

    public enum CommentType {
        LINE,       // 行注释 //
        BLOCK,      // 块注释 /* */
        DOCUMENTATION, // 文档注释 /** */
        UNKNOWN     // 未知类型
    }
}
