package com.template.plugin.services.engine.interfaces;

import com.template.plugin.services.IService;

public interface IGameStateService extends IService {

    enum GameState {
        LOBBY, STARTING, RUNNING, ENDING, RESETTING
    }

    GameState getState();

    void setState(GameState state);

    boolean isState(GameState state);
}
