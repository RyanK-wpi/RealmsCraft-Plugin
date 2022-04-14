package me.copdead.realmscraft_death_test.player_class;

import me.copdead.realmscraft_death_test.Realmscraft_death_test;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public abstract class PlayerClass {
    private static final Realmscraft_death_test plugin = (Realmscraft_death_test) Bukkit.getPluginManager().getPlugin("Realmscraft_death_test");
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
