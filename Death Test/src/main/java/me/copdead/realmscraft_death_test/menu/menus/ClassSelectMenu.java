package me.copdead.realmscraft_death_test.menu.menus;

import me.copdead.realmscraft_death_test.menu.Menu;
import me.copdead.realmscraft_death_test.menu.MenuManager;
import me.copdead.realmscraft_death_test.menu.PlayerMenuUtility;
import me.copdead.realmscraft_death_test.spells.SpellBook;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ClassSelectMenu extends Menu {

    public ClassSelectMenu(PlayerMenuUtility pmu) {
        super(pmu);
    }

    @Override
    public String getMenuName() {
        return "Class Selection";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if(e.getCurrentItem() == null) return;
        switch (e.getCurrentItem().getType()) {
            case IRON_SWORD:
                p.closeInventory();
                p.sendMessage("You have selected Fighter, have fun!");
                p.getInventory().removeItem(new SpellBook().getBook()); //TODO this should probably go in "give fighter stuff"
                //give them fighter stuff
                break;
            case ENCHANTED_BOOK:
                p.closeInventory();
                new PathSpellSelectMenu(MenuManager.getPlayerMenuUtility(p), e.getSlot()-3).open();
                break;
        }
    }

    @Override
    public void setMenuItems() {
        //Fighter
        ItemStack fighter = new ItemStack(Material.IRON_SWORD, 1);
        ItemMeta f_meta = fighter.getItemMeta();
        f_meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Fighter");
        ArrayList<String> f_lore = new ArrayList<>();
        f_lore.add(ChatColor.AQUA + "Two armor bars");
        f_lore.add(ChatColor.AQUA + "and all weapons");
        f_meta.setLore(f_lore);
        fighter.setItemMeta(f_meta);

        //1 path
        ItemStack path1 = new ItemStack(Material.ENCHANTED_BOOK, 1);
        ItemMeta meta_1 = path1.getItemMeta();
        meta_1.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "One Path");
        ArrayList<String> lore_1 = new ArrayList<>();
        lore_1.add(ChatColor.AQUA + "One armor bar");
        lore_1.add(ChatColor.AQUA + "and 5 spells");
        meta_1.setLore(lore_1);
        path1.setItemMeta(meta_1);

        //2 path
        ItemStack path2 = new ItemStack(Material.ENCHANTED_BOOK, 2);
        ItemMeta meta_2 = path2.getItemMeta();
        meta_2.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Two Path");
        ArrayList<String> lore_2 = new ArrayList<>();
        lore_2.add(ChatColor.AQUA + "No armor bar");
        lore_2.add(ChatColor.AQUA + "and 11 spells");
        meta_2.setLore(lore_2);
        path2.setItemMeta(meta_2);

        //3 path
        ItemStack path3 = new ItemStack(Material.ENCHANTED_BOOK, 3);
        ItemMeta meta_3 = path3.getItemMeta();
        meta_3.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Three Path");
        ArrayList<String> lore_3 = new ArrayList<>();
        lore_3.add(ChatColor.AQUA + "No armor bar");
        lore_3.add(ChatColor.AQUA + "and 16 spells");
        meta_3.setLore(lore_3);
        path3.setItemMeta(meta_3);

        inventory.setItem(2, fighter);
        inventory.setItem(4, path1);
        inventory.setItem(5, path2);
        inventory.setItem(6, path3);
    }
}
