package inputmethod.InputMethodChecker2;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.PointerByReference;
import inputmethod.InputMethodSwitchStrategy;
import mmarquee.automation.Element;
import mmarquee.automation.UIAutomation;
import mmarquee.uiautomation.IUIAutomationElement;
import mmarquee.uiautomation.IUIAutomationLegacyIAccessiblePattern;
import mmarquee.uiautomation.IUIAutomationLegacyIAccessiblePatternConverter;
import mmarquee.uiautomation.TreeScope;

import java.util.List;

public class UIAutomationSwitcher implements InputMethodSwitchStrategy {

    public static final int SUCCESS = 0;
    public static final int COM_INIT_FAILED_STA = 1;
    public static final int AUTOMATION_CREATE_FAILED = 2;
    public static final int TRAY_WINDOW_NOT_FOUND = 3;
    public static final int ELEMENT_FROM_HANDLE_FAILED = 4;
    public static final int BUTTONS_NOT_FOUND = 5;
    public static final int BUTTON_INVOKE_FAILED = 6;
    public static final int NO_VALID_BUTTON = 7;

    public static int clickInputMethodButton() {
        try {
            UIAutomation automation = UIAutomation.getInstance();
            //todo 查找任务栏窗口 win10可能不一样的ClassName待测试
            WinDef.HWND hTrayWnd = User32.INSTANCE.FindWindow("Shell_TrayWnd", null);
            if (hTrayWnd == null) {
                return TRAY_WINDOW_NOT_FOUND;
            }
            Element root = automation.getElementFromHandle(hTrayWnd);
            if (root == null) {
                return ELEMENT_FROM_HANDLE_FAILED;
            }
            Variant.VARIANT.ByValue variant = new Variant.VARIANT.ByValue();
            variant.setValue(3, new WinDef.LONG(50000L));
            PointerByReference propertyCondition = automation.createPropertyCondition(30003, variant);

            List<Element> buttons = root.findAll(new TreeScope(TreeScope.DESCENDANTS), propertyCondition);
            if (buttons == null || buttons.isEmpty()) {
                return BUTTONS_NOT_FOUND;
            }

            // 逆向遍历按钮 输入法一般都在倒数位次 会快一点
            for (int i = buttons.size() - 1; i >= 0; i--) {
                Element button = buttons.get(i);
                IUIAutomationElement element = button.getElement();
                String name = button.getName();

                if (name != null && isInputMethodButton(name)) {
                    try {
                        PointerByReference legacyPatternRef = new PointerByReference();
                        element.getCurrentPattern(10018,  legacyPatternRef);
                        IUIAutomationLegacyIAccessiblePattern legacy = IUIAutomationLegacyIAccessiblePatternConverter.pointerToInterface(legacyPatternRef);
                        legacy.doDefaultAction();
                        return SUCCESS;
                    } catch (Exception e) {
                        return BUTTON_INVOKE_FAILED;
                    }
                }
            }
            return NO_VALID_BUTTON;

        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("RPC_E_CHANGED_MODE")) {
                return COM_INIT_FAILED_STA;
            }
            return AUTOMATION_CREATE_FAILED;
        }
    }

    private static boolean isInputMethodButton(String name) {
        return name.contains(" 输入指示器") ||
                name.contains(" 输入模式") ||
                name.contains(" 语言栏") ||
                name.contains("ENG") ||
                name.contains("CHS") ||
                name.contains(" 中") ||
                name.contains(" 英");
    }

    public static void main(String[] args) {
        int result = clickInputMethodButton();
        System.out.println("返回值" + result);
    }

    @Override
    public void change() {
        clickInputMethodButton();
    }
}