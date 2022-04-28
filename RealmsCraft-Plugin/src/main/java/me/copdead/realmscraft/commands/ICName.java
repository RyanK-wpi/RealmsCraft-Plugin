package me.copdead.realmscraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ICName implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!command.getName().equalsIgnoreCase("nick")) return true;

        switch (args.length) {
            case 0:
                sender.sendMessage(ChatColor.RED + "You did not specify a nickname!");
                return true;

            case 1:
                if(!(sender instanceof Player)) {
                    sender.sendMessage("Only players can run that command!");
                    return true;
                }
                ((Player) sender).setDisplayName(args[0]);
                sender.sendMessage(ChatColor.YELLOW + "Your nickname is now "+args[0]+"!");
                return true;

            case 2:
                if(!sender.isOp()) {
                    sender.sendMessage("You don't have permission to run that command!");
                    return true;
                }

                Player target = Bukkit.getPlayer(args[0]);
                if(target == null) {
                    sender.sendMessage(ChatColor.RED + "Could not find player '"+args[0]+"'");
                    return true;
                }

                target.setDisplayName(args[1]);
                sender.sendMessage(ChatColor.YELLOW + args[0] + "'s nickname is now "+args[1]+"!");
                return true;

            default:
                sender.sendMessage(ChatColor.RED + "You cannot have spaces in your nickname!");
                return true;
        }
    }
}
