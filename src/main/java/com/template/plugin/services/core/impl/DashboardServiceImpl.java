package com.template.plugin.services.core.impl;

import com.template.plugin.core.PluginCore;
import com.template.plugin.services.core.interfaces.IDashboardService;
import com.template.plugin.services.core.interfaces.ITaskService;
import org.bukkit.Bukkit;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DashboardServiceImpl implements IDashboardService {

  private PluginCore core;
  private HttpClient httpClient;
  private String apiUrl;
  private String pollUrl;
  private boolean enabled;

  @Override
  public void onEnable(PluginCore core) {
    this.core = core;
    this.httpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_1_1)
        .connectTimeout(Duration.ofSeconds(5))
        .build();

    // Configurações vindas do config.yml
    org.bukkit.configuration.file.FileConfiguration cfg = core.getPlugin().getConfig();
    this.apiUrl = cfg.getString("dashboard.api_url", "http://localhost:3000/api/events");
    this.pollUrl = cfg.getString("dashboard.poll_url", this.apiUrl.replace("/events", "/admin/poll"));
    this.enabled = cfg.getBoolean("dashboard.enabled", true);

    if (!enabled) {
      core.getLogger().info("[Dashboard] Integration disabled via configuration.");
      return;
    }

    core.getLogger().info("[Dashboard] Bridge System Ready. API: " + apiUrl);

    // Envia evento de início de sessão
    sendEvent("SESSION_START", Map.of("version", core.getPlugin().getComponentLogger().getName()));

    // Loop de Heartbeat / Stats
    core.getService(ITaskService.class).runTimer(() -> {
      Map<String, Object> stats = new HashMap<>();
      stats.put("players_online", Bukkit.getOnlinePlayers().size());
      stats.put("max_players", Bukkit.getMaxPlayers());
      stats.put("tps", 20.0); // Placeholder, idealmente viria de um serviço de TPS
      sendEvent("SERVER_STATS", stats);
    }, 100L, 200L); // A cada 10 segundos

    // Loop de Polling de Comandos (Assíncrono)
    core.getService(ITaskService.class).runTimerAsync(this::pollCommands, 200L, 40L); // A cada 2 segundos
  }

  private void pollCommands() {
    if (!enabled || httpClient == null)
      return;

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(pollUrl))
        .GET()
        .timeout(Duration.ofSeconds(2))
        .build();

    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenAccept(response -> {
          if (response.statusCode() == 200) {
            String body = response.body();
            if (body != null && body.startsWith("[") && body.length() > 2) {
              try {
                JSONArray cmds = new JSONArray(body);
                for (int i = 0; i < cmds.length(); i++) {
                  processRemoteCommand(cmds.getJSONObject(i));
                }
              } catch (Exception e) {
                core.getLogger().warning("[Dashboard] Error parsing commands: " + e.getMessage());
              }
            }
          }
        }).exceptionally(ex -> {
          // Evitar spam de logs se falhar periodicamente
          return null;
        });
  }

  private void processRemoteCommand(JSONObject cmd) {
    String action = cmd.optString("action");
    String target = cmd.optString("target");
    Object value = cmd.opt("value");

    core.getLogger().info("[Dashboard] Remote Action: " + action + " -> " + target + " (" + value + ")");

    // Executar na main thread do Bukkit
    Bukkit.getScheduler().runTask(core.getPlugin(), () -> {
      // Aqui entra a lógica de processamento de comandos.
      // Para ser escalável, isso poderia usar um EventBus interno ou um
      // CommandDispatcher.
      switch (action) {
        case "BROADCAST":
          Bukkit.broadcast(net.kyori.adventure.text.Component.text("§6[Dashboard] §f" + value));
          break;
        case "KICK":
          org.bukkit.entity.Player p = Bukkit.getPlayer(target);
          if (p != null)
            p.kick(net.kyori.adventure.text.Component.text("§cKicked via Web Dashboard"));
          break;
        default:
          core.getLogger().warning("[Dashboard] Unknown remote action: " + action);
          break;
      }
    });
  }

  @Override
  public void onDisable() {
    this.httpClient = null;
  }

  @Override
  public void sendEvent(String type, Map<String, Object> data) {
    if (!enabled || httpClient == null)
      return;

    JSONObject payload = new JSONObject();
    payload.put("type", type);
    payload.put("timestamp", System.currentTimeMillis());
    payload.put("data", new JSONObject(data));

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(apiUrl))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
        .build();

    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
  }

  @Override
  public void sendLog(String message) {
    sendEvent("LOG", Map.of("message", message));
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
