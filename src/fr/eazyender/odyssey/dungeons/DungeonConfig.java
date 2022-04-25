package fr.eazyender.odyssey.dungeons;

import java.io.File;
import org.bukkit.Location;

import org.bukkit.configuration.file.YamlConfiguration;


public class DungeonConfig {


	public static Dungeon getDungeon(String id) {
		return (Dungeon) YamlConfiguration.loadConfiguration(new File("plugins/OdysseyPlugin/Dungeons/" + id + ".yml")).get(id);
	}

	
	public static Location getStartLoc(String dungeonId, int id, int pos) {
		Dungeon d = getDungeon(dungeonId);
		return applyOffset(d.getStartLocs().get(pos), d.getOffsets().get(id));
	}
	
	public static Location applyOffset(Location loc, String offset) {
		Location newLoc = loc.clone();
		newLoc.add(Double.parseDouble(offset.split(",")[0]), 0, Double.parseDouble(offset.split(",")[1]));
		return newLoc;
	}
	

	

}
