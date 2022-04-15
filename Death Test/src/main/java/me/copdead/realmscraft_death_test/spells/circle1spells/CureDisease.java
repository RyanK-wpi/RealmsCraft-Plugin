package me.copdead.realmscraft_death_test.spells.circle1spells;

import me.copdead.realmscraft_death_test.Realmscraft_death_test;
import me.copdead.realmscraft_death_test.death.Body;
import me.copdead.realmscraft_death_test.spells.Spell;
import me.copdead.realmscraft_death_test.spells.spell_support.Cast;
import me.copdead.realmscraft_death_test.spells.spell_support.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CureDisease extends Spell implements Cast, Cooldown {
    private static final Realmscraft_death_test plugin = (Realmscraft_death_test) Bukkit.getPluginManager().getPlugin("Realmscraft_death_test");

    public CureDisease() {
        super("Cure Disease", 1, 101);
    }

    @Override
    public void spellEffect(Player caster) {
        Object target = Cast.cast_include_bodies(caster);

        if(target == null) caster.sendMessage("Missed");

        else if(target instanceof Body) {
            Body body = (Body) target;
            caster.sendMessage("Target: " + body.getWhoDied().getName() + "'s Body");
        }

        else {
            Entity entity = (Entity) target;
            caster.sendMessage("Target: " + entity.getName());
        }
    }

    @Override
    public void getDescription(Player caster) {
        caster.sendMessage("This spell cures diseases!");
    }

}
