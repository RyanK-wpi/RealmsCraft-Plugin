package me.copdead.realmscraft_death_test.menu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
    }
}
