package fr.eazyender.odyssey.gameplay.city.building;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.util.Vector;
public class IRuin {
	
	private World world;
	private Vector pos;
	private IBuild build;
	int rotationY;
	private int tier;
	
	public IRuin(Vector pos, int rotationY, World world, IBuild build, int tier) {
		this.pos = pos;
		this.rotationY = rotationY;
		this.world = world;
		this.build = build;
		this.tier = tier;
	}
	
	public void loop() {
		
	}
	
	public boolean destroy() {
		
		return false;
	}
	
	@Override
	public String toString() {
		
		String str = pos.getX() + "!!Base;" + pos.getY() + "!!Base;" + pos.getZ() + "!!Base;";
		str += rotationY + "!!Base;";
		str += world.getName() + "!!Base;";
		str += build.getName() + "!!Base;";
		str += tier + "!!Base;";
	
		return str;
	} 
	
	public static IRuin fromString(String str) {
		IRuin ruin = null;
		
		String[] str_b = str.split("!!Base;");
		Vector pos = new Vector(Double.parseDouble(str_b[0]),
				Double.parseDouble(str_b[1]),
				Double.parseDouble(str_b[2]));
		
		int rotationY = Integer.parseInt(str_b[3]);
		World world = Bukkit.getWorld(str_b[4]);
		IBuild build = BuildManager.getBuildByName(str_b[5]);
		int tier = Integer.parseInt(str_b[6]);
		
		ruin = new IRuin(pos,rotationY,world,build,tier);
		
		return ruin;
	}

}
