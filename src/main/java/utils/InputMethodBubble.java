package utils;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.JBColor;
import com.intellij.ui.awt.RelativePoint;
import enums.InputState;

import java.awt.Component;
import java.awt.Point;
import java.util.concurrent.atomic.AtomicLong;

public class InputMethodBubble {
    private static final long MIN_SHOW_INTERVAL_MS = 200;
    private static final long FADEOUT_TIME_MS = 500;
    private static final AtomicLong lastShownAt = new AtomicLong(0);

    public static void show(Editor editor, InputState from, InputState to) {
        if (editor == null) {
            return;
        }
        Component component = editor.getContentComponent();
        if (component == null) {
            return;
        }
        Point point = editor.visualPositionToXY(editor.getCaretModel().getVisualPosition());
        show(component, point, from, to);
    }

    public static void show(Component component, InputState from, InputState to) {
        if (component == null) {
            return;
        }
        Point center = new Point(component.getWidth() / 2, component.getHeight() / 4);
        show(component, center, from, to);
    }

    private static void show(Component component, Point point, InputState from, InputState to) {
        if (component == null || point == null || from == null || to == null) {
            return;
        }
        if (ApplicationManager.getApplication() == null) {
            return;
        }
        if (ApplicationManager.getApplication().isHeadlessEnvironment()) {
            return;
        }
        long now = System.currentTimeMillis();
        long last = lastShownAt.get();
        if (now - last < MIN_SHOW_INTERVAL_MS) {
            return;
        }
        if (!lastShownAt.compareAndSet(last, now)) {
            return;
        }

        String text = "输入法：" + toText(from) + " → " + toText(to);
        ApplicationManager.getApplication().invokeLater(() -> {
            Balloon balloon = JBPopupFactory.getInstance()
                    .createHtmlTextBalloonBuilder(text, null, new JBColor(0x2B2D30, 0x2B2D30), new JBColor(0xE6E6E6, 0xE6E6E6), null)
                    .setFadeoutTime(FADEOUT_TIME_MS)
                    .createBalloon();
            balloon.show(new RelativePoint(component, point), Balloon.Position.above);
        });
    }

    private static String toText(InputState state) {
        return state == InputState.CHINESE ? "中文" : "英文";
    }
}

