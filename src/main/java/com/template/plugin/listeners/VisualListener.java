package com.template.plugin.listeners;

import com.template.plugin.core.PluginCore;
import com.template.plugin.services.visual.interfaces.IScoreboardService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class VisualListener implements Listener {

    private final IScoreboardService scoreboardService;

    public VisualListener(PluginCore core) {
        this.scoreboardService = core.getService(IScoreboardService.class);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        scoreboardService.createScoreboard(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        scoreboardService.removeScoreboard(event.getPlayer());
    }
}
