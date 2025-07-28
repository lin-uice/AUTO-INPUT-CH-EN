package inputmethod;

import enums.InputState;
//import com.intellij.jna.JnaLoader;
//import com.intellij.jna.JnaLoader;
//import com.intellij.jna.JnaLoader;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;
//import com.intellij.jna.JnaLoader

import static com.sun.jna.platform.win32.WinUser.KEYBDINPUT.KEYEVENTF_KEYUP;
import static com.sun.jna.platform.win32.WinUser.VK_LSHIFT;

//支持搜狗，微软，百度输入法
public class InputMethodChecker {
    //这部分难以重构.因为不是spring框架,因此重构之后,反而会影响性能
    private static long lastPressTime = 0;
    private static final long MIN_PRESS_INTERVAL_MS = 100; // 设置最小间隔为500毫秒

    static {
        try {
            Native.load("imm32", Imm32.class);
            System.out.println("imm加载成功!!!!!!");
        } catch (Exception e) {
            System.out.println("imm加载失败");
        }
//        JnaLoader.load(logger);
////        JnaLoader.load
//        System.out.println("Jna加载了吗"+JnaLoader.isLoaded());
//       System.out.println("InputMethodChecker!!!!!");
//        System.out.println("InputMethodChecker!!!!!");
    }

    // Imm32.dll 接口：用于获取输入法窗口句柄
    public interface Imm32 extends Library {
        // 加载 Imm32.dll 库
        Imm32 INSTANCE = (Imm32) Native.load("imm32", Imm32.class);

        // 获取默认的 IME 窗口句柄
        // 参数: hWnd - 应用程序窗口句柄
        // 返回值: 默认 IME 窗口的句
        Pointer ImmGetDefaultIMEWnd(Pointer hWnd);

        //释放窗口
        Pointer ImmGetContext(HWND hWnd);

        boolean ImmReleaseContext(HWND hWnd, Pointer hIMC);

    }

    // User32.dll 接口：用于获取激活窗口和发送消息
    public interface User32 extends StdCallLibrary {
        // 加载 User32.dll 库，并应用
        User32 INSTANCE = (User32) Native.load("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);

        //获取当前激活的窗口句柄
        // 返回值: 当前激活窗口的句柄
        HWND GetForegroundWindow();

        // 向指定窗口发送消息
        // 参数: hWnd - 接收消息的窗口句柄
        //       Msg - 消息的标识符
        //       wParam - 消息的附加参数
        //       lParam - 消息的附加参数
        // 返回值: 发送消息的结果，具体类型视消息而定
        long SendMessage(HWND hWnd, int Msg, long wParam, long lParam);

        //模拟按键盘按键
        void keybd_event(byte bVk, byte bScan, int dwFlags, int extraInfo);
    }

    private static final int WM_IME_CONTROL = 0x0283;
    private static final int IMC_GETOPENSTATUS = 0x0001;

    public static InputState getCurrentMode() {
        return isEnglishMode() ? InputState.ENGLISH : InputState.CHINESE;
    }

    /**
     * 检测当前激活窗口是否处于英文输入法模式。
     *
     * @return 如果是英文输入法模式返回true，否则返回false。
     */
    public static boolean isEnglishMode() {
        try {
            User32 user32 = User32.INSTANCE;
            Imm32 imm32 = Imm32.INSTANCE;
//            User32.INSTANCE.

            // 获取当前激活窗口句柄
            HWND activeWindow = user32.GetForegroundWindow();
            if (activeWindow == null) {
                System.out.println("未找到激活窗口");
                return false;
            }

            // 获取输入法窗口句柄
            Pointer imeWnd = imm32.ImmGetDefaultIMEWnd(activeWindow.getPointer());
            if (imeWnd == null) {
                System.out.println("未找到输入法窗口");
                return true; // 英文输入法可能没有 IME 窗口
            }


            // 发送 IMC_GETOPENSTATUS 查询输入法是否打开
            long result = user32.SendMessage(new HWND(imeWnd), WM_IME_CONTROL, IMC_GETOPENSTATUS, 0);
            //执行结束,释放上下文
            Pointer hIMC = imm32.ImmGetContext(activeWindow);
            if (hIMC != null) {
                imm32.ImmReleaseContext(activeWindow, hIMC);
            }

            //
            return result == 0;
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("获取输入法异常");
            return false;
        }
    }

    public static void pressShift() {
        long now = System.currentTimeMillis();
        if (now - lastPressTime < MIN_PRESS_INTERVAL_MS) {
            // 如果距离上次按下的时间小于500毫秒，则跳过本次操作
            return;
        }
        User32 user32 = User32.INSTANCE;

        // 按下左 Shift
        user32.keybd_event((byte) VK_LSHIFT, (byte) 0, 0, 0);
        System.out.println("按下 Shift");


        // 释放左 Shift
        user32.keybd_event((byte) VK_LSHIFT, (byte) 0, KEYEVENTF_KEYUP, 0);
        System.out.println("释放 Shift");
        lastPressTime = now;

    }


}