package fr.eazyender.odyssey.utils.item;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class IWand extends IItem{
	
	
	private double mana_boost = 0.0;
	private double[] element_boost = {1.0,1.0,1.0,1.0};
	private int[] color = {255,255,255};
	
	public IWand(String name, List<String> desc, int custommodeldata, int[] color, double mana_boost, double[] element_boost) {
		super(name, desc, custommodeldata);
		this.mana_boost = mana_boost;
		this.element_boost = element_boost;
		this.color = color;
	}
	
	

	public int[] getColor() {
		return color;
	}



	public void setColor(int[] color) {
		this.color = color;
	}



	public double getMana_boost() {
		return mana_boost;
	}

	public void setMana_boost(double mana_boost) {
		this.mana_boost = mana_boost;
	}

	public double[] getElement_boost() {
		return element_boost;
	}

	public void setElement_boost(double[] element_boost) {
		this.element_boost = element_boost;
	}


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getLore() {
		return this.desc;
	}

	public void setLore(List<String> lore) {
		this.desc = lore;
		
	}

	public int getCustomModelData() {
		return this.customModelData;
	}

	public void setCustomModelData(int customModelData) {
		this.customModelData = customModelData;
		
	}
	
	@Override
	public ItemStack getItemStack() {
		
		ItemStack item = new ItemStack(Material.STICK, 1);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(name);
		im.setLore(desc);
		im.setCustomModelData(customModelData);
		item.setItemMeta(im);
		
		return item;
	}
	
	

}
