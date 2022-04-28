package me.copdead.realmscraft.spells.spell_support;

import jline.internal.Nullable;
import me.copdead.realmscraft.spells.SpellSelectionManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

public interface Cast {

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
