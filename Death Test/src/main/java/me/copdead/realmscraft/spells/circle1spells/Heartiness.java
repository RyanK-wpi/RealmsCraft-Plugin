package me.copdead.realmscraft.spells.circle1spells;

import me.copdead.realmscraft.spells.Spell;
import org.bukkit.entity.Player;

public class Heartiness extends Spell {
    public Heartiness() {
        super("Heartiness", 1, 106);
    }

    @Override
    public void spellEffect(Player caster) {
        caster.sendMessage("You are Hearty! (This spell works automatically)");
    }

    @Override
    public String getDescription() {
        return "Having this spell makes it harder to destroy the spellcaster’s body. The next time the spellcaster’s " +
                "body is destroyed it will take 200 extra blows to successfully destroy their body. If struck for only " +
                "200 blows, instead of the full 400 blows, the spellcaster must inform the individual(s) destroying " +
                "their body that “The job is not yet done.” A spellcaster can only be under the effect of one Heartiness " +
                "spell at a time. A use is considered to be over whenever the spellcaster receives at least 200 body " +
                "destroying blows, but is in effect until either their body is destroyed or they are raised.";
    }
}
