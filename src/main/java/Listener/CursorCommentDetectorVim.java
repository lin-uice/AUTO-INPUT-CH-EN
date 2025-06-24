package Listener;

import ENUM.CursorState;
import InputMethod.InputMethodChecker;
import com.intellij.openapi.diagnostic.Logger;
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
import com.intellij.jna.JnaLoader;
import com.maddyhome.idea.vim.api.VimEditor;
import com.maddyhome.idea.vim.common.ModeChangeListener;
import com.maddyhome.idea.vim.listener.VimListenerManager;
import com.maddyhome.idea.vim.newapi.IjVimEditorKt;
import com.maddyhome.idea.vim.state.mode.Mode;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.editor.event.EditorMouseListener;
import com.intellij.openapi.editor.event.EditorMouseMotionListener;
import com.intellij.openapi.editor.event.EditorMouseEvent;
//import com.intellij.idea.vim.common

import java.awt.event.MouseEvent;

public class CursorCommentDetectorVim implements CaretListener, EditorMouseListener, EditorMouseMotionListener, ModeChangeListener {
    CursorState cursorState = CursorState.OUTEDITOR;
    private Logger LOG = Logger.getInstance(CursorCommentDetectorVim.class);
    private boolean isCursorInContentArea = false;
    static VimModeChecker vimModeChecker;
//    static InputMethodChecker inputMethodChecker;
//    static{
//        inputMethodChecker=new InputMethodChecker();
//    }
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
        CursorState newCursorState;
//        System.out.println("现在是插入吗"+vimModeChecker.isInsertMode());
        if (!isCursorInContentArea) {

            if(GlobalMouseTracker.isMouseInIdeaWindow()){
                newCursorState = CursorState.OUTIDE;
                result = "Cursor is in the editor but outside of code area.";
            }
            else {
                newCursorState=CursorState.OUTEDITOR;
                result = "Cursor is outside";
            }
//            result = "Cursor is in the editor but outside of code area.";
        } else if (elementAtCaret == null || PsiTreeUtil.getParentOfType(elementAtCaret, PsiComment.class) != null) {
            newCursorState = CursorState.INCOMMENT;
            result = "Cursor is in a comment.";
        } else {
            newCursorState = CursorState.INCODE;
            result = "Cursor is in code.";
        }
        if(newCursorState.equals(cursorState)){
            //不做任何操作
        }
        else {
            cursorState = newCursorState;
            if(cursorState.getLanguage().equals(InputMethodChecker.GetMode()))
            {//如果状态相等,就不需要进行切换输入法.
                 }
            else {
                InputMethodChecker.pressShift();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
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

    @Override
    public void modeChanged(@NotNull VimEditor vimEditor, @NotNull Mode mode) {
//        System.out.println("modeChanged"+"hhhhhhhhhh");
//        System.out.println("modeChanged"+"hhhhhhhhhh");
//        System.out.println("modeChanged"+"hhhhhhhhhh");
//        LOG.warn("VimModechanged: isInsertMode"+vimModeChecker.isInsertMode());


        Editor editor = IjVimEditorKt.getIj(vimEditor);

        checkAndPrint(editor);
    }
}
