package com.template.plugin.services.engine.impl;

import com.template.plugin.core.PluginCore;
import com.template.plugin.events.GameStateChangeEvent;
import com.template.plugin.services.engine.interfaces.IGameStateService;
import com.template.plugin.services.engine.interfaces.IGameStateService.GameState;
import org.bukkit.Bukkit;

public class GameStateServiceImpl implements IGameStateService {

    private GameState state = GameState.LOBBY;
    private PluginCore core;

    @Override
    public void onEnable(PluginCore core) {
        this.core = core;
    }

    @Override
    public void onDisable() {
    }

    @Override
    public GameState getState() {
        return state;
    }

    @Override
    public void setState(GameState newState) {
        if (this.state == newState)
            return;

        GameState oldState = this.state;
        this.state = newState;

        // Dispatch custom event
        Bukkit.getPluginManager().callEvent(new GameStateChangeEvent(oldState, newState));

        core.getLogger().info("[GAME] State changed from " + oldState + " to " + newState);
    }

    @Override
    public boolean isState(GameState state) {
        return this.state == state;
    }
}
