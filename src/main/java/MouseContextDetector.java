
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseMotionListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

public class MouseContextDetector implements EditorMouseMotionListener {
    public MouseContextDetector() {
        System.out.println("MouseListener instance created!");
    }
//    @Override
//    public void mou
    @Override
    public void mouseMoved(@NotNull EditorMouseEvent e) {
//        System.out.println("Mouse moved in editor!");
//        Editor editor = e.getEditor();
//        Project project = editor.getProject(); // ✅ 从 editor 获取 project
//        System.out.println("Project: " + project);
//        if (project == null) return;
//
//        LogicalPosition pos = editor.xyToLogicalPosition(e.getMouseEvent().getPoint());
//        int offset = editor.logicalPositionToOffset(pos);
//
//        VirtualFile file = editor.getVirtualFile();
//        if (file == null) return;
//
//        PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
//        if (psiFile == null) return;
//
//        PsiElement element = psiFile.findElementAt(offset);
//        if (element == null) return;
//
//        String result;
//        if (PsiTreeUtil.getParentOfType(element, PsiComment.class) != null) {
//            result = "Mouse is in a comment.";
//        } else {
//            result = "Mouse is in code.";
//        }
//        System.out.println( result);
//
//        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
//        if (statusBar != null) {
//            statusBar.setInfo(result);
//        }
    }

    @Override
    public void mouseDragged(@NotNull EditorMouseEvent e) {
        // No-op
    }
}