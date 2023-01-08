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
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.gameplay.city.building.IBuildObject;
import fr.eazyender.odyssey.gameplay.city.building.IDynamicBuild;
import fr.eazyender.odyssey.utils.CharRepo;
import fr.eazyender.odyssey.utils.NBTEditor;
import fr.eazyender.odyssey.utils.TextUtils;
import net.md_5.bungee.api.ChatColor;

public class BOContainer extends IBuildObject implements Listener{

	private List<ItemStack> container;
	private int ligne;
	private String name;
	private boolean isOpen = false;
	
	public BOContainer() {}
	
	public BOContainer(IDynamicBuild build_owner, UUID owner, Vector pos) {
		super(build_owner, owner, pos);
		this.container =  new ArrayList<ItemStack>();
		this.ligne = 1;
		
		
	}
	
	@Override
	public Material getIcon() {
		return Material.CHEST;
	}
	
	@Override
	public String getName() {
		return name;
	}

	
	@Override
	public void render() {
		
		ArmorStand as = null;
		
		if(!this.build_owner.getWorld().getNearbyEntities(new Location(this.build_owner.getWorld(), this.pos.getX(), this.pos.getY(), this.pos.getZ()), 1, 1, 1).isEmpty()) {
			for (Entity entity : this.build_owner.getWorld().getNearbyEntities(new Location(this.build_owner.getWorld(), this.pos.getX(), this.pos.getY(), this.pos.getZ()), 1, 1, 1)) {
				if(entity instanceof ArmorStand) {
					ArmorStand astand = (ArmorStand) entity;
					if(astand.getPersistentDataContainer().get(new NamespacedKey(OdysseyPl.getOdysseyPlugin(), "storage"), PersistentDataType.INTEGER) != null) {
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
			as.setCustomName("\uEfc8" + " §l" + ChatColor.of(new Color(124, 117, 82)) + name);
			as.setCustomNameVisible(true);
			as.setVisible(false);
			
			as.getPersistentDataContainer().set(new NamespacedKey(OdysseyPl.getOdysseyPlugin(), "storage"), PersistentDataType.INTEGER, 1);
		}
	}
	
	@Override
	public void trigger(Player player) {
		if(!isOpen) {
			createGui(player);
		}else {
			player.sendMessage(TextUtils.aide + "Le stockage est déjà utilisé par quelqu'un");
		}
	}
	
	 @Override
	 public void load(Object obj) {
		 
		 if(obj instanceof Chest) {
			 
			 Chest chest = (Chest)obj;
			 Inventory inv = chest.getBlockInventory();
			 
			 int count = 0;
			 ItemStack[] content = inv.getContents();
			 
			 for (int i = 0; i < content.length; i++) {
				ItemStack item = content[i];
				if(item != null && item.getType().equals(Material.CHEST)){
					count++;
				}
			}
			 
			 
			ligne = count;
			if(count == 0) ligne = 1;
			
			int[] slots_perm = {
					   2,3,4,5,6,7,
					   11,12,13,14,15,16,
					   20,21,22,23,24,25,
					   29,30,31,32,33,34,
					   38,39,40,41,42,43};
			while(container.size() < ligne * slots_perm.length) {
				container.add(null);
			}
			 
			name = chest.getCustomName();
			 
		 }
		 
	 }
	 
	 public static BOContainer fromString(IDynamicBuild build, String str) {
		 
		 BOContainer object = null;
		 
		 String[] str_a = str.split("!!obj;");
		 String name = str_a[0];
		 UUID owner = UUID.fromString(str_a[1]);
		 Vector pos = new Vector(Double.parseDouble(str_a[2]),
					Double.parseDouble(str_a[3]),
					Double.parseDouble(str_a[4]));
		 
		 int ligne = Integer.parseInt(str_a[5]);
		 List<ItemStack> items = new ArrayList<ItemStack>();
		 
		 
		 if(str_a.length > 6) {
		 String[] str_ab = str_a[6].split("!!obji;");
		 if(str_ab.length == 0) str_ab = str_a[6].split("!!obj_interne;");
		 for (int i = 0; i < str_ab.length; i++) {
			String str_b = str_ab[i];
			if(NBTEditor.NBTCompound.fromJson(str_b) != null) {
			items.add(NBTEditor.getItemFromTag(NBTEditor.NBTCompound.fromJson(str_b)));}
			else
			{items.add(null);}
		}
		
		  int[] slots_perm = {
				   2,3,4,5,6,7,
				   11,12,13,14,15,16,
				   20,21,22,23,24,25,
				   29,30,31,32,33,34,
				   38,39,40,41,42,43};
		while(items.size() < ligne*slots_perm.length-1) {
			items.add(null);
		}
		 
		}
		 
		 object = new BOContainer(build,owner,pos);
		 object.name = name;
		 object.ligne = ligne;
		 object.container = items;
		 
		 return object;
		}
	
	 @Override
	 public String toString() {
		 
		 String str = "CONTAINER!!objtype;";
		
		 str += name + "!!obj;";
		 str += owner + "!!obj;";
		 str += pos.getX() + "!!obj;" + pos.getY() + "!!obj;" + pos.getZ() + "!!obj;";
		 
		 str += ligne + "!!obj;";
		for (int i = 0; i < container.size(); i++) {
			ItemStack item = container.get(i);
			if(item != null) { if(i != 0)str += "!!obji;";
			str += NBTEditor.getItemNBTTag(item).toJson();
			}else {
				str += "!!obji;";
				str += "air";
			}
		}
		 
		 
		 return str;
		}
	 
	 public void createGui(Player player) {
		 isOpen = true;
		 BOContainerHolder holder = new BOContainerHolder(this, 0);
		 Inventory inv = Bukkit.createInventory(holder, 9*6 , "§r§f" + CharRepo.BUILD_STOCKAGE_54 + "Page 1/"+ligne);
			
		 List<ItemStack> cut_items = container.subList(0, holder.slots_perm.length);
			 
			 for (int i = 0; i < holder.slots_perm.length; i++) {
					
					 int slot = holder.slots_perm[i];
					 if(i < cut_items.size() && cut_items.get(i) != null) {
						 inv.setItem(slot, cut_items.get(i));
					 } 
				 }
	
			player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
			player.openInventory(inv);
		}
	 
	 public static void changePage(Player player, BOContainerHolder holder,  int page) {
		 
		 holder.getObject().isOpen = true;
		 Inventory inv = Bukkit.createInventory(holder, 9*6 , "§r§f" + CharRepo.BUILD_STOCKAGE_54 +  "Page " + (page+1) + "/" + holder.getObject().ligne);
		 List<ItemStack> cut_items = holder.getObject().container.subList(page * holder.slots_perm.length, holder.slots_perm.length * (page+1));
		 
		 for (int i = 0; i < holder.slots_perm.length; i++) {
			
			 int slot = holder.slots_perm[i];
			 if(i < cut_items.size() && cut_items.get(i) != null) {
				 inv.setItem(slot, cut_items.get(i));
			 } 
		 }
		 
		 player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
		 player.openInventory(inv);
		 holder.setPage(page);
		 
	 }
	 
	 @EventHandler
	 public static void onClick(InventoryClickEvent event){
		    Inventory inv = event.getInventory();
		    if (inv.getHolder() instanceof BOContainerHolder) {
		      BOContainerHolder holder = (BOContainerHolder)inv.getHolder();
		      if (event.isShiftClick())
		        event.setCancelled(true); 
		      if (!(event.getClickedInventory() instanceof org.bukkit.inventory.PlayerInventory) && !Arrays.stream(holder.slots_perm).anyMatch(i -> (i == event.getSlot()))) {
		        event.setCancelled(true);
		        if (event.getSlot() == 0) {
		          if (holder.getPage() > 0)
		            changePage((Player)event.getWhoClicked(), holder, holder.getPage() - 1); 
		        } else if (event.getSlot() == 36 && 
		          holder.getPage() < (holder.getObject()).ligne - 1) {
		          changePage((Player)event.getWhoClicked(), holder, holder.getPage() + 1);
		        } 
		      } 
		    } 
		  }
	 
	 @EventHandler
	  public static void onDragClick(InventoryDragEvent event) {
	    Inventory inv = event.getInventory();
	    if (inv.getHolder() instanceof BOContainerHolder) {
	      BOContainerHolder holder = (BOContainerHolder)inv.getHolder();
	      boolean flag = false;
	      for (Iterator<Integer> iterator = event.getInventorySlots().iterator(); iterator.hasNext(); ) {
	        int s = ((Integer)iterator.next()).intValue();
	        boolean holder_contain = false;
	        byte b;
	        int i, arrayOfInt[];
	        for (i = (arrayOfInt = holder.slots_perm).length, b = 0; b < i; ) {
	          int s2 = arrayOfInt[b];
	          if (s2 == s) {
	            holder_contain = true;
	            break;
	          } 
	          b++;
	        } 
	        if (!holder_contain)
	          flag = true; 
	      } 
	      if (!(inv instanceof org.bukkit.inventory.PlayerInventory) && flag)
	        event.setCancelled(true); 
	    } 
	  }
		
	@EventHandler
	 public static void leaveGui(InventoryCloseEvent event) {
	       Inventory inv = event.getInventory();
	       
	       if(inv.getHolder() instanceof BOContainerHolder) {
	    	   BOContainerHolder holder = (BOContainerHolder) inv.getHolder();
	    	   holder.getObject().isOpen = false;
	    	   
	    	   for (int i = 0; i < holder.slots_perm.length; i++) {
	   			
	  			 int slot = holder.slots_perm[i];
	  			 holder.getObject().container.set(i + holder.getPage() * holder.slots_perm.length, inv.getItem(slot));
	  			 
	  		 }
	       }
	       
	       
	 }
	 
	 

}

class BOContainerHolder implements InventoryHolder{

	private BOContainer object;
	private int page;
	protected final int[] slots_perm = {
			   2,3,4,5,6,7,
			   11,12,13,14,15,16,
			   20,21,22,23,24,25,
			   29,30,31,32,33,34,
			   38,39,40,41,42,43};
	
	public BOContainerHolder(BOContainer object, int page) {
		this.object = object;
		this.page = page;
	}
	
	@Override
	public Inventory getInventory() {
		// TODO Auto-generated method stub
		return null;
	}

	public BOContainer getObject() {
		return object;
	}

	public void setObject(BOContainer object) {
		this.object = object;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
	
	
	
}
