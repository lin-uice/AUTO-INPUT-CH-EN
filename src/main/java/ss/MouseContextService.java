//package ss;
//
//import com.intellij.openapi.Disposable;
//import com.intellij.openapi.components.Service;
//import com.intellij.openapi.project.Project;
//import com.intellij.openapi.util.Disposer;
//import com.intellij.openapi.diagnostic.Logger;
//@Service(Service.Level.PROJECT)
//public final class MouseContextService implements Disposable {
//
//    private static final Logger LOG = Logger.getInstance(MouseContextService.class);
//
//    public MouseContextService(Project project) {
//        // 记录 Hello World 日志
//        LOG.info("Hello World");
//
//        System.out.println("✅ MouseContextService initialized for project: " + project.getName());
//    }
//
//    @Override
//    public void dispose() {
////        Disposer.dispose(listenerDisposable);
//    }
//
//    // ...
//}