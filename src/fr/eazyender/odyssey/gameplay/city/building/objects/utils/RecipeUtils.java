package fr.eazyender.odyssey.gameplay.city.building.objects.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import fr.eazyender.odyssey.OdysseyPl;

public class RecipeUtils {
	
	private static List<IRecipeFurnace> furnace_recipes = new ArrayList<IRecipeFurnace>();
	
	public static void initRecipes() {
		
		//Furnace recipe
		
	}
	
	public static IRecipeFurnace getFurnaceRecipe(ItemStack ore1, ItemStack ore2) {
		NamespacedKey key = new NamespacedKey(OdysseyPl.getOdysseyPlugin(), "id");
		for (IRecipeFurnace rec : furnace_recipes) {
			if(ore1 != null && ore2 != null && ore1.hasItemMeta() && ore1.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING) && ore2.hasItemMeta() && ore2.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
			if((rec.getOre1().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING).equalsIgnoreCase(ore1.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING))
					&& rec.getOre2().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING).equalsIgnoreCase(ore2.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING))) ||
					(rec.getOre1().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING).equalsIgnoreCase(ore2.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING))
							&& rec.getOre2().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING).equalsIgnoreCase(ore1.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING)))){
				return rec;
			}
			}else if(ore1 != null && ore2 == null && ore1.hasItemMeta() && ore1.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
				if(rec.getOre1().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING).equalsIgnoreCase(ore1.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING)))return rec;
				if(rec.getOre2().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING).equalsIgnoreCase(ore1.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING)))return rec;
			}else if(ore1 == null && ore2 != null && ore2.hasItemMeta() && ore2.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
				if(rec.getOre1().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING).equalsIgnoreCase(ore2.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING)))return rec;
				if(rec.getOre2().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING).equalsIgnoreCase(ore2.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING)))return rec;
			}
		}
		
		return null;		
	}

}
