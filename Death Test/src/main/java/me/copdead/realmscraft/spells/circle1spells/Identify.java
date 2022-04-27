package me.copdead.realmscraft.spells.circle1spells;

import me.copdead.realmscraft.spells.Spell;
import me.copdead.realmscraft.spells.spell_support.AlertMarshals;
import me.copdead.realmscraft.spells.spell_support.Cooldown;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class Identify extends Spell implements AlertMarshals, Cooldown {
    public Identify() {
        super("Identify", 1, 3, 107);
    }

    @Override
    public void spellEffect(Player caster) {
        AlertMarshals.alertMarshals(caster, "Identify", ChatColor.GOLD);
        Cooldown.cooldownLimited(caster, 10, this);
    }

    @Override
    public String getDescription() {
        return "This spell allows the spellcaster to take any one item (not a living/dead creature) to the EH or MM to " +
                "ask what it is and expect an answer. This spell can also determine what species an unknown creature is. " +
                "If the spellcaster can successfully reach visual inspection range, recite the verbal, and the creature " +
                "is not hostile, it must state what species it is. The response is not IC speech by the creature, and it " +
                "can answer while dead.";
    }
}
