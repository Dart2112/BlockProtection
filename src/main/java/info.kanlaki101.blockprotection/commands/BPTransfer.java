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
            		String transferTo;
            		if (args.length > 0) {
            			transferTo = args[0];
            		} else {
            			transferTo = null;
            		}
            		pl.getServer().getScheduler().scheduleSyncDelayedTask(pl, new TransferRunnable(pl, p, transferTo));
                    return true;
                }
        	}
        }
        return true;
    }
}
