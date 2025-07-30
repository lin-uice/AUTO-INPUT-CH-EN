
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.extensions.PluginId;
import com.maddyhome.idea.vim.common.ModeChangeListener;
import listener.BaseInputMethodDetector;
import listener.VimInputMethodDetector;
import listener.EditorFocusTracker;
import com.intellij.openapi.application.ApplicationActivationListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.project.Project;
import com.maddyhome.idea.vim.api.VimInjectorKt;
import com.maddyhome.idea.vim.common.VimListenersNotifier;
import com.maddyhome.idea.vim.newapi.IjVimInjectorKt;
import org.jetbrains.annotations.NotNull;
import com.maddyhome.idea.vim.api.VimInjector;

import java.util.HashSet;
import java.util.Set;


//测试
public class InputMethodPlugin implements EditorFactoryListener {
    private static boolean vimInitialized = false;
    private static final Set<ModeChangeListener> registeredListeners = new HashSet<>();


    @Override
    public void editorCreated(@NotNull EditorFactoryEvent event) {


        Editor editor = event.getEditor();
        Project project = editor.getProject();
        System.out.println("editorCreated" + editor);
        System.out.println("project" + project);
        PluginId vimPlugin = PluginId.getId("IdeaVIM");
        IdeaPluginDescriptor plugin = PluginManagerCore.getPlugin(vimPlugin);


        System.out.println("Plugin exists: " + (plugin != null));
        System.out.println("Plugin enabled: " + (plugin != null && plugin.isEnabled()));
        try {
            Class.forName("com.maddyhome.idea.vim.VimPlugin");
            System.out.println("现在是vim模式");
            VimInputMethodDetector listener = new VimInputMethodDetector();
            vimMethodFactory(listener);
            baseMethodFactory(editor, project, listener);
        } catch (Exception e) {
            System.out.println("现在无vim模式");
            BaseInputMethodDetector listener = new BaseInputMethodDetector();
            baseMethodFactory(editor, project, listener);
        }
        //        if (plugin != null && plugin.isEnabled()) {
//            try {
//                System.out.println("现在是vim模式");
//                VimInputMethodDetector listener = new VimInputMethodDetector();
//                vimMethodFactory(listener);
//                baseMethodFactory(editor, project, listener);
//            } catch (Exception e) {
//                System.out.println("现在无vim模式");
//            }
//
//        } else {
//            try {
//                System.out.println("现在无vim模式");
//                BaseInputMethodDetector listener = new BaseInputMethodDetector();
//                baseMethodFactory(editor, project, listener);
//            } catch (Exception e) {
//                System.out.println(e);
//            }
//        }

    }


    @Override
    public void editorReleased(@NotNull EditorFactoryEvent event) {
    }

    private void baseMethodFactory(Editor editor, Project project, BaseInputMethodDetector listener) {
        editor.getCaretModel().addCaretListener(listener);
        ApplicationManager.getApplication().getMessageBus().connect().subscribe(ApplicationActivationListener.TOPIC, listener);
        EditorFocusTracker.addFocusListener(project, hasFocus -> {
            if (hasFocus) {
                BaseInputMethodDetector.OUTEDITOR = false;
                System.out.println("获得了注意"); // 比如启用光标监听这个时候,其实不进行操作
            } else {
                System.out.println("失去了注意");
                BaseInputMethodDetector.OUTEDITOR = true;
                listener.chekOutEditor();
            }
        });
    }

    private void vimMethodFactory(ModeChangeListener listener) {
        // 避免重复添加相同的监听器
        if (registeredListeners.contains(listener)) {
            System.out.println("监听器已存在，跳过添加");
            return;
        }

        // 只初始化一次Vim组件
        if (!vimInitialized) {
            System.out.println("初始化Vim组件");
            IjVimInjectorKt.initInjector();
            vimInitialized = true;
        }

        VimInjector vimInjector = VimInjectorKt.getInjector();
        if (vimInjector != null) {
            VimListenersNotifier listenersNotifier = vimInjector.getListenersNotifier();
            if (listenersNotifier != null) {
                listenersNotifier.getModeChangeListeners().add(listener);
                registeredListeners.add(listener);
                System.out.println("成功添加Vim模式变更监听器");
            }
        }
    }


}