package Listener;

import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.PsiComment;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.editor.event.EditorMouseListener;
import com.intellij.openapi.editor.event.EditorMouseMotionListener;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.ex.util.EditorUtil;

import java.awt.event.MouseEvent;

public class CursorCommentDetector implements CaretListener, EditorMouseListener, EditorMouseMotionListener {

    private boolean isCursorInContentArea = false;
    static VimModeChecker vimModeChecker;
    @Override
    public void caretPositionChanged(@NotNull CaretEvent e) {
        checkAndPrint(e.getEditor());
    }

    // 在 CursorCommentDetector.java 中添加这个静态方法
    public static void installGlobalMouseListener(Project project,Editor editor) {
        System.out.println("CursorCommentDetector!!!!!");
        GlobalMouseTracker.installFor(project);
        vimModeChecker = new VimModeChecker(editor);

    }


    @Override
    public void mouseMoved(EditorMouseEvent event) {
        Editor editor = event.getEditor();
        MouseEvent awtEvent = event.getMouseEvent();

        // 判断是否在内容区域内
        boolean inContentArea = ((EditorEx) editor).getContentComponent().contains(awtEvent.getX(), awtEvent.getY());

        if (inContentArea != isCursorInContentArea) {
            isCursorInContentArea = inContentArea;
            if (!isCursorInContentArea) {
                System.out.println("Cursor is in the editor but outside of code area.");
            }
        }

        checkAndPrint(editor);
    }

    @Override
    public void mouseEntered(EditorMouseEvent event) {
        isCursorInContentArea = true;
        System.out.println("Cursor entered the editor content area.");
    }

    @Override
    public void mouseExited(EditorMouseEvent event) {
        isCursorInContentArea = false;
        System.out.println("Cursor exited the editor content area.");
    }

    private void checkAndPrint(Editor editor) {
        Project project = editor.getProject();
        if (project == null) return;

        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
        if (psiFile == null) return;

        LogicalPosition logicalPosition = editor.getCaretModel().getLogicalPosition();
        int offset = editor.logicalPositionToOffset(logicalPosition);
        PsiElement elementAtCaret = psiFile.findElementAt(offset);

        String result;
        System.out.println("现在是vim模式吗"+vimModeChecker.isInsertMode());
        if (!isCursorInContentArea) {

            if(GlobalMouseTracker.isMouseInIdeaWindow()){
                result = "Cursor is in the editor but outside of code area.";
            }
            else {
                result = "Cursor is outside";
            }
//            result = "Cursor is in the editor but outside of code area.";
        } else if (elementAtCaret == null || PsiTreeUtil.getParentOfType(elementAtCaret, PsiComment.class) != null) {
            result = "Cursor is in a comment.";
        } else {
            result = "Cursor is in code.";
        }

        System.out.println(result);
    }

    // 必须实现的 EditorMouseListener 接口方法（可以留空）
    @Override
    public void mouseClicked(EditorMouseEvent event) {
    }

    @Override
    public void mousePressed(EditorMouseEvent event) {
    }

    @Override
    public void mouseReleased(EditorMouseEvent event) {
    }
}
