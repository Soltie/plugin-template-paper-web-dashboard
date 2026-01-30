package com.template.plugin.utils;

import com.template.plugin.services.visual.interfaces.IGuiService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class Menu implements InventoryHolder, IGuiService.GuiHolder {

    protected Inventory inventory;

    public Menu(String title, int rows) {
        this.inventory = Bukkit.createInventory(this, rows * 9, title);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void onOpen(Player player) {
    }

    @Override
    public void onClose(Player player) {
    }

    @Override
    public void onClick(int slot, Player player, InventoryClickEvent event) {
        event.setCancelled(true); // Default behavior: prevent stealing
    }
}
