package fr.eazyender.odyssey.gameplay.city.building;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public class IBuild {
	
	private String name;
	private String schem_path;
	private String biome;
	private double[] size = {.0,.0,.0};
	private String type;
	
	//Index : 0 for prebuild, 1 for T1 to T2, ...
	private List<List<ItemStack>> upgrade;
	private List<Double> work_time;
	
	private List<Integer> decay_time;
	
	
	public IBuild(String name, String schem_path, String biome, double[] size,String type, List<List<ItemStack>> upgrade, List<Double> work_time, List<Integer> decay_time) {
		this.name = name;
		this.schem_path = schem_path;
		this.biome = biome;
		this.size = size;
		this.type = type;
		
		this.upgrade = upgrade;
		this.work_time = work_time;
		this.decay_time = decay_time;
	}
	
	

	public List<Integer> getDecay_time() {
		return decay_time;
	}



	public void setDecay_time(List<Integer> decay_time) {
		this.decay_time = decay_time;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSchem_path() {
		return schem_path;
	}

	public void setSchem_path(String schem_path) {
		this.schem_path = schem_path;
	}

	public String getBiome() {
		return biome;
	}

	public void setBiome(String biome) {
		this.biome = biome;
	}

	public double[] getSize() {
		return size;
	}

	public void setSize(double[] size) {
		this.size = size;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<List<ItemStack>> getUpgrade() {
		return upgrade;
	}

	public void setUpgrade(List<List<ItemStack>> upgrade) {
		this.upgrade = upgrade;
	}

	public List<Double> getWork_time() {
		return work_time;
	}

	public void setWork_time(List<Double> work_time) {
		this.work_time = work_time;
	}
	
	
	
	

}
