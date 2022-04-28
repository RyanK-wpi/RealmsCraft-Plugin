package me.copdead.realmscraft.spells.circle1spells;

import me.copdead.realmscraft.spells.Spell;
import me.copdead.realmscraft.spells.spell_support.AlertMarshals;
import me.copdead.realmscraft.spells.spell_support.Cast;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class FightersIntuition_Fighter extends Spell implements AlertMarshals, Cast {
    public FightersIntuition_Fighter() {
        super("Fighter's Intuition", 0, 104);
    }

    @Override
    public void spellEffect(Player caster) {
        Entity target = Cast.cast(caster);
        if(target == null) return;

        AlertMarshals.alertMarshalsWithTarget(caster, "Fighter's Intuition", ChatColor.WHITE, target);
        caster.getInventory().setItemInMainHand(null);
    }

    @Override
    public String getDescription() {
        return null;
    }
}
