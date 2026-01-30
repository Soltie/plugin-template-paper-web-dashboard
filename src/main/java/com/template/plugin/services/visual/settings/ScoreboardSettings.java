package com.template.plugin.services.visual.settings;

import org.bukkit.configuration.ConfigurationSection;
import java.util.Collections;
import java.util.List;

public class ScoreboardSettings {
    private final boolean enabled;
    private final String title;
    private final int updateInterval;
    private final List<String> lines;

    public ScoreboardSettings(ConfigurationSection config) {
        this.enabled = config.getBoolean("enabled", true);
        this.title = config.getString("title", "&b&lSCOREBOARD");
        this.updateInterval = config.getInt("update-interval", 20);
        this.lines = config.getStringList("lines");
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getTitle() {
        return title;
    }

    public int getUpdateInterval() {
        return updateInterval;
    }

    public List<String> getLines() {
        return lines != null ? lines : Collections.emptyList();
    }
}
