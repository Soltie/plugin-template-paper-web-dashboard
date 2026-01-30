package com.template.plugin.services.engine.interfaces;

import com.template.plugin.services.IService;
import org.bukkit.entity.Player;
import java.util.Collection;
import java.util.UUID;

public interface ITeamService extends IService {
    void createTeam(String id);

    void deleteTeam(String id);

    void addToTeam(String teamId, Player player);

    void removeFromTeam(Player player);

    Collection<UUID> getTeamMembers(String teamId);

    String getTeam(Player player);

    boolean hasTeam(Player player);
}
