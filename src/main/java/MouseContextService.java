//import com.intellij.openapi.Disposable;
//import com.intellij.openapi.components.Service;
//import com.intellij.openapi.diagnostic.Logger;
//import com.intellij.openapi.editor.EditorFactory;
//import com.intellij.openapi.editor.event.EditorMouseMotionListener;
//import com.intellij.openapi.project.Project;
//import com.intellij.openapi.util.Disposer;
//
//@Service(Service.Level.PROJECT)
//public final class MouseContextService implements Disposable {
//
//    private static final Logger LOG = Logger.getInstance(MouseContextService.class);
//
//    private final Disposable listenerDisposable = Disposer.newDisposable("MouseContextListener");
//
//    public MouseContextService(Project project) {
//        LOG.info("✅ MouseContextService initialized for project: " + project.getName());
//        System.out.println("✅ [Console] MouseContextService initialized.");
//    }
//
//    @Override
//    public void dispose() {
//        Disposer.dispose(listenerDisposable);
//    }
//}