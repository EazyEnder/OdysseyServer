package fr.eazyender.odyssey.utils.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

public class WorldUtils {
	
	public static List<IWorld> worlds = new ArrayList<IWorld>();
	
	public static void initWorlds() {
		
		//MONDE TEST
		Map<String, Double> temp = new HashMap<String, Double>();
		temp.put("PLAINS", 10.0);
		worlds.add(new IWorld("Test","world",4, 0.0,true,null,temp));
		
	}
	
	public static IWorld getWorldOfPlayer(Player player) {
		IWorld world = null;
		String id = player.getWorld().getName();
		for (IWorld w : WorldUtils.worlds) {
			if(world == null) {
				if(w.getId().equals(id)) {
					world = w;
				}
			}
		}
		return world;
	}

}
