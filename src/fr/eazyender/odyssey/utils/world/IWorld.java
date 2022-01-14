package fr.eazyender.odyssey.utils.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IWorld {
	
	private String name;
	private String id;
	private int surface;
	private List<String> biome_list = new ArrayList<String>();
	private Map<String,Double> temp = new HashMap<String,Double>();
	
	public IWorld(String name, String id, int surface, List<String> biome_list, Map<String,Double> temp) {
		this.name = name;
		this.id = id;
		this.surface = surface;
		this.biome_list = biome_list;
		this.temp = temp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSurface() {
		return surface;
	}

	public void setSurface(int surface) {
		this.surface = surface;
	}

	public List<String> getBiome_list() {
		return biome_list;
	}

	public void setBiome_list(List<String> biome_list) {
		this.biome_list = biome_list;
	}

	public Map<String, Double> getTemp() {
		return temp;
	}

	public void setTemp(Map<String, Double> temp) {
		this.temp = temp;
	}

	
	

}
