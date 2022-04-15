package me.copdead.realmscraft_death_test.spells.spell_support;

import jline.internal.Nullable;
import me.copdead.realmscraft_death_test.Realmscraft_death_test;
import me.copdead.realmscraft_death_test.spells.SpellSelectionManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

public interface Cast {
    Realmscraft_death_test plugin = (Realmscraft_death_test) Bukkit.getPluginManager().getPlugin("Realmscraft_death_test");

    @Nullable
    static Entity cast(Player p) {
        RayTraceResult target = p.getWorld().rayTraceEntities(p.getEyeLocation(), p.getLocation().getDirection(), 2, entity -> entity != p);
        if(target == null) return null;     //Let the spell decide what to do if you miss!
        else return target.getHitEntity();
    }

    @Nullable
    static Entity cast_include_player(Player p) {
        if(p.getLocation().getPitch() > 70 && p.getLocation().getPitch() <= 90) {
            return p;
        }
        else return cast(p);
    }

    @Nullable
    static Object cast_include_bodies(Player p) {
        //Check for body clicked
        if (SpellSelectionManager.getBodySpellList().containsKey(p)) {
            return SpellSelectionManager.getBodySpellList().get(p);
        }

        //Otherwise look for entities
        else {
            return cast_include_player(p);
        }
    }
}
