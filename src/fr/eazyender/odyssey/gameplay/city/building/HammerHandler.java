package fr.eazyender.odyssey.gameplay.city.building;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import fr.eazyender.odyssey.utils.CharRepo;
import fr.eazyender.odyssey.utils.ItemTools;
import fr.eazyender.odyssey.utils.TextUtils;

public class HammerHandler implements Listener{
	
	public static Map<UUID, IBuildingSettings> settings = new HashMap<UUID, IBuildingSettings>();
	public static Map<UUID, IBuildUnderConstruction> in_works = new HashMap<UUID, IBuildUnderConstruction>();
	
	
	@EventHandler
	public void onHammerUsed(PlayerInteractEvent event) {
		
		Player player = event.getPlayer();
		
		if(player.getInventory().getItemInMainHand().isSimilar(ItemTools.getHammer())){
			
			//Right click = set Position
			if(!player.isSneaking() && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) {
				if(!settings.containsKey(player.getUniqueId()))settings.put(player.getUniqueId(), new IBuildingSettings(player.getUniqueId(), player.getLocation().toVector().clone(), 0, null));
				
				settings.get(player.getUniqueId()).refreshSettings(settings.get(player.getUniqueId()).getRotationY(), BuildManager.builds.get(0), true);
			}
			//Left click = Rotate
			else if(!player.isSneaking() && (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR)) {
				if(settings.containsKey(player.getUniqueId())) {
					settings.get(player.getUniqueId()).refreshSettings((settings.get(player.getUniqueId()).getRotationY() + 90) % 360, null, false);
				}else {
					player.sendMessage(TextUtils.aide + "Vous ne pouvez pas faire la rotation d'un bâtiment (Aucun bâtiment en cours de placement).");
				}
			}
			
			//RIGHT CLICK = Place
			else if(player.isSneaking() && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) {
				if(settings.containsKey(player.getUniqueId())) {
					settings.get(player.getUniqueId()).beginConstruction();
					settings.get(player.getUniqueId()).task.cancel();
					settings.remove(player.getUniqueId());
				}else {
					player.sendMessage(TextUtils.aide + "Vous ne pouvez pas placer un bâtiment (Aucun bâtiment en cours de placement).");
				}
			}
			//LEFT CLICK = Menu
			else if(player.isSneaking() && (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR)) {
				if(settings.containsKey(player.getUniqueId()))openMenu(player);
				else player.sendMessage(TextUtils.aide + "Vous devez d'abord mettre au moins une position avec " + TextUtils.mouse_rc);
			}
			
			event.setCancelled(true);
			
		}
		
	}
	
	private static void openMenu(Player player) {
		Inventory inv = Bukkit.createInventory(new GUIHolderMenu(), 9*6 , "§r§f" + CharRepo.BUILD_MENU);
		player.playSound(player.getLocation(), Sound.BLOCK_LAVA_POP, 1, 1);
		player.openInventory(inv);
	}
	
	private static void openSubMenu(Player player, String type) {
		
		Inventory inv;
		GUIHolderSubMenu holder = new GUIHolderSubMenu(type);
		
		switch(type) {
		case "MILITAIRE": inv = Bukkit.createInventory(holder, 9*6 , "§r§f" + CharRepo.BUILD_MENU_MILS); break;
		case "METIERS": inv = Bukkit.createInventory(holder, 9*6 , "§r§f" + CharRepo.BUILD_MENU_METS); break;
		case "HABITATION": inv = Bukkit.createInventory(holder, 9*6 , "§r§f" + CharRepo.BUILD_MENU_HABS); break;
		case "INSTITUTION": inv = Bukkit.createInventory(holder, 9*6 , "§r§f" + CharRepo.BUILD_MENU_INSTS); break;
		case "DIVERS": inv = Bukkit.createInventory(holder, 9*6 , "§r§f" + CharRepo.BUILD_MENU_DIVS); break;
		default: inv = Bukkit.createInventory(holder, 9*6 , "§r§f" + CharRepo.BUILD_MENU_MILS); break;}
		
		int index = 0;
		for (IBuild build : BuildManager.builds) {
			if(index < holder.slots_perm.length)
			if(build.getType().equalsIgnoreCase(type)) {
				inv.setItem(holder.slots_perm[index], getBuildLogo(build));
				index++;
			}
		}
		
		player.openInventory(inv);
	}
	
