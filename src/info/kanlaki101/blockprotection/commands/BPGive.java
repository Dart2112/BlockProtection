package info.kanlaki101.blockprotection.commands;

import info.kanlaki101.blockprotection.BlockProtection;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BPGive implements CommandExecutor {
	BlockProtection pl;
    
    public BPGive(BlockProtection instance) {
        pl = instance;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("bpgive")) {
            if (sender instanceof Player) {
            	Player p = (Player) sender;
                if (args.length == 1)
                {
                	p.sendMessage("Block Transfer ON. Blocks will be added to: "+args[0]);
                	pl.GPlayer= args[0];
                } else if(args.length == 0){
                	pl.GPlayer = "";
                	p.sendMessage("Block Transfer OFF. Blocks will be added to: "+p.getName());
                }
            }
        }
        return true;
    }
}
