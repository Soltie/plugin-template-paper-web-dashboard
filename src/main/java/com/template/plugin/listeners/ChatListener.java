package com.template.plugin.listeners;

import com.template.plugin.services.visual.interfaces.IChatService;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    private final IChatService chatService;

    public ChatListener(IChatService chatService) {
        this.chatService = chatService;
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        chatService.handleChat(event);
    }
}
