package fr.eazyender.odyssey.gameplay.city.building;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public class IBuild {
	
	private String name;
	private String schem_path;
	private String biome;
	private double[] size = {.0,.0,.0};
	private String job;
	
	//Index : 0 for prebuild, 1 for T1 to T2, ...
	private List<List<ItemStack>> upgrade;
	private List<Double> work_time;
	
	public IBuild(String name, String schem_path, String biome, double[] size,String job, List<List<ItemStack>> upgrade, List<Double> work_time) {
		this.name = name;
		this.schem_path = schem_path;
		this.biome = biome;
		this.size = size;
		this.job = job;
		
		this.upgrade = upgrade;
		this.work_time = work_time;
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

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
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
