package fr.eazyender.odyssey.dungeons.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import fr.eazyender.odyssey.dungeons.Dungeon;
import fr.eazyender.odyssey.dungeons.DungeonConfig;
import fr.eazyender.odyssey.gameplay.items.ItemUtils;
import fr.eazyender.odyssey.player.group.PlayerGroup;
import net.md_5.bungee.api.ChatColor;

public class DungeonGui {
	
	public static void openGui(Player p, int floor) {
		Inventory inv = Bukkit.createInventory(new DungeonGuiHolder(floor, DungeonGuiState.MAINPAGE), 36);
		
		inv.setItem(0, new ItemStack(Material.DIRT));
		
		p.openInventory(inv);
	}
	
	public static Material WHITE_FILLER = Material.WHITE_STAINED_GLASS_PANE;
	public static Material CANCEL = Material.RED_WOOL;
	public static Material START = Material.LIME_WOOL;
	
	
	public static void openDungeon(Player p, String dungeon, DungeonGuiHolder holder) {
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
		
		Dungeon d = DungeonConfig.getDungeon(dungeon);
		Inventory inv = Bukkit.createInventory(new DungeonGuiHolder(holder.getPage(), DungeonGuiState.STARTING).setDungeon(DungeonConfig.getDungeon(dungeon)), 27, ChatColor.of(d.getName().split("_")[0]) + d.getName().split("_")[1]);
		for(int i = 0; i < 9; i++) inv.setItem(i, ItemUtils.getItem(new ItemStack(WHITE_FILLER), "", null, 0));
		inv.setItem(9, ItemUtils.getItem(new ItemStack(WHITE_FILLER), "", null, 0));
		inv.setItem(14, ItemUtils.getItem(new ItemStack(WHITE_FILLER), "", null, 0));
		inv.setItem(15, ItemUtils.getItem(new ItemStack(WHITE_FILLER), "", null, 0));
		inv.setItem(17, ItemUtils.getItem(new ItemStack(WHITE_FILLER), "", null, 0));
		for(int i = 18; i < 27; i++) inv.setItem(i, ItemUtils.getItem(new ItemStack(WHITE_FILLER), "", null, 0));
		
		int i = 10;
		for(Player player : group.getPlayers()) {
			inv.setItem(i, ItemUtils.getItem(getHead(player), player.getName(), null, 0));
			i++;
		}
		
		inv.setItem(15, ItemUtils.getItem(new ItemStack(CANCEL), ChatColor.of("#ab0909") + "Retour", null, 0));
		inv.setItem(16, ItemUtils.getItem(new ItemStack(START), ChatColor.of("#03fc0b") + "Lancer le donjon", null, 0));
		
		p.openInventory(inv);
	}
	
	public static ItemStack getHead(OfflinePlayer p) {
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta skullMeta = (SkullMeta) skull.getItemMeta(); 
		skullMeta.setOwningPlayer(p);
		skull.setItemMeta(skullMeta); 
		return skull;
	}
	
	
	
	
}
