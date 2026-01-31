package com.template.plugin.services.visual.interfaces;

import com.template.plugin.services.IService;
import io.papermc.paper.event.player.AsyncChatEvent;

public interface IChatService extends IService {
    void handleChat(AsyncChatEvent event);

    void handleJoin(org.bukkit.entity.Player player);

    void handleQuit(org.bukkit.entity.Player player);
}
