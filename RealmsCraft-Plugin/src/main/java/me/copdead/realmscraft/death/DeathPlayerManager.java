package me.copdead.realmscraft.death;

import me.copdead.realmscraft.RealmsCraft;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class DeathPlayerManager {
    private final RealmsCraft plugin;
    private Location afterlife;

    public DeathPlayerManager(RealmsCraft plugin) {
        this.plugin = plugin;
        this.afterlife = readAfterlife();
    }

    void playerDies(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0, false, false, false));
        //Add to dead players list? Possible conflicts with multiple deaths?
    }

    void playerRespawned(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 4, false, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 0, false, false, false));
    }

    public void playerRevived(Player player, Body body) {
        player.teleport(body.getCorpse().getBukkitEntity().getLocation().add(0, 0.2,0));
        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation().add(0, 1, 0), 15, 0.5, 0.5, 0.5);

        //return players inventory, and dump extra items at feet
        //close body inventory if players are in it
        Bukkit.getOnlinePlayers().forEach(p -> {if(p.getOpenInventory().getTopInventory() == body.getStealables()) p.closeInventory();});
        if(!body.getStealables().isEmpty()) {
            Map<Integer, ItemStack> overflow = player.getInventory().addItem(
                    Arrays.stream(body.getStealables().getContents()).filter(Objects::nonNull).toArray(ItemStack[]::new));
            overflow.values().forEach(itemStack -> player.getWorld().dropItemNaturally(player.getLocation(), itemStack));
        }

        //remove body
        plugin.getBodyManager().sendLeavePacket(body);
    }


    //************************************************
    //            AFTERLIFE MANAGEMENT
    //************************************************

    public void setAfterlife(Player p) {
        afterlife = p.getLocation();
        storeAfterlife();
    }

    public void setAfterlife(Player p, double x, double y, double z) {
        afterlife = new Location(p.getWorld(), x, y, z);
        storeAfterlife();
    }

    public void setAfterlife(String world, double x, double y, double z) {
        afterlife = new Location(Bukkit.getWorld(world), x, y, z);
        storeAfterlife();
    }

    Location getAfterlife() {
        return afterlife;
    }

    private void storeAfterlife() {
        plugin.getDataManager().getConfig().set("afterlife.location", afterlife);
        plugin.getDataManager().saveConfig();
    }

    private Location readAfterlife() {
        if(plugin.getDataManager().getConfig().contains("afterlife.location")) {
            return plugin.getDataManager().getConfig().getLocation("afterlife.location");
        }
        return new Location(Bukkit.getWorlds().get(0), 0, 0,0);
    }
}
