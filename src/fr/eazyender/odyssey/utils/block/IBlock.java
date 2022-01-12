package fr.eazyender.odyssey.utils.block;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class IBlock {
	
	private Material material = Material.NOTE_BLOCK;
	
	private String name;
	private int id;
	private Sound place_sound;
	private int source;
	private int mining_level;
	private int tool;
	private int item_model;
	
	public IBlock(String name, int id, int source, Material material, int item_model,Sound place_sound, int mining_level, int tool) {
		this.name = name;
		this.id = id;
		this.source = source;
		this.material = material;
		this.item_model = item_model;
		this.place_sound = place_sound;
		this.mining_level = mining_level;
		this.tool = tool;
	}

	public String getName() {
		return name;
	}
	
	

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getItem_model() {
		return item_model;
	}

	public void setItem_model(int item_model) {
		this.item_model = item_model;
	}

	public Material getMaterial() {
		return material;
	}

	public Sound getPlace_sound() {
		return place_sound;
	}

	public void setPlace_sound(Sound place_sound) {
		this.place_sound = place_sound;
	}

	public int getMining_level() {
		return mining_level;
	}

	public void setMining_level(int mining_level) {
		this.mining_level = mining_level;
	}

	public int getTool() {
		return tool;
	}

	public void setTool(int tool) {
		this.tool = tool;
	}
	
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.PAPER,1);
		ItemMeta im = item.getItemMeta();
		if(material.equals(Material.NOTE_BLOCK)) {
			if(getSource()!=0) {
				im.setDisplayName(BlockUtils.blocks.get(getSource()-1).getName());
				im.setCustomModelData(BlockUtils.blocks.get(getSource()-1).getItem_model());
			}else {
				im.setDisplayName(getName());
				im.setCustomModelData(getItem_model());	
			}
		}
		item.setItemMeta(im);
		return item;
	}
	
	
}
