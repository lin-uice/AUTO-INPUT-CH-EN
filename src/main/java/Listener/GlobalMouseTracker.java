package Listener;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class GlobalMouseTracker {

    protected final Project project;
    protected Window ideaWindow;
    protected static boolean isMouseInIdeaWindow;
//    boolean sttatic installed = false;
    public GlobalMouseTracker(Project project) {
        this.project = project;

        WindowManager windowManager = WindowManager.getInstance();
        Component component = windowManager.getIdeFrame(project).getComponent();
        ideaWindow = SwingUtilities.getWindowAncestor(component);


    }
    public void WindowsListener(){

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