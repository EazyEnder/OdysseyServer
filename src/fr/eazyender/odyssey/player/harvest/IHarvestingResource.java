package fr.eazyender.odyssey.player.harvest;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class IHarvestingResource {
	
	private String name;
	private List<Material> blocks;
	private List<Double> drops;
	private ItemStack resource;
	private String tool;
	private double timer;
	
	
	public IHarvestingResource(String name, ItemStack resource, List<Material> blocks, List<Double> drops, String tool, double timer) {
		this.name = name;
		this.blocks = blocks;
		this.drops = drops;
		this.resource =resource;
		this.tool = tool;
		this.timer = timer;
	}
	
	public IHarvestingResource(String name, ItemStack resource, Material block, Double drop, String tool, double timer) {
		this.name = name;
		this.blocks = new ArrayList<Material>();
		this.blocks.add(block);
		this.drops = new ArrayList<Double>();
		this.drops.add(drop);
		this.resource =resource;
		this.tool = tool;
		this.timer = timer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Material> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<Material> blocks) {
		this.blocks = blocks;
	}

	public List<Double> getDrops() {
		return drops;
	}

	public void setDrops(List<Double> drops) {
		this.drops = drops;
	}

	public ItemStack getResource() {
		return resource;
	}

	public void setResource(ItemStack resource) {
		this.resource = resource;
	}

	public String getTool() {
		return tool;
	}

	public void setTool(String tool) {
		this.tool = tool;
	}

	public double getTimer() {
		return timer;
	}

	public void setTimer(double timer) {
		this.timer = timer;
	}
	
	
	
	

}
