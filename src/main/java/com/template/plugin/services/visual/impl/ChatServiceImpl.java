package com.template.plugin.services.visual.impl;

import com.template.plugin.core.PluginCore;
import com.template.plugin.services.visual.interfaces.IChatService;
import com.template.plugin.services.core.interfaces.IConfigService;
import com.template.plugin.services.core.interfaces.IPlaceholderService;

public class ChatServiceImpl implements IChatService {

    private IConfigService configService;
    private IPlaceholderService placeholderService;

    @Override
    public void onEnable(PluginCore core) {
        this.configService = core.getService(IConfigService.class);
        this.placeholderService = core.getService(IPlaceholderService.class);
    }

    @Override
    public void onDisable() {
        // Nothing to disable
    }

    @Override
    public void handleChat(io.papermc.paper.event.player.AsyncChatEvent event) {
        com.template.plugin.services.visual.settings.ChatSettings settings = configService.getChatSettings();
        if (!settings.isEnabled())
            return;

        org.bukkit.entity.Player player = event.getPlayer();
        String format = settings.getFormat(); // e.g. "<player>: <message>"

        com.template.plugin.services.core.interfaces.IMessageService messageService = com.template.plugin.BasePlugin
                .getInstance().getAppContext().getCore()
                .getService(com.template.plugin.services.core.interfaces.IMessageService.class);

        // 1. Resolve Paper placeholders in format
        format = placeholderService.apply(player, format);

        // 2. Prepare for modern renderer
        // Paper's AsyncChatEvent handles message and renderer separately.
        // We can use a custom renderer or just modify the Component.

        final String finalFormat = format;
        event.renderer((source, sourceDisplayName, message, viewer) -> {
            // Re-parse format as component with placeholders
            // Better yet, just use a simple template
            return messageService.parse(finalFormat.replace("{player}", player.getName()))
                    .replaceText(builder -> builder.matchLiteral("{message}").replacement(message));
        });
    }
}
