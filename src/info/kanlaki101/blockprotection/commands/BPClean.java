package info.kanlaki101.blockprotection.commands;

import info.kanlaki101.blockprotection.BlockProtection;
import info.kanlaki101.blockprotection.utilities.TransferRunnable;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BPClean implements CommandExecutor {
	BlockProtection plugin;
	
	public BPClean(BlockProtection plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(cmd.getName().equalsIgnoreCase("bpclean")) {
			if(sender instanceof Player) {
        		Player p = (Player) sender;
        		if ((p.hasPermission("bp.clean")) || (p.hasPermission("bp.admin"))) {
            		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new TransferRunnable(plugin, p, null));
                    return true;
                }
        	}
		}
		return true;
	}
}
