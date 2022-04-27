package me.copdead.realmscraft.spells.circle1spells;

import me.copdead.realmscraft.death.Body;
import me.copdead.realmscraft.spells.Spell;
import me.copdead.realmscraft.spells.spell_support.Cast;
import me.copdead.realmscraft.spells.spell_support.Cooldown;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class CureDisease extends Spell implements Cast, Cooldown {

    public CureDisease() {
        super("Cure Disease", 1, 5, 101);
    }

    @Override
    public void spellEffect(Player caster) {
        Object target = Cast.cast_include_bodies(caster);

        if(target == null) return;

        if(target instanceof Player) {
            Player player = (Player) target;

            if(!player.hasPotionEffect(PotionEffectType.HUNGER)) return;
            player.removePotionEffect(PotionEffectType.HUNGER);
        }

        else if(target instanceof Body) {
            Body body = (Body) target;

            if(!body.isDiseased()) return;
            body.setDiseased(false);
        }

        Cooldown.cooldownLimited(caster, 5, this);
    }

    @Override
    public String getDescription() {
        return "This spell will cure the recipient of all diseases that are currently affecting them. " +
                "It will not provide protection from catching a disease after the spell is cast";
    }

}
