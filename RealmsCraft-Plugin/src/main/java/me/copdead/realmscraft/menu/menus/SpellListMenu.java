package me.copdead.realmscraft.menu.menus;

import me.copdead.realmscraft.RealmsCraft;
import me.copdead.realmscraft.menu.Menu;
import me.copdead.realmscraft.menu.PlayerMenuUtility;
import me.copdead.realmscraft.spells.SpellSelectionManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;

public class SpellListMenu extends Menu {
    private static final RealmsCraft plugin = (RealmsCraft) Bukkit.getPluginManager().getPlugin("Realmscraft_death_test");
    private Inventory spellSelectInv;

    SpellListMenu(PlayerMenuUtility pmu, Inventory spellSelectInv) {
        super(pmu);
        this.spellSelectInv = spellSelectInv;
    }

    @Override
    public String getMenuName() {
        return "Your Spells";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        if(e.getCurrentItem() == null
                || e.getCurrentItem().getType() == Material.AIR
                || e.getCurrentItem().getType() == Material.BARRIER
                || e.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE) return;

        int circle = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "spellcircle"), PersistentDataType.INTEGER);
        String name = e.getCurrentItem().getItemMeta().getDisplayName();
        e.getWhoClicked().sendMessage(SpellSelectionManager.getSpells().get(circle).get(name).getDescription());
    }

    @Override
    public void setMenuItems() {
        for(int i = 0; i < getSlots(); i++) {
            inventory.setItem(i, spellSelectInv.getItem(i));
        }
        //remove buttons
        inventory.setItem(7, FILLER_GLASS);
        inventory.setItem(46, FILLER_GLASS);
        inventory.setItem(52, FILLER_GLASS);
    }
}
