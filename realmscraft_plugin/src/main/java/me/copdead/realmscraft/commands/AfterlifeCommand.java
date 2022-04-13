package me.copdead.realmscraft.commands;

import me.copdead.realmscraft.death.Death;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AfterlifeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //Set afterlife location
        if(!cmd.getName().equalsIgnoreCase("afterlife")) return true;

        if(!sender.isOp()) return true;

        if(args.length == 0) {
            if(sender instanceof Player) {
                Death.setAfterlife((Player) sender);
                sender.sendMessage(ChatColor.YELLOW + "Afterlife now set to " +
                        ((Player) sender).getLocation().getX() + " " + ((Player) sender).getLocation().getY() + " " + ((Player) sender).getLocation().getZ());
                return true;
            }
            sender.sendMessage("Only players can use that command!");
            return true;
        }

        if(args.length == 3) {
            try {
                double x = Double.parseDouble(args[0]);
                double y = Double.parseDouble(args[1]);
                double z = Double.parseDouble(args[2]);

                if(sender instanceof Player) {
                    Death.setAfterlife((Player) sender, x, y, z);
                    sender.sendMessage(ChatColor.YELLOW + "Afterlife now set to " + x + " " + y + " " + z);
                }

                Death.setAfterlife(Bukkit.getWorlds().get(0).getName(), x, y, z);
                sender.sendMessage("Afterlife now set to " + x + " " + y + " " + z);
                return true;
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "You must use numbers to set Afterlife coordinates!");
                return true;
            }
        }

        if(args.length == 4) {
            try {
                double x = Double.parseDouble(args[1]);
                double y = Double.parseDouble(args[2]);
                double z = Double.parseDouble(args[3]);

                if(Bukkit.getWorld(args[0]) == null) {
                    sender.sendMessage(ChatColor.RED + "Could not find world '" + args[0] + "'");
                    return true;
                }

                Death.setAfterlife(args[0], x, y, z);
                sender.sendMessage(ChatColor.YELLOW + "Afterlife now set to " + x + " " + y + " " + z + " in world '" + args[0] + "'");
                return true;
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "You must use numbers to set Afterlife coordinates!");
                return true;
            }
        }

        else {
            sender.sendMessage("You did not specify an afterlife location!");
            return true;
        }
    }
}
