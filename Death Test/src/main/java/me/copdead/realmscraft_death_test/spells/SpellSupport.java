package me.copdead.realmscraft_death_test.spells;

import me.copdead.realmscraft_death_test.Realmscraft_death_test;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpellSupport {
    private static Realmscraft_death_test plugin;

    public SpellSupport(Realmscraft_death_test plugin) {
        SpellSupport.plugin = plugin;
    }

    //****************************************
//            SPELL SUPPORT
//****************************************

    public static Entity cast(Player p) {
        return p.getWorld().rayTraceEntities(p.getEyeLocation(), p.getLocation().getDirection(), 2).getHitEntity();
    }

    public static Entity cast_include_player(Player p) {
        //If the player is looking at their feet, return player.
        if(p.getLocation().getPitch() > 70 && p.getLocation().getPitch() < 90) {
            return p;
        }
        else return cast(p);
    }

    /*private static void alertMarshals(Player caster, String spell, ChatColor Color) {
        for(String entry : RealmsCraft.getMarshalTeam().getEntries()) {
            Player p = Bukkit.getPlayer(entry);
            if(p != null && p.isOnline()) {
                p.sendMessage(Color + caster.getName() + " has used " + spell + "!" +
                        new ClickEvent(ClickEvent.Action.RUN_COMMAND, "investigate " + caster.getName()));
            }
        }
    }*/

//****************************************
//           SPELL COOLDOWNS
//****************************************

    public static void cooldownLimited(Player p, ItemStack spell, ItemStack cooldown, long delay) {
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

    public static void cooldownUnlimited(Player p, ItemStack spell, ItemStack cooldown, long delay) {
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
