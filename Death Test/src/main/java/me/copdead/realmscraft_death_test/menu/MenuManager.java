package me.copdead.realmscraft_death_test.menu;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class MenuManager {
    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();

    public static PlayerMenuUtility getPlayerMenuUtility(Player p) {
        if(playerMenuUtilityMap.containsKey(p))
            return playerMenuUtilityMap.get(p);

        PlayerMenuUtility pmu = new PlayerMenuUtility(p);
        playerMenuUtilityMap.put(p, pmu);
        return pmu;
    }
}
