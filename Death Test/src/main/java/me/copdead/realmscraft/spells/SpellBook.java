package me.copdead.realmscraft.spells;

import me.copdead.realmscraft.menu.MenuManager;
import me.copdead.realmscraft.menu.menus.ClassSelectMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class SpellBook {

    public SpellBook() {
    }

    public ItemStack getBook() {
        ItemStack spellbook = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta bookmeta = spellbook.getItemMeta();
        bookmeta.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Class Selection Book");
        ArrayList<String> booklore = new ArrayList<>();
        booklore.add(ChatColor.LIGHT_PURPLE + "Use this to begin");
        booklore.add(ChatColor.LIGHT_PURPLE + "class selection");
        bookmeta.setLore(booklore);
        spellbook.setItemMeta(bookmeta);

        return spellbook;
    }

    public void useBook(Player caster) {
        if (MenuManager.getPlayerMenuUtility(caster).getSpellsList() == null) {
            new ClassSelectMenu(MenuManager.getPlayerMenuUtility(caster)).open();
            return;
        }

        MenuManager.getPlayerMenuUtility(caster).getSpellsList().open();
    }
}
