package info.kanlaki101.blockprotection.commands;

import info.kanlaki101.blockprotection.BlockProtection;
import info.kanlaki101.blockprotection.utilities.BPConfigHandler;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BPClear implements CommandExecutor {
	BlockProtection pl;
	public BPClear(BlockProtection instance) {
		pl = instance;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player)) return true;
		Player p = (Player)sender;
		String player = p.getName();
		String noperm = "You do not have permission to use this command.";
		ChatColor YELLOW = ChatColor.YELLOW;
		
		if (args.length > 1) return true;
		
		if (cmd.getName().equalsIgnoreCase("bpclear")) {
			BPConfigHandler.loadFriendsList();
			if (args.length > 0) return true;
			if (!p.hasPermission("bp.friend")) {
				p.sendMessage(YELLOW + noperm);
				return true;
			}
			if (BPConfigHandler.getFriendslist(player) == null) {
				p.sendMessage(YELLOW + "You do not have a friends list.");
			}
			else {
				BPConfigHandler.friendslist.set(player, null);
				p.sendMessage(YELLOW + "Your friends list has been cleared.");
				BPConfigHandler.saveFriendsList();
			}
		}
		return true;
	}
}
