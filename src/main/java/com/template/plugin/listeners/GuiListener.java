package com.template.plugin.listeners;

import com.template.plugin.services.visual.interfaces.IGuiService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

public class GuiListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory() == null)
            return;
        if (!(event.getWhoClicked() instanceof org.bukkit.entity.Player))
            return;

        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof IGuiService.GuiHolder) {
            ((IGuiService.GuiHolder) holder).onClick(event.getRawSlot(),
                    (org.bukkit.entity.Player) event.getWhoClicked(), event);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory() == null)
            return;
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof IGuiService.GuiHolder) {
            ((IGuiService.GuiHolder) holder).onClose((org.bukkit.entity.Player) event.getPlayer());
        }
    }

    // Optional: onOpen if strictly needed, but usually handled in openMenu service
    // method
}
