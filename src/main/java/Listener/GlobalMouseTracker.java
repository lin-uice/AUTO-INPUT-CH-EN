package Listener;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class GlobalMouseTracker {

    private final Project project;
    private Window ideaWindow;
    private static boolean isMouseInIdeaWindow;
//    boolean sttatic installed = false;
    public GlobalMouseTracker(Project project) {
        this.project = project;

        WindowManager windowManager = WindowManager.getInstance();
        Component component = windowManager.getIdeFrame(project).getComponent();
        ideaWindow = SwingUtilities.getWindowAncestor(component);

        if (ideaWindow == null) {
            System.err.println("❌ GlobalMouseTracker: Failed to get IDEA window.");
            return;
        }

//        System.out.println("✅ GlobalMouseTracker: Successfully registered for window: " + ideaWindow.getName());

        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
//            System.out.println("💡 捕获到 AWT 事件: " + event);

            if (event instanceof MouseEvent mouseEvent) {
                Point mousePoint = MouseInfo.getPointerInfo().getLocation();
                Rectangle windowBounds = ideaWindow.getBounds();
                boolean isInsideIdeaWindow = windowBounds.contains(mousePoint);

//                System.out.println("🖱️ 鼠标位置: " + mousePoint);
//                System.out.println("📐 IDEA 窗口范围: " + windowBounds);

                if (!isInsideIdeaWindow && mouseEvent.getID() == MouseEvent.MOUSE_EXITED) {
                    isMouseInIdeaWindow = false;
                    System.out.println("【全局】鼠标已完全离开 IntelliJ IDEA 窗口");
                } else if (isInsideIdeaWindow && mouseEvent.getID() == MouseEvent.MOUSE_ENTERED) {
                    isMouseInIdeaWindow = true;
                    System.out.println("【全局】鼠标重新进入 IntelliJ IDEA 窗口");
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
    }
    private static boolean installed = false;
    public static void installFor(Project project) {
//        static boolean installed = false;
//        System.out.println("GlobalMouseTracker: installFor");
        if (installed) return;
        new GlobalMouseTracker(project);
        installed = true;
    }
    public static boolean isMouseInIdeaWindow() {
        return isMouseInIdeaWindow;
    }
}