package me.copdead.realmscraft.spells.circle1spells;

import me.copdead.realmscraft.death.Body;
import me.copdead.realmscraft.spells.Spell;
import me.copdead.realmscraft.spells.SpellImplementationEventManager;
import me.copdead.realmscraft.spells.spell_support.Cast;
import me.copdead.realmscraft.spells.spell_support.Cooldown;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class SpeakWithDead extends Spell implements Cast, Cooldown {
    public SpeakWithDead() {
        super("Speak with Dead", 1, 10, 116);
    }

    @Override
    public void spellEffect(Player caster) {
        Object target = Cast.cast_include_bodies(caster);

        if(!(target instanceof Body)) return;
        Body body = (Body) target;

        caster.sendMessage("Ask " + body.getWhoDied().getDisplayName() + " a question.");

        //get next message the player types
        SpellImplementationEventManager.registerHandler(caster, (player, msg) -> {
            body.getWhoDied().sendMessage(player.getDisplayName() + " asks: \n" + msg);

            //give responses
            TextComponent message = new TextComponent();

            TextComponent yes = new TextComponent("[Yes]");
            yes.setColor(ChatColor.DARK_GREEN);
            yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tellraw " + player.getName() +
                    " {\"text\":\""+ body.getWhoDied().getDisplayName() +" responds with: Yes.\",\"color\":\"gray\"}"));

            TextComponent no = new TextComponent("[No]");
            no.setColor(ChatColor.DARK_RED);
            no.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tellraw " + player.getName() +
                    " {\"text\":\""+ body.getWhoDied().getDisplayName() +" responds with: No.\",\"color\":\"gray\"}"));

            TextComponent abstain = new TextComponent("[Abstain]");
            abstain.setColor(ChatColor.GOLD);
            abstain.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tellraw " + player.getName() +
                    " {\"text\":\""+ body.getWhoDied().getDisplayName() +" responds with: Abstain.\",\"color\":\"gray\"}"));

            message.addExtra(yes);
            message.addExtra(" ");
            message.addExtra(no);
            message.addExtra(" ");
            message.addExtra(abstain);
            body.getWhoDied().spigot().sendMessage(message);
        });

        Cooldown.cooldownLimited(caster, 0, this);
    }

    @Override
    public String getDescription() {
        return "This spell allows the spellcaster to ask a corpse one “yes or no” question. The corpse may only answer " +
                "“Yes,” “No,” or “Abstain,” and it may not lie. An abstention means that the spirit cannot or does not " +
                "want to answer the question. Before asking the questions, the spellcaster must explain to the corpse’s " +
                "player what the acceptable responses are and that the character may not lie.";
    }
}
