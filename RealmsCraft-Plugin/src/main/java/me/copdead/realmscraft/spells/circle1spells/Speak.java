package me.copdead.realmscraft.spells.circle1spells;

import me.copdead.realmscraft.spells.Spell;
import me.copdead.realmscraft.spells.spell_support.AlertMarshals;
import me.copdead.realmscraft.spells.spell_support.Cast;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

public class Speak extends Spell implements Cast, AlertMarshals {
    public Speak() {
        super("Speak", 1, 2, 115);
    }

    @Override
    public void spellEffect(Player caster) {
        Entity target = Cast.cast(caster);

        if(target instanceof Mob) {
            AlertMarshals.alertMarshalsWithTarget(caster, "Speak", ChatColor.DARK_GREEN, target);
            return;

            //give marshals option to refuse??
        }

        if(!(target instanceof Player)) return;
        Player player = (Player) target;

        player.sendMessage(caster.getDisplayName() + "has tried to cast 'Speak' on you. Do you accept?");

        TextComponent yes = new TextComponent("[Yes]");
        yes.setColor(ChatColor.DARK_GREEN);
        yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/confirmspeak yes" + caster.getName()));

        TextComponent no = new TextComponent("[No]");
        no.setColor(ChatColor.DARK_RED);
        no.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/confirmspeak no" + caster.getName()));

        yes.addExtra(" ");
        yes.addExtra(no);
        player.spigot().sendMessage(yes);
    }

    @Override
    public String getDescription() {
        return "This spell allows the spellcaster to approach a creature and present an offering to them. If the " +
                "offering is taken, the creature now has the ability to speak and understand the language of the " +
                "spellcaster. This ability lasts until the creature is no longer in possession of the offering. No " +
                "creature approached has to take the offering, nor is there any guarantee that the creature will speak " +
                "to you.";
    }
}
