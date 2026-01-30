package com.template.plugin.services.engine.impl;

import com.template.plugin.core.PluginCore;
import com.template.plugin.models.User;
import com.template.plugin.services.engine.interfaces.ITeamService;
import com.template.plugin.services.core.interfaces.IUserService;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TeamServiceImpl implements ITeamService {

    private final Map<String, Set<UUID>> teams = new ConcurrentHashMap<>();
    private IUserService<?> userService;

    @Override
    public void onEnable(PluginCore core) {
        this.userService = core.getService(IUserService.class);
    }

    @Override
    public void onDisable() {
        teams.clear();
    }

    @Override
    public void createTeam(String id) {
        teams.putIfAbsent(id, Collections.newSetFromMap(new ConcurrentHashMap<>()));
    }

    @Override
    public void deleteTeam(String id) {
        Set<UUID> members = teams.remove(id);
        if (members != null) {
            for (UUID uuid : members) {
                userService.getUser(uuid).ifPresent(u -> ((User) u).setTeamId(null));
            }
        }
    }

    @Override
    public void addToTeam(String teamId, Player player) {
        removeFromTeam(player);
        createTeam(teamId);

        teams.get(teamId).add(player.getUniqueId());
        userService.getUser(player.getUniqueId()).ifPresent(u -> ((User) u).setTeamId(teamId));
    }

    @Override
    public void removeFromTeam(Player player) {
        String teamId = getTeam(player);
        if (teamId != null) {
            Set<UUID> members = teams.get(teamId);
            if (members != null)
                members.remove(player.getUniqueId());

            userService.getUser(player.getUniqueId()).ifPresent(u -> ((User) u).setTeamId(null));
        }
    }

    @Override
    public Collection<UUID> getTeamMembers(String teamId) {
        return teams.getOrDefault(teamId, Collections.emptySet());
    }

    @Override
    public String getTeam(Player player) {
        return userService.getUser(player.getUniqueId())
                .map(u -> ((User) u).getTeamId())
                .orElse(null);
    }

    @Override
    public boolean hasTeam(Player player) {
        return getTeam(player) != null;
    }
}
