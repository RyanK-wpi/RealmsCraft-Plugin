package me.copdead.realmscraft_death_test.player_class;

import me.copdead.realmscraft_death_test.menu.Menu;
import me.copdead.realmscraft_death_test.menu.MenuManager;
import me.copdead.realmscraft_death_test.menu.menus.SpellListMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class PlayerCasterClass extends PlayerClass {
    protected boolean isArmored = false;

    protected void giveSpells(Player player) {
        Menu spellList = MenuManager.getPlayerMenuUtility(player).getSpellsList();
        if(!(spellList instanceof SpellListMenu)) return;

        //spellList menu inventory not created until opened
        spellList.open();
        player.closeInventory();

        int i = 9;
        for(ItemStack spell : spellList.getInventory().getContents()) {
            if(spell == null || spell.getType() != Material.STICK) continue;

            player.getInventory().setItem(i, spell);
            i++;
        }
    }

    public void setArmored(boolean armored) {
        isArmored = armored;
    }
}
