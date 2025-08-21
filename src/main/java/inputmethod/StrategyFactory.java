package inputmethod;

import java.util.Map;
import java.util.function.Supplier;

import inputmethod.InputMethodChecker1.InputMethodChecker;
import inputmethod.InputMethodChecker2.UIAutomationSwitcher;

public class StrategyFactory {
    private static final Map<String, Supplier<InputMethodSwitchStrategy>> STRATEGIES =
        Map.of( 
            "UIAutomationSwitcher", UIAutomationSwitcher::new,
            "InputMethodChecker", InputMethodChecker::new
        );
 
    public static InputMethodSwitchStrategy createStrategy(String className) {
        Supplier<InputMethodSwitchStrategy> supplier = STRATEGIES.get(className); 
        if (supplier == null) {
            throw new IllegalArgumentException("无效策略: " + className);
        }
        return supplier.get(); 
    }
}