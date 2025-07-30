package enums;

public enum CursorState {
   OUTIDE("OUTSIDE",InputState.CHINESE),//在IDE外
    INCOMMENT("INCOMMENT",InputState.CHINESE),//在注释中
    INCODE("INCODE",InputState.ENGLISH);//在代码中
    private final String Code;
    private final InputState Language;

    CursorState(String Code, InputState Language) {
        this.Code = Code;
        this.Language = Language;
    }

    public String getCode() {
        return Code;
    }

    public InputState getLanguage() {
        return Language;
    }
}
