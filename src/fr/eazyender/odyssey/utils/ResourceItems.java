package fr.eazyender.odyssey.utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import fr.eazyender.odyssey.OdysseyPl;
import net.md_5.bungee.api.ChatColor;

public class ResourceItems {
	
	private static List<ItemStack> items = new ArrayList<ItemStack>();
	
	public static void initItems() {
		
		List<String> fibre_lore = new ArrayList<String>();
		fibre_lore.add(ChatColor.of(new Color(175, 186, 141)) +"Expansion cellulaire possédant d'intéressantes");
		fibre_lore.add(ChatColor.of(new Color(175, 186, 141)) +"propriétés mécaniques et thermiques.");
		items.add(generateItem(Material.RED_DYE,
				ChatColor.of(new Color(172, 188, 122)) + "Fibre végétale",
				"fibre",1,fibre_lore,"CRAFT",null));
		
		List<String> bois_lore = new ArrayList<String>();
		bois_lore.add(ChatColor.of(new Color(157, 86, 67)) +"Matériau d'origine végétale résistant");
		bois_lore.add(ChatColor.of(new Color(157, 86, 67)) +"utile dans beaucoup de cas dont la");
		bois_lore.add(ChatColor.of(new Color(157, 86, 67)) +"construction.");
		items.add(generateItem(Material.OAK_LOG,
				ChatColor.of(new Color(187, 131, 80)) + "Bois",
				"bois",0,bois_lore,"FUEL",300));
		
		List<String> stone_lore = new ArrayList<String>();
		stone_lore.add(ChatColor.of(new Color(120, 120, 120)) +"Matériau naturel solide formé par");
		stone_lore.add(ChatColor.of(new Color(120, 120, 120)) +"un assemblage de minéraux");
		items.add(generateItem(Material.COBBLESTONE,
				ChatColor.of(new Color(142, 142, 142)) + "Roche",
				"stone",0,stone_lore,"CRAFT",null));
		
	}
	
	public static ItemStack getItemById(String id) {
		
		for (ItemStack item : items) {
			if(item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(OdysseyPl.getOdysseyPlugin(), "id"), PersistentDataType.STRING) && 
					item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(OdysseyPl.getOdysseyPlugin(), "id"), PersistentDataType.STRING).equalsIgnoreCase(id))
				return item;
		}
		
		return null;
	}
	
	public static ItemStack generateItem(Material mat, String name, String id, int custommodeldata, List<String> lore, String type, Object type_data) {
		
		ItemStack item = new ItemStack(mat);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setCustomModelData(custommodeldata);
		itemmeta.setDisplayName(name);
		lore.add("§f§lType§r§f: " + type);
		itemmeta.setLore(lore);
		
		itemmeta.getPersistentDataContainer().set(new NamespacedKey(OdysseyPl.getOdysseyPlugin(), "id"), PersistentDataType.STRING, id);
		itemmeta.getPersistentDataContainer().set(new NamespacedKey(OdysseyPl.getOdysseyPlugin(), "type"), PersistentDataType.STRING, type);
		if(type_data instanceof Integer) {
			itemmeta.getPersistentDataContainer().set(new NamespacedKey(OdysseyPl.getOdysseyPlugin(), "type_data"), PersistentDataType.INTEGER, (Integer)type_data);
		}else if(type_data instanceof Double) {
			itemmeta.getPersistentDataContainer().set(new NamespacedKey(OdysseyPl.getOdysseyPlugin(), "type_data"), PersistentDataType.DOUBLE, (Double)type_data);
		}else if(type_data instanceof String) {
			itemmeta.getPersistentDataContainer().set(new NamespacedKey(OdysseyPl.getOdysseyPlugin(), "type_data"), PersistentDataType.STRING, (String)type_data);
		}
		
		
		item.setItemMeta(itemmeta);
		
		return item;
	}

}
