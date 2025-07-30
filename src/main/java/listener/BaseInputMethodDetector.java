package listener;

import enums.CursorState;
import inputmethod.InputMethodChecker;
import com.intellij.openapi.application.ApplicationActivationListener;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.*;
import com.intellij.openapi.wm.IdeFrame;
import org.jetbrains.annotations.NotNull;
import utils.CommentUtils;


public class BaseInputMethodDetector implements CaretListener,  ApplicationActivationListener {
    //
    public static CursorState cursorState = CursorState.INCODE;


    public static boolean OUTIDEA;
    public static boolean OUTEDITOR;
    public static boolean ISINSERT = false;




    @Override
    public void caretPositionChanged(@NotNull CaretEvent e) {
        checkAndPrint(e.getEditor());
    }






    protected void checkAndPrint(Editor editor) {

        CursorState newCursorState;
        boolean commentType = CommentUtils.isInComment(editor);
        if (commentType) {
            newCursorState = CursorState.INCOMMENT;
        } else {
            newCursorState = CursorState.INCODE;
        }
        if (newCursorState.equals(cursorState)) {
            //不做任何操作

        } else {
            cursorState = newCursorState;
            if (cursorState.getLanguage().equals(InputMethodChecker.getCurrentMode())) {//如果状态相等,就不需要进行切换输入法.

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
            if (!cursorState.getLanguage().equals(InputMethodChecker.getCurrentMode())) {
                InputMethodChecker.pressShift();
            }

        }

    }

    public void chekOutEditor() {
        try {
            Thread.sleep(10);
        } catch(InterruptedException e) {
        }
        if (OUTIDEA == false && OUTEDITOR) {

            System.out.println("当前在IDE内,但是在editor外");
            CursorState newCursorState = CursorState.INCODE;
            if (!cursorState.equals(newCursorState)) {
                cursorState = newCursorState;
                if (!cursorState.getLanguage().equals(InputMethodChecker.getCurrentMode())) {
                    InputMethodChecker.pressShift();
                }

            }
//
        }
    }


}
