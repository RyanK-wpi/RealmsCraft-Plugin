package me.copdead.realmscraft.commands;

import me.copdead.realmscraft.spells.SpellItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RealmscraftCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //Give spells
        if(cmd.getName().equalsIgnoreCase("givespells")) {
            if(!sender.isOp()) return true;

            if(!(sender instanceof Player)) {
                sender.sendMessage("Only players can use that command!");
                return true;
            }
            Player player = (Player) sender;

            player.getInventory().addItem(SpellItemManager.cure_disease_focus);
            player.getInventory().addItem(SpellItemManager.detect_magic_focus);
            player.getInventory().addItem(SpellItemManager.disrupt_light_focus);
            return true;
        }

        //Nicknames
        /*if(cmd.getName().equalsIgnoreCase("nick")) {
            //check permissions

            //check input nickname
            if(args.length == 0) {
                sender.sendMessage(ChatColor.RED + "You did not specify a nickname!");
                return true;
            }

            String nick = "";

            player.sendMessage(ChatColor.YELLOW + "Your nickname is now " + nick + "!");
            RealmsCraft.getPlugin().getConfig().set(player.getName(), nick);
            RealmsCraft.getPlugin().saveConfig();


        }*/


        return true;
    }
}
