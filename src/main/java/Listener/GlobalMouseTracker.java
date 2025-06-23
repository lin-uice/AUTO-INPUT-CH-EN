package Listener;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;

public class GlobalMouseTracker {

    private final Project project;
    private Window ideaWindow;

    public GlobalMouseTracker(Project project) {
        this.project = project;
        // 获取当前项目对应的IDEA窗口
        WindowManager windowManager = WindowManager.getInstance();
        ideaWindow = (Window) windowManager.getIdeFrame(project).getComponent().getRootPane().getParent();

        // 添加全局鼠标事件监听器
        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
            if (event instanceof MouseEvent mouseEvent) {
                Point mousePoint = MouseInfo.getPointerInfo().getLocation();
                boolean isInsideIdeaWindow = ideaWindow.getBounds().contains(mousePoint);

                if (!isInsideIdeaWindow && event.getID() == MouseEvent.MOUSE_EXITED) {
                    System.out.println("【全局】鼠标已完全离开 IntelliJ IDEA 窗口");
                } else if (isInsideIdeaWindow && event.getID() == MouseEvent.MOUSE_ENTERED) {
                    System.out.println("【全局】鼠标重新进入 IntelliJ IDEA 窗口");
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
    }

    public static void installFor(Project project) {
        new GlobalMouseTracker(project);
    }
}