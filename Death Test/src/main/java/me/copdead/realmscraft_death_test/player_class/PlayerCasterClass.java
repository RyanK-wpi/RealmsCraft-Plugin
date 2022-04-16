package me.copdead.realmscraft_death_test.player_class;

import me.copdead.realmscraft_death_test.menu.Menu;
import me.copdead.realmscraft_death_test.menu.MenuManager;
import me.copdead.realmscraft_death_test.menu.menus.SpellListMenu;
import me.copdead.realmscraft_death_test.spells.Spell;
import me.copdead.realmscraft_death_test.spells.SpellSelectionManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.ArrayList;
import java.util.List;

public abstract class PlayerCasterClass extends PlayerClass {
    protected boolean isArmored = false;

    protected void giveSpells(Player player) {
        Menu spellList = MenuManager.getPlayerMenuUtility(player).getSpellsList();
        if(!(spellList instanceof SpellListMenu)) return;

        //spellList menu inventory not created until opened
        spellList.open();
        player.closeInventory();

        //Create a spell list to catch duplicate spells
        List<Spell> playerSpellList = new ArrayList<>();

        int i = 9;
        for(ItemStack spell : spellList.getInventory().getContents()) {
            if(spell == null || spell.getType() != Material.WOODEN_SWORD) continue;

            int circle = spell.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "spellcircle"), PersistentDataType.INTEGER);
            String name = spell.getItemMeta().getDisplayName();
            Spell s = SpellSelectionManager.getSpells().get(circle).get(name);

            Score score = SpellSelectionManager.getSpellCounter().getObjective(name).getScore(player.getName());

            //New Spell
            if(!playerSpellList.contains(s)) {
                player.getInventory().setItem(i, spell);
                playerSpellList.add(s);

                score.setScore(s.getUses());
                i++;
            }

            //Repeated Spell
            else {
                int amount = score.getScore();
                amount = amount + s.getUses();
                score.setScore(amount);
            }
        }
    }

    public void setArmored(boolean armored) {
        isArmored = armored;
    }
}
