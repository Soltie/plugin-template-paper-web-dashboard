package com.template.plugin.services.visual.settings;

import org.bukkit.configuration.ConfigurationSection;
import java.util.Collections;
import java.util.List;

public class ChatSettings {
    private final boolean enabled;
    private final String format;
    private final String consoleFormat;
    private final List<String> hoverTooltip;

    public ChatSettings(ConfigurationSection config) {
        this.enabled = config.getBoolean("enabled", true);
        this.format = config.getString("format", "&f{player}&7: &f{message}");
        this.consoleFormat = config.getString("console-format", "[CHAT] {player}: {message}");
        this.hoverTooltip = config.getStringList("hover-tooltip");
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getFormat() {
        return format;
    }

    public String getConsoleFormat() {
        return consoleFormat;
    }

    public List<String> getHoverTooltip() {
        return hoverTooltip != null ? hoverTooltip : Collections.emptyList();
    }
}