	private static ItemStack getBuildLogo(IBuild build) {
		ItemStack item = new ItemStack(Material.PAPER);
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(build.getName());
		List<String> lore = new ArrayList<String>();
		
		lore.add("§f§lType§r§f: " + build.getType());
		lore.add("§f§lTaille§r§f: " + build.getSize());
		lore.add("§f§lTemps de build§r§f: " + build.getWork_time().get(0));
		
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	@EventHandler
	 public static void onClick(InventoryClickEvent event) {
		 Inventory inv = event.getInventory();
		 
		 if(inv.getHolder() instanceof GUIHolderMenu) {
			 GUIHolderMenu holder = (GUIHolderMenu) inv.getHolder();
	    	 Player player = (Player)event.getWhoClicked();
			 
			 if(!(event.getClickedInventory() instanceof PlayerInventory)) {
				 if(Arrays.stream(holder.slots_top).anyMatch(i -> i == event.getSlot())) {openSubMenu(player,"MILITAIRE");player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);}
				 else if(Arrays.stream(holder.slots_top).anyMatch(i -> i+3 == event.getSlot())) {openSubMenu(player,"METIERS");player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);}
				 else if(Arrays.stream(holder.slots_top).anyMatch(i -> i+6 == event.getSlot())) {openSubMenu(player,"HABITATION");player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);}
				 else if(Arrays.stream(holder.slots_bot_left).anyMatch(i -> i == event.getSlot())) {openSubMenu(player,"INSTITUTION");player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);}
				 else if(Arrays.stream(holder.slots_bot_right).anyMatch(i -> i == event.getSlot())) {openSubMenu(player,"DIVERS");player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);}
			 }
			 
			 
	    	 event.setCancelled(true);   
		 }else if(inv.getHolder() instanceof GUIHolderSubMenu){
			 GUIHolderSubMenu holder = (GUIHolderSubMenu) inv.getHolder();
			 Player player = (Player)event.getWhoClicked();
			 
			 if(!(event.getClickedInventory() instanceof PlayerInventory)) {
				 if(inv.getItem(event.getSlot()) != null && inv.getItem(event.getSlot()).getType().equals(Material.PAPER)) {
					 ItemStack item = inv.getItem(event.getSlot());
					 
					 if(!settings.containsKey(player.getUniqueId()))settings.put(player.getUniqueId(), new IBuildingSettings(player.getUniqueId(), player.getLocation().toVector().clone(), 0, null));
					 settings.get(player.getUniqueId()).refreshSettings(settings.get(player.getUniqueId()).getRotationY(), BuildManager.getBuildByName(item.getItemMeta().getDisplayName()), false);
					 
					 player.closeInventory();
					 player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
					 player.sendMessage(TextUtils.aide + "Vous avez choisi le bâtiment: " + item.getItemMeta().getDisplayName());
				 }else if(event.getSlot() == 18){
					 openMenu(player);
					 player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
				 }else if(event.getSlot() == 0){
					 String[] types = {"MILITAIRE","METIERS","HABITATION","INSTITUTION","DIVERS"};
					 int index = 0;
					 for (int j = 0; j < types.length; j++) {
						String string = types[j];
						if(holder.getType().equalsIgnoreCase(string)) index = j;
					 }
					 
					 if(index == 0)index = types.length;
					 
					 openSubMenu(player, types[index-1]);
					 player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
				 }else if(event.getSlot() == 36){
					 String[] types = {"MILITAIRE","METIERS","HABITATION","INSTITUTION","DIVERS"};
					 int index = 0;
					 for (int j = 0; j < types.length; j++) {
						String string = types[j];
						if(holder.getType().equalsIgnoreCase(string)) index = j;
					 }
					 
					 if(index == types.length-1)index = -1;
					 
					 openSubMenu(player, types[index+1]);
					 player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
				 }
			 }
			 
			 event.setCancelled(true);  
		 }
	 }
	

}

class GUIHolderSubMenu implements InventoryHolder{
	
	private String type;
	protected final int[] slots_perm = {
			   2,3,4,5,6,7,
			   11,12,13,14,15,16,
			   20,21,22,23,24,25,
			   29,30,31,32,33,34,
			   38,39,40,41,42,43};
	
	public GUIHolderSubMenu(String type) {this.type = type;}
	
	@Override
	public Inventory getInventory() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getType() {
		return type;
	}
	
	
	
}

class GUIHolderMenu implements InventoryHolder{
	
	protected final int[] slots_top = {
			   0,1,2,9,10,11,18,19,20,27,28,29};
	protected final int[] slots_bot_left = {
			36,37,38,39,40,
			45,46,47,48,49
	};
	protected final int[] slots_bot_right = {
			41,42,43,44,
			50,51,52,53
	};
	
	public GUIHolderMenu() {}
	
	@Override
	public Inventory getInventory() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
