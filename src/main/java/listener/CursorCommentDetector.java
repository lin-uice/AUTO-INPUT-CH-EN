package listener;

import enums.CursorState;
import inputmethod.InputMethodChecker;
import com.intellij.openapi.application.ApplicationActivationListener;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.IdeFrame;
import org.jetbrains.annotations.NotNull;
//import com.intellij.idea.vim.common


public class CursorCommentDetector  implements CaretListener,  ApplicationActivationListener {
    //
    static CursorState cursorState = CursorState.INCODE;

    static InputMethodChecker inputMethodChecker;
    public static EditorFocusTracker editorFocusTracker = new EditorFocusTracker();

    static boolean OUTIDEA;
    public static boolean OUTEDITOR;


    public CursorCommentDetector(Project project) {
//        WindowsListener();
    }



    @Override
    public void caretPositionChanged(@NotNull CaretEvent e) {
//        System.out.println("CursorCommentDetector.caretPositionChanged");
        checkAndPrint(e.getEditor());
    }


    // 在 CursorCommentDetector.java 中添加这个静态方法
    public static void installGlobalMouseListener(Project project, Editor editor) {
        System.out.println("CursorCommentDetector!!!!!");
        cursorState = CursorState.INCODE;
//        CursorState newCursorState;
        boolean iscomment = CommentUtils.identifyCommentType(editor);
        if (iscomment) {
            cursorState = CursorState.INCOMMENT;
//            result = "Cursor is in a comment.";
        } else {
            cursorState = CursorState.INCODE;
//            result = "Cursor is in code.";
        }


    }



    private void checkAndPrint(Editor editor) {

        String result;
        CursorState newCursorState;
        boolean commentType = CommentUtils.identifyCommentType(editor);
        if (commentType) {
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



    @Override
    public void applicationActivated(IdeFrame ideFrame) {
        OUTIDEA = false;
    }

    @Override
    public void applicationDeactivated(IdeFrame ideFrame) {
        OUTIDEA = true;

        System.out.println("鼠标已离开IDEA主窗口");
        CursorState newCursorState = CursorState.OUTIDE;
        System.out.println("之前光标状态为：" + cursorState);
        System.out.println("现在光标状态为：" + newCursorState);
        if (!cursorState.equals(newCursorState)) {
            cursorState = newCursorState;
//            System.out.println("鼠标已离开IDEA主窗口");
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
