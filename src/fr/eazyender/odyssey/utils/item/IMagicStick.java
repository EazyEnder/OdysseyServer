package fr.eazyender.odyssey.utils.item;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class IMagicStick extends IItem{

	public IMagicStick(String name, List<String> desc, int customModelData) {
		super(name, desc, customModelData);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
		
	}

	@Override
	public List<String> getLore() {
		return desc;
	}

	@Override
	public void setLore(List<String> lore) {
		this.desc = lore;
	}

	@Override
	public int getCustomModelData() {
		return customModelData;
	}

	@Override
	public void setCustomModelData(int customModelData) {
		this.customModelData=customModelData;
		
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
