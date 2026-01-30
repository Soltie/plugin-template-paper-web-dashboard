package com.template.plugin.services.visual.impl;

import com.template.plugin.core.PluginCore;
import com.template.plugin.services.core.interfaces.IConfigService;
import com.template.plugin.services.core.interfaces.IPlaceholderService;
import com.template.plugin.services.visual.interfaces.ITabService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TabServiceImpl implements ITabService {

    private PluginCore core;
    private IConfigService configService;
    private IPlaceholderService placeholderService;
    private int taskId = -1;

    @Override
    public void onEnable(PluginCore core) {
        this.core = core;
        this.configService = core.getService(IConfigService.class);
        this.placeholderService = core.getService(IPlaceholderService.class);
        startTask();
    }

    @Override
    public void onDisable() {
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
        }
    }

    private void startTask() {
        int interval = configService.getConfig().getVisual().getTab().getUpdateInterval();
        taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(core.getPlugin(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                updateTab(player);
            }
        }, interval, interval).getTaskId();
    }

    @Override
    public void updateTab(Player player) {
        com.template.plugin.services.visual.settings.TabSettings settings = configService.getTabSettings();

        String header = placeholderService.apply(player, String.join("\n", settings.getHeader()));
        String footer = placeholderService.apply(player, String.join("\n", settings.getFooter()));

        header = ChatColor.translateAlternateColorCodes('&', header);
        footer = ChatColor.translateAlternateColorCodes('&', footer);

        // Sending tab via API (1.13+) or Reflection for older versions
        // setPlayerListHeaderFooter is available in newer Bukkit API
        try {
            player.setPlayerListHeaderFooter(header, footer);
        } catch (NoSuchMethodError e) {
            // Fallback for older versions if needed (omit for template simplicity unless
            // requested)
        }
    }
}
