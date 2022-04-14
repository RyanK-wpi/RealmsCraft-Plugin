package me.copdead.realmscraft_death_test.commands;

import me.copdead.realmscraft_death_test.menu.MenuManager;
import me.copdead.realmscraft_death_test.spells.SpellBook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SelectClass implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!command.getName().equalsIgnoreCase("selectclass")) return true;

        if(args.length == 1) {
            //Find the player
            Player player = null;
            for(Player p : Bukkit.getOnlinePlayers()) {
                if(p.getName().equalsIgnoreCase(args[0])) player = p;
            }
            if(player == null) {
                sender.sendMessage(ChatColor.RED + "Could not find player '" + args[0] + "'");
                return true;
            }

            //Give player class select book and reset class selection
            player.getInventory().addItem(new SpellBook().getBook());
            MenuManager.getPlayerMenuUtility(player).setSpellsList(null);
            return true;
        }

        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players can run that command!");
            return true;
        }

        Player player = (Player) sender;
        player.getInventory().addItem(new SpellBook().getBook());
        MenuManager.getPlayerMenuUtility(player).setSpellsList(null);
        return true;
    }
}
