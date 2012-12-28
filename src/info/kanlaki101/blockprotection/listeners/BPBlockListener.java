package info.kanlaki101.blockprotection.listeners;

import info.kanlaki101.blockprotection.BlockProtection;
import info.kanlaki101.blockprotection.utilities.BPBlockLocation;
import info.kanlaki101.blockprotection.utilities.BPConfigHandler;
import info.kanlaki101.blockprotection.utilities.WorldDatabase;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BPBlockListener implements Listener {
	BlockProtection pl;

	public BPBlockListener(BlockProtection instance) {
		pl = instance;
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		String player = e.getPlayer().getName();
		
		if (pl.Users.contains(player)) {
			Block block = e.getBlockPlaced();
			int blockID = block.getTypeId();
			if (!BPConfigHandler.getBlacklist().contains(blockID)) {
				BPBlockLocation blockLoc = new BPBlockLocation(block);
				pl.worldDatabases.get(blockLoc.getWorld()).put(blockLoc, e.getPlayer().getName());
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {	
		Block block = e.getBlock();
		BPBlockLocation blockLoc = new BPBlockLocation(block);
		Player p = e.getPlayer();
		WorldDatabase database = pl.worldDatabases.get(blockLoc.getWorld());
		
		if (database.containsKey(blockLoc)) {
			String player = p.getName();
			String blockowner = database.get(blockLoc);
			if (!blockowner.equals(player)) {
				if (!(BPConfigHandler.getFriendslist(blockowner) == null)) {
					if (BPConfigHandler.getFriendslist(blockowner).contains(player)) {
						database.remove(blockLoc);
						return;
					} else {
						e.setCancelled(true);
						p.sendMessage(ChatColor.YELLOW + "You can not break blocks owned by: " + blockowner);
					}
				} else if (pl.UsersBypass.contains(player)) {
					database.remove(blockLoc);
				} else {							
					e.setCancelled(true);
					p.sendMessage(ChatColor.YELLOW + "You can not break blocks owned by: " + blockowner);
				} 
			} else
				database.remove(blockLoc);
		}
		
		/*
		*Check if the player is holding the "utility-tool" and has the admin permission. If so, do not let him break blocks"
		*This is used for things that break instantly (redstone, saplings, etc)
		*/
		if (p.getItemInHand().getTypeId() == BPConfigHandler.getUtilTool()) {
			if (pl.isAuthorized(p, "bp.admin")) e.setCancelled(true);
		}
	}
}