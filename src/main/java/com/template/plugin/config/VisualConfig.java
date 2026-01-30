package com.template.plugin.config;

import org.bukkit.configuration.file.FileConfiguration;
import java.util.List;

public class VisualConfig {

    private final ScoreboardSettings scoreboard;
    private final ChatSettings chat;
    private final TabSettings tab;

    public VisualConfig(FileConfiguration config) {
        this.scoreboard = new ScoreboardSettings(config);
        this.chat = new ChatSettings(config);
        this.tab = new TabSettings(config);
    }

    public ScoreboardSettings getScoreboard() {
        return scoreboard;
    }

    public ChatSettings getChat() {
        return chat;
    }

    public TabSettings getTab() {
        return tab;
    }

    public static class ScoreboardSettings {
        private final boolean enabled;
        private final String title;
        private final List<String> lines;
        private final int updateInterval;

        public ScoreboardSettings(FileConfiguration config) {
            this.enabled = config.getBoolean("scoreboard.enabled", true);
            this.title = config.getString("scoreboard.title", "&b&lMY SERVER");
            this.lines = config.getStringList("scoreboard.lines");
            this.updateInterval = config.getInt("scoreboard.update_interval", 20);
        }

        public boolean isEnabled() {
            return enabled;
        }

        public String getTitle() {
            return title;
        }

        public List<String> getLines() {
            return lines;
        }

        public int getUpdateInterval() {
            return updateInterval;
        }
    }

    public static class ChatSettings {
        private final boolean enabled;
        private final String format;
        // Future: Interaction events

        public ChatSettings(FileConfiguration config) {
            this.enabled = config.getBoolean("chat.enabled", true);
            this.format = config.getString("chat.format", "&7{player}&8: &f{message}");
        }

        public boolean isEnabled() {
            return enabled;
        }

        public String getFormat() {
            return format;
        }
    }

    public static class TabSettings {
        private final boolean enabled;
        private final String header;
        private final String footer;
        private final int updateInterval;

        public TabSettings(FileConfiguration config) {
            this.enabled = config.getBoolean("tablist.enabled", true);
            this.header = config.getString("tablist.header", "&bWelcome to &fMy Server");
            this.footer = config.getString("tablist.footer", "&7Players: &b{online}");
            this.updateInterval = config.getInt("tablist.update_interval", 60);
        }

        public boolean isEnabled() {
            return enabled;
        }

        public String getHeader() {
            return header;
        }

        public String getFooter() {
            return footer;
        }

        public int getUpdateInterval() {
            return updateInterval;
        }
    }
}
