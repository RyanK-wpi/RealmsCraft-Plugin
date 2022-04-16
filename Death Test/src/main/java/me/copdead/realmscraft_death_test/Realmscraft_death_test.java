package me.copdead.realmscraft_death_test;

import me.copdead.realmscraft_death_test.commands.RevivePlayer;
import me.copdead.realmscraft_death_test.commands.SelectClass;
import me.copdead.realmscraft_death_test.commands.SetAfterlife;
import me.copdead.realmscraft_death_test.death.DeathBodyManager;
import me.copdead.realmscraft_death_test.death.DeathEventManager;
import me.copdead.realmscraft_death_test.death.DeathPlayerManager;
import me.copdead.realmscraft_death_test.events.PacketReaderManager;
import me.copdead.realmscraft_death_test.files.DataManager;
import me.copdead.realmscraft_death_test.menu.MenuEventManager;
import me.copdead.realmscraft_death_test.menu.MenuManager;
import me.copdead.realmscraft_death_test.spells.SpellEventManager;
import me.copdead.realmscraft_death_test.spells.SpellSelectionManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Realmscraft_death_test extends JavaPlugin {

    //Managers
    private DeathBodyManager bodyManager;
    private DeathPlayerManager deadPlayerManager;
    private PacketReaderManager packetManager;

    private DataManager dataManager;

    @Override
    public void onEnable() {
        //DataManager must be initialized first, as other managers read data with this class!
        this.dataManager = new DataManager(this);

        this.bodyManager = new DeathBodyManager(this);
        this.packetManager = new PacketReaderManager(this);
        this.deadPlayerManager = new DeathPlayerManager(this);
        MenuManager menuManager = new MenuManager();
        SpellSelectionManager spellManager = new SpellSelectionManager();

        //re-inject online players
        if(!Bukkit.getOnlinePlayers().isEmpty())
            Bukkit.getOnlinePlayers().forEach(player -> packetManager.inject(player));

        //Event Managers
        getServer().getPluginManager().registerEvents(new DeathEventManager(this), this);
        getServer().getPluginManager().registerEvents(new MenuEventManager(), this);
        getServer().getPluginManager().registerEvents(new SpellEventManager(this), this);

        //Register Commands
        //Command register? for future command management?
        getCommand("revive").setExecutor(new RevivePlayer());
        getCommand("afterlife").setExecutor(new SetAfterlife());
        getCommand("selectclass").setExecutor(new SelectClass());
    }

    @Override
    public void onDisable() {
        //un-inject players
        Bukkit.getOnlinePlayers().forEach(player -> packetManager.uninject(player));
    }

    //Methods
    public DeathBodyManager getBodyManager() {
        return bodyManager;
    }

    public DeathPlayerManager getDeadPlayerManager() {
        return deadPlayerManager;
    }

    public PacketReaderManager getPacketManager() {
        return packetManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }


}
