package me.copdead.realmscraft.death;

import me.copdead.realmscraft.RealmsCraft;
import me.copdead.realmscraft.events.ClickBodyEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DeathEventManager implements Listener {
    private final RealmsCraft plugin;
    private final DeathBodyManager bodyManager;
    private final DeathPlayerManager deadPlayerManager;

    public DeathEventManager(RealmsCraft plugin) {
        this.plugin = plugin;
        this.bodyManager = plugin.getBodyManager();
        this.deadPlayerManager = plugin.getDeadPlayerManager();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        bodyManager.newBody(event.getEntity());
        deadPlayerManager.playerDies(event.getEntity());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        event.setRespawnLocation(deadPlayerManager.getAfterlife());
        //It takes about 1-2 ticks for the player to reload
        Bukkit.getScheduler().runTaskLater(plugin, () -> deadPlayerManager.playerRespawned(event.getPlayer()), 5);
    }

    /*@EventHandler
    public void onDebt(EntityDamageEvent event) {
        //check for lethal damage?
    }*/

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getPacketManager().inject(event.getPlayer());

        if(bodyManager.getBodies() == null) return;
        if(bodyManager.getBodies().isEmpty()) return;
        bodyManager.sendJoinPacket(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getPacketManager().uninject(event.getPlayer());
    }

    @EventHandler
    public void onClickBody(ClickBodyEvent event) {
        if(event.getAction() == Action.LEFT_CLICK_AIR) bodyManager.soullessBlow(event.getBody(), event.getPlayer());
        if(event.getAction() == Action.RIGHT_CLICK_AIR) {
            //only allow empty hands
            if(!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) return;

            //player searching the body
            if(event.getPlayer().isSneaking()) bodyManager.searchBody(event.getPlayer(), event.getBody());

            //player grabbing the body
            else bodyManager.grabBody(event.getBody(), event.getPlayer());
        }
    }
}
