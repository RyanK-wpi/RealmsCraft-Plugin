package me.copdead.realmscraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InvestigateCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //Investigate a player
        if(!cmd.getName().equalsIgnoreCase("investigate")) return true;

        if(!sender.isOp()) return true;

        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players can use that command!");
            return true;
        }
        Player player = (Player) sender;

        //check that a player was specified
        if(args.length == 0) {
            player.sendMessage(ChatColor.RED + "Please choose a player to spectate!");
            return true;
        }
        if(args.length > 1) {
            player.sendMessage(ChatColor.RED + "Please choose only one player to spectate!");
            return true;
        }

        //check for specified player
        Player target = Bukkit.getPlayer(args[0]);
        if(target == null) {
            player.sendMessage(ChatColor.RED + "Could not find player: " + args[0]);
            return true;
        }

        //Set investigator to spectator mode and spectate the target
        player.setGameMode(GameMode.SPECTATOR);
        player.setSpectatorTarget(target);
        player.sendMessage(ChatColor.YELLOW + "Now spectating " + target.getName());
        return true;
    }
}
