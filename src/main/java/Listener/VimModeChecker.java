package Listener;//package Listener;
/// /
/// /
/// /import .maddyhome.idea.vim.VimPlugin;

import com.esotericsoftware.kryo.kryo5.minlog.Log;
import com.intellij.openapi.editor.Editor;
import com.maddyhome.idea.vim.VimKeyListener;
import com.maddyhome.idea.vim.VimPlugin;
import com.maddyhome.idea.vim.api.VimEditor;
import com.maddyhome.idea.vim.common.ModeChangeListener;
import com.maddyhome.idea.vim.helper.ModeHelper;
//import com.maddyhome.idea.vim.newapi.IjVimEditorKt;
import com.maddyhome.idea.vim.listener.VimInsertListener;
import com.maddyhome.idea.vim.newapi.IjVimEditorKt;
import com.maddyhome.idea.vim.state.VimStateMachine;
import com.maddyhome.idea.vim.state.mode.Mode;
import groovy.util.logging.Log4j;
import groovy.util.logging.Slf4j;

//import java.util.logging.Logger;

//import java.util.logging.Logger;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;

//@Slf4j
public class VimModeChecker  {
    private static final Logger LOG = Logger.getInstance(VimModeChecker.class);
    private Editor editor;
//    private final VimKeyListener keyListener;
    public VimModeChecker(Editor editor) {
        this.editor = editor;
    }

//    @Override
//    public void handleKey(){
//        LOG.info("keyPressed");
//    }




    public boolean isInsertMode() {
        if (editor == null || isIdeaVimAvailable()) {
            LOG.warn("editor是否为空" + (editor == null));
            LOG.warn("isIdeaVimAvailable" + isIdeaVimAvailable());

        }
        VimEditor vimEditor = IjVimEditorKt.getVim(editor);
        Mode mode = vimEditor.getMode();
//        Log.INFO("hello");
//        LOG.info("VimModeChecker: isInsertMode" + mode);

        return mode instanceof Mode.INSERT;

    }


    private boolean isIdeaVimAvailable() {
        try {
            Class.forName("com.maddyhome.idea.vim.newapi.IjVimEditorKt");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }



}