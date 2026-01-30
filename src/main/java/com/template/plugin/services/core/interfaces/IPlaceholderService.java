package com.template.plugin.services.core.interfaces;

import com.template.plugin.services.IService;
import org.bukkit.OfflinePlayer;

public interface IPlaceholderService extends IService {
    String apply(OfflinePlayer player, String text);
}
