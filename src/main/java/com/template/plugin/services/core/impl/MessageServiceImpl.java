package com.template.plugin.services.core.impl;

import com.template.plugin.core.PluginCore;
import com.template.plugin.services.core.interfaces.IConfigService;
import com.template.plugin.services.core.interfaces.IMessageService;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.List;

public class MessageServiceImpl implements IMessageService {

    private PluginCore core;
    private IConfigService configService;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.legacyAmpersand();

    @Override
    public void onEnable(PluginCore core) {
        this.core = core;
        this.configService = core.getService(IConfigService.class);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void sendMessage(CommandSender target, String key, String... placeholders) {
        Component msg = getComponent(key, placeholders);
        if (msg != Component.empty()) {
            target.sendMessage(msg);
        }
    }

    @Override
    public void sendMessage(Audience target, Component component) {
        target.sendMessage(component);
    }

    @Override
    public void broadcast(String key, String... placeholders) {
        Component msg = getComponent(key, placeholders);
        if (msg != Component.empty()) {
            core.getPlugin().getServer().sendMessage(msg);
        }
    }

    @Override
    public Component getComponent(String key, String... placeholders) {
        String raw = getRaw(key);
        raw = applyPlaceholders(raw, placeholders);
        return parse(raw);
    }

    @Override
    public String get(String key, String... placeholders) {
        return LegacyComponentSerializer.legacySection().serialize(getComponent(key, placeholders));
    }

    @Override
    public List<Component> getListComponent(String key, String... placeholders) {
        // Mocking list retrieval - in real case would come from config
        return new ArrayList<>();
    }

    @Override
    public Component parse(String text) {
        if (text == null || text.isEmpty())
            return Component.empty();
        // Detect if it's legacy or MiniMessage. Simple check: if contains & or § use
        // legacy
        if (text.contains("&") || text.contains("§")) {
            return legacySerializer.deserialize(text);
        }
        return miniMessage.deserialize(text);
    }

    @Override
    @Deprecated
    public String colorize(String text) {
        return LegacyComponentSerializer.legacySection().serialize(legacySerializer.deserialize(text));
    }

    private String getRaw(String key) {
        // Integration with configService
        if (configService != null && configService.getConfig() != null) {
            if (key.equalsIgnoreCase("prefix")) {
                return configService.getConfig().getMessages().getPrefix();
            } else if (key.equalsIgnoreCase("no_permission")) {
                return configService.getConfig().getMessages().getNoPermission();
            } else if (key.equalsIgnoreCase("reload_success")) {
                return configService.getConfig().getMessages().getReloadSuccess();
            }
        }
        return key;
    }

    private String applyPlaceholders(String message, String... placeholders) {
        if (placeholders == null)
            return message;
        for (int i = 0; i < placeholders.length - 1; i += 2) {
            String k = placeholders[i];
            String v = placeholders[i + 1];
            message = message.replace(k, v);
        }
        return message;
    }
}
