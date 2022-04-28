package me.copdead.realmscraft.spells.circle1spells;

import me.copdead.realmscraft.spells.Spell;
import me.copdead.realmscraft.spells.spell_support.Cooldown;
import org.bukkit.entity.Player;

public class ProtectionFromBoulder extends Spell implements Cooldown {
    public ProtectionFromBoulder() {
        super("Protection From Boulder", 1, 2, 112);
    }

    @Override
    public void spellEffect(Player caster) {
        //How the hell does boulder work???

        //only let Player accounts throw boulders???

    }

    @Override
    public String getDescription() {
        return "The spellcaster is protected from the next “boulder” call that strikes them. This protection extends to " +
                "all equipment they are carrying.";
    }
}
