package me.copdead.realmscraft.death;

import me.copdead.realmscraft.RealmsCraft;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DeathEventManager implements Listener {

    private final RealmsCraft plugin;

    public DeathEventManager(RealmsCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        DeathBodyManager bodyManager = plugin.getBodyManager();
        Body body = new Corpse(plugin).createCorpse(event.getEntity());
        bodyManager.getBodies().add(body);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        new Death(plugin).playerDies(event.getPlayer());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        //show joining players corpses
        if(plugin.getBodyManager().getBodies() == null) return;
        if(plugin.getBodyManager().getBodies().isEmpty()) return;
        plugin.getBodyManager().addJoinPacket(event.getPlayer());
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        //only apply to corpses
        if(!(event.getEntity() instanceof Player)) return;
        if(!event.getEntity().getScoreboardTags().toString().contains("corpse")) return;

        //if the damage is enough to 'kill' the corpse, cancel the damage and render it soulless
        Player corpse = (Player) event.getEntity();
        if(corpse.getHealth() < event.getDamage()) {
            event.setCancelled(true);
            new Death(plugin).renderSoulless(corpse);
        }
    }




}
