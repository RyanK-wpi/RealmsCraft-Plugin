package me.copdead.realmscraft_death_test.spells.spell_support;

import me.copdead.realmscraft_death_test.Realmscraft_death_test;
import me.copdead.realmscraft_death_test.spells.Spell;
import me.copdead.realmscraft_death_test.spells.SpellSelectionManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Score;

public interface Cooldown {
    Realmscraft_death_test plugin = (Realmscraft_death_test) Bukkit.getPluginManager().getPlugin("Realmscraft_death_test");

    //Number of uses left displayed as a Title Screen? (number defined in implementation, given to cooldown interface)

    //Store amount in scoreboards -> named with spell. (add int amount to spell, have it initialize scoreboards in playerCasterClass

    //KNOWN BUGS - IF ITEM IS PICKED UP IN INVENTORY, BUKKITRUNNABLE LOSES TRACK OF IT


    static void cooldownLimited(Player p, long delay, Spell spell) {
        Score score = SpellSelectionManager.getSpellCounter().getObjective(spell.getName()).getScore(p.getName());
        int amount = score.getScore();
        if(amount > 1) {
            if(delay == 0) {
                score.setScore(amount - 1);
                TextComponent message = new TextComponent("Uses: " + (amount - 1));
                message.setItalic(true);
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, message);
                return;
            }
            score.setScore(amount - 1);
            cooldown(p, delay);

            //update amount message
            TextComponent message = new TextComponent("Uses: " + (amount - 1));
            message.setItalic(true);
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, message);
        }
        //Out of Spells!
        if(amount == 1) {
            p.getInventory().setItemInMainHand(spell.getOutOfSpells());
        }
    }

    static void cooldown(Player player, double delay) {
        final ItemStack[] spell = {player.getInventory().getItemInMainHand()};
        final Player[] caster = {player};
        final int[] timeout = {0};

        new BukkitRunnable() {
            double progress = 59;
            double time = 59/(delay * 20); //seconds * ticks per second

            ItemMeta spellMeta = spell[0].getItemMeta();

            @Override
            public void run() {
                if(timeout[0] > (60 * 20)) cancel(); //if cant find player after 1 min (60s * 20tick) cancel

                //if the player is not online
                if(!caster[0].isOnline()) {
                    timeout[0]++;
                    return; //do not progress the timer if you cant find the player!
                }

                boolean playerRelogged = true;
                for(Player p : Bukkit.getOnlinePlayers()) {
                    if(!p.equals(caster[0])) continue;

                    playerRelogged = false;
                    break;
                }

                //check if they have logged back in
                if(playerRelogged) {
                    boolean playerLost = true;
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        if(!p.getName().equals(caster[0].getName())) continue;

                        caster[0] = p;
                        playerLost = false;
                        break;
                    }
                    if(playerLost) {
                        timeout[0]++;
                        return; //do not progress the timer if you cant find the player!
                    }
                }

                //if spell is not in the player's inventory
                boolean spellmoved = true;
                for(ItemStack item : caster[0].getInventory().getContents()) {
                    if(item == null) continue;
                    if (item.equals(spell[0])) {
                        spellmoved = false;
                        break;
                    }
                }

                //look for the spell, and re-assign it
                if(spellmoved) {
                    boolean spellLost = true;
                    for(ItemStack item : caster[0].getInventory().getContents()) {
                        if(item == null) continue;
                        if(!item.getType().equals(Material.WOODEN_SWORD)) continue;
                        if(item.getItemMeta() == null || !item.getItemMeta().getDisplayName().equals(spell[0].getItemMeta().getDisplayName())) continue;

                        spell[0] = item;
                        spellLost = false;
                        break;
                    }
                    if(spellLost) {
                        timeout[0]++;
                        return; //do not progress timer if you cant find the spell!
                    }
                }

                ((Damageable) spellMeta).setDamage((int) Math.floor(progress));
                spell[0].setItemMeta(spellMeta);
                progress = progress - time;

                if(progress <= 0) {
                    ((Damageable) spellMeta).setDamage(0);
                    spell[0].setItemMeta(spellMeta);
                    cancel();
                }

                //success, cancel timeout!
                timeout[0] = 0;
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
