package info.kanlaki101.blockprotection;

import info.kanlaki101.blockprotection.commands.BP;
import info.kanlaki101.blockprotection.commands.BPAdd;
import info.kanlaki101.blockprotection.commands.BPAdmin;
import info.kanlaki101.blockprotection.commands.BPClean;
import info.kanlaki101.blockprotection.commands.BPClear;
import info.kanlaki101.blockprotection.commands.BPGive;
import info.kanlaki101.blockprotection.commands.BPList;
import info.kanlaki101.blockprotection.commands.BPReload;
import info.kanlaki101.blockprotection.commands.BPRemove;
import info.kanlaki101.blockprotection.commands.BPSave;
import info.kanlaki101.blockprotection.commands.BPTool;
import info.kanlaki101.blockprotection.commands.BPTransfer;
import info.kanlaki101.blockprotection.listeners.BPBlockListener;
import info.kanlaki101.blockprotection.listeners.BPPlayerListener;
import info.kanlaki101.blockprotection.listeners.WorldListener;
import info.kanlaki101.blockprotection.utilities.BPConfigHandler;
import info.kanlaki101.blockprotection.utilities.BPDatabase;
import info.kanlaki101.blockprotection.utilities.LoggedOutChecker;
import info.kanlaki101.blockprotection.utilities.WorldDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class BlockProtection extends JavaPlugin {
	
	public Logger log;
	private BPBlockListener blockListener = new BPBlockListener(this);
	private BPPlayerListener playerListener = new BPPlayerListener(this);
	private WorldListener wListener = new WorldListener(this);
	public BPConfigHandler configHandler = new BPConfigHandler(this);
	public HashMap<String, WorldDatabase> worldDatabases = new HashMap<>();
	public List<String> Users = new ArrayList<>();
	public List<String> UsersBypass = new ArrayList<>();
	public HashMap<String, Date> loggedOut = new HashMap<>();
	public BPDatabase database;
    public String GPlayer = "";
    public WorldEditPlugin worldEdit;

    @Override
    public void onLoad() {
    	this.log = this.getLogger();
    }
    
	public void onEnable() {
		setupConfiguration();
		setupDatabase();
		registerListeners();
		setupLoggedOutPlayers();
		registerCommands();
		checkWorldEdit();
		
		log.info("Enabling...");
	}

	@SuppressWarnings("unchecked")
	private void setupLoggedOutPlayers() {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(this.getDataFolder(), "loggedout.dat")));
			Object obj = in.readObject();
			if(obj instanceof HashMap<?, ?>) {
				loggedOut = (HashMap<String, Date>) obj;
			}
			in.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		this.getServer().getScheduler().scheduleSyncDelayedTask(this, new LoggedOutChecker(this), 4 * 60 * 20L);
	}

	private void checkWorldEdit() {
		WorldEditPlugin wePlugin = (WorldEditPlugin)getServer().getPluginManager().getPlugin("WorldEdit");
        if (wePlugin != null)
        	worldEdit = wePlugin;
	}

	private void setupConfiguration() {
		configHandler.setupConfig();
		configHandler.setupFriendsList();
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
		getCommand("bpgive").setExecutor(new BPGive(this));
        getCommand("bptransfer").setExecutor(new BPTransfer(this));
        getCommand("bpclean").setExecutor(new BPClean(this));
	}

	public void onDisable() {
		log.info("Database saving...");
		database.close();
		saveLoggedOutPlayers();
		log.info("Disabling...");
	}

	private void saveLoggedOutPlayers() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(this.getDataFolder(), "loggedout.dat")));
			out.writeObject(loggedOut);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}