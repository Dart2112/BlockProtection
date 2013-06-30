package info.kanlaki101.blockprotection.commands;

import info.kanlaki101.blockprotection.BlockProtection;
import info.kanlaki101.blockprotection.utilities.BPConfigHandler;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BPAdd implements CommandExecutor {
	BlockProtection pl;
	public BPAdd(BlockProtection instance) {
		pl = instance;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player)) return true;
		Player p = (Player)sender;
		String player = p.getName();
		String noperm = "You do not have permission to use this command.";
		ChatColor YELLOW = ChatColor.YELLOW;
		
		if (args.length > 1) return true;
		
		if (cmd.getName().equalsIgnoreCase("bpadd")) {
			BPConfigHandler.loadConfig();
			BPConfigHandler.loadFriendsList();
			if (!p.hasPermission("bp.friend")) {
				p.sendMessage(YELLOW + noperm);
				return true;
			}
			if (args.length == 0) {
				p.sendMessage(YELLOW + "You must specify a player to add!");
				return true;
			}
			
			if (BPConfigHandler.getFriendslist(player) == null) {
				String [] list = {args[0]};
				BPConfigHandler.friendslist.set(player, Arrays.asList(list));
				p.sendMessage(YELLOW + args[0] + " has been added to your friends list.");
				BPConfigHandler.saveFriendsList();
			} else {
				if (BPConfigHandler.getFriendslist(player).contains(args[0])) { //Check if the player is already on their friends list
					p.sendMessage(YELLOW + args[0] + " is already on your friends list.");
					return true;
				}
				List<String> friends = BPConfigHandler.friendslist.getStringList(player);
				friends.add(args[0]);
				BPConfigHandler.friendslist.set(player, friends);
				p.sendMessage(YELLOW + args[0] + " has been added to your friends list.");
				BPConfigHandler.saveFriendsList();
			}
		}
			
	return true;
	}
}
