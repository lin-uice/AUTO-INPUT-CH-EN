package ss;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.diagnostic.Logger;
public class ssss implements ProjectActivity {
//    private static final Logger LOG = Logger.getInstance(MouseContextService.class);
    private static final Logger LOG = Logger.getInstance(ssss.class);
    @Override
    public @Nullable Object execute(@NotNull Project project, @NotNull Continuation<? super Unit> continuation) {
        LOG.warn("Hello");
        return null;
    }
}
