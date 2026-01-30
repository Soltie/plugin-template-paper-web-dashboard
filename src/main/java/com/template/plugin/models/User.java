package com.template.plugin.models;

import java.util.UUID;

/**
 * Base User model.
 */
public class User {

    private final UUID uuid;
    private final String playerName;

    // Transient fields (not persisted)
    private PlayerState state = PlayerState.SPECTATING;
    private String teamId;

    public User(UUID uuid, String playerName) {
        this.uuid = uuid;
        this.playerName = playerName;
    }

    public User(UUID uuid) {
        this(uuid, "Unknown");
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public enum PlayerState {
        PLAYING, SPECTATING, DEAD, WAITING
    }
}
