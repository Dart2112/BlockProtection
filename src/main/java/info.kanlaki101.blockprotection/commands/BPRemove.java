package info.kanlaki101.blockprotection.commands;

import info.kanlaki101.blockprotection.BlockProtection;
import info.kanlaki101.blockprotection.utilities.BPConfigHandler;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BPRemove implements CommandExecutor {
	BlockProtection pl;
	public BPRemove(BlockProtection instance) {
		pl = instance;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player)) { 
			sender.sendMessage("You have to be a player to use this command!");
			return true;
		}
		Player p = (Player)sender;
		String player = p.getName();
		String noperm = "You do not have permission to use this command.";
		ChatColor YELLOW = ChatColor.YELLOW;
		
		if (args.length > 1) {
			sender.sendMessage("To many arguments!");
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("bpremove")) {
			BPConfigHandler.loadFriendsList();
			if (!p.hasPermission("bp.friend")) {
				p.sendMessage(YELLOW + noperm);
				return true;
			}
			if (BPConfigHandler.friendslist.getList(player) == null) {
				p.sendMessage(YELLOW + "You do not have a friends list.");
				return true;
			}
			if (args.length == 0) {
				p.sendMessage(YELLOW + "You must specify a player to remove!");
				return true;
			}
			
			if (BPConfigHandler.getFriendslist(player).contains(args[0])) {
				BPConfigHandler.getFriendslist(player).remove(args[0]);
		        if (BPConfigHandler.getFriendslist(player).isEmpty()) {
		        	BPConfigHandler.friendslist.set(player, null);
		        }
		        BPConfigHandler.saveFriendsList();
		        p.sendMessage(YELLOW + args[0] + " has been removed from your friends list.");
			} 
			else {
				p.sendMessage(YELLOW + args[0] + " is not on your friends list.");
			}
		}	
	return true;
	}
}
