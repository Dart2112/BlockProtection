package info.kanlaki101.blockprotection.commands;

import info.kanlaki101.blockprotection.BlockProtection;
import info.kanlaki101.blockprotection.utilities.TransferRunnable;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BPTransfer implements CommandExecutor {
	BlockProtection pl;

    public BPTransfer(BlockProtection instance) {
        pl = instance;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("bptransfer")) {
        	if(sender instanceof Player) {
        		Player p = (Player) sender;
        		if ((p.hasPermission("bp.transfer")) || (p.hasPermission("bp.admin"))) {
            		String player;
            		if (args.length > 0) {
            			player = args[0];
            		} else {
            			player = null;
            		}
            		pl.getServer().getScheduler().scheduleSyncDelayedTask(pl, new TransferRunnable(pl, p, player));
                    return true;
                }
        	}
        }
        return true;
    }
}
