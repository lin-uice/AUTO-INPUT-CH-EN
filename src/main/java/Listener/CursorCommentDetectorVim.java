package Listener;

import ENUM.CursorState;
import InputMethod.InputMethodChecker;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
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

import java.awt.*;
import java.awt.event.MouseEvent;

public class CursorCommentDetectorVim extends GlobalMouseTracker implements CaretListener, EditorMouseListener, EditorMouseMotionListener, ModeChangeListener {
    //
    CursorState cursorState = CursorState.OUTEDITOR;
    private Logger LOG = Logger.getInstance(CursorCommentDetectorVim.class);
    private boolean isCursorInContentArea = false;
    static VimModeChecker vimModeChecker;
    static InputMethodChecker inputMethodChecker;

    static {
        inputMethodChecker = new InputMethodChecker();
    }

    public CursorCommentDetectorVim(Project project) {
        super(project);
        WindowsListener();
    }

    @Override
    public void WindowsListener() {
        if (ideaWindow == null) {
            System.err.println("❌ GlobalMouseTracker: Failed to get IDEA window.");
            return;
        }

//        System.out.println("✅ GlobalMouseTracker: Successfully registered for window: " + ideaWindow.getName());

        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
//            System.out.println("💡 捕获到 AWT 事件: " + event);

            if (event instanceof MouseEvent mouseEvent) {
                Point mousePoint = MouseInfo.getPointerInfo().getLocation();
                Rectangle windowBounds = ideaWindow.getBounds();
                boolean isInsideIdeaWindow = windowBounds.contains(mousePoint);
//                checkAndPrint();
//                System.out.println("🖱️ 鼠标位置: " + mousePoint);
//                System.out.println("📐 IDEA 窗口范围: " + windowBounds);
                //进入,改为英文.退出,改为中文.
                CursorState newCursorState = CursorState.INCODE;
                if (mouseEvent.getID() == MouseEvent.MOUSE_EXITED) {
                    isMouseInIdeaWindow = false;//判断是否在idea内
                    newCursorState = CursorState.OUTIDE;
                    System.out.println("【全局】鼠标已完全离开 IntelliJ IDEA 窗口");
                } else if (mouseEvent.getID() == MouseEvent.MOUSE_ENTERED) {
                    isMouseInIdeaWindow = true;
                    newCursorState = CursorState.INCODE;
                    System.out.println("【全局】鼠标重新进入 IntelliJ IDEA 窗口");
                }
//                System.out.println("【全局】当前输入法状态为：" + newCursorState);
//                System.out.println("【全局】旧的输入法状态为：" + cursorState);
//                if (!newCursorState.equals(cursorState))
                if (!cursorState.getLanguage().equals(InputMethodChecker.GetMode())) {
//                        inputMethodChecker.pressShift();
                }
//                cursorState = newCursorState;


            }
        }, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
    }


    @Override
    public void caretPositionChanged(@NotNull CaretEvent e) {
//        System.out.println("CursorCommentDetector.caretPositionChanged");
        checkAndPrint(e.getEditor());
    }


    // 在 CursorCommentDetector.java 中添加这个静态方法
    public static void installGlobalMouseListener(Project project, Editor editor) {
        System.out.println("CursorCommentDetector!!!!!");
        GlobalMouseTracker.installFor(project);
        vimModeChecker = new VimModeChecker(editor);


    }


    @Override
    public void mouseMoved(EditorMouseEvent event) {

    }

    @Override
    public void mouseEntered(EditorMouseEvent event) {
//        isCursorInContentArea = true;
//        checkAndPrint(event.getEditor());
//        System.out.println("Cursor entered the editor content area.");
    }

    private void checkPrintMode() {

    }

    @Override
    public void mouseExited(EditorMouseEvent event) {
//        isCursorInContentArea = false;
//        checkAndPrint(event.getEditor());
//        System.out.println("Cursor exited the editor content area.");
    }


    public boolean isInComment(Editor editor) {
        Project project = editor.getProject();
        return false;
    }

    private void checkAndPrint(Editor editor) {

        String result;
        CursorState newCursorState;
//        System.out.println("【全局】鼠标已完全离开 IntelliJ IDEA 窗口");
        if (!isMouseInIdeaWindow) {
            newCursorState = CursorState.OUTIDE;
            result = "Cursor is in the editor but outside of code area.";
        } else {
//            System.out.println(CommentUtils.identifyCommentType(editor));
//            printCurrentLine(editor);
            CommentUtils.CommentType commentType = CommentUtils.identifyCommentType(editor);
            if (commentType != null) {
                newCursorState = CursorState.INCOMMENT;
//            result = "Cursor is in a comment.";
            } else {
                newCursorState = CursorState.INCODE;
//            result = "Cursor is in code.";
            }
        }
        System.out.println("旧模式" + cursorState);
        System.out.println("新模式" + newCursorState);
        System.out.println(InputMethodChecker.GetMode());
        System.out.println(cursorState.getCode());
        if (newCursorState.equals(cursorState)) {
            //不做任何操作

        } else {
//            System.out.println("旧模式" + cursorState);
            cursorState = newCursorState;

//            System.out.println("新模式" + newCursorState);

//            System.out.println(CursorState.);
            if (cursorState.getLanguage().equals(InputMethodChecker.GetMode())) {//如果状态相等,就不需要进行切换输入法.

            } else {
                InputMethodChecker.pressShift();
            }
        }
//        System.out.println(result);
    }

    // 测试实现
    public static void printCurrentLine(Editor editor) {
        if (editor == null) return;

        Document document = editor.getDocument();
        CaretModel caretModel = editor.getCaretModel();
        int offset = caretModel.getOffset();
//        caretModel.
        int lineNumber = document.getLineNumber(offset);

        int startOffset = document.getLineStartOffset(lineNumber);
        int endOffset = document.getLineEndOffset(lineNumber);

        String currentLine = document.getText(new TextRange(startOffset, endOffset));
    }


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


        Editor editor = IjVimEditorKt.getIj(vimEditor);

        checkAndPrint(editor);
    }
}
