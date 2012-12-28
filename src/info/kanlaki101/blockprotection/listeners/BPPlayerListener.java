package info.kanlaki101.blockprotection.listeners;

import info.kanlaki101.blockprotection.BlockProtection;
import info.kanlaki101.blockprotection.utilities.BPBlockLocation;
import info.kanlaki101.blockprotection.utilities.BPConfigHandler;
import info.kanlaki101.blockprotection.utilities.WorldDatabase;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BPPlayerListener implements Listener {
	
	BlockProtection pl;

	public BPPlayerListener(BlockProtection instance) {
		pl = instance;
	}
	ChatColor YELLOW = ChatColor.YELLOW;
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		String player = event.getPlayer().getName();
		BPConfigHandler.loadConfig();
		if (BPConfigHandler.enableByDefault() == true) pl.Users.add(player);
		if (BPConfigHandler.bypassByDefault() == true) pl.UsersBypass.add(player);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		String player = event.getPlayer().getName();
		if (pl.Users.contains(player)) pl.Users.remove(player);
		if (pl.UsersBypass.contains(player)) pl.UsersBypass.remove(player);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (e.getItem() == null) return;
		int item = p.getItemInHand().getTypeId();
		
		if (item == BPConfigHandler.getUtilTool()) {
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (!pl.isAuthorized(p, "bp.user")) return;
				blockInfo(e);
			}
			
			if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
				if (!pl.isAuthorized(p, "bp.admin")) return;
				addBlock(e);
			}
		}
	}
	
	
	private void blockInfo(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		BPBlockLocation blockLoc = new BPBlockLocation(e.getClickedBlock());
		WorldDatabase database = pl.worldDatabases.get(blockLoc.getWorld());
		if (database.containsKey(blockLoc)) {
			p.sendMessage(YELLOW + "Block owned by: " + database.get(blockLoc) + ".");
		}
		else {
			p.sendMessage(YELLOW + "Block not owned.");
		}
	}
	
	private void addBlock(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		BPBlockLocation blockLoc = new BPBlockLocation(e.getClickedBlock());
		WorldDatabase database = pl.worldDatabases.get(blockLoc.getWorld());
		int blockID = e.getClickedBlock().getTypeId();
		
		if (database.containsKey(blockLoc)) {
			p.sendMessage(YELLOW + "Can not add. Block already owned.");
		}
		else {
			if (!BPConfigHandler.getBlacklist().contains(blockID)) {
				database.put(blockLoc, p.getName());
				p.sendMessage(YELLOW + "Block added to the database.");
			}
			else {
				p.sendMessage(YELLOW + "Can not add block. It is on the blacklist.");
			}
		}
	}
	
}