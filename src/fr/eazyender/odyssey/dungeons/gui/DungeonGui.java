package fr.eazyender.odyssey.dungeons.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.eazyender.odyssey.gameplay.items.ItemUtils;
import fr.eazyender.odyssey.player.group.PlayerGroup;
import net.md_5.bungee.api.ChatColor;

public class DungeonGui {
	
	public static void openGui(Player p, int floor) {
		Inventory inv = Bukkit.createInventory(new DungeonGuiHolder(), 36);
		
		
		
		p.openInventory(inv);
	}
	
	public static Material WHITE_FILLER = Material.WHITE_STAINED_GLASS_PANE;
	
	public static void openDungeon(Player p, String dungeon) {
		PlayerGroup group = PlayerGroup.getGroup(p);
		if (group != null) {
			if (group.getHost() != p) {
				p.sendMessage(ChatColor.of("#ff0000") + "Seul le chef du groupe peut lancer un donjon !");
				p.closeInventory();
				return;
			} else if (group.getPlayers().size() > 4) {
				p.sendMessage(ChatColor.of("#ff0000") + "Votre groupe doit être constitué de maximum 4 joueurs pour lancer un donjon !");
				p.closeInventory();
				return;
			}
		} else {
			group = new PlayerGroup(p);
		}
		
		Inventory inv = Bukkit.createInventory(new DungeonGuiHolder(), 27);
		for(int i = 0; i < 8; i++) inv.setItem(i, ItemUtils.getItem(new ItemStack(WHITE_FILLER), "", null, 0));
		inv.setItem(9, ItemUtils.getItem(new ItemStack(WHITE_FILLER), "", null, 0));
		inv.setItem(17, ItemUtils.getItem(new ItemStack(WHITE_FILLER), "", null, 0));
		for(int i = 18; i < 26; i++) inv.setItem(i, ItemUtils.getItem(new ItemStack(WHITE_FILLER), "", null, 0));
		
		
		
		p.openInventory(inv);
	}
	
	
	
	
}
