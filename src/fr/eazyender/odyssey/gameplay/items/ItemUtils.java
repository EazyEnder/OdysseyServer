package fr.eazyender.odyssey.gameplay.items;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.eazyender.odyssey.gameplay.stats.Stat;
import fr.eazyender.odyssey.utils.NBTEditor;
import net.md_5.bungee.api.ChatColor;

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
		if (NBTEditor.getString(is, "type") == null)
			return null;
		return ItemType.valueOf(NBTEditor.getString(is, "type"));
	}

	public static ItemRank getRank(ItemStack is) {
		if (NBTEditor.getString(is, "rank") == null)
			return null;
		return ItemRank.valueOf(NBTEditor.getString(is, "rank"));
	}

	public static int getNumericInfo(ItemStack is, String key) {
		return NBTEditor.getInt(is, key);
	}

	public static String getInfo(ItemStack is, String key) {
		return NBTEditor.getString(is, key);
	}

	public static ArrayList<String> buildLore(ItemStack is) {
		ArrayList<String> lore = new ArrayList<>();
		if (getRank(is) != null && getType(is) != null) {
			ItemRank rank = getRank(is);
			lore.add("§8§m                            ");
			lore.add("       " + rank.getColor() + "[" + rank.name() + "]");
			lore.add("       " + 
					"§f" + getType(is).name().substring(0, 1) + getType(is).name().substring(1).toLowerCase());
			lore.add("§8§m                            ");
			lore.add(" ");

		}
		if (getInfo(is, "description") != null) {
			String description = getInfo(is, "description");
			description += " ";

			String lastColor = null;
			for (String lineDesc : splitDescription(description)) {
				if (lastColor == null || lastColor == "") {
					lastColor = "§f";
				}
				lore.add(lastColor + lineDesc.replace("&", "§"));
				lastColor = org.bukkit.ChatColor.getLastColors(lineDesc);

			}
			lore.add("§8§m                            ");
			lore.add(" ");
		}
		for (Stat stat : Stat.values()) {
			if (getStat(is, stat) != 0) {
				double value = getStat(is, stat);
				NumberFormat nf = new DecimalFormat("##.###");
				 
				String sign = "+";
				if (value < 0) sign = "";
				String percentage = "";
				if (stat == Stat.REGENAURA || stat == Stat.REGENMP) {
					value = (value / 25) * 100;
					percentage = "%";
				}
				else if (stat == Stat.FIRE || stat == Stat.WATER || stat == Stat.EARTH || stat == Stat.WIND || stat == Stat.LIGHT || stat == Stat.SHADOW) {
					percentage = "%";
				} 
				lore.add(stat.getShowing() + " : §f" + sign + nf.format(value) + percentage);
			}
		}

		return lore;
	}

	public static ItemStack cloneIfNotNull(ItemStack is) {
		if (is != null) return is.clone();
		else return null;
	}
	
	public static ArrayList<ItemStack> cloneIfNotNull(ItemStack[] items) {
		ArrayList<ItemStack> itemsReturn = new ArrayList<>();
		for(ItemStack is : items) {
			if (is != null) itemsReturn.add(is.clone());
			else itemsReturn.add(null);
		}
		return itemsReturn;

	}
	
	public static ArrayList<String> splitDescription(String s) {
		String regex = ".{1,30}\\s";
		Matcher m = Pattern.compile(regex).matcher(s);
		ArrayList<String> lines = new ArrayList<>();
		while (m.find()) {
			lines.add(m.group());
		}
		return lines;
	}

	public static final Pattern HEX_PATTERN = Pattern.compile("&#(\\w{5}[0-9a-f])");

	public String translateHexCodes(String textToTranslate) {
		Matcher matcher = HEX_PATTERN.matcher(textToTranslate);
		StringBuffer buffer = new StringBuffer();
		while (matcher.find())
			matcher.appendReplacement(buffer, ChatColor.of("#" + matcher.group(1)).toString());
		return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
	}

}
