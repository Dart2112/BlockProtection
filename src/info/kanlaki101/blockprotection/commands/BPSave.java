package info.kanlaki101.blockprotection.commands;

import info.kanlaki101.blockprotection.BlockProtection;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BPSave implements CommandExecutor {

	BlockProtection plugin;
	
	public BPSave(BlockProtection blockProtection) {
		this.plugin = blockProtection;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		String noperm = "You do not have permission to use this command.";
		ChatColor YELLOW = ChatColor.YELLOW;
		
		if(command.getName().equalsIgnoreCase("bpsave")) {
			if(!sender.hasPermission("bp.admin")) {
				sender.sendMessage(YELLOW + noperm);
				return true;
			} else {
				plugin.database.save();
				sender.sendMessage(YELLOW + "Database saved.");
				return true;
			}
		}
		return false;
	}

}
