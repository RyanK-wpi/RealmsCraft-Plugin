package me.copdead.realmscraft.spells.circle1spells;

import me.copdead.realmscraft.spells.Spell;
import me.copdead.realmscraft.spells.SpellSelectionManager;
import me.copdead.realmscraft.spells.spell_support.Cast;
import me.copdead.realmscraft.spells.spell_support.Cooldown;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class ImmunityToPoison extends Spell implements Cast, Cooldown {
    private Spell materialComponent = SpellSelectionManager.getSpells().get(0).get("Immunity to Poison");

    public ImmunityToPoison() {
        super("Immunity to Poison", 1, 3, 108);
    }

    @Override
    public void spellEffect(Player caster) {
        Entity target = Cast.cast_include_player(caster);

        if(!(target instanceof Player)) return;
        Player player = (Player) target;

        if(player.hasPotionEffect(PotionEffectType.POISON))
            player.removePotionEffect(PotionEffectType.POISON);
        else player.getInventory().addItem(materialComponent.getSpell());

        Cooldown.cooldownLimited(caster, 3, this);
    }

    @Override
    public String getDescription() {
        return "This spell makes the recipient immune to the next dose of poison that would have otherwise affected " +
                "their PC during the event. When damaged by the next poison attack, whether ingested or delivered by a " +
                "poisoned weapon, call “Immunity to Poison!” Only one Immunity to Poison is used at a time. The " +
                "recipient must take any mundane damage from a poisoned weapon regardless of whether they are protected " +
                "from the actual poison. The recipient must be given the MC when the spell is cast, and they are " +
                "responsible for disposing of it when the immunity has been used. More than one Immunity to Poison can " +
                "be cast upon a recipient; the effect is stackable. The MC of the spell is not stealable or transferable " +
                "after it is cast. This spell can also be cast as an antidote for any one poison that the recipient has " +
                "been subjected to, but in this case it will not provide any further protection.";
    }
}
