package com.template.plugin.events;

import com.template.plugin.services.engine.interfaces.IGameStateService.GameState;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStateChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final GameState oldState;
    private final GameState newState;

    public GameStateChangeEvent(GameState oldState, GameState newState) {
        this.oldState = oldState;
        this.newState = newState;
    }

    public GameState getOldState() {
        return oldState;
    }

    public GameState getNewState() {
        return newState;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
