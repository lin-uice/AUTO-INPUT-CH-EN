package ENUM;

public enum CommentSyntax {
        JAVA("//", "/*", "/**"),
    PYTHON("#", "'''", "\"\"\""),
    JAVASCRIPT("//", "/*", "/**"),
    KOTLIN("//", "/*", "/**"),
    YAML("#", null, null),
    SHELL("#", null, null),

    // 新增语言
        // 新增语言
    C_SHARP("//", "/*", "/**"),
    TYPESCRIPT("//", "/*", "/**"),
    PHP("//", "/*", "/**"),
    RUBY("#", "=begin", "=begin"),
    SWIFT("//", "/*", "/**"),
    GO("//", "/*", "/**"),
    RUST("//", "/*", "/**"),
    SCALA("//", "/*", "/**"),
    PERL("#", "=pod", "=pod"),
    LUA("--", "--[[", "--[["),
    SQL("--", "/*", null),
    CSS(null, "/*", null),
    HTML(null, "<!--", null),
    XML(null, "<!--", null),

    // 补充 C 和 C++
    C("//", "/*", "/**"),
    CPP("//", "/*", "/**");

    // 可以继续扩展其他语言
    ;
    
    public final String linePrefix;
    public final String blockStart;
    public final String docStart;

    CommentSyntax(String linePrefix, String blockStart, String docStart) {
        this.linePrefix = linePrefix;
        this.blockStart = blockStart;
        this.docStart = docStart;
    }
}
