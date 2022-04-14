package me.copdead.realmscraft_death_test.spells.circle1spells;

import me.copdead.realmscraft_death_test.spells.Spell;
import me.copdead.realmscraft_death_test.spells.SpellSupport;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CureDisease extends Spell {

    public CureDisease() {
        this.name = "Cure Disease";
        this.circle = 1;
        this.model = 101;

        registerSpell();
    }

    @Override
    public void castSpell(Player caster) {
        Entity target = SpellSupport.cast_include_player(caster);

        if(target == null) caster.sendMessage("Missed");
        caster.sendMessage("Target: " + target.getName());
    }

    @Override
    public void getDescription(Player caster) {
        caster.sendMessage("This spell cures diseases!");
    }

}
