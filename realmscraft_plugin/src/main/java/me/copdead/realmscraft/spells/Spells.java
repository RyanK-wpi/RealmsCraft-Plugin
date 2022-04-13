package me.copdead.realmscraft.spells;

import me.copdead.realmscraft.RealmsCraft;
import me.copdead.realmscraft.death.Body;
import net.md_5.bungee.api.chat.ClickEvent;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Levelled;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;

public class Spells {

    private static Plugin plugin = RealmsCraft.getPlugin();

//****************************************
//                SPELLS
//****************************************
//   *****   FIRST CIRCLE SPELLS   *****
    static void cure_disease(Player p, ItemStack focus) {
        //cure disease
        Entity target = cast_include_player(p);

        if((target instanceof Player || (target instanceof Zombie && target.getScoreboardTags().contains("corpse")))
                && ((LivingEntity) target).hasPotionEffect(PotionEffectType.HUNGER)) {
            //effect
            ((LivingEntity) target).removePotionEffect(PotionEffectType.HUNGER);

            //cooldown
            cooldownLimited(p, focus, SpellItemManager.cure_disease_cd, 200L);
        }
    }

    static void detect_magic(Player p, ItemStack focus) {
        //effect
        alertMarshals(p, "Detect Magic", ChatColor.DARK_PURPLE);

        //cooldown
        cooldownLimited(p, focus, SpellItemManager.detect_magic_cd, 200L);
    }

    static void disrupt_light(Player p, ItemStack focus) {
        //effect
        for(Entity e : p.getNearbyEntities(18.5, 18.5, 18.5)) { //60ft radius
            if(e instanceof Player) {
                Player target = ((Player) e);
                for(int i=0; i < target.getInventory().getSize(); i++) {
                    if(target.getInventory().getItem(i) != null) {
                        if(target.getInventory().getItem(i).isSimilar(SpellItemManager.light_focus))
                            cooldownUnlimited(target, SpellItemManager.light_focus, SpellItemManager.light_focus_cd, 5L * 60L * 20L);
                    }
                }
            }
        }

        //cooldown
        cooldownLimited(p, focus, SpellItemManager.disrupt_light_cd, 200L);
    }

    static void fighter_intuition_caster(Player p, ItemStack focus) {
        Entity target = cast(p);
        if(target instanceof Player) {
            Player t = ((Player) target);

            if(!t.getScoreboardTags().toString().contains("Fighter")) {
                p.sendMessage("This player is not a fighter!");
                return;
            }

            for(int i=0; i < t.getInventory().getSize(); i++) {
                if(t.getInventory().getItem(i) != null) {
                    if(t.getInventory().getItem(i).isSimilar(SpellItemManager.fighters_intuition_fighter_focus)) {
                        p.sendMessage("This player already has intuition!");
                        return;
                    }
                }
            }
            //effect
            //t.getInventory().removeItem(SpellItemManager.fighters_intuition_cd);
            t.getInventory().addItem(SpellItemManager.fighters_intuition_fighter_focus);

            //cooldown
            cooldownLimited(p, focus, SpellItemManager.fighters_intuition_cd, 1L);
        }
    }

    static void fighter_intuition_fighter(Player p, ItemStack focus) {
        //effect
        alertMarshals(p, "Fighter's Intuition", ChatColor.WHITE); //Should probably include the target, right?

        //cooldown
        //replaceItem(p, focus, SpellItemManager.fighters_intuition_cd);
        p.getInventory().removeItem(focus);
    }

    static void ghost_blade(Player p, ItemStack focus) {
        //effect
        ItemStack mainHand = p.getInventory().getItemInMainHand();
        ItemStack offHand = p.getInventory().getItemInOffHand();
        ItemStack hand;

        if(mainHand == focus) hand = offHand;
        else hand = mainHand;

        if(hand.getType().toString().toLowerCase().contains("sword") || hand.getType().toString().toLowerCase().contains("axe")) {
            ItemMeta meta = hand.getItemMeta();
            meta.setLore(Collections.singletonList("Ghost Blade"));
            hand.setItemMeta(meta);

            //cooldown
            cooldownLimited(p, focus, SpellItemManager.ghost_blade_cd, 200L);
        }
        p.sendMessage("You can only cast Ghost Blade on a weapon!");
    }

    //HEARTINESS implementation handled by death

    static void identify (Player p, ItemStack focus) {
        //effect
        alertMarshals(p, "Identify", ChatColor.GOLD);

        //cooldown
        cooldownLimited(p, focus, SpellItemManager.identify_cd, 300L);
    }

