package me.copdead.realmscraft.death;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.copdead.realmscraft.RealmsCraft;
import me.copdead.realmscraft.spells.SpellItemManager;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.protocol.game.PacketPlayOutEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.EntityPose;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

class Corpse {

    private final RealmsCraft plugin;

    Corpse(RealmsCraft plugin) {
        this.plugin = plugin;
    }

    Body createCorpse(Player p) {
        //Corpse setup
        Body body = new Body();                                                                                         //body object this will become
        MinecraftServer server = ((CraftServer)Bukkit.getServer()).getServer();                                         //Get the Server
        WorldServer world = ((CraftWorld) p.getWorld()).getHandle();                                                    //Get World Player is in
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), ChatColor.stripColor(p.getDisplayName()));         //just use player name, names cannot exceed 16Char

        //Set corpse texture to player
        String[] skin = getSkin(p);
        gameProfile.getProperties().put("textures", new Property("textures", skin[0], skin[1]));

        //Create corpse
        EntityPlayer corpse = new EntityPlayer(server, world, gameProfile);
        CraftPlayer corpsePlayer = corpse.getBukkitEntity();

        corpsePlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(200);
        corpsePlayer.setHealth(200);
        corpsePlayer.addScoreboardTag("corpse");

        //set position lying down at lowest block
        Location deathLoc = p.getLocation();
        deathLoc.setY(deathLoc.getBlockY());
        while(deathLoc.getBlock().isPassable()) {
            deathLoc.subtract(0, 1,0);
        }
        corpse.a(deathLoc.getX(), deathLoc.getY() + 1, deathLoc.getZ());

        //Fake bed for better lay down
        Location bed = deathLoc.add(1, 1, 0);
        corpse.e(new BlockPosition(bed.getX(), bed.getY(), bed.getZ()));

        corpse.b(EntityPose.c);                                                                                         //i think method b is setPose?

        //Overlays (second layer of skins)
        DataWatcher watcher = corpse.ai();                                                                              //I mean, it returns a datawatcher at least
        byte b = 0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40;                                                        //From the wiki, bit masks for cape/jacket/sleeves/pants/hat
        watcher.b(DataWatcherRegistry.a.a(17), b);                                                                 //I hate this

        //randomize fall position
        deathLoc.subtract(1, 0, 0);
        PacketPlayOutEntity.PacketPlayOutRelEntityMove move = new PacketPlayOutEntity.PacketPlayOutRelEntityMove(
                corpse.ae(), (byte) 0, (byte) ((deathLoc.getY() - 1.6 - deathLoc.getY())*32), (byte) 0, false
        );

        /*
        //set bed to lay down corpse
        Location bed = p.getLocation();
        bed.setX(bed.getBlockX());
        bed.setY(-64);
        bed.setZ(bed.getBlockZ());
        bed.getBlock().setType(Material.RED_BED);
        final Directional direction = (Directional) bed.getBlock().getBlockData();
        direction.setFacing(getBlockFaceDirection(p));

        bed.getBlock().setBlockData(direction);

        //Set Corpse Position
        corpsePlayer.sleep(bed, true);
        Bukkit.getScheduler().runTaskLater(plugin, () -> setCorpseLoc(corpsePlayer, deathLoc.add(0, 1, 0)), 2L);
        */

        //show soulless blows
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();

        Objective objective = board.registerNewObjective("soullessblows", "health", "/ 200");
        objective.setDisplaySlot(DisplaySlot.BELOW_NAME);

        //Apply Spell Effects
        if(p.hasPotionEffect(PotionEffectType.HUNGER)) {
            corpsePlayer.addPotionEffect(
                    new PotionEffect(PotionEffectType.HUNGER, 999999, 0, true, true)); //DISEASED
            body.setDiseased(true);
        }

        Inventory inv = p.getInventory();
        for(int i=0; i < inv.getSize(); i++) {
            if(inv.getItem(i) != null) {
                if(inv.getItem(i).isSimilar(SpellItemManager.heartiness_focus)) {                                       //HEARTINESS
                    //set health to 400
                    corpsePlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(400);
                    corpsePlayer.setHealth(400);
                    objective.setDisplayName(" /400");
                }

                if(inv.getItem(i).isSimilar((SpellItemManager.regeneration_focus))) {                                   //REGENERATION
                    body.setRegenerating(true);
                    body.setWhenRegen(System.currentTimeMillis());
                }
            }
        }

        //update Scoreboard
        corpsePlayer.setScoreboard(board);
        corpsePlayer.setHealth(corpsePlayer.getHealth());
        //TODO Scoreboard display packets

        //Send corpse packet to players
        addNPCPacket(corpse, watcher, move);

        //create body and add to list
        body.setWhoDied(p.getUniqueId());
        body.setStealables(getPlayerStealables(p));
        body.setCorpse(corpse);
        return body;
    }

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

    private void addNPCPacket(EntityPlayer npc, DataWatcher watcher, PacketPlayOutEntity.PacketPlayOutRelEntityMove move) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().b ;
            //show player on server
            connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, npc));             //adds corpse to player list. required for player packets
            connection.a(new PacketPlayOutNamedEntitySpawn(npc));

            //scoreboard data

            //show skin overlay data
            connection.a(new PacketPlayOutEntityMetadata(npc.e, watcher, true));
            connection.a(move);

            //Remove corpse from player tab list
            new BukkitRunnable() {
                @Override
                public void run() {
                    connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, npc));     //removes corpse from player list
                }
            }.runTaskAsynchronously(plugin);
        }
    }

    /*
    private BlockFace getBlockFaceDirection(Player p) {
        double rot = (p.getLocation().getYaw() - 90) % 360;
        if (rot < 0) rot += 360;

        if (0 <= rot && rot < 45) {
            return BlockFace.NORTH;
        } else if (45 <= rot && rot < 135) {
            return BlockFace.EAST;
        } else if (135 <= rot && rot < 225) {
            return BlockFace.SOUTH;
        } else if (225 <= rot && rot < 315) {
            return BlockFace.WEST;
        } else if (315 <= rot && rot < 360) {
            return BlockFace.NORTH;
        } else return BlockFace.WEST; //Default orientation
    }

    private void setCorpseLoc(Player corpse, Location loc) {
        corpse.teleport(loc);
    }
     */

    private ItemStack[] getPlayerStealables(Player p) {
        Inventory inv = p.getInventory();
        ItemStack[] stealables = new ItemStack[inv.getSize()];

        for(int i = 0; i < inv.getSize(); i++) {
            if(inv.getItem(i) != null &&
                    inv.getItem(i).getItemMeta().getLore().toString().toLowerCase().contains("stealable")) {
                inv.setItem(i, null);
                stealables[i] = inv.getItem(i);
            }
        }

        //remove nulls from itemstack array
        return Arrays.stream(stealables).filter(Objects::nonNull).toArray(ItemStack[]::new);
    }
}
