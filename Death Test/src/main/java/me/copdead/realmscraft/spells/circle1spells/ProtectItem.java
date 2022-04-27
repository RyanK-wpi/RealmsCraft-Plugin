package me.copdead.realmscraft.spells.circle1spells;

import me.copdead.realmscraft.spells.Spell;
import org.bukkit.entity.Player;

public class ProtectItem extends Spell {
    public ProtectItem() {
        super("Protect Item", 1, 3, 113);
    }

    @Override
    public void spellEffect(Player caster) {
        //similar to ghost blade?
    }

    @Override
    public String getDescription() {
        return "This spell allows a single non-armor item to be protected from the next attack that would normally " +
                "damage it. For example, a protected sword struck by a boulder would not be destroyed, but the wielder " +
                "would still suffer normal damage (e.g. death usually). The call for this spell is “Protect Item.” A " +
                "particular item may only have one casting of Protect Item on it at a time. This spell does not protect " +
                "against Disenchant.";
    }
}
