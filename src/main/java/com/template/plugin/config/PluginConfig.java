package com.template.plugin.config;

import org.bukkit.configuration.file.FileConfiguration;

public class PluginConfig {

    private final General general;
    private final Messages messages;
    private final Storage storage;
    private final VisualConfig visual;

    public PluginConfig(FileConfiguration config) {
        config.addDefault("dashboard.enabled", true);
        config.addDefault("dashboard.api_url", "http://localhost:3000/api/events");
        config.addDefault("dashboard.poll_url", "http://localhost:3000/api/admin/poll");
        config.options().copyDefaults(true);
        this.general = new General(config);
        this.messages = new Messages(config);
        this.storage = new Storage(config);
        this.visual = new VisualConfig(config);
    }

    public General getGeneral() {
        return general;
    }

    public Messages getMessages() {
        return messages;
    }

    public Storage getStorage() {
        return storage;
    }

    public VisualConfig getVisual() {
        return visual;
    }

    public static class General {
        private final boolean debugMode;

        public General(FileConfiguration config) {
            this.debugMode = config.getBoolean("general.debug_mode", false);
        }

        public boolean isDebugMode() {
            return debugMode;
        }
    }

    public static class Messages {
        private final String prefix;
        private final String reloadSuccess;
        private final String noPermission;

        public Messages(FileConfiguration config) {
            this.prefix = config.getString("messages.prefix", "&8[&bTemplate&8] &7");
            this.reloadSuccess = config.getString("messages.reload_success", "Config reloaded.");
            this.noPermission = config.getString("messages.no_permission", "&cNo permission.");
        }

        public String getPrefix() {
            return prefix;
        }

        public String getReloadSuccess() {
            return reloadSuccess;
        }

        public String getNoPermission() {
            return noPermission;
        }
    }

    public static class Storage {
        private final String type;

        public Storage(FileConfiguration config) {
            this.type = config.getString("storage.type", "YAML");
        }

        public String getType() {
            return type;
        }
    }
}
