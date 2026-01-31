package com.template.plugin.services.visual.settings;

import org.bukkit.configuration.ConfigurationSection;
import java.util.Collections;
import java.util.List;

public class ChatSettings {
    private final boolean enabled;
    private final String format;
    private final String consoleFormat;
    private final List<String> hoverTooltip;

    // Join/Quit settings
    private final boolean joinEnabled;
    private final String joinMessage;
    private final boolean quitEnabled;
    private final String quitMessage;

    public ChatSettings(ConfigurationSection config) {
        this.enabled = config.getBoolean("enabled", true);
        this.format = config.getString("format", "&f{player}&7: &f{message}");
        this.consoleFormat = config.getString("console-format", "[CHAT] {player}: {message}");
        this.hoverTooltip = config.getStringList("hover-tooltip");

        ConfigurationSection joinSection = config.getConfigurationSection("join-message");
        if (joinSection != null) {
            this.joinEnabled = joinSection.getBoolean("enabled", true);
            this.joinMessage = joinSection.getString("message", "&8[&a+&8] &7{player} joined!");
        } else {
            this.joinEnabled = true;
            this.joinMessage = "&8[&a+&8] &7{player} joined!";
        }

        ConfigurationSection quitSection = config.getConfigurationSection("quit-message");
        if (quitSection != null) {
            this.quitEnabled = quitSection.getBoolean("enabled", true);
            this.quitMessage = quitSection.getString("message", "&8[&c-&8] &7{player} left.");
        } else {
            this.quitEnabled = true;
            this.quitMessage = "&8[&c-&8] &7{player} left.";
        }
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

    public boolean isJoinEnabled() {
        return joinEnabled;
    }

    public String getJoinMessage() {
        return joinMessage;
    }

    public boolean isQuitEnabled() {
        return quitEnabled;
    }

    public String getQuitMessage() {
        return quitMessage;
    }
}
