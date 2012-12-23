package info.kanlaki101.blockprotection.utilities;

import info.kanlaki101.blockprotection.BlockProtection;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;

/*
 * Database file is saved in the following format:
 * bytes: 4 - 4 - 4 - variable (null-terminated)
 * data:  x - y - z - world - player
 */
public class BPDatabase {

	Logger log;
	BlockProtection plugin;
	String prefix = "[BlockProtection] ";
	
	// File f is the database file
	public BPDatabase(BlockProtection plugin) {
		this.plugin = plugin;
	}
	
	public void scheduleAutosave() {
		int time = plugin.getConfig().getInt("save-interval") * 60;
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

			public void run() {
				Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + prefix + "Saving database. Expect some lag ...");
				save();
				log.info(prefix + "Database saved.");
				
			}
			
		}, time * 20L, time * 20L);
	}
	
	public void save() {
		for(String w : plugin.worldDatabases.keySet()) {
			plugin.worldDatabases.get(w).save();
		}
	}
	
	public void registerDatabases() {
		for(World w : plugin.getServer().getWorlds()) {
			File dbFile = new File(plugin.getDataFolder(), w.getName()+".db");
			plugin.worldDatabases.put(w.getName(), new WorldDatabase(dbFile, plugin));
		}
	}
	
	public void close() {
		save();
	}
}
