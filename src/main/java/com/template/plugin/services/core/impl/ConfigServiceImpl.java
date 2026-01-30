package com.template.plugin.services.core.impl;

import com.template.plugin.config.PluginConfig;
import com.template.plugin.core.PluginCore;
import com.template.plugin.services.core.interfaces.IConfigService;
import com.template.plugin.services.visual.settings.ChatSettings;
import com.template.plugin.services.visual.settings.ScoreboardSettings;
import com.template.plugin.services.visual.settings.TabSettings;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigServiceImpl implements IConfigService {

    private PluginCore core;
    private PluginConfig config;

    private ScoreboardSettings scoreboardSettings;
    private TabSettings tabSettings;
    private ChatSettings chatSettings;

    @Override
    public void onEnable(PluginCore core) {
        this.core = core;
        this.core.getPlugin().saveDefaultConfig();

        // Ensure other configs exist
        saveResourceIfNotExists("scoreboard.yml");
        saveResourceIfNotExists("tab.yml");
        saveResourceIfNotExists("chat.yml");

        reload();
    }

    private void saveResourceIfNotExists(String name) {
        File file = new File(core.getPlugin().getDataFolder(), name);
        if (!file.exists()) {
            core.getPlugin().saveResource(name, false);
        }
    }

    @Override
    public void onDisable() {
        // No-op for config
    }

    @Override
    public void reload() {
        this.core.getPlugin().reloadConfig();
        this.config = new PluginConfig(this.core.getPlugin().getConfig());

        this.scoreboardSettings = new ScoreboardSettings(loadYaml("scoreboard.yml"));
        this.tabSettings = new TabSettings(loadYaml("tab.yml"));
        this.chatSettings = new ChatSettings(loadYaml("chat.yml"));
    }

    private YamlConfiguration loadYaml(String name) {
        File file = new File(core.getPlugin().getDataFolder(), name);
        return YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public PluginConfig getConfig() {
        return config;
    }

    @Override
    public ScoreboardSettings getScoreboardSettings() {
        return scoreboardSettings;
    }

    @Override
    public TabSettings getTabSettings() {
        return tabSettings;
    }

    @Override
    public ChatSettings getChatSettings() {
        return chatSettings;
    }
}
