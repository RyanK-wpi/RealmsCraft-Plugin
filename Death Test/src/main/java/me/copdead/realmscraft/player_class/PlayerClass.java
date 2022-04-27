package me.copdead.realmscraft.player_class;

import me.copdead.realmscraft.RealmsCraft;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class PlayerClass {
    protected static final RealmsCraft plugin = JavaPlugin.getPlugin(RealmsCraft.class);
    private static final NamespacedKey key = new NamespacedKey(plugin, "class");

    public static String getPlayerClass(Player player) {
        PersistentDataContainer container = player.getPersistentDataContainer();

        if(!container.has(key, PersistentDataType.STRING)) {
            container.set(key, PersistentDataType.STRING, "none");
        }

        return container.get(key, PersistentDataType.STRING);
    }

    protected void setPlayerClass(Player player, String playerClass) {
        player.getPersistentDataContainer().set(key, PersistentDataType.STRING, playerClass);
    }

    public abstract void giveClassItems(Player player);
}
