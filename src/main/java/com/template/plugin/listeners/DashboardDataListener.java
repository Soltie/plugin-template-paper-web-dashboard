package com.template.plugin.listeners;

import com.template.plugin.core.PluginCore;
import com.template.plugin.services.core.interfaces.IDashboardService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Listener responsável por capturar eventos do servidor e encaminhar para o
 * DashboardService.
 */
public class DashboardDataListener implements Listener {

  private final IDashboardService dashboardService;

  public DashboardDataListener(PluginCore core) {
    this.dashboardService = core.getService(IDashboardService.class);
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onChat(AsyncPlayerChatEvent event) {
    Map<String, Object> data = new HashMap<>();
    data.put("player", event.getPlayer().getName());
    data.put("message", event.getMessage());
    dashboardService.sendEvent("CHAT", data);
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onJoin(PlayerJoinEvent event) {
    Map<String, Object> data = new HashMap<>();
    data.put("player", event.getPlayer().getName());
    data.put("action", "JOIN");
    dashboardService.sendEvent("PLAYER_ACTIVITY", data);
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onQuit(PlayerQuitEvent event) {
    Map<String, Object> data = new HashMap<>();
    data.put("player", event.getPlayer().getName());
    data.put("action", "QUIT");
    dashboardService.sendEvent("PLAYER_ACTIVITY", data);
  }
}
