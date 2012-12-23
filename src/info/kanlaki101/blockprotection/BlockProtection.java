package info.kanlaki101.blockprotection;

import info.kanlaki101.blockprotection.commands.BP;
import info.kanlaki101.blockprotection.commands.BPAdd;
import info.kanlaki101.blockprotection.commands.BPAdmin;
import info.kanlaki101.blockprotection.commands.BPClear;
import info.kanlaki101.blockprotection.commands.BPList;
import info.kanlaki101.blockprotection.commands.BPReload;
import info.kanlaki101.blockprotection.commands.BPRemove;
import info.kanlaki101.blockprotection.commands.BPSave;
import info.kanlaki101.blockprotection.commands.BPTool;
import info.kanlaki101.blockprotection.listeners.BPBlockListener;
import info.kanlaki101.blockprotection.listeners.BPPlayerListener;
import info.kanlaki101.blockprotection.listeners.WorldListener;
import info.kanlaki101.blockprotection.utilities.BPConfigHandler;
import info.kanlaki101.blockprotection.utilities.BPDatabase;
import info.kanlaki101.blockprotection.utilities.WorldDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockProtection extends JavaPlugin {
	
	public Logger log;
	private BPBlockListener blockListener = new BPBlockListener(this);
	private BPPlayerListener playerListener = new BPPlayerListener(this);
	private WorldListener wListener = new WorldListener(this);
	public BPConfigHandler configHandler = new BPConfigHandler(this);
	public HashMap<String, WorldDatabase> worldDatabases = new HashMap<String, WorldDatabase>();
	public List<String> Users = new ArrayList<String>();
	public List<String> UsersBypass = new ArrayList<String>();
	public BPDatabase database;
    public Permission permission = null;
	
    @Override
    public void onLoad() {
    	this.log = this.getLogger();
    }
    
	public void onEnable() {
		setupConfiguration();
		setupPermissions();
		setupDatabase();
		registerListeners();
		registerCommands();
		
		log.info("Enabling...");
	}

	private void setupConfiguration() {
		configHandler.setupConfig();
		configHandler.setupFriendslist();
	}

	private void setupDatabase() {
		database = new BPDatabase(this);
		database.registerDatabases();
		database.scheduleAutosave();
	}

	private void registerListeners() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(blockListener, this);
		pm.registerEvents(playerListener, this);
		pm.registerEvents(wListener, this);
	}

	private void registerCommands() {
		getCommand("bp").setExecutor(new BP(this));
		getCommand("bpadmin").setExecutor(new BPAdmin(this));
		getCommand("bpreload").setExecutor(new BPReload(this));
		getCommand("bpadd").setExecutor(new BPAdd(this));
		getCommand("bpremove").setExecutor(new BPRemove(this));
		getCommand("bplist").setExecutor(new BPList(this));
		getCommand("bptool").setExecutor(new BPTool(this));
		getCommand("bpclear").setExecutor(new BPClear(this));
		getCommand("bpsave").setExecutor(new BPSave(this));
	}

	public void onDisable() {
		log.info("Database saving...");
		database.close();
		log.info("Disabling...");
	}
    
    public Boolean setupPermissions() { //Check for permissions plugin (VAULT)
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
	
	public boolean isAuthorized(CommandSender sender, String node) { //Check if the sender is an OP or has the correct permissions
		if (permission.has(sender, node) || sender.isOp()) return true;
		return false;
	}
	
	public boolean isAuthorized(Player player, String node) { //Check if player is an OP or has the correct permissions
		if (permission.has(player, node) || player.isOp()) return true;
		return false;
	}
	
}