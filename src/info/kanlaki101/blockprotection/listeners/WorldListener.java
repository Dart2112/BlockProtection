package info.kanlaki101.blockprotection.listeners;

import java.io.File;

import info.kanlaki101.blockprotection.BlockProtection;
import info.kanlaki101.blockprotection.utilities.WorldDatabase;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class WorldListener implements Listener {

	BlockProtection plugin;
	
	public WorldListener(BlockProtection plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onWorldLoad(WorldLoadEvent event) {
		String world = event.getWorld().getName();
		File dbFile = new File(plugin.getDataFolder(), world+".db");
		plugin.worldDatabases.put(world, new WorldDatabase(dbFile, plugin));
	}
	
	@EventHandler
	public void onWorldUnload(WorldUnloadEvent event) {
		plugin.worldDatabases.remove(event.getWorld().getName());
	}
}
