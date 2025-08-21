package inputmethod;

import inputmethod.InputMethodChecker2.UIAutomationSwitcher;
import view.SettingsState;

public class InputMethodSwitcher {
    // 私有构造防止实例化
    private InputMethodSwitcher() {
    }

    /**
     * 执行输入法切换（根据配置自动选择策略）
     */
    public static void change() {
        System.out.println("切换输入法");
        try {
            SettingsState state = SettingsState.getInstance();
            StrategyFactory.createStrategy(state.strategyClass).change();
        } catch (Exception e) {
            new UIAutomationSwitcher().change();
        }
    }
}