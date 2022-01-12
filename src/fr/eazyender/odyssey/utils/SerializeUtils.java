package fr.eazyender.odyssey.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class SerializeUtils {
	
	public static String getBlockLocationString(Location loc) {
		String str = null;
		
		if(loc != null) {
			str = "";
			
			if(loc.getWorld() != null) {
				str = str + loc.getWorld().getName();
			}else {
				str = str + "NaN";
			}
			
			str = str + "," + loc.getBlockX();
			str = str + "," + loc.getBlockY();
			str = str + "," + loc.getBlockZ();
		}
		
		return str;
	}
	
	public static Location getBlockLocationFromString(String str) {
		
		String[] str_array = str.split(",");
		World world = null;
		if(str_array[0] != "NaN" && str_array.length == 4) {
		world = Bukkit.getWorld(str_array[0]);
		int x = Integer.parseInt(str_array[1]);
		int y = Integer.parseInt(str_array[2]);
		int z = Integer.parseInt(str_array[3]);
		return new Location(world,x,y,z);
		}
		
		return null;
	}

}
