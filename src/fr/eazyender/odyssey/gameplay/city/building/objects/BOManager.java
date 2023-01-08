package fr.eazyender.odyssey.gameplay.city.building.objects;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.gameplay.city.building.IBuildObject;
import fr.eazyender.odyssey.gameplay.city.building.IDynamicBuild;
import fr.eazyender.odyssey.utils.CharRepo;
import fr.eazyender.odyssey.utils.NBTEditor;
import fr.eazyender.odyssey.utils.TextUtils;
import net.md_5.bungee.api.ChatColor;

public class BOManager extends IBuildObject implements Listener{

	private boolean isOpen = false;
	
	private static final ItemStack integrity_item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1),
			decay_item = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE, 1);
	
	
	public BOManager() {}
	
	public BOManager(IDynamicBuild build_owner, UUID owner, Vector pos) {
		super(build_owner, owner, pos);
		
		
	}
	
	@Override
	public void render() {
		
		ArmorStand as = null;
		
		if(!this.build_owner.getWorld().getNearbyEntities(new Location(this.build_owner.getWorld(), this.pos.getX(), this.pos.getY(), this.pos.getZ()), 1, 1, 1).isEmpty()) {
			for (Entity entity : this.build_owner.getWorld().getNearbyEntities(new Location(this.build_owner.getWorld(), this.pos.getX(), this.pos.getY(), this.pos.getZ()), 1, 1, 1)) {
				if(entity instanceof ArmorStand) {
					ArmorStand astand = (ArmorStand) entity;
					if(astand.getPersistentDataContainer().get(new NamespacedKey(OdysseyPl.getOdysseyPlugin(), "manager"), PersistentDataType.INTEGER) != null) {
						as = astand;
					}
				}
			}
		}
		if(as == null) {
			as = (ArmorStand) this.build_owner.getWorld().spawnEntity(
					new Location(this.build_owner.getWorld(), this.pos.getX(), this.pos.getY(), this.pos.getZ())
					, EntityType.ARMOR_STAND);
			as.setGravity(false);
			as.setCanPickupItems(false);
			as.setCustomName("\uEf12");
			as.setCustomNameVisible(true);
			as.setVisible(false);
			
			as.getPersistentDataContainer().set(new NamespacedKey(OdysseyPl.getOdysseyPlugin(), "manager"), PersistentDataType.INTEGER, 1);
		}
	}
	
	@Override
	public void trigger(Player player) {
		if(!isOpen) {
			createGui(player);
		}else {
			player.sendMessage(TextUtils.aide + "L'interface est déjà utilisée par quelqu'un.");
		}
	}
	
	 @Override
	 public void load(Object obj) {
		 
		 
	 }
	 
	 public static BOManager fromString(IDynamicBuild build, String str) {
		 
		 BOManager object = null;
		 
		 String[] str_a = str.split("!!obj;");
		 String name = str_a[0];
		 UUID owner = UUID.fromString(str_a[1]);
		 Vector pos = new Vector(Double.parseDouble(str_a[2]),
					Double.parseDouble(str_a[3]),
					Double.parseDouble(str_a[4]));
		 
		 object = new BOManager(build,owner,pos);
		 object.name = name;

		 return object;
		}
	
	 @Override
	 public String toString() {
		 
		 String str = "MANAGER!!objtype;";
		
		 str += name + "!!obj;";
		 str += owner + "!!obj;";
		 str += pos.getX() + "!!obj;" + pos.getY() + "!!obj;" + pos.getZ();
		 
		 
		 return str;
		}
	 
	 public void createGui(Player player) {
		 isOpen = true;
		 BOManagerHolder holder = new BOManagerHolder(this);
		 Inventory inv = Bukkit.createInventory(holder, 9*6 , "§r§f" + CharRepo.BUILD_MANAGER_54);
			
		 
		 
		 	int decay_time = this.build_owner.getDecay_time();
		 	int max_decay_time = this.build_owner.getBuild().getDecay_time().get(this.build_owner.getDecay_tier());
		 	
		 	int ratio = (int) Math.round((1 - (double)decay_time / (double)max_decay_time) / 2 * 10);
		 	System.out.println(decay_time);
		 	System.out.println(1 - (double)decay_time / (double)max_decay_time);
		 	for (int i = 0; i < ratio; i++) {
				inv.setItem(13+i, decay_item);
			}
		 	
		 	
			player.playSound(player.getLocation(), Sound.BLOCK_LAVA_POP, 1, 1);
			player.openInventory(inv);
		}
	 @EventHandler
	 public static void onClick(InventoryClickEvent event){
		    Inventory inv = event.getInventory();
		    if (inv.getHolder() instanceof BOManagerHolder) {
		    	event.setCancelled(true);	
		    	Player player = (Player)event.getWhoClicked();
		    	BOManagerHolder holder = (BOManagerHolder) inv.getHolder();
		    	
		    	if(Arrays.stream(holder.slots).anyMatch(i -> i+3 == event.getSlot())) {
		    		holder.getObject().openMenuObjects(player);
		    		player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);}
		    }else if(inv.getHolder() instanceof BOObjectsListHolder) {
		    	
		    	event.setCancelled(true);	
		    	Player player = (Player)event.getWhoClicked();
		    	BOObjectsListHolder holder = (BOObjectsListHolder) inv.getHolder();
		    	
		    	
		    	if(event.getSlot() == 49)holder.getObject().createGui(player);
		    	
		    }
		  }
	 
	 @EventHandler
	  public static void onDragClick(InventoryDragEvent event) {
	    Inventory inv = event.getInventory();
	    if (inv.getHolder() instanceof BOManagerHolder) {
	    	event.setCancelled(true);
	    } 
	  }
		
	@EventHandler
	 public static void leaveGui(InventoryCloseEvent event) {
	       Inventory inv = event.getInventory();
	       
	       if(inv.getHolder() instanceof BOManagerHolder) {
	    	   BOManagerHolder holder = (BOManagerHolder) inv.getHolder();
	    	   holder.getObject().isOpen = false;
	       }else if(inv.getHolder() instanceof BOObjectsListHolder) {
	    	   BOObjectsListHolder holder = (BOObjectsListHolder) inv.getHolder();
	    	   holder.getObject().isOpen = false;
	       }
	       
	       
	 }
	
	public void openMenuObjects(Player player) {
		BOObjectsListHolder holder = new BOObjectsListHolder(this, 0);
		 Inventory inv = Bukkit.createInventory(holder, 9*6 , "§r§f" + CharRepo.NEUTRAL_LIST_54);
			
		 	ItemStack logo = new ItemStack(Material.PAPER,1);
	 		ItemMeta logo_meta = logo.getItemMeta();
	 		logo_meta.setDisplayName("§f§lObjets du bâtiments");
	 		logo.setItemMeta(logo_meta);
		 	inv.setItem(4, logo);
		 
		 	for (int i = 0; i < holder.getObject().build_owner.getObjects().size() && i < 9*4-1; i++) {
		 		
		 		
		 		int y = i + 9;
		 		
		 		IBuildObject obj = holder.getObject().build_owner.getObjects().get(i);
		 		if(obj instanceof BOManager)continue;
		 		ItemStack item = new ItemStack(obj.getIcon(),1);
		 		ItemMeta meta = item.getItemMeta();
		 		meta.setDisplayName("§r§f" + obj.getName());
		 		
		 		List<String> lore = new ArrayList<String>();
		 		lore.add("§r§fPosition : " + 
		 		"(X : " +  obj.getPos().getX() +
		 		",Y : " +  obj.getPos().getY() +
		 		",Z : " +  obj.getPos().getZ() + ")");
		 		meta.setLore(lore);
		 		
		 		item.setItemMeta(meta);
		 		
				inv.setItem(y,item);
			}
		 
			
			player.openInventory(inv);
	}
	 
	 

}
class BOObjectsListHolder implements InventoryHolder{

	private BOManager object;
	protected final int[] slots = {
			   27,29,30,36,37,38,45,46,47};
	
	public BOObjectsListHolder(BOManager object, int page) {
		this.object = object;
	}
	
	@Override
	public Inventory getInventory() {
		// TODO Auto-generated method stub
		return null;
	}

	public BOManager getObject() {
		return object;
	}

	public void setObject(BOManager object) {
		this.object = object;
	}
	
	
}

class BOManagerHolder implements InventoryHolder{

	private BOManager object;
	protected final int[] slots = {
			   27,29,30,36,37,38,45,46,47};
	
	public BOManagerHolder(BOManager object) {
		this.object = object;
	}
	
	@Override
	public Inventory getInventory() {
		// TODO Auto-generated method stub
		return null;
	}

	public BOManager getObject() {
		return object;
	}

	public void setObject(BOManager object) {
		this.object = object;
	}
	
	
}
