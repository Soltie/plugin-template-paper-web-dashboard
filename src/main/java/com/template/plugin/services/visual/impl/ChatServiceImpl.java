package com.template.plugin.services.visual.impl;

import com.template.plugin.core.PluginCore;
import com.template.plugin.services.visual.interfaces.IChatService;
import com.template.plugin.services.core.interfaces.IConfigService;
import com.template.plugin.services.core.interfaces.IPlaceholderService;
import com.template.plugin.services.visual.settings.ChatSettings;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
        ChatSettings settings = configService.getChatSettings();
        if (!settings.isEnabled())
            return;

        Player player = event.getPlayer();
        String format = settings.getFormat();
        format = placeholderService.apply(player, format);

        com.template.plugin.services.core.interfaces.IMessageService messageService = com.template.plugin.BasePlugin
                .getInstance().getAppContext().getCore()
                .getService(com.template.plugin.services.core.interfaces.IMessageService.class);

        final String finalFormat = format;
        event.renderer((source, sourceDisplayName, message, viewer) -> {
            return messageService.parse(finalFormat.replace("{player}", player.getName()))
                    .replaceText(builder -> builder.matchLiteral("{message}").replacement(message));
        });
    }

    @Override
    public void handleJoin(Player player) {
        ChatSettings settings = configService.getChatSettings();
        if (!settings.isJoinEnabled())
            return;

        String message = settings.getJoinMessage();
        message = placeholderService.apply(player, message);
        message = message.replace("{player}", player.getName());

        Bukkit.getServer().broadcast(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
    }

    @Override
    public void handleQuit(Player player) {
        ChatSettings settings = configService.getChatSettings();
        if (!settings.isQuitEnabled())
            return;

        String message = settings.getQuitMessage();
        message = placeholderService.apply(player, message);
        message = message.replace("{player}", player.getName());

        Bukkit.getServer().broadcast(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
    }
}
