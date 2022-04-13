package me.copdead.realmscraft.death;

import me.copdead.realmscraft.RealmsCraft;
import me.copdead.realmscraft.spells.SpellItemManager;
import net.minecraft.network.protocol.game.PacketPlayOutEntity;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.EntityPose;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;



public class Death {

    private final RealmsCraft plugin;

    Death(RealmsCraft plugin) {
        this.plugin = plugin;
    }

    private static Location afterlife = new Location(Bukkit.getWorlds().get(0), 0, 0,0);


    void renderSoulless(Player p) {
        p.setInvulnerable(true);
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.playSound(p.getLocation(), Sound.ENTITY_GHAST_DEATH, 2.0F, 1.0F);
        });

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();

        Objective objective = board.registerNewObjective("soulless", "dummy", ChatColor.RED + "" + ChatColor.BOLD + "Soulless");
        objective.setDisplaySlot(DisplaySlot.BELOW_NAME);

        p.addScoreboardTag("soulless");
        soulEffect(p);
    }

    private void soulEffect(Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(p == null) this.cancel();

                p.getWorld().spawnParticle(Particle.FALLING_LAVA, p.getLocation(), 1, 1.5 ,0,1.5);
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 5L);
    }

    void playerDies(Player p) {
        p.addScoreboardTag("dead");
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 0, false, false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 5, false, false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 999999, 0, false, false));

        Inventory inv = p.getInventory();
        for(int i=0; i < inv.getSize(); i++) {
            if (inv.getItem(i) != null) {
                if (inv.getItem(i).isSimilar(SpellItemManager.death_watch_focus)) {
                    p.setGameMode(GameMode.SPECTATOR);
                    return;
                }
            }
        }

        p.teleport(getAfterlife());
    }

    public static void setAfterlife(Player p) {
        afterlife = p.getLocation();
    }

    public static void setAfterlife(Player p, double x, double y, double z) {
        afterlife = new Location(p.getWorld(), x, y, z);
    }

    public static void setAfterlife(String world, double x, double y, double z) {
        afterlife = new Location(Bukkit.getWorld(world), x, y, z);
    }

    static Location getAfterlife() {
        return afterlife;
    }

    //************************************************
    //              MANIPULATE CORPSES
    //************************************************

    static void grabCorpse() {

    }

    static void corpsePickup() {

    }

    static void corpseDropoff() {

    }

    static void corpseFollowing(Player player, EntityPlayer corpse) {
        corpse.b(EntityPose.a);

        PacketPlayOutEntity.PacketPlayOutRelEntityMove move = new PacketPlayOutEntity.PacketPlayOutRelEntityMove(
                corpse.ae(),
                (byte) ((player.getLocation().getX() - corpse.getBukkitEntity().getLocation().getX())*32*128),
                (byte) ((player.getLocation().getY() - corpse.getBukkitEntity().getLocation().getY())*32*128),
                (byte) ((player.getLocation().getZ() - corpse.getBukkitEntity().getLocation().getZ())*32*128),
                false
        );

    }
}
