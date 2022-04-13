package me.copdead.realmscraft;

import me.copdead.realmscraft.commands.AfterlifeCommand;
import me.copdead.realmscraft.commands.InvestigateCommand;
import me.copdead.realmscraft.death.DeathBodyManager;
import me.copdead.realmscraft.death.DeathEventManager;
import me.copdead.realmscraft.spells.SpellItemManager;
import me.copdead.realmscraft.tasks.RegenTask;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public final class RealmsCraft extends JavaPlugin {

    private static RealmsCraft instance;

    //managers
    private DeathBodyManager bodyManager;
    private SpellItemManager spellManager;

    @Override
    public void onEnable() {        // Plugin startup logic
        //Get the Plugin
        instance = this;
        this.bodyManager = new DeathBodyManager();
        //this.spellManager = new SpellItemManager();

        //SpellItemManager.init();

        //event managers
        //getServer().getPluginManager().registerEvents(new SpellEventManager(), this);
        getServer().getPluginManager().registerEvents(new DeathEventManager(this), this);
        getServer().getPluginManager().registerEvents(new RCQEventManager(), this);

        //initiate commands
        //getCommand("givespells").setExecutor(new RealmscraftCommands());
        getCommand("investigate").setExecutor(new InvestigateCommand());
        getCommand("afterlife").setExecutor(new AfterlifeCommand());

        //constant effects
        new RegenTask(bodyManager).runTaskTimerAsynchronously(this, 0L, 5L);                        //heart particles for regen players
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public DeathBodyManager getBodyManager() {
        return bodyManager;
    }

    public SpellItemManager getSpellManager() {
        return spellManager;
    }




    public static RealmsCraft getPlugin() {
        return instance;
    }






    private static ScoreboardManager manager = Bukkit.getScoreboardManager();
    private static Scoreboard scoreboard = manager.getNewScoreboard();
    private static Team marshalTeam = scoreboard.registerNewTeam("Marshals");

    public static Team getMarshalTeam() {
        return marshalTeam;
    }
}

class RCQEventManager implements Listener {
    //Stop players from spreading or splitting objects in inventory
    //may want to filter for only spell objects
    @EventHandler
    public void onRightClickInventory(InventoryClickEvent event) {
        if(event.isRightClick()) {
            event.setResult(Event.Result.DENY);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDragInventory(InventoryDragEvent event) {
        event.setResult(Event.Result.DENY);
        event.setCancelled(true);
    }
}
