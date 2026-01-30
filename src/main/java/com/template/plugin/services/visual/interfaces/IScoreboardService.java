package com.template.plugin.services.visual.interfaces;

import com.template.plugin.services.IService;
import org.bukkit.entity.Player;

public interface IScoreboardService extends IService {
    void createScoreboard(Player player);

    void removeScoreboard(Player player);

    void updateScoreboard(Player player);
}
