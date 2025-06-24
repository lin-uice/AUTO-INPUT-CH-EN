package InputMethod.test;

import java.awt.im.InputContext;

public class InputMethodChecker {
    public boolean isEnglishMode() {
        // 获取默认的输入上下文
        InputContext context = InputContext.getInstance();
        
        // 检查当前语言环境是否是英语
        return "en".equals(context.getLocale().getLanguage());
    }
    
    public static void main(String[] args) {
        InputMethodChecker checker = new InputMethodChecker();
        System.out.println("Is English mode: " + checker.isEnglishMode());
    }
}