    static void immuneToPoisonCaster(Player p, ItemStack focus) {
        Entity target = cast_include_player(p);
        if(target == null) return;
        if(target instanceof Player) {
            //effect
            ((Player) target).getInventory().addItem(SpellItemManager.immunity_to_poison_user_focus);
            //poison calls handled by Spell event manager

            //cooldown
            cooldownLimited(p, focus, SpellItemManager.immunity_to_poison_caster_focus, 100L);
        }
    }

    static void implement(Player p, ItemStack focus) {
        //lose chosen effects on cooldown (disenchant)
        //to be implemented after spell prep methods written/understood
    }

    static void light(Player p) {
        lightFollower(p, new Location(p.getWorld(), 0, 0, 0, 0, 0));
    }

    //MENTOR cannot be used, as these are not legal realms events

    static void pas(Player p, ItemStack focus) {
        //uhhh use damage event to stop target from damaging?
        //stop them from moving towards you as well?
    }


//****************************************
//           SPELL COOLDOWNS
//****************************************
    private static void cooldownLimited(Player p, ItemStack spell, ItemStack cooldown, long delay) {
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

    private static void cooldownUnlimited(Player p, ItemStack spell, ItemStack cooldown, long delay) {
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

//****************************************
//            SPELL SUPPORT
//****************************************
    private static Entity cast(Player p) {
        return p.getWorld().rayTraceEntities(p.getEyeLocation(), p.getLocation().getDirection(), 2).getHitEntity();
    }

    private static Entity cast_include_player(Player p) {
        //If the player is looking at their feet, return player.
        if(p.getLocation().getPitch() > 70 && p.getLocation().getPitch() < 90) {
            return p;
        }
        else return cast(p);
    }

    private static void alertMarshals(Player caster, String spell, ChatColor Color) {
        for(String entry : RealmsCraft.getMarshalTeam().getEntries()) {
            Player p = Bukkit.getPlayer(entry);
            if(p != null && p.isOnline()) {
                p.sendMessage(Color + caster.getName() + " has used " + spell + "!" +
                        new ClickEvent(ClickEvent.Action.RUN_COMMAND, "investigate " + caster.getName()));
            }
        }
    }

    private static void lightFollower(Player p, Location lastLight) {

        //still holding light focus?
        if(!(p.getInventory().getItemInMainHand().isSimilar(SpellItemManager.light_focus)
                || p.getInventory().getItemInOffHand().isSimilar(SpellItemManager.light_focus))) {
            lastLight.getBlock().setType(Material.AIR);
            return;
        }

        Location checker = p.getLocation();
        checker.setX(checker.getBlockX());
        checker.setY(checker.getBlockY());
        checker.setZ(checker.getBlockZ());
        checker.setPitch(0);
        checker.setYaw(0);

        //try to light player location
        if(tryLight(checker.add(0, 1,0), lastLight) ||            //try head pos
            tryLight(checker.add(0 ,-1, 0), lastLight) ||         //try feet pos
            tryLight(checker.add(0, 2, 0), lastLight)) {          //try above head

            Bukkit.getScheduler().runTaskLater(plugin, () -> lightFollower(p, checker), 2L);
            return;
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> lightFollower(p, lastLight), 2L);
    }

    private static Boolean tryLight(Location newLoc, Location oldLoc) {
        if(newLoc.equals(oldLoc)) return true;
        Material test = newLoc.getBlock().getType();
        if(test.equals(Material.AIR) || test.equals(Material.VOID_AIR) || test.equals(Material.CAVE_AIR)) {
            oldLoc.getBlock().setType(Material.AIR);
            newLoc.getBlock().setType(Material.LIGHT);
            final Levelled level = (Levelled) newLoc.getBlock();
            level.setLevel(9);
            oldLoc.getBlock().setBlockData(level, true);
            return true;
        }
        return false;
    }

    public static void reviveCorpse(Body corpse) {
        //do something
        Player deadPlayer = Bukkit.getPlayer(corpse.getWhoDied());

        //what to do if player has logged out?
        if(deadPlayer == null) return;


        //remove body
        Bukkit.getOnlinePlayers().forEach(player -> {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
            connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, corpse.getCorpse()));             //adds corpse to player list. required for player packets
            //connection.a(new PacketPlayOutEntityDestroy(corpse.getCorpse().getBukkitEntity().getUniqueId()));
        });



        //return stealables
        Inventory inv = deadPlayer.getInventory();
        inv.addItem(corpse.getStealables()).values().forEach(itemStack -> {
            deadPlayer.getWorld().dropItem(deadPlayer.getLocation(), itemStack);                                        //drop items that wont fit in players inventory at their feet
        });
    }
}