package com.template.plugin.services.visual.interfaces;

import com.template.plugin.services.IService;
import org.bukkit.entity.Player;

public interface ITabService extends IService {
    void updateTab(Player player);
}
