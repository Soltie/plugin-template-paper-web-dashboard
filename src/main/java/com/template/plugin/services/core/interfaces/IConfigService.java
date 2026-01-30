package com.template.plugin.services.core.interfaces;

import com.template.plugin.config.PluginConfig;
import com.template.plugin.services.IService;
import com.template.plugin.services.visual.settings.ChatSettings;
import com.template.plugin.services.visual.settings.ScoreboardSettings;
import com.template.plugin.services.visual.settings.TabSettings;

public interface IConfigService extends IService {
    void reload();

    PluginConfig getConfig();

    ScoreboardSettings getScoreboardSettings();

    TabSettings getTabSettings();

    ChatSettings getChatSettings();
}
