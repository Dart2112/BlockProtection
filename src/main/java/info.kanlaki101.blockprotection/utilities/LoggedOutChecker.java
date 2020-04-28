package info.kanlaki101.blockprotection.utilities;

import info.kanlaki101.blockprotection.BlockProtection;

import java.util.Date;
import java.util.Iterator;

public class LoggedOutChecker implements Runnable {

	BlockProtection plugin;
	
	public LoggedOutChecker(BlockProtection plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		Date now = new Date();
		for (String player : plugin.loggedOut.keySet()) {
			int diffInDays = (int) ((now.getTime() - plugin.loggedOut.get(player).getTime()) / (1000 * 60 * 60 * 24));
			if (diffInDays > 5) {
				removePlayerFromDatabase(player);
			}
		}
	}

	private void removePlayerFromDatabase(String player) {
		for(String w : plugin.worldDatabases.keySet()) {
			WorldDatabase db = plugin.worldDatabases.get(w);
			if(db.database.containsValue(player)) {
				plugin.getLogger().warning("Removing all protections from player " + player);
				for (BPBlockLocation loc : db.database.keySet()) {
					if (db.database.get(loc).equals(player)) {
						db.remove(loc);
					}
				}
			}
		}
	}
}
