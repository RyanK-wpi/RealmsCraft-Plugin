package me.copdead.realmscraft;

import me.copdead.realmscraft.commands.*;
import me.copdead.realmscraft.death.DeathBodyManager;
import me.copdead.realmscraft.death.DeathEventManager;
import me.copdead.realmscraft.death.DeathPlayerManager;
import me.copdead.realmscraft.events.PacketReaderManager;
import me.copdead.realmscraft.files.DataManager;
import me.copdead.realmscraft.marshalling.MarshalTeam;
import me.copdead.realmscraft.menu.MenuEventManager;
import me.copdead.realmscraft.menu.MenuManager;
import me.copdead.realmscraft.spells.SpellEventManager;
import me.copdead.realmscraft.spells.SpellImplementationEventManager;
import me.copdead.realmscraft.spells.SpellSelectionManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class RealmsCraft extends JavaPlugin {

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
        MarshalTeam marshals = new MarshalTeam();

        //re-inject online players
        if(!Bukkit.getOnlinePlayers().isEmpty())
            Bukkit.getOnlinePlayers().forEach(player -> packetManager.inject(player));

        //Event Managers
        getServer().getPluginManager().registerEvents(new DeathEventManager(this), this);
        getServer().getPluginManager().registerEvents(new MenuEventManager(), this);
        getServer().getPluginManager().registerEvents(new SpellEventManager(this), this);
        getServer().getPluginManager().registerEvents(new SpellImplementationEventManager(this), this);

        //Register Commands
        //Command register? for future command management?
        getCommand("revive").setExecutor(new RevivePlayer());
        getCommand("afterlife").setExecutor(new SetAfterlife());
        getCommand("selectclass").setExecutor(new SelectClass());
        getCommand("investigate").setExecutor(new InvestigatePlayer());
        getCommand("confirmspeak").setExecutor(new ConfirmSpeak());
        getCommand("nick").setExecutor(new ICName());
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
