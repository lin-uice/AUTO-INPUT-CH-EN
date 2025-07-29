package enums;

/**
 * @author crl
 * @version 1.0
 * @description: TODO
 * @date 2025/7/29 18:55
 */

public class InputMethodState {
    private static final InputMethodState INSTANCE = new InputMethodState();

    // 私有构造函数，防止外部实例化
    private InputMethodState() {}

    // 全局访问点
    public static InputMethodState getInstance() {
        return INSTANCE;
    }

    private boolean outOfIdea;
    private boolean outOfEditor;
    private boolean isInsertMode;


    public boolean isOutOfIdea() {
        return outOfIdea;
    }

    public void setOutOfIdea(boolean outOfIdea) {
        this.outOfIdea = outOfIdea;
    }

    public boolean isOutOfEditor() {
        return outOfEditor;
    }

    public void setOutOfEditor(boolean outOfEditor) {
        this.outOfEditor = outOfEditor;
    }

    public boolean isInsertMode() {
        return isInsertMode;
    }

    public void setInsertMode(boolean insertMode) {
        isInsertMode = insertMode;
    }
}