package view;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage; 
import org.jetbrains.annotations.NotNull; 
import org.jetbrains.annotations.Nullable; 
 
@State(
    name = "InputMethodSettings",
    storages = @Storage("InputMethodSettings.xml") 
)
public class SettingsState implements PersistentStateComponent<SettingsState> {
    
    public String strategyClass = "UIAutomationSwitcher";  // 默认策略 
 
    @Nullable
    @Override
    public SettingsState getState() {
        return this;
    }
 
    @Override
    public void loadState(@NotNull SettingsState state) {
        this.strategyClass  = state.strategyClass; 
    }

    public static SettingsState getInstance() {
        return ApplicationManager.getApplication().getService(SettingsState.class);
    }
}