package me.copdead.realmscraft.menu;

import org.bukkit.entity.Player;

public class PlayerMenuUtility {
    private Player owner;
    private Menu spellsList = null;

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
