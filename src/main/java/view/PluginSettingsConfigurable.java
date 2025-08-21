package view;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.Messages;
import inputmethod.InputMethodSwitchStrategy;
import inputmethod.StrategyFactory;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PluginSettingsConfigurable implements Configurable {
    private static final String DEFAULT_STRATEGY = "UIAutomationSwitcher";

    private JPanel settingsPanel;
    private JComboBox<String> strategyComboBox;
    private final SettingsState state;

    public PluginSettingsConfigurable() {
        this.state = SettingsState.getInstance();
        if (this.state == null) {
            throw new IllegalStateException("SettingsState must not be null");
        }
    }

    @Override
    public JComponent createComponent() {
        settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 策略选择组件
        JPanel strategyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        strategyPanel.add(new JLabel("切换策略:"));

        strategyComboBox = new ComboBox<>(new String[]{
                "UIAutomationSwitcher",
                "InputMethodChecker"
        });
        strategyComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                String display = value.toString()
                        .replace("UIAutomationSwitcher", "UIAutomation模拟点击托盘输入法按钮(默认，性能好，不触发焦点切换、不触发键盘监听事件)")
                        .replace("InputMethodChecker", "shift快捷键(兼容性最好，但是误触发键盘监听事件)");
                return super.getListCellRendererComponent(list, display, index, isSelected, cellHasFocus);
            }
        });

        strategyPanel.add(strategyComboBox);
        settingsPanel.add(strategyPanel);

        // 操作按钮
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JButton toNewZHBut = new JButton("测试输入法切换");
        toNewZHBut.addActionListener(this::toNewZH);
        buttonPanel.add(toNewZHBut);

        settingsPanel.add(buttonPanel);
        return settingsPanel;
    }

    private void toNewZH(ActionEvent e) {
        try {
            String strategyName = (String) strategyComboBox.getSelectedItem();
            InputMethodSwitchStrategy strategy = StrategyFactory.createStrategy(strategyName);
            strategy.change();
        } catch (Exception ex) {
            Messages.showErrorDialog("切换失败: " + ex.getMessage(), "操作异常");
        }
    }

    @Override
    public boolean isModified() {
        String selected = (String) strategyComboBox.getSelectedItem();
        return state != null && !selected.equals(state.strategyClass);
    }

    @Override
    public void apply() {
        if (state != null) {
            state.strategyClass = (String) strategyComboBox.getSelectedItem();
        } else {
            Messages.showWarningDialog("配置状态未初始化，设置未保存", "系统警告");
        }
    }

    @Override
    public void reset() {
        if (state != null && state.strategyClass != null) {
            strategyComboBox.setSelectedItem(state.strategyClass);
        } else {
            strategyComboBox.setSelectedItem(DEFAULT_STRATEGY);
        }
    }

    @Override
    public String getDisplayName() {
        return "智能输入法切换插件";
    }
}