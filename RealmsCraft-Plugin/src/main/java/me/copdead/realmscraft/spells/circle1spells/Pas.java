package me.copdead.realmscraft.spells.circle1spells;

import me.copdead.realmscraft.spells.Spell;
import me.copdead.realmscraft.spells.spell_support.Cast;
import me.copdead.realmscraft.spells.spell_support.Cooldown;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class Pas extends Spell implements Cast, Cooldown {
    public Pas() {
        super("Pas", 1, 3, 111);
    }

    @Override
    public void spellEffect(Player caster) {
        Entity target = Cast.cast(caster);

        if(target == null) return;

        if(!(target instanceof Player || target instanceof Mob)) return;

        NamespacedKey key = new NamespacedKey(plugin, "pas");
        target.getPersistentDataContainer().set(key, PersistentDataType.STRING, caster.getName());

        //Multiple people cast pas?
        //disable runnable if attacked?
        new BukkitRunnable() {
            @Override
            public void run() {
                target.getPersistentDataContainer().remove(key);
            }
        }.runTaskLater(plugin, 60*20);

        Cooldown.cooldownLimited(caster, 0, this);
    }

    @Override
    public String getDescription() {
        return "This spell creates an uneasy, temporary truce between the target and the PC. To cast this spell, the " +
                "spellcaster offers something of value to the target and says something along the lines of, “Pas, friend " +
                "orc, and accept these shiny bits to let me pass unharmed.” If the target accepts the offering, they are " +
                "magically bound to not attack the spellcaster for 60 seconds, unless the target is attacked. If the " +
                "target is attacked or the spellcaster is slain, this spell ends immediately. Protect the Soul will " +
                "block the effects of this spell, as will Resist Magic.";
    }
}
