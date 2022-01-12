package fr.eazyender.odyssey.utils.item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public abstract class IItem {
	
	protected String name;
	protected List<String> desc = new ArrayList<String>();
	protected int customModelData;
	
	public IItem(String name, List<String> desc, int customModelData) {
		this.name = name;
		this.desc = desc;
		this.customModelData = customModelData;
	}

	public abstract String getName();

	public abstract void setName(String name);
	
	public abstract List<String> getLore();
	
	public abstract void setLore(List<String> lore);

	public abstract int getCustomModelData();

	public abstract void setCustomModelData(int customModelData);
	
	public abstract ItemStack getItemStack();
	
	

}
