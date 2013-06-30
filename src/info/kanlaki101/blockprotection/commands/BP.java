package info.kanlaki101.blockprotection.commands;

import info.kanlaki101.blockprotection.BlockProtection;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BP implements CommandExecutor {
	BlockProtection pl;
	public BP(BlockProtection instance) {
		pl = instance;
	}
		
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("bp")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.YELLOW + "You have to be a player to use this command");
				return true;
			}
			if (!sender.hasPermission("bp.user")) {
				sender.sendMessage(ChatColor.YELLOW + "You do not have permission to use this command.");
				return true;
			}
			
			Player p = (Player) sender;
			String player = p.getName();
			
			if (args.length == 1) {
				if(args[0].equalsIgnoreCase("on")) {
					if(pl.Users.contains(player)) {
						p.sendMessage(ChatColor.YELLOW + "BlockProtection is already on!");
						return true;
					} else {
						p.sendMessage(ChatColor.YELLOW + "BlockProtection is turned on now!");
						pl.Users.add(player);
					}
				} else if(args[0].equalsIgnoreCase("off")) {
					if(pl.Users.contains(player)) {
						p.sendMessage(ChatColor.YELLOW + "BlockProtection is turned off now!");
						pl.Users.remove(player);
					} else {
						p.sendMessage(ChatColor.YELLOW + "BlockProtection is already off!");
						return true;
					}
				}
				return true;
			}
			
			if (pl.Users.contains(player)) {
				pl.Users.remove(player);
				p.sendMessage(ChatColor.YELLOW + "Your blocks are no longer protected!");
			} 
			else {
				pl.Users.add(player);
				p.sendMessage(ChatColor.YELLOW + "Your blocks are now protected!");
			}
		}
		return true;
	}
}
