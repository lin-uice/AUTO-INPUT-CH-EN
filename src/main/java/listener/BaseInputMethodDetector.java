package listener;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import enums.CursorState;
import enums.InputState;
import inputmethod.InputMethodChecker;
import com.intellij.openapi.application.ApplicationActivationListener;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.*;
import com.intellij.openapi.wm.IdeFrame;
import org.jetbrains.annotations.NotNull;
import utils.CommentUtils;
import utils.InputMethodBubble;

public class BaseInputMethodDetector implements CaretListener,  ApplicationActivationListener {
    //
    public static CursorState cursorState = CursorState.INCODE;


    public static boolean OUTIDEA;
    public static boolean OUTEDITOR;
    public static boolean ISINSERT = false;




    @Override
    public void caretPositionChanged(@NotNull CaretEvent e) {
        Editor editor = e.getEditor();
        if (editor.getSelectionModel().hasSelection()) {
            return;
        }
        checkAndPrint(editor);
    }






    protected void checkAndPrint(Editor editor) {
        if (editor == null || editor.getSelectionModel().hasSelection()) {
            return;
        }
        Project project = editor.getProject();
        if (project == null || project.isDisposed()) {
            return;
        }
        ApplicationManager.getApplication().invokeLater(() -> {
            if (project.isDisposed()) {
                return;
            }
            ApplicationManager.getApplication().runWriteAction(() -> {
                PsiDocumentManager.getInstance(project).commitDocument(editor.getDocument());
            });
            ApplicationManager.getApplication().runReadAction(() -> {
                check(editor);
            });

        }, ModalityState.defaultModalityState());

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
            Editor editor = null;
            if (ideFrame != null && ideFrame.getProject() != null) {
                editor = FileEditorManager.getInstance(ideFrame.getProject()).getSelectedTextEditor();
            }
            switchToIfNeeded(editor, cursorState.getLanguage(), ideFrame);

        }

    }

    public void chekOutEditor(Editor editor) {
        try {
            Thread.sleep(10);
        } catch(InterruptedException e) {
        }
        if (OUTIDEA == false && OUTEDITOR) {

            System.out.println("当前在IDE内,但是在editor外");
            CursorState newCursorState = CursorState.INCODE;
            if (!cursorState.equals(newCursorState)) {
                cursorState = newCursorState;
                switchToIfNeeded(editor, cursorState.getLanguage(), null);

            }
//
        }
    }
    protected void check(Editor editor){
        CursorState newCursorState;
        boolean commentType = CommentUtils.isInComment(editor);
        if (commentType) {
            newCursorState = CursorState.INCOMMENT;
        } else if (CommentUtils.isInChineseString(editor)) {
            newCursorState = CursorState.INSTRING;
        } else {
            newCursorState = CursorState.INCODE;
        }
        if (newCursorState.equals(cursorState)) {
            //不做任何操作

        } else {
            cursorState = newCursorState;
            switchToIfNeeded(editor, cursorState.getLanguage(), null);
        }
    }

    protected void switchToIfNeeded(Editor editor, InputState target, IdeFrame fallbackFrame) {
        InputState from = InputMethodChecker.getCurrentMode();
        if (from == target) {
            return;
        }
        InputMethodChecker.pressShift();
        if (editor != null) {
            InputMethodBubble.show(editor, from, target);
            return;
        }
        if (fallbackFrame != null && fallbackFrame.getComponent() != null) {
            InputMethodBubble.show(fallbackFrame.getComponent(), from, target);
        }
    }


}
