package fr.eazyender.odyssey.utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class ItemTools {
	
	private static ItemStack HAMMER;
	
	public static ItemStack getHammer() {
		
		if(HAMMER != null) {
			return HAMMER;
		}
		
		ItemStack hammer = new ItemStack(Material.STICK, 1); 
		
		ItemMeta meta = hammer.getItemMeta();
		meta.setCustomModelData(299792458);
		meta.setDisplayName(ChatColor.of(new Color(104, 89, 60)) + "Maillet de construction");
		
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.of(new Color(104, 94, 73)) + "Outil permettant la construction de bâtiments.");
		meta.setLore(lore);
		
		hammer.setItemMeta(meta);
		HAMMER = hammer;
		
		return hammer;
		
	}

}
