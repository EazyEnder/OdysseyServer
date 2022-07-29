package fr.eazyender.odyssey.gameplay.city.building.objects.utils;

import org.bukkit.inventory.ItemStack;

public class IRecipeFurnace {
	
	private String name;
	private double heat;
	private ItemStack ore1, ore2, result;
	private int work_time;
	private int level;
	
	public IRecipeFurnace(String name, ItemStack ore1, ItemStack ore2, ItemStack result, int work_time, double heat, int level) {
		this.name = name;
		this.heat = heat;
		this.ore1 = ore1;
		this.ore2 = ore2;
		this.result = result;
		this.work_time = work_time;
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public double getHeat() {
		return heat;
	}

	public ItemStack getOre1() {
		return ore1;
	}

	public ItemStack getOre2() {
		return ore2;
	}

	public ItemStack getResult() {
		return result;
	}

	public int getWork_time() {
		return work_time;
	}

	public int getLevel() {
		return level;
	}
	
	

}
