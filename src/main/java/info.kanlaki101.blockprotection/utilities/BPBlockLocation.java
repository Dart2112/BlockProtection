package info.kanlaki101.blockprotection.utilities;

import java.io.Serializable;

import org.bukkit.block.Block;

public class BPBlockLocation implements Serializable {

	private static final long serialVersionUID = 7539402891616033846L;
	private int x,y,z;
	private String world;
	
	
	public BPBlockLocation(Block b) {
		this.x = b.getX();
		this.y = b.getY();
		this.z = b.getZ();
		this.world = b.getWorld().getName();
	}
	
	public BPBlockLocation(int x, int y, int z, String world) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = world;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof BPBlockLocation) {
			BPBlockLocation o = (BPBlockLocation)obj;
			return (o.x == x) && (o.y == y) && (o.z == z) && world.equals(o.getWorld());
		}
		return false;
	}
	
	public int hashCode() {
		int hashCode = x+y+z;
		for(char c : world.toCharArray()) {
			hashCode += c;
		}
		return hashCode;
	}
	
	public String toString() {
		return "("+x+","+y+","+z+ "," + world + ")";
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}
	
	public String getWorld() {
		return world;
	}
}

