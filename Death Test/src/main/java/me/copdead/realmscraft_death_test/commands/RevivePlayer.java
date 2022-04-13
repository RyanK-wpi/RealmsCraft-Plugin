package me.copdead.realmscraft_death_test.commands;

import me.copdead.realmscraft_death_test.Realmscraft_death_test;
import me.copdead.realmscraft_death_test.death.Body;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RevivePlayer implements CommandExecutor {
    private static final Realmscraft_death_test plugin = (Realmscraft_death_test) Bukkit.getPluginManager().getPlugin("Realmscraft_death_test");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!command.getName().equalsIgnoreCase("revive")) return true;
        if(args.length != 1) {
            sender.sendMessage(ChatColor.RED + "You must specify a player to revive!");
            return true;
        }

        //Find the player
        Player player = null;
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p.getName().equalsIgnoreCase(args[0])) player = p;
        }
        if(player == null) {
            sender.sendMessage(ChatColor.RED + "Could not find player '" + args[0] + "'");
            return true;
        }

        //Find the body and revive
        for(Body body : plugin.getBodyManager().getBodies()) {
            if(body.getWhoDied().equals(player)) {
                plugin.getDeadPlayerManager().playerRevived(player, body);
                return true;
            }
        }
        sender.sendMessage(ChatColor.RED + "Could not find a body for " + args[0]);
        return true;
    }
}
