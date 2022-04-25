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
	String name;
	HashMap<Integer, Location> startLocs;
	HashMap<Integer, String> offsets;
	HashMap<Location, String> mobs;
	String song;
	

	public Dungeon(String id, HashMap<Integer, Location> startLocs, HashMap<Integer, String> offsets,
			HashMap<Location, String> mobs, String song, String name) {
		this.id = id;
		this.startLocs = startLocs;
		this.offsets = offsets;
		this.mobs = mobs;
		this.song = song;
		this.name = name;
	}




	public String getSong() {
		return song;
	}




	public void setSong(String song) {
		this.song = song;
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

	
	

	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	@Override
	public Map<String, Object> serialize() {

		HashMap<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("startLocs", startLocs);
		map.put("mobs", mobs);
		map.put("offsets", offsets);
		map.put("song", song);
		map.put("name", name);
		
		
		
		return map;
	}
	
	 @SuppressWarnings("unchecked")
	public static Dungeon deserialize(Map<String, Object> map) {
	     	String id = (String) map.get("id");
	     	HashMap<Integer,Location> startLocs = (HashMap<Integer,Location>) map.get("startLocs");
	     	HashMap<Integer, String> offsets =  (HashMap<Integer, String>) map.get("offsets");
	     	HashMap<Location, String> mobs = (HashMap<Location, String>) map.get("mobs");
	     	String song = (String) map.get("song");
	     	String name = (String) map.get("name");
	     	return new Dungeon(id,startLocs,offsets,mobs, song, name);
	    }

	
	 
	
}
