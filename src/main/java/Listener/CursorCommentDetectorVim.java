package Listener;

import ENUM.CursorState;
import InputMethod.InputMethodChecker;
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
//import com.intellij.idea.vim.common


public class CursorCommentDetectorVim extends GlobalMouseTracker implements CaretListener, ModeChangeListener, ApplicationActivationListener {
    //
    static CursorState cursorState = CursorState.INCODE;
    private Logger LOG = Logger.getInstance(CursorCommentDetectorVim.class);
    private boolean isCursorInContentArea = false;
    static VimModeChecker vimModeChecker;
    static InputMethodChecker inputMethodChecker;
    public static EditorFocusTracker editorFocusTracker = new EditorFocusTracker();
    private static boolean ISENSERT = false;

    static {
        inputMethodChecker = new InputMethodChecker();
    }

    public CursorCommentDetectorVim(Project project) {
        super(project);
//        WindowsListener();
    }



    @Override
    public void caretPositionChanged(@NotNull CaretEvent e) {
        checkAndPrint(e.getEditor());
    }


    // 在 CursorCommentDetector.java 中添加这个静态方法
    public static void installGlobalMouseListener(Project project, Editor editor) {
        System.out.println("CursorCommentDetector!!!!!");
        GlobalMouseTracker.installFor(project);
        vimModeChecker = new VimModeChecker(editor);
        cursorState = CursorState.INCODE;
//        CursorState newCursorState;
        boolean commentType = CommentUtils.identifyCommentType(editor);
        if (commentType) {
            cursorState = CursorState.INCOMMENT;
//            result = "Cursor is in a comment.";
        } else {
            cursorState = CursorState.INCODE;
//            result = "Cursor is in code.";
        }


    }





    @Deprecated
    //这是测试用的函数
    public boolean isInComment(Editor editor) {
        Project project = editor.getProject();
        return false;
    }

    private void checkAndPrint(Editor editor) {


        String result;
        CursorState newCursorState;
        if (ISENSERT == false) {
            newCursorState = CursorState.INCODE;
        } else {
            boolean commentType = CommentUtils.identifyCommentType(editor);
            if (commentType) {
                newCursorState = CursorState.INCOMMENT;
            } else {
                newCursorState = CursorState.INCODE;
            }
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
    @Deprecated
    // 这是测试时用的函数.
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
    public void modeChanged(@NotNull VimEditor vimEditor, @NotNull Mode mode) {


        Editor editor = IjVimEditorKt.getIj(vimEditor);
        //如果当前不是插入模式,则改为插入模式
        mode = vimEditor.getMode();
        if (mode instanceof Mode.INSERT) {
            ISENSERT = true;
        } else {
            ISENSERT = false;
        }
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
        System.out.println("之前光标状态为：" + cursorState);
        System.out.println("现在光标状态为：" + newCursorState);
        if (!cursorState.equals(newCursorState)) {
            cursorState = newCursorState;
            if (!cursorState.getLanguage().equals(InputMethodChecker.GetMode())) {
                InputMethodChecker.pressShift();
                System.out.println("切换输入法为：" );
            }

        }

    }

    public void chekOutEditor() {
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
