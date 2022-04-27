package me.copdead.realmscraft.menu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class MenuEventManager implements Listener {

    public MenuEventManager() {

    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        if(event.getClickedInventory() == null) return;                         //clicked outside inventory
        if(event.getClickedInventory().getHolder() == null) return;
        InventoryHolder holder = event.getClickedInventory().getHolder();

        if(holder instanceof Menu) {
            event.setCancelled(true);

            if(event.getCurrentItem() == null) return;

            Menu menu = (Menu) holder;
            menu.handleMenu(event);
        }

        //prevent player from shift clicking items into menus
        if(event.getWhoClicked().getOpenInventory().getTopInventory().getHolder() == null) return;
        InventoryHolder holder1 = event.getWhoClicked().getOpenInventory().getTopInventory().getHolder();

        if(holder1 instanceof Menu) {
            if(event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) event.setCancelled(true);
        }
    }
}
