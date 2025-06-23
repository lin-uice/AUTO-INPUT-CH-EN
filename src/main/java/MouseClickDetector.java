import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.*;
//import com.intellij.openapi.editor.EditorMou
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseMotionListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
//import
public class MouseClickDetector implements EditorMouseListener {

    @Override
    public void mousePressed(@NotNull EditorMouseEvent e) {
        handleMouseEvent(e);
    }

    @Override
    public void mouseReleased(@NotNull EditorMouseEvent e) {
        handleMouseEvent(e);
    }

    private void handleMouseEvent(@NotNull EditorMouseEvent e) {
//        Editor editor = e.getEditor();
//        Project project = editor.getProject();
//        if (project == null) return;
//
//        // 获取鼠标点击的位置
//        LogicalPosition pos = editor.xyToLogicalPosition(e.getMouseEvent().getPoint());
//        int offset = editor.logicalPositionToOffset(pos);
//
//        VirtualFile file = editor.getVirtualFile();
//        if (file == null) return;
//
//        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
//        if (psiFile == null) return;
//
//        PsiElement elementAt = psiFile.findElementAt(offset);
//        if (elementAt == null) return;
//
//        System.out.println("Clicked on: " + elementAt.getText());
//        // 在这里可以执行你想做的任何操作，比如更新状态栏、显示提示等
    }
}