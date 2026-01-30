package com.template.plugin.services.core.impl;

import com.template.plugin.core.PluginCore;
import com.template.plugin.services.core.interfaces.IPlaceholderService;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class PlaceholderServiceImpl implements IPlaceholderService {

    private boolean hasPapi = false;
    private PluginCore core;

    @Override
    public void onEnable(PluginCore core) {
        this.core = core;
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            hasPapi = true;
            core.getPlugin().getLogger().info("Hooked into PlaceholderAPI!");
        }
    }

    @Override
    public void onDisable() {
        // Unhook if necessary
    }

    @Override
    public String apply(OfflinePlayer player, String text) {
        if (text == null)
            return "";
        if (player == null)
            return text;

        // 1. External Hook (PAPI) via Reflection
        if (hasPapi) {
            try {
                Object papiClass = Class.forName("me.clip.placeholderapi.PlaceholderAPI");
                java.lang.reflect.Method method = ((Class<?>) papiClass).getMethod("setPlaceholders",
                        OfflinePlayer.class, String.class);
                Object result = method.invoke(null, player, text);
                if (result instanceof String) {
                    text = (String) result;
                }
            } catch (Exception e) {
                // Fail silently or log once
                core.getPlugin().getLogger().warning("Failed to invoke PlaceholderAPI: " + e.getMessage());
                hasPapi = false; // Disable hook to prevent span
            }
        }

        // 2. Internal Fallbacks (Essential placeholders)
        text = text.replace("{player}", player.getName());
        // Add more internal placeholders here if needed

        return text;
    }
}
