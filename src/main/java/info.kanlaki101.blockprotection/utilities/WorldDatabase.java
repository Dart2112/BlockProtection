package info.kanlaki101.blockprotection.utilities;

import info.kanlaki101.blockprotection.BlockProtection;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.logging.Logger;

public class WorldDatabase {

    HashMap<BPBlockLocation, String> database = new HashMap<BPBlockLocation, String>();
    File dbFile;
    Logger log;

    public WorldDatabase(File dbFile, BlockProtection plugin) {
        this.dbFile = dbFile;
        this.dbFile.getParentFile().mkdirs();
        this.log = plugin.getLogger();
        try {
            if (dbFile.createNewFile())
                log.info("Created new database file for " + dbFile.getName().replaceAll(".db", "") + "");
            else
                log.info("Using existing database file for " + dbFile.getName().replaceAll(".db", "") + "");
            loadDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        RandomAccessFile rf = null;
        try {
            dbFile.delete();

            rf = new RandomAccessFile(dbFile, "rw");

            for (BPBlockLocation currBlock : database.keySet()) {
                rf.writeInt(currBlock.getX());
                rf.writeInt(currBlock.getY());
                rf.writeInt(currBlock.getZ());
                rf.writeChar('|');
                rf.writeChars(currBlock.getWorld());
                rf.writeChar('|');
                rf.writeChars(database.get(currBlock));
                rf.writeChar('\0');
            }
            rf.close();
        } catch (IOException ex) {
            log.warning("Could not save database file: " + dbFile.getName());
        } finally {
            if (rf != null) {
                try {
                    rf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadDatabase() {
        RandomAccessFile rf = null;
        try {
            rf = new RandomAccessFile(dbFile, "r");
            while (true) {
                int x = rf.readInt();
                int y = rf.readInt();
                int z = rf.readInt();
                rf.readChar();
                String world = "";
                String player = "";
                char curr;
                while ((curr = rf.readChar()) != '|') {
                    world += curr;
                }
                while ((curr = rf.readChar()) != '\0') {
                    player += curr;
                }
                database.put(new BPBlockLocation(x, y, z, world), player);
            }
        } catch (EOFException ex) {
            log.info("Finished loading database.");
        } catch (IOException ex) {
            log.warning("Error while reading the database.");
            ex.printStackTrace();
        }
    }

    public void put(BPBlockLocation key, String value) {
        database.put(key, value);
    }

    public boolean containsKey(BPBlockLocation key) {
        return database.containsKey(key);
    }

    public String get(BPBlockLocation key) {
        return database.get(key);
    }

    public void remove(BPBlockLocation key) {
        database.remove(key);
    }
}
