package fr.eazyender.odyssey.gameplay.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.eazyender.odyssey.gameplay.stats.Stat;
import fr.eazyender.odyssey.utils.NBTEditor;

public class ItemUtils {

	public static ItemStack getItem(ItemStack is, String name, List<String> lore, int modelData) {
		ItemMeta meta = is.getItemMeta();
		if (name != null)
			meta.setDisplayName(name);
		if (lore != null) {
			List<String> loreL = new ArrayList<>();
			for (String line : lore)
				loreL.add(line.replace("&", "§"));
			meta.setLore(loreL);
		}
		meta.setCustomModelData(modelData);
		is.setItemMeta(meta);

		return is;
	}
	
	public static void giveItem(Player p, String id) {
		ItemStack item = ItemDB.getItem(id);
		HashMap<Integer, ItemStack> map = p.getInventory().addItem(item);
		if (!map.isEmpty()) {
			p.getWorld().dropItem(p.getLocation(), item);
		}
		
	}
	
	public static int getStat(ItemStack is, Stat stat) {
		return NBTEditor.getInt(is, stat.name());
	}
	
	public static ItemType getType(ItemStack is) {
		if (NBTEditor.getString(is, "type") == null) return null;
		return ItemType.valueOf(NBTEditor.getString(is, "type"));
	}
	
	public static ItemRank getRank(ItemStack is) {
		if (NBTEditor.getString(is, "rank") == null) return null;
		return ItemRank.valueOf(NBTEditor.getString(is, "rank"));
	}
	
	public static int getNumericInfo(ItemStack is, String key) {
		return NBTEditor.getInt(is, key);
	}
	
	public static String getInfo(ItemStack is, String key) {
		return NBTEditor.getString(is, key);
	}

	
}

