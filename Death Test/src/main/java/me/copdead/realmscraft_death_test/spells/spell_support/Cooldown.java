package me.copdead.realmscraft_death_test.spells.spell_support;

import me.copdead.realmscraft_death_test.Realmscraft_death_test;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Cooldown {
    Realmscraft_death_test plugin = (Realmscraft_death_test) Bukkit.getPluginManager().getPlugin("Realmscraft_death_test");

    //Change cooldown to damage values on item?

    //Number of uses left displayed as a Title Screen? (number defined in implementation, given to cooldown interface)

    static void cooldownLimited(Player p, ItemStack spell, ItemStack cooldown, long delay) {
        int amount = spell.getAmount();
        if(amount > 1) {
            if(delay == 1L) {
                spell.setAmount(amount - 1);
                return;
            }
            replaceItem(p, spell, cooldown);
            Bukkit.getScheduler().runTaskLater(plugin, () ->
                    cooldownHelper(p, spell, cooldown, amount - 1), delay);
        }
        if(amount == 1) {
            //Give out of X item!
            p.getInventory().setItemInMainHand(cooldown);
        }
    }

    static void cooldownUnlimited(Player p, ItemStack spell, ItemStack cooldown, long delay) {
        replaceItem(p, spell, cooldown);
        Bukkit.getScheduler().runTaskLater(plugin, () ->
                cooldownHelper(p, spell, cooldown, 1), delay);
    }

    private static void cooldownHelper(Player p, ItemStack spell, ItemStack cooldown, int amount) {
        if(p == null) return; //Make sure the player is still online
        //future dev: pass the cooldown to an eventhandler hashmap to return items to players when they relog?

        spell.setAmount(amount);
        replaceItem(p, cooldown, spell);
    }

    private static void replaceItem(Player p, ItemStack itemToReplace, ItemStack replacingItem) {
        for(int i=0; i < p.getInventory().getSize(); i++) {
            if(p.getInventory().getItem(i) != null) {
                if(p.getInventory().getItem(i).isSimilar(itemToReplace))
                    p.getInventory().setItem(i, replacingItem);
            }
        }
    }
}
