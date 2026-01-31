package com.template.plugin.listeners;

import com.template.plugin.core.PluginCore;
import com.template.plugin.services.core.interfaces.IUserService;
import com.template.plugin.services.visual.interfaces.IChatService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Listener de infraestrutura para gerenciar o ciclo de vida dos dados do
 * jogador.
 * Não deve conter regras de negócio.
 */
public class PlayerLifecycleListener implements Listener {

    private final PluginCore core;
    private final IUserService<?> userService;
    private final IChatService chatService;

    public PlayerLifecycleListener(PluginCore core) {
        this.core = core;
        this.userService = core.getService(IUserService.class);
        this.chatService = core.getService(IChatService.class);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED)
            return;

        try {
            // Carrega dados de forma sincrona ou espera o futuro (estamos em thread async)
            // Se o load for muito pesado, o IUserService já deve tratar isso.
            userService.loadUser(event.getUniqueId()).join();
        } catch (Exception e) {
            core.getLogger().severe("Failed to load user data for " + event.getName());
            e.printStackTrace();
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Data load error. Contact admin.");
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.joinMessage(null); // Disable default message
        chatService.handleJoin(player);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.quitMessage(null); // Disable default message
        chatService.handleQuit(player);

        // Salva e invalida cache
        java.util.UUID uuid = player.getUniqueId();
        userService.saveUser(uuid)
                .thenRun(() -> {
                    userService.invalidate(uuid);
                    core.getService(com.template.plugin.services.engine.interfaces.ICooldownService.class).clear(uuid);
                });
    }
}
