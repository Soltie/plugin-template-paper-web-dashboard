package com.template.plugin.services.visual.settings;

import org.bukkit.configuration.ConfigurationSection;
import java.util.Collections;
import java.util.List;

public class TabSettings {
    private final boolean enabled;
    private final int updateInterval;
    private final List<String> header;
    private final List<String> footer;

    public TabSettings(ConfigurationSection config) {
        this.enabled = config.getBoolean("enabled", true);
        this.updateInterval = config.getInt("update-interval", 60);
        this.header = config.getStringList("header");
        this.footer = config.getStringList("footer");
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getUpdateInterval() {
        return updateInterval;
    }

    public List<String> getHeader() {
        return header != null ? header : Collections.emptyList();
    }

    public List<String> getFooter() {
        return footer != null ? footer : Collections.emptyList();
    }
}
