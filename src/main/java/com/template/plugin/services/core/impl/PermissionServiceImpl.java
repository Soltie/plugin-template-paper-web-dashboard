package com.template.plugin.services.core.impl;

import com.template.plugin.core.PluginCore;
import com.template.plugin.services.core.interfaces.IPermissionService;
import org.bukkit.entity.Player;

public class PermissionServiceImpl implements IPermissionService {

    @Override
    public void onEnable(PluginCore core) {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean hasPermission(Player player, String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public boolean hasPermission(Player player, String permission, boolean defaultOp) {
        if (defaultOp && player.isOp())
            return true;
        return player.hasPermission(permission);
    }

    @Override
    public void addPermission(Player player, String permission) {
        // Implementar attachment se for interno
    }

    @Override
    public void removePermission(Player player, String permission) {
        // Implementar attachment se for interno
    }

    @Override
    public String getGroup(Player player) {
        return "default"; // Placeholder
    }
}
