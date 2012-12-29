package info.kanlaki101.blockprotection.listeners;

import info.kanlaki101.blockprotection.BlockProtection;
import info.kanlaki101.blockprotection.utilities.BPBlockLocation;
import info.kanlaki101.blockprotection.utilities.BPConfigHandler;
import info.kanlaki101.blockprotection.utilities.WorldDatabase;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
		Block block = e.getBlockPlaced();
		if(!BPConfigHandler.allowPlacingAbove()) {
			if(isNearProtectedBlock(block, player)) e.setCancelled(true);
		}
		
		if (pl.Users.contains(player)) {
			int blockID = block.getTypeId();
			if (!BPConfigHandler.getBlacklist().contains(blockID)) {
				BPBlockLocation blockLoc = new BPBlockLocation(block);
				pl.worldDatabases.get(blockLoc.getWorld()).put(blockLoc, e.getPlayer().getName());
			}
		}
	}
	
	private boolean isNearProtectedBlock(Block b, String player) {
		BlockFace[] faces = new BlockFace[] {BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST, BlockFace.SOUTH,
				BlockFace.SOUTH_WEST, BlockFace.SOUTH_EAST, BlockFace.WEST, BlockFace.EAST};
		for(BlockFace face : faces) {
			Block relative = b.getRelative(face);
			if(isProtected(relative, player)) return true;
		}
		return false;
	}

	private boolean isProtected(Block relative, String player) {
		BPBlockLocation blockLoc = new BPBlockLocation(relative);
		WorldDatabase database = pl.worldDatabases.get(blockLoc.getWorld());
		if(database.containsKey(blockLoc)) {
			String blockowner = database.get(blockLoc);
			if(!blockowner.equals(player)) {
				if(!isFriendOf(player, blockowner)) {
					if (pl.UsersBypass.contains(player)) {
						return false;
					}
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isFriendOf(String player, String owner) {
		if(!(BPConfigHandler.getFriendslist(owner) == null)) {
			if(BPConfigHandler.getFriendslist(owner).contains(player)) {
				return true;
			}
		}
		return false;
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
				if (isFriendOf(player, blockowner) || pl.UsersBypass.contains(player)) {
					database.remove(blockLoc);
					return;
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