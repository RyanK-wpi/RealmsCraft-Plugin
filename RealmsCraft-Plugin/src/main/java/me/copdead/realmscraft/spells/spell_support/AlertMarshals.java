package me.copdead.realmscraft.spells.spell_support;

import me.copdead.realmscraft.marshalling.MarshalTeam;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface AlertMarshals {

    static void alertMarshals(Player caster, String spell, ChatColor color) {
        TextComponent message = new TextComponent(caster.getDisplayName() + " has used " + spell + "!");
        message.setColor(color);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/investigate " + caster.getName()));

        sendMessageToMarshals(message);
    }

    static void alertMarshalsWithTarget(Player caster, String spell, ChatColor color, Entity target) {
        TextComponent message = new TextComponent();

        TextComponent casterComp = new TextComponent(caster.getDisplayName());
        casterComp.setColor(color);
        casterComp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/investigate " + caster.getName()));

        TextComponent linkText = new TextComponent(" has used " + spell + " on ");
        linkText.setColor(color);

        String targetName = target.getCustomName();
        if(targetName == null) targetName = target.getName();
        TextComponent targetComp = new TextComponent(targetName + "!");
        targetComp.setColor(color);
        targetComp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/investigate " + target.getUniqueId()));

        message.addExtra(casterComp);
        message.addExtra(linkText);
        message.addExtra(targetComp);

        sendMessageToMarshals(message);
    }

    private static void sendMessageToMarshals(TextComponent message) {
        for (String entry : MarshalTeam.getMarshals().getEntries()) {
            Player p = Bukkit.getPlayer(entry);
            if (p != null && p.isOnline()) {
                p.spigot().sendMessage(message);
            }
        }
    }
}
