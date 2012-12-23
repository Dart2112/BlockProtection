package info.kanlaki101.blockprotection.utilities;

import java.io.Serializable;

import org.bukkit.block.Block;

public class BPBlockLocation implements Serializable{

	private static final long serialVersionUID = 1L;
	private int x,y,z;
	private String world;
	
	
	public BPBlockLocation(Block b) {
		x=b.getX();
		y=b.getY();
		z=b.getZ();
		world = b.getWorld().getName();
	}
	
	public BPBlockLocation(int x, int y, int z, String world) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = world;
	}
	
	public BPBlockLocation(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public boolean equals(Object obj) {
		BPBlockLocation o = (BPBlockLocation)obj;
		if ((o.x == x) && (o.y == y) && (o.z == z) && world.equals(o.getWorld()))
			return true;
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

