package me.copdead.realmscraft.spells.circle1spells;

import me.copdead.realmscraft.menu.MenuManager;
import me.copdead.realmscraft.spells.Spell;
import me.copdead.realmscraft.spells.SpellSelectionManager;
import me.copdead.realmscraft.spells.spell_support.Cooldown;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class DisruptLight extends Spell implements Cooldown {
    private Spell light = null;
    public DisruptLight() {
        super("Disrupt Light", 1, 5, 103);
    }

    @Override
    public void spellEffect(Player caster) {
        if(light == null) light = SpellSelectionManager.getSpells().get(1).get("Light");

        for(Entity e : caster.getNearbyEntities(18.5, 18.5, 18.5)) { //60ft radius
            if(!(e instanceof Player)) continue;
            Player target = ((Player) e);

            if(!MenuManager.getPlayerMenuUtility(target).getSpellsList().getInventory().contains(light.getSpell())) continue;
            Cooldown.cooldown(target, (5*60), light);
        }

        Cooldown.cooldownLimited(caster, 5, this);
    }

    @Override
    public String getDescription() {
        return "This spell cancels Light spells cast by other spellcasters. Once the Disrupt Light spellcaster is within " +
                "sight and hearing of a Light spellcaster, they may loudly call out their verbal. Upon completion of the " +
                "verbal, all other spellcasters within hearing range must put away their active Light spells. This action " +
                "is OOC, and those affected must do so even if they hear the spell while dead. Spellcasters so affected " +
                "cannot recast the Light spell for five minutes, after which time they may reuse the same chemical light " +
                "sticks.";
    }
}
