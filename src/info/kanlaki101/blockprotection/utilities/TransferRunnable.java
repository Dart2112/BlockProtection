package info.kanlaki101.blockprotection.utilities;

import info.kanlaki101.blockprotection.BlockProtection;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.Selection;

public class TransferRunnable implements Runnable {

	BlockProtection plugin;
	Player p;
	String transferTo;
	
	public TransferRunnable(BlockProtection plugin, Player p, String transferTo) {
		this.plugin = plugin;
		this.p = p;
		this.transferTo = transferTo;
	}
	
    public void run() {
    	final Selection select = plugin.worldEdit.getSelection(p);
    	final World world = p.getWorld();
    	
    	if(transferTo != null) {
    		p.sendMessage(ChatColor.RED + " [BProtect] Starting Transfer of Ownership. This may take a bit.");
    	} else {
    		p.sendMessage(ChatColor.RED + " [BProtect] Starting cleanup of selected area. This may take a bit.");
    	}
    	Location max = select.getMaximumPoint();
    	Location min = select.getMinimumPoint();
    	for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
    		for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
    			for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
    				Location loc = new Location(world, x, y, z);
    				if ((loc.getBlock().getType() != Material.AIR) && (loc.getBlock().getType() != Material.LAVA) && (loc.getBlock().getType() != Material.WATER)) {
    					BPBlockLocation blockLoc = new BPBlockLocation(x, y, z, world.getName());
    					if (plugin.worldDatabases.get(blockLoc.getWorld()).containsKey(blockLoc)) {
    						String blockowner = plugin.worldDatabases.get(blockLoc.getWorld()).get(blockLoc);
    						if (blockowner != null)
    							plugin.worldDatabases.get(blockLoc.getWorld()).remove(blockLoc);
    					}
    					if (transferTo!=null) {
    						plugin.worldDatabases.get(blockLoc.getWorld()).put(blockLoc, transferTo);
    					} else {
    						continue;
    					}
    				}
    			}
    		}
    	}
    	if(transferTo != null) {
    		p.sendMessage(ChatColor.GREEN + " [BProtect] Transfer of Ownership done!");
    	} else {
    		p.sendMessage(ChatColor.GREEN + " [BProtect] Cleanup is done.");
    	}
    }
}
