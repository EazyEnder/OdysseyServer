package fr.eazyender.odyssey.gameplay.masteries;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.configuration.file.YamlConfiguration;

public class ExpHandler {
	
	final static File experienceFile = new File("plugins/OdysseyPlugin/xp.yml");
	
	public static HashMap<String, Double> xpMobs = new HashMap<>();
	
	public static void init() {
		if (!experienceFile.exists()) {
			try {
				experienceFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			YamlConfiguration config = YamlConfiguration.loadConfiguration(experienceFile);
			for(String key : config.getKeys(false)) {
				xpMobs.put(key, config.getDouble(key));
			}
		}
	}
	
	

}
