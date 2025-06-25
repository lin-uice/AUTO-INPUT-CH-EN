package Listener;

import ENUM.CursorState;
import InputMethod.InputMethodChecker;
import Listener.test.EditorFocusTracker;
import com.intellij.openapi.application.ApplicationActivationListener;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.wm.IdeFrame;
import com.maddyhome.idea.vim.api.VimEditor;
import com.maddyhome.idea.vim.common.ModeChangeListener;
import com.maddyhome.idea.vim.newapi.IjVimEditorKt;
import com.maddyhome.idea.vim.state.mode.Mode;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.editor.event.EditorMouseListener;
import com.intellij.openapi.editor.event.EditorMouseMotionListener;
import com.intellij.openapi.editor.event.EditorMouseEvent;
//import com.intellij.idea.vim.common


public class CursorCommentDetectorVim extends GlobalMouseTracker implements CaretListener, EditorMouseListener, EditorMouseMotionListener, ModeChangeListener, ApplicationActivationListener {
    //
    static CursorState cursorState = CursorState.INCODE;
    private Logger LOG = Logger.getInstance(CursorCommentDetectorVim.class);
    private boolean isCursorInContentArea = false;
    static VimModeChecker vimModeChecker;
    static InputMethodChecker inputMethodChecker;
    public static EditorFocusTracker editorFocusTracker = new EditorFocusTracker();

    static {
        inputMethodChecker = new InputMethodChecker();
    }

    public CursorCommentDetectorVim(Project project) {
        super(project);
//        WindowsListener();
    }


//    @Override
//    public void WindowsListener() {
//        if (ideaWindow == null) {
//            System.err.println("❌ GlobalMouseTracker: Failed to get IDEA window.");
//            return;
//        }
//
////        System.out.println("✅ GlobalMouseTracker: Successfully registered for window: " + ideaWindow.getName());
//
//        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
////            System.out.println("💡 捕获到 AWT 事件: " + event);
//
//            if (event instanceof MouseEvent mouseEvent) {
//                Point mousePoint = MouseInfo.getPointerInfo().getLocation();
//                Rectangle windowBounds = ideaWindow.getBounds();
//                boolean isInsideIdeaWindow = windowBounds.contains(mousePoint);
////                checkAndPrint();
////                System.out.println("🖱️ 鼠标位置: " + mousePoint);
////                System.out.println("📐 IDEA 窗口范围: " + windowBounds);
//                //进入,改为英文.退出,改为中文.
//                CursorState newCursorState = CursorState.INCODE;
//                if (mouseEvent.getID() == MouseEvent.MOUSE_EXITED) {
//                    isMouseInIdeaWindow = false;//判断是否在idea内
//                    newCursorState = CursorState.OUTIDE;
//                    System.out.println("【全局】鼠标已完全离开 IntelliJ IDEA 窗口");
//                } else if (mouseEvent.getID() == MouseEvent.MOUSE_ENTERED) {
//                    isMouseInIdeaWindow = true;
//                    newCursorState = CursorState.INCODE;
//                    System.out.println("【全局】鼠标重新进入 IntelliJ IDEA 窗口");
//                }
////                System.out.println("【全局】当前输入法状态为：" + newCursorState);
////                System.out.println("【全局】旧的输入法状态为：" + cursorState);
////                if (!newCursorState.equals(cursorState))
//                if (!cursorState.getLanguage().equals(InputMethodChecker.GetMode())) {
////                        inputMethodChecker.pressShift();
//                }

    /// /                cursorState = newCursorState;
//
//
//            }
//        }, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
//    }
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
        cursorState = CursorState.INCODE;
//        CursorState newCursorState;
        CommentUtils.CommentType commentType = CommentUtils.identifyCommentType(editor);
        if (commentType != null) {
            cursorState = CursorState.INCOMMENT;
//            result = "Cursor is in a comment.";
        } else {
            cursorState = CursorState.INCODE;
//            result = "Cursor is in code.";
        }


    }


    @Override
    public void mouseMoved(EditorMouseEvent event) {

    }

    @Override
    public void mouseEntered(EditorMouseEvent event) {

    }

    private void checkPrintMode() {

    }

    @Override
    public void mouseExited(EditorMouseEvent event) {
    }


    public boolean isInComment(Editor editor) {
        Project project = editor.getProject();
        return false;
    }

    private void checkAndPrint(Editor editor) {

        String result;
        CursorState newCursorState;
        CommentUtils.CommentType commentType = CommentUtils.identifyCommentType(editor);
        if (commentType != null) {
            newCursorState = CursorState.INCOMMENT;
        } else {
            newCursorState = CursorState.INCODE;
        }
        System.out.println("旧模式" + cursorState);
        System.out.println("新模式" + newCursorState);
//        System.out.println(InputMethodChecker.GetMode());
//        System.out.println(cursorState.getCode());
        if (newCursorState.equals(cursorState)) {
            //不做任何操作

        } else {
            cursorState = newCursorState;
            if (cursorState.getLanguage().equals(InputMethodChecker.GetMode())) {//如果状态相等,就不需要进行切换输入法.

            } else {
                InputMethodChecker.pressShift();
            }
        }
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

    static boolean OUTIDEA;
    public static boolean OUTEDITOR;

    @Override
    public void applicationActivated(IdeFrame ideFrame) {
        OUTIDEA = false;



    }

    @Override
    public void applicationDeactivated(IdeFrame ideFrame) {
        OUTIDEA = true;

        System.out.println("鼠标已离开IDEA主窗口");
        CursorState newCursorState = CursorState.OUTIDE;
        if (!cursorState.equals(newCursorState)) {
            cursorState = newCursorState;
            if (!cursorState.getLanguage().equals(InputMethodChecker.GetMode())) {
                InputMethodChecker.pressShift();
            }

        }

    }

    public void chekOutEditor() {
        //这个应该在更新状态后.
        //现在检测最基本的功能
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
        }
        if (OUTIDEA == false && OUTEDITOR) {

            System.out.println("当前在IDE内,但是在editor外");
            CursorState newCursorState = CursorState.INCODE;
            if (!cursorState.equals(newCursorState)) {
                cursorState = newCursorState;
                if (!cursorState.getLanguage().equals(InputMethodChecker.GetMode())) {
                    InputMethodChecker.pressShift();
                }

            }
//
        }
    }


}
