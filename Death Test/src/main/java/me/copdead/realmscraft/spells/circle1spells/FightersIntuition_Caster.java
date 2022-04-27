package me.copdead.realmscraft.spells.circle1spells;

import me.copdead.realmscraft.player_class.PlayerClass;
import me.copdead.realmscraft.spells.Spell;
import me.copdead.realmscraft.spells.SpellSelectionManager;
import me.copdead.realmscraft.spells.spell_support.Cast;
import me.copdead.realmscraft.spells.spell_support.Cooldown;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class FightersIntuition_Caster extends Spell implements Cast, Cooldown {
    public FightersIntuition_Caster() {
        super("Fighter's Intuition", 1, 3, 104);
    }

    @Override
    public void spellEffect(Player caster) {
        Entity target = Cast.cast(caster);

        if(!(target instanceof Player)) return;
        Player player = (Player) target;

        if(!PlayerClass.getPlayerClass(player).equals("Fighter")) {
            caster.sendMessage("That player is not a Fighter!");
            return;
        }

        Spell fighterIntuition = SpellSelectionManager.getSpells().get(0).get("Fighter's Intuition");
        player.getInventory().addItem(fighterIntuition.getSpell());
        Cooldown.cooldownLimited(caster, 0, this);
    }

    @Override
    public String getDescription() {
        return "This spell must be cast on a non-spellcaster by placing the sash on the fighter and giving an " +
                "explanation of how it works. This does not make the fighter an enchanted being.\n" +
                "\n" +
                "This fighter may now call out “Fighter’s Intuition” once. When the fighter does this, they may or may " +
                "not learn information about a monster they can see. It is up to the event staff to decide to provide " +
                "this information or not. The information can be anything: weakness, methods of defeating, or even what " +
                "the NPC likes to eat. If the event staff does not provide any information about the monster, the use of " +
                "the spell is not expended and the fighter may attempt to use it again.";
    }
}
