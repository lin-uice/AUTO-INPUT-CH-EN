package utils;

import ENUM.CommentSyntax;

import java.util.HashMap;
import java.util.Map;

public class CommentSyntaxMapper {
    // 使用静态内部类封装语言映射关系
    private static class LanguageMapping {
        static final Map<String, CommentSyntax> INSTANCE;
        
        static {
            Map<String, CommentSyntax> map = new HashMap<>();
            // 初始化语言映射（保持不变）
            map.put("java", CommentSyntax.JAVA);
            map.put("python", CommentSyntax.PYTHON);
            map.put("javascript", CommentSyntax.JAVASCRIPT);
            map.put("kotlin", CommentSyntax.KOTLIN);
            map.put("yaml", CommentSyntax.YAML);
            map.put("shell", CommentSyntax.SHELL);
            map.put("csharp", CommentSyntax.C_SHARP);
            map.put("typescript", CommentSyntax.TYPESCRIPT);
            map.put("php", CommentSyntax.PHP);
            map.put("ruby", CommentSyntax.RUBY);
            map.put("swift", CommentSyntax.SWIFT);
            map.put("go", CommentSyntax.GO);
            map.put("rust", CommentSyntax.RUST);
            map.put("scala", CommentSyntax.SCALA);
            map.put("perl", CommentSyntax.PERL);
            map.put("lua", CommentSyntax.LUA);
            map.put("sql", CommentSyntax.SQL);
            map.put("css", CommentSyntax.CSS);
            map.put("html", CommentSyntax.HTML);
            map.put("xml", CommentSyntax.XML);
            map.put("c", CommentSyntax.C);
            map.put("cpp", CommentSyntax.CPP);
            
            INSTANCE = Map.copyOf(map); // 使用不可变Map增强安全性
        }
    }
    
    /**
     * 根据语言 ID 获取对应的注释语法
     * @param languageId 语言 ID（如 "java", "python"）
     * @return 对应的 CommentSyntax 枚举值，若未找到则返回 null
     */
    public static CommentSyntax getSyntaxForLanguage(String languageId) {
        if (languageId == null) {
            return null;
        }
        return LanguageMapping.INSTANCE.get(languageId.toLowerCase());
    }
}    