package com.template.plugin.services.visual.interfaces;

import com.template.plugin.services.IService;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public interface IGuiService extends IService {
    void openMenu(Player player, GuiHolder menu);

    interface GuiHolder extends InventoryHolder {
        void onClick(int slot, Player player, org.bukkit.event.inventory.InventoryClickEvent event);

        void onOpen(Player player);

        void onClose(Player player);

        Inventory getInventory();
    }
}
