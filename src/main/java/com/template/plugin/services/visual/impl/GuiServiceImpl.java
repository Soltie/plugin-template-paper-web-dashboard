package com.template.plugin.services.visual.impl;

import com.template.plugin.core.PluginCore;
import com.template.plugin.services.visual.interfaces.IGuiService;
import org.bukkit.entity.Player;

public class GuiServiceImpl implements IGuiService {

    private PluginCore core;

    @Override
    public void onEnable(PluginCore core) {
        this.core = core;
    }

    @Override
    public void onDisable() {
        // Close all open menus if needed, but usually safe to leave open or close via
        // loop
    }

    @Override
    public void openMenu(Player player, GuiHolder menu) {
        if (menu != null && menu.getInventory() != null) {
            player.openInventory(menu.getInventory());
            menu.onOpen(player);
        }
    }
}
