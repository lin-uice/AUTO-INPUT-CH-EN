package InputMethod;

public class Runstart {
    public static void main(String[] args) {
        InputMethodChecker imc = new InputMethodChecker();
        boolean isEnglishMode = imc.isEnglishMode();
        System.out.println("当前是否为英文输入法模式: " + isEnglishMode);
//        imc.pressShift();
        try {
            Thread.sleep(100); // ⏸️ 加个短暂停顿让 IME 更新状态
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isEnglishMode = imc.isEnglishMode();}
}