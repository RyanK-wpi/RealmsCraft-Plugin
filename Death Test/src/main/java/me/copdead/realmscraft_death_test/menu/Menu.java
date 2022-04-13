package me.copdead.realmscraft_death_test.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class Menu implements InventoryHolder {
    protected Inventory inventory = null;
    protected PlayerMenuUtility pmu;
    protected ItemStack FILLER_GLASS = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);

    public Menu(PlayerMenuUtility pmu) {
        this.pmu = pmu;
    }

    public abstract String getMenuName();

    public abstract int getSlots();

    public abstract void handleMenu(InventoryClickEvent e);

    public abstract void setMenuItems();

    public void open() {
        if(inventory != null) {
            pmu.getOwner().openInventory(inventory);
            return;
        }

        inventory = Bukkit.createInventory(this, getSlots(), getMenuName());
        this.setMenuItems();

        pmu.getOwner().openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
