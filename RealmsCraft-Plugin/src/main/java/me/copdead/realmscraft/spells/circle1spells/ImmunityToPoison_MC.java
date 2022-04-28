package me.copdead.realmscraft.spells.circle1spells;

import me.copdead.realmscraft.spells.Spell;
import org.bukkit.entity.Player;

public class ImmunityToPoison_MC extends Spell {
    public ImmunityToPoison_MC() {
        super("Immunity to Poison", 0, 108);
    }

    @Override
    public void spellEffect(Player caster) {
        //implementation handled in "SpellImplementationEventManager"
        caster.sendMessage("Holding this item makes you immune to the next call of \"Poison\"");
    }

    @Override
    public String getDescription() {
        return null;
    }
}
