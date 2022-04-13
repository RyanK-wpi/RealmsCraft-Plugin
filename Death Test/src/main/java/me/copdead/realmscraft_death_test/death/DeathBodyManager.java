package me.copdead.realmscraft_death_test.death;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.copdead.realmscraft_death_test.Realmscraft_death_test;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
//import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.scores.ScoreboardTeam;
import net.minecraft.world.scores.ScoreboardTeamBase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R1.scoreboard.CraftScoreboard;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;

public class DeathBodyManager {
    private static List<Body> bodies;
    private static List<Player> dragging;
    private static List<Player> searching;
    private final Realmscraft_death_test plugin;

    private static final double CORPSE_SPEED = 0.2;         //Must be < 8!

    public DeathBodyManager(Realmscraft_death_test plugin) {
        bodies = new ArrayList<>();
        dragging = new ArrayList<>();
        searching = new ArrayList<>();
        this.plugin = plugin;
    }

    public List<Body> getBodies() {
        return bodies;
    }

    void newBody(Player p)  {
        Body body = new Body();
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();                                        //Get the Server
        WorldServer world = ((CraftWorld) p.getWorld()).getHandle();                                                    //Get World Player is in
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), p.getName());                                      //just use player name, names cannot exceed 16Char

        //Set corpse texture to player
        String[] skin = getSkin(p);
        gameProfile.getProperties().put("textures", new Property("textures", skin[0], skin[1]));

        EntityPlayer corpse = new EntityPlayer(server, world, gameProfile);

        //set position lying down at lowest block
        Location deathLoc = p.getLocation();
        deathLoc.setY(deathLoc.getBlockY());
        while(deathLoc.getBlock().isPassable()) {
            deathLoc.subtract(0, 1,0);
        }
        corpse.b(deathLoc.getX(), deathLoc.getY() + 0.85, deathLoc.getZ(), deathLoc.getYaw(), deathLoc.getPitch());

        corpse.b(EntityPose.d);                                                                                         //Crawling pose has larger hitbox and centered nametag

        //Overlays (second layer of skins)
        DataWatcher watcher = corpse.ai();
        byte b = 0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40;                                                        //From the wiki, bit masks for cape/jacket/sleeves/pants/hat
        watcher.b(DataWatcherRegistry.a.a(17), b);
        watcher.a(DataWatcherRegistry.s.a(0));                                                                    //registers pose
        // corpse.ai().b(new DataWatcherObject<>(17, DataWatcherRegistry.a), (byte) 127);                               //also works, but less clear imo

        //create body and add to the list
        body.setStealables(getPlayerStealables(p));
        body.setCorpse(corpse);
        body.setWhoDied(p);
        bodies.add(body);

        //Spell effects
        if(p.hasPotionEffect(PotionEffectType.HUNGER)) body.setDiseased(true);
        if(p.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) body.setHearty(true);

        //Send packets to players
        Bukkit.getOnlinePlayers().forEach(player -> addBodyPacket(player, corpse));
    }

    //Overload newBody for data storage?
    /*void newBody(Player p, Location loc, Inventory stealables, boolean diseased, boolean hearty, boolean regenerating) {

    }*/

    private String[] getSkin(Player player) {
        //get player properties
        EntityPlayer p = ((CraftPlayer) player).getHandle();
        GameProfile profile = p.getBukkitEntity().getProfile();
        Property property = profile.getProperties().get("textures").iterator().next();

        //parse properties for skin
        String texture = property.getValue();
        String signature = property.getSignature();

        return new String[] {texture, signature};
    }

    private ItemStack[] getPlayerStealables(Player p) {
        Inventory inv = p.getInventory();
        ItemStack[] stealables = new ItemStack[inv.getSize()];

        for(int i = 0; i < inv.getSize(); i++) {
            if(inv.getItem(i) == null) continue;
            if(inv.getItem(i).getItemMeta() == null) continue;
            if(inv.getItem(i).getItemMeta().getLore() == null) continue;
            if(inv.getItem(i).getItemMeta().getLore().toString().toLowerCase().contains("stealable")) {
                stealables[i] = inv.getItem(i);
                inv.setItem(i, null);
            }
        }

        //remove nulls from itemstack array
        return Arrays.stream(stealables).filter(Objects::nonNull).toArray(ItemStack[]::new);
    }

    //************************************************
    //              PACKET MANAGEMENT
    //************************************************

    void sendJoinPacket(Player p) {
        for (Body body : bodies) {
            addBodyPacket(p, body.getCorpse());
        }
    }

    private void addBodyPacket(Player p, EntityPlayer corpse) {
        PlayerConnection connection = ((CraftPlayer) p).getHandle().b;
        //ServerGamePacketListenerImpl connection = ((CraftPlayer) p).getHandle().connection;

        //connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER));

        //Create the player
        connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, corpse));
        connection.a(new PacketPlayOutNamedEntitySpawn(corpse));

        //Match head rotation with body (heads will default facing west)
        float yaw = corpse.getBukkitEntity().getLocation().getYaw();
        connection.a(new PacketPlayOutEntityHeadRotation(corpse, (byte) ((yaw%360)*256/360)));

        //show skin overlay data
        connection.a(new PacketPlayOutEntityMetadata(corpse.ae(), corpse.ai(), true));

        //Hide body nametag
        ScoreboardTeam team = new ScoreboardTeam(
                ((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), p.getName());        //create a new team with the players name
        team.a(ScoreboardTeamBase.EnumNameTagVisibility.b);                                                             //set nametag visibility to NEVER (b)


        connection.a(PacketPlayOutScoreboardTeam.a(team));
        connection.a(PacketPlayOutScoreboardTeam.a(team, true));
        connection.a(PacketPlayOutScoreboardTeam.a(team, corpse.co(), PacketPlayOutScoreboardTeam.a.a));

        //Spell effects
        //connection.a(new PacketPlayOutEntityEffect(corpse.ae(), new MobEffect(MobEffects.q, 999999)));

        //Remove corpse from player tab list
        //If corpse is removed from the tab list before a player renders them, they won't keep their skin
        /*
        new BukkitRunnable() {
            @Override
            public void run() {
                connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, corpse));
            }
        }.runTaskAsynchronously(plugin);

         */
    }

    void sendLeavePacket(Body body) {
        Bukkit.getOnlinePlayers().forEach(player -> removeBodyPacket(player, body.getCorpse()));

        body.setRevived(true);
        bodies.remove(body);
    }

    private void removeBodyPacket(Player p, EntityPlayer corpse) {
        PlayerConnection connection = ((CraftPlayer) p).getHandle().b;

        connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, corpse));
        connection.a(new PacketPlayOutEntityDestroy(corpse.ae()));
    }

    private void updateBodyPacket(Body body, double yaw) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().b;

            //Packets
            connection.a(new PacketPlayOutEntityMetadata(body.getCorpse().ae(), body.getCorpse().ai(), true));          //update pose
            connection.a(new PacketPlayOutEntityTeleport(body.getCorpse()));                                            //update position
            connection.a(new PacketPlayOutEntityHeadRotation(body.getCorpse(), (byte) ((yaw%360)*256/360)));            //update rotation
        });
    }

    //************************************************
    //              BODY INTERACTIONS
    //************************************************

    void soullessBlow(Body body, Player p) {
        //use particles to let player know they are hitting the body
        body.getCorpse().getBukkitEntity().getWorld().spawnParticle(
                Particle.BLOCK_DUST, body.getCorpse().getBukkitEntity().getLocation(), 10, 0.25, 0.4, 0.25, 1, Material.REDSTONE_BLOCK.createBlockData());
        if(body.isSoulless()) {
            p.sendMessage("This body is soulless.");
            return;
        }

        body.setSoulBlows(body.getSoulBlows() + 1);

        int soulBlows = body.getSoulBlows();
        if(soulBlows ==  50) p.sendMessage("This body has taken 50 soul blows.");
        if(soulBlows == 100) p.sendMessage("This body has taken 100 soul blows.");
        if(soulBlows == 150) p.sendMessage("This body has taken 150 soul blows.");
        if(soulBlows == 200) {
            if(!body.isHearty()) {
                p.sendMessage("This body has been rendered soulless.");
                body.setSoulless(true);
                return;
            }
            p.sendMessage("This body has taken 200 soul blows. The job is not yet complete.");
        }
        if(soulBlows == 250) p.sendMessage("This body has taken 250 soul blows.");
        if(soulBlows == 300) p.sendMessage("This body has taken 300 soul blows.");
        if(soulBlows == 350) p.sendMessage("This body has taken 350 soul blows.");
        if(soulBlows >= 400) {
            p.sendMessage("This body has been rendered soulless");
            body.setSoulless(true);
        }

    }

    void searchBody(Player p, Body b) {
        if(!p.isSneaking()) return;
        if(searching.contains(p)) return;
        if(p.getLocation().distanceSquared(b.getCorpse().getBukkitEntity().getLocation()) > 2.25) return;

        //create a searchbar countdown for the player
        BossBar searchBar = Bukkit.createBossBar("Searching...", BarColor.YELLOW, BarStyle.SOLID);
        searchBar.addPlayer(p);
        searchBar.setVisible(true);
        searching.add(p);

        //schedule the bar to tick down, cancelling if the player stops shifting
        new BukkitRunnable() {
            double progress = 1.0;
            double time = 1.0/(15 * 20); //seconds * ticks per second

            @Override
            public void run() {
                //check if player is still searching body (sneaking and within 2m)
                if(!p.isSneaking() || b.isRevived() ||
                        p.getLocation().distanceSquared(b.getCorpse().getBukkitEntity().getLocation()) > 2.25) {           //Distance squared used for performance
                    p.sendMessage("Search aborted!");
                    searchBar.removeAll();
                    searching.remove(p);
                    cancel();
                }

                //update search time
                searchBar.setProgress(progress);

                progress = progress - time;
                if(progress <= 0.0) {
                    p.openInventory(b.getStealables());
                    searchBar.removeAll();
                    searching.remove(p);
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    //************************************************
    //              BODY MANIPULATION
    //************************************************

    void grabBody(Body b, Player p) {
        //if clicking your body, drop it
        if(b.getFollowing() == p) {
            dropBody(b, p);
            return;
        }
        if(dragging.contains(p)) return;
        if(b.getCarried() != null) return;

        dragging.add(p);
        b.setFollowing(p);

        //Stand the corpse up
        b.getCorpse().b(EntityPose.a); //0: STANDING, 1: FALL_FLYING, 2: SLEEPING, 3: SWIMMING, 4: SPIN_ATTACK, 5: SNEAKING, 6: LONG_JUMPING, 7: DYING

        //pose packet
        PlayerConnection connection = ((CraftPlayer) p).getHandle().b;
        connection.a(new PacketPlayOutEntityMetadata(b.getCorpse().ae(), b.getCorpse().ai(), true));

        //Make body follow Player
        b.setCarried(new BukkitRunnable() {

            @Override
            public void run() {
                if(!p.isOnline()) dropBody(b, p);
                if(b.isRevived()) dropBody(b, p);

                double dist = b.getCorpse().getBukkitEntity().getLocation().distanceSquared(p.getLocation());

                //Close enough, do nothing
                if(dist < 1) return;

                //move body towards player
                if(dist <= 2.25) {
                    Vector dir = p.getLocation().toVector().subtract(b.getCorpse().getBukkitEntity().getLocation().toVector());
                    dir.normalize().multiply(CORPSE_SPEED);

                    //get facing direction
                    double yaw = Math.toDegrees(Math.atan2(dir.getZ(), dir.getX())) + 270;                                    //New angle, not a delta.
                    double pitch = -Math.toDegrees(Math.atan2(dir.getY(),
                            Math.sqrt(Math.pow(dir.getX(), 2) + Math.pow(dir.getY(),2))));                                    //New angle, not a delta.

                    //set the actual location
                    b.getCorpse().b(
                            b.getCorpse().getBukkitEntity().getLocation().getX() + dir.getX(),
                            b.getCorpse().getBukkitEntity().getLocation().getY() + dir.getY(),
                            b.getCorpse().getBukkitEntity().getLocation().getZ() + dir.getZ(),
                                (byte) ((yaw%360)*256/360), (byte) ((pitch%360)*256/360));

                    //update packets
                    updateBodyPacket(b, yaw);

                    return;
                }

                //Player too far away! drop the body!
                if(dist > 2.25) {
                    dropBody(b, p);
                }
            }
        }.runTaskTimer(plugin, 0, 1));
    }

    private void dropBody(Body b, Player p) {
        //cancel the runnable
        b.getCarried().cancel();
        b.setCarried(null);
        b.setFollowing(null);

        //change pose
        b.getCorpse().b(EntityPose.d);

        //drop the body to the floor - 0.15
        Location deathLoc = b.getCorpse().getBukkitEntity().getLocation();
        deathLoc.setY(deathLoc.getBlockY());
        while(deathLoc.getBlock().isPassable()) {
            deathLoc.subtract(0, 1,0);
        }
        b.getCorpse().b(deathLoc.getX(), deathLoc.getY() + 0.85, deathLoc.getZ(), deathLoc.getYaw(), deathLoc.getPitch());

        //set head rotation so it doesn't look weird
        float yaw = b.getCorpse().getBukkitYaw();

        //update packets
        updateBodyPacket(b, yaw);

        dragging.remove(p);
    }
}
