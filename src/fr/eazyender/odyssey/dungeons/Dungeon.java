package fr.eazyender.odyssey.dungeons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("Dungeon")
public class Dungeon implements ConfigurationSerializable {

	String id;
	HashMap<Integer, Location> startLocs;
	HashMap<Integer, String> offsets;
	HashMap<Location, String> mobs;
	

	public Dungeon(String id, HashMap<Integer, Location> startLocs, HashMap<Integer, String> offsets,
			HashMap<Location, String> mobs) {
		this.id = id;
		this.startLocs = startLocs;
		this.offsets = offsets;
		this.mobs = mobs;
	}




	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public HashMap<Integer, Location> getStartLocs() {
		return startLocs;
	}


	public void setStartLocs(HashMap<Integer, Location> startLocs) {
		this.startLocs = startLocs;
	}


	public HashMap<Integer, String> getOffsets() {
		return offsets;
	}


	public void setOffsets(HashMap<Integer, String> offsets) {
		this.offsets = offsets;
	}


	public HashMap<Location, String> getMobs() {
		return mobs;
	}


	public void setMobs(HashMap<Location, String> mobs) {
		this.mobs = mobs;
	}


	@Override
	public Map<String, Object> serialize() {

		HashMap<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("startLocs", startLocs);
		map.put("mobs", mobs);
		map.put("offsets", offsets);
		
		
		
		
		
		return map;
	}
	
	 @SuppressWarnings("unchecked")
	public static Dungeon deserialize(Map<String, Object> map) {
	     	String id = (String) map.get("id");
	     	HashMap<Integer,Location> startLocs = (HashMap<Integer,Location>) map.get("startLocs");
	     	HashMap<Integer, String> offsets =  (HashMap<Integer, String>) map.get("offsets");
	     	HashMap<Location, String> mobs = (HashMap<Location, String>) map.get("mobs");
	     	return new Dungeon(id,startLocs,offsets,mobs);
	    }

	
	 
	
}
