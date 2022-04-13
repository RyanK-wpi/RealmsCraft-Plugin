package me.copdead.realmscraft_death_test.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class PlayerMenuUtility {
    private Player owner;
    private Menu spellsList;

    PlayerMenuUtility(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public Menu getSpellsList() {
        return spellsList;
    }

    public void setSpellsList(Menu spellsList) {
        this.spellsList = spellsList;
    }
}
