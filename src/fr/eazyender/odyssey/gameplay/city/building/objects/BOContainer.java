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
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.gameplay.city.building.IBuildObject;
import fr.eazyender.odyssey.gameplay.city.building.IDynamicBuild;
import fr.eazyender.odyssey.utils.NBTEditor;
import net.md_5.bungee.api.ChatColor;

public class BOContainer extends IBuildObject implements Listener{

	private List<ItemStack> container;
	private int ligne;
	private String name;
	
	public BOContainer() {
		
	}
	
	public BOContainer(IDynamicBuild build_owner, UUID owner, Vector pos) {
		super(build_owner, owner, pos);
		this.container =  new ArrayList<ItemStack>();
		this.ligne = 1;
		
		
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
		createGui(player);
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
		 String[] str_ab = str_a[6].split("!!obj_interne;");
		 for (int i = 0; i < str_ab.length; i++) {
			String str_b = str_ab[i];
			items.add(NBTEditor.getItemFromTag(NBTEditor.NBTCompound.fromJson(str_b)));
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
			if(item != null) { if(i != 0)str += "!!obj_interne;";
			str += NBTEditor.getItemNBTTag(item).toJson();
			}
		}
		 
		 
		 return str;
		}
	 
	 public void createGui(Player player) {
			if(ligne > 6)ligne = 6;
			Inventory inv = Bukkit.createInventory(new BOContainerHolder(this, 1), 9*ligne , "\uEfc8" + " §l" + ChatColor.of(new Color(124, 117, 82)) + name);
			
			for (int i = 0; i < container.size(); i++) {
				inv.setItem(i, container.get(i));
			}
	
			player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
			player.openInventory(inv);
		}
		
	@EventHandler
	 public static void leaveGui(InventoryCloseEvent event) {
	       Player player = (Player)event.getPlayer();
	       Inventory inv = event.getInventory();
	       
	       if(inv.getHolder() instanceof BOContainerHolder) {
	    	   BOContainerHolder holder = (BOContainerHolder) inv.getHolder();
	    	   
	    	   holder.getObject().container = Arrays.asList(inv.getContents());
	       }
	       
	       
	 }
	 
	 

}

class BOContainerHolder implements InventoryHolder{

	private BOContainer object;
	private int page;
	
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
