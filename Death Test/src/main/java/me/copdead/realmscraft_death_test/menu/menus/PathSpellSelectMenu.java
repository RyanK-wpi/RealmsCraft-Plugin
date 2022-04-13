package me.copdead.realmscraft_death_test.menu.menus;

import me.copdead.realmscraft_death_test.menu.Menu;
import me.copdead.realmscraft_death_test.menu.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class PathSpellSelectMenu extends Menu {
    private int path;
    private boolean armored;

    PathSpellSelectMenu(PlayerMenuUtility pmu, int path) {
        super(pmu);
        this.path = path;
        pmu.setSpellsList(this);
    }

    @Override
    public String getMenuName() {
        return "Select Spells";
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

        new CircleSpellSelectMenu(pmu, e.getSlot(), (int) Math.ceil(e.getSlot()/9.0), (int) Math.ceil(e.getSlot()/9.0)).open();

        //TODO Confirm Spell list and Back buttons -> include warnings
        //Confirm spell list -> new SpellListMenu(Menu PathSpellSelectMenu)
        //take in this menu, replace inventory, remove buttons, change handler so that clicking gets spell descritption
        //set self PMU spellList, so spellcaster clicking book afterwards opens that version instead
    }

    @Override
    public void setMenuItems() {
        ItemStack spell = new ItemStack(Material.STICK);
        ItemMeta spellMeta = spell.getItemMeta();
        spellMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Choose a Spell");
        spell.setItemMeta(spellMeta);

        ItemStack closed = new ItemStack(Material.BARRIER);
        ItemMeta closedMeta = closed.getItemMeta();
        closedMeta.setDisplayName(ChatColor.DARK_RED + "SPELL UNAVAILABLE");
        ArrayList<String> closedLore = new ArrayList<>();
        closedLore.add(ChatColor.RED + "Choose a higher path");
        closedLore.add(ChatColor.RED + "or remove your armor");
        closedLore.add(ChatColor.RED + "to use this spell");
        closedMeta.setLore(closedLore);
        closed.setItemMeta(closedMeta);

        switch (path) {
            case 3:
                inventory.setItem(5, spell);
                inventory.setItem(14, spell);
                inventory.setItem(23, spell);
                inventory.setItem(32, spell);
                inventory.setItem(41, spell);
            case 2:
                inventory.setItem(4, spell);
                inventory.setItem(13, spell);
                inventory.setItem(22, spell);
                inventory.setItem(31, spell);
                inventory.setItem(40, spell);
                inventory.setItem(49, spell);
            case 1:
                inventory.setItem(3, spell);
                inventory.setItem(12, spell);
                inventory.setItem(21, spell);
                inventory.setItem(30, spell);
                inventory.setItem(39, spell);
        }

        if(armored) {
            inventory.setItem(41, closed);
            inventory.setItem(49, closed);
            inventory.setItem(39, closed);
        }

        //set the Filler Glass
        inventory.setItem(0, FILLER_GLASS);
        inventory.setItem(1, FILLER_GLASS);
        inventory.setItem(2, FILLER_GLASS);
        //SPELLS
        inventory.setItem(6, FILLER_GLASS);
        inventory.setItem(7, FILLER_GLASS);
        inventory.setItem(8, FILLER_GLASS);

        inventory.setItem(9, FILLER_GLASS);
        inventory.setItem(10, FILLER_GLASS);
        inventory.setItem(11, FILLER_GLASS);
        //SPELLS
        inventory.setItem(15, FILLER_GLASS);
        inventory.setItem(16, FILLER_GLASS);
        inventory.setItem(17, FILLER_GLASS);

        inventory.setItem(18, FILLER_GLASS);
        inventory.setItem(19, FILLER_GLASS);
        inventory.setItem(20, FILLER_GLASS);
        //SPELLS
        inventory.setItem(24, FILLER_GLASS);
        inventory.setItem(25, FILLER_GLASS);
        inventory.setItem(26, FILLER_GLASS);

        inventory.setItem(27, FILLER_GLASS);
        inventory.setItem(28, FILLER_GLASS);
        inventory.setItem(29, FILLER_GLASS);
        //SPELLS
        inventory.setItem(33, FILLER_GLASS);
        inventory.setItem(34, FILLER_GLASS);
        inventory.setItem(35, FILLER_GLASS);

        inventory.setItem(36, FILLER_GLASS);
        inventory.setItem(37, FILLER_GLASS);
        inventory.setItem(38, FILLER_GLASS);
        //SPELLS
        inventory.setItem(42, FILLER_GLASS);
        inventory.setItem(43, FILLER_GLASS);
        inventory.setItem(44, FILLER_GLASS);

        inventory.setItem(45, FILLER_GLASS);
        inventory.setItem(46, FILLER_GLASS);
        inventory.setItem(47, FILLER_GLASS);
        //SPELLS
        inventory.setItem(51, FILLER_GLASS);
        inventory.setItem(52, FILLER_GLASS);
        inventory.setItem(53, FILLER_GLASS);
    }
}
