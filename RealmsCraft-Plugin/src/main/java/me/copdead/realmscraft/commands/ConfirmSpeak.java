package me.copdead.realmscraft.commands;

import me.copdead.realmscraft.spells.SpellSelectionManager;
import me.copdead.realmscraft.spells.spell_support.Cooldown;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ConfirmSpeak implements CommandExecutor, Cooldown {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!command.getName().equalsIgnoreCase("confirmspeak")) return true;

        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players can use that command!");
            return true;
        }
        Player player = (Player) sender;

        if(args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Improper arguments!");
            return true;
        }

        Player caster = Bukkit.getPlayer(args[1]);
        if(caster == null) {
            sender.sendMessage(ChatColor.RED + "Could not find player '" + args[1] + "'");
            return true;
        }

        //clear the command from player's chat
        player.sendMessage(StringUtils.repeat("\n", 100));

        switch (args[0]) {
            case "yes":
                //make sure caster still has valid spells
                if(SpellSelectionManager.getSpellCounter().getObjective("Speak").getScore(caster.getName()).getScore() <= 0) {
                    player.sendMessage(ChatColor.AQUA + "Sorry, it looks like that player has run out of castings of 'Speak.'");
                    return true;
                }
                caster.sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + player.getDisplayName() + " has agreed to speak with you!");
                Cooldown.cooldownLimited(caster, 0, SpellSelectionManager.getSpells().get(1).get("Speak"));
                break;

            case "no":
                player.sendMessage(ChatColor.AQUA + "Sorry, it looks like that person doesn't wish to speak with you.");
                break;

            default:
                player.sendMessage(ChatColor.RED + "Invalid argument '" + args[0] + "'");
        }
        return true;
    }
}
