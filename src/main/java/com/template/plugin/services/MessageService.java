package com.template.plugin.services;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class MessageService {

    private final Map<String, String> messages = new HashMap<>();

    public MessageService() {
        // Load default generic messages
        messages.put("error.no_permission", "&cYou do not have permission.");
        messages.put("error.player_only", "&cOnly players can use this command.");
        messages.put("success.generic", "&aOperation successful.");
    }

    public void sendMessage(CommandSender sender, String key, Object... args) {
        String msg = get(key, args);
        sender.sendMessage(msg);
    }

    public String get(String key, Object... args) {
        String msg = messages.getOrDefault(key, key);
        if (args != null && args.length > 0) {
            try {
                msg = String.format(msg, args);
            } catch (Exception e) {
                // Ignore formatting errors
            }
        }
        return colorize(msg);
    }

    private String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
