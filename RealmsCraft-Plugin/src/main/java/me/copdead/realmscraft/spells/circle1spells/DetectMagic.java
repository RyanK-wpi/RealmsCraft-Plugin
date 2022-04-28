package me.copdead.realmscraft.spells.circle1spells;

import me.copdead.realmscraft.spells.Spell;
import me.copdead.realmscraft.spells.spell_support.AlertMarshals;
import me.copdead.realmscraft.spells.spell_support.Cooldown;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class DetectMagic extends Spell implements AlertMarshals, Cooldown {

    public DetectMagic() {
        super("Detect Magic", 1, 5, 102);
    }

    @Override
    public void spellEffect(Player caster) {
        AlertMarshals.alertMarshals(caster, "Detect Magic", ChatColor.DARK_PURPLE);
        Cooldown.cooldownLimited(caster, 5, this);
    }

    @Override
    public String getDescription() {
        return "This spell allows the spellcaster to take any one item (not a living or dead creature) to the EH or MM " +
                "to ask whether casting Identify upon the object will yield any information the spellcaster cannot " +
                "determine by looking at it, such as “It’s a stick,” or “It’s a sword.” It may be cast on a living or " +
                "dead being to detect a magical item it carries, such as a spell focus or magic weapon. In this case, it " +
                "will not tell what the item does, only that it is there and which item it is. If cast in this manner " +
                "multiple times and there are multiple magical items, it will not repeat magic items until there are no " +
                "new items to reveal.";
    }
}
