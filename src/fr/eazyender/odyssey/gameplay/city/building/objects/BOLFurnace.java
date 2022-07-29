package fr.eazyender.odyssey.gameplay.city.building.objects;

import java.awt.Color;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Furnace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.gameplay.city.building.IDynamicBuild;
import fr.eazyender.odyssey.gameplay.city.building.objects.utils.IRecipeFurnace;
import fr.eazyender.odyssey.gameplay.city.building.objects.utils.RecipeUtils;
import fr.eazyender.odyssey.utils.CharRepo;
import fr.eazyender.odyssey.utils.NBTEditor;
import net.md_5.bungee.api.ChatColor;

public class BOLFurnace extends IBuildObjectLoop implements Listener{

	private ItemStack fuel, ore1, ore2, result1, result2;
	private String name;
	private int level;
	private BOLFurnaceHolder holder;
	private Inventory inv;
	private int worktime;
	
	public BOLFurnace() {}
	
	public BOLFurnace(IDynamicBuild build_owner, UUID owner, Vector pos) {
		super(build_owner, owner, pos);
		this.fuel = null;
		this.ore1 = null;
		this.ore2 = null;
		this.result1 = null;
		this.result2 = null;
		this.level = 1;
		this.holder = new BOLFurnaceHolder(this);
		this.inv = Bukkit.createInventory(holder, 9*6 , "§r§f" + CharRepo.BUILD_FURNACE_54);
		this.worktime = 0;
	}
	
	@Override
	public void render() {
		
		ArmorStand as = null;
		
		if(!this.build_owner.getWorld().getNearbyEntities(new Location(this.build_owner.getWorld(), this.pos.getX(), this.pos.getY(), this.pos.getZ()), 1, 1, 1).isEmpty()) {
			for (Entity entity : this.build_owner.getWorld().getNearbyEntities(new Location(this.build_owner.getWorld(), this.pos.getX(), this.pos.getY(), this.pos.getZ()), 1, 1, 1)) {
				if(entity instanceof ArmorStand) {
					ArmorStand astand = (ArmorStand) entity;
					if(astand.getPersistentDataContainer().get(new NamespacedKey(OdysseyPl.getOdysseyPlugin(), "furnace"), PersistentDataType.INTEGER) != null) {
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
			as.setCustomName("?" + " §l" + ChatColor.of(new Color(124, 117, 82)) + name);
			as.setCustomNameVisible(true);
			as.setVisible(false);
			
			as.getPersistentDataContainer().set(new NamespacedKey(OdysseyPl.getOdysseyPlugin(), "furnace"), PersistentDataType.INTEGER, 1);
		}
	}
	
	@Override
	public void trigger(Player player) {
		createGui(player);
	}
	
	@Override
	 public void load(Object obj) {
		 if(obj instanceof Furnace) {
			 Furnace furnace = (Furnace)obj;
			 level = furnace.getInventory().getFuel() != null ? furnace.getInventory().getFuel().getAmount() : 1; 
			 name = "Four T" + level;
		
		 
		 } 
	 }
	
	public static BOLFurnace fromString(IDynamicBuild build, String str) {
		 
		BOLFurnace object = null;
		 
		 String[] str_a = str.split("!!obj;");
		 String name = str_a[0];
		 UUID owner = UUID.fromString(str_a[1]);
		 Vector pos = new Vector(Double.parseDouble(str_a[2]),
					Double.parseDouble(str_a[3]),
					Double.parseDouble(str_a[4]));
		 
		 int level = Integer.parseInt(str_a[5]);
		 ItemStack fuel = null
		 , ore1 = null
		 , ore2 = null
		 , result1 = null
		 , result2 = null
		 ;
		 
		 
		 if(str_a.length > 6) {
		 String[] str_ab = str_a[6].split("!!obji;");
		 
		 fuel = (str_ab.length >= 1 && NBTEditor.NBTCompound.fromJson(str_ab[0]) != null) ? 
				 NBTEditor.getItemFromTag(NBTEditor.NBTCompound.fromJson(str_ab[0]))
				 : null;
		 ore1 = (str_ab.length >= 2 && NBTEditor.NBTCompound.fromJson(str_ab[1]) != null) ? 
				 NBTEditor.getItemFromTag(NBTEditor.NBTCompound.fromJson(str_ab[1]))
				 : null;
		 ore2 = (str_ab.length >= 3 && NBTEditor.NBTCompound.fromJson(str_ab[2]) != null) ? 
				 NBTEditor.getItemFromTag(NBTEditor.NBTCompound.fromJson(str_ab[2]))
				 : null;
		 result1 = (str_ab.length >= 4 && NBTEditor.NBTCompound.fromJson(str_ab[3]) != null) ? 
				 NBTEditor.getItemFromTag(NBTEditor.NBTCompound.fromJson(str_ab[3]))
				 : null;
		 result2 = (str_ab.length >= 5 && NBTEditor.NBTCompound.fromJson(str_ab[4]) != null) ? 
				 NBTEditor.getItemFromTag(NBTEditor.NBTCompound.fromJson(str_ab[4]))
				 : null;
		 }
		 
		 object = new BOLFurnace(build,owner,pos);
		 object.name = name;
		 object.level = level;
		 object.fuel = fuel; object.ore1 = ore1; object.ore2 = ore2; object.result1 = result1; object.result2 = result2;
		 
		 return object;
	}
	
	 @Override
	 public String toString() {
		 
		 String str = "FURNACE!!objtype;";
		
		 str += name + "!!obj;";
		 str += owner + "!!obj;";
		 str += pos.getX() + "!!obj;" + pos.getY() + "!!obj;" + pos.getZ() + "!!obj;";
		 
		 str += level + "!!obj;";
		 str += fuel != null ? NBTEditor.getItemNBTTag(fuel).toJson() : "air"; str += "!!obji;";
		 str += ore1 != null ? NBTEditor.getItemNBTTag(fuel).toJson() : "air"; str += "!!obji;";
		 str += ore2 != null ? NBTEditor.getItemNBTTag(fuel).toJson() : "air"; str += "!!obji;";
		 str += result1 != null ? NBTEditor.getItemNBTTag(fuel).toJson() : "air"; str += "!!obji;";
		 str += result2 != null ? NBTEditor.getItemNBTTag(fuel).toJson() : "air"; 
		 
		 
		 return str;
	}
	 
	 @SuppressWarnings("unused")
	 @Override
	 public void loop() {
		 
		 ItemStack fuel = inv.getItem(19);
		 ItemStack ore1 = inv.getItem(12);
		 ItemStack ore2 = inv.getItem(30);
		 ItemStack result1 = inv.getItem(24);
		 ItemStack result2 = inv.getItem(42);
		 
		 IRecipeFurnace recipe = RecipeUtils.getFurnaceRecipe(ore1, ore2);
		 if(recipe == null) {worktime = 0; return;}
		 if(!(result1 != null && result1.hasItemMeta() && result1.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(OdysseyPl.getOdysseyPlugin(), "id"), PersistentDataType.STRING)
			 && result1.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(OdysseyPl.getOdysseyPlugin(), "id"), PersistentDataType.STRING).equalsIgnoreCase(recipe.getResult().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(OdysseyPl.getOdysseyPlugin(), "id"), PersistentDataType.STRING)))) return;
		 if(result1 != null && result1.getAmount() >= result1.getMaxStackSize()) return;
		 if(result2 != null && result2.getAmount() >= result2.getMaxStackSize()) return;
		 
		 boolean haveFuel = false;
		 int heat = 0;
		 if(fuel != null && fuel.hasItemMeta()) {
			 if(fuel.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(OdysseyPl.getOdysseyPlugin(), "type"), PersistentDataType.STRING)
				 && fuel.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(OdysseyPl.getOdysseyPlugin(), "type"), PersistentDataType.STRING).equalsIgnoreCase("FUEL"))
			 haveFuel = true;
			 if(fuel.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(OdysseyPl.getOdysseyPlugin(), "type_data"), PersistentDataType.INTEGER))
				 heat = fuel.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(OdysseyPl.getOdysseyPlugin(), "type_data"), PersistentDataType.INTEGER);
		 }
		 if(!haveFuel) {decreaseWorkTime();return;}
		 if(heat < recipe.getHeat()) {decreaseWorkTime(); return;}
		 
		 if(worktime % 10 == 0 && worktime != 0) fuel.setAmount(fuel.getAmount()-1);
		 
		 if(addWorkTime(recipe)) {
			 
			 if(ore1 != null)ore1.setAmount(ore1.getAmount()-1);
			 if(ore2 != null)ore2.setAmount(ore2.getAmount()-1);
			 
			 if(result1!=null)result1.setAmount(result1.getAmount()+1); else inv.setItem(24,recipe.getResult());
			 //if(result2!=null)result2.setAmount(result2.getAmount()+1);
			 
			 if(worktime % 10 == 0 && worktime != 0) fuel.setAmount(fuel.getAmount()-1);
			 worktime = 0;
		 }
		 
		 
		 
		 
		 
	 }
	 
	 private boolean addWorkTime(IRecipeFurnace recipe) {
		 if(worktime < recipe.getWork_time()) {
			 worktime++;
			 return false;
		 }
		 return true;
	 }
	 
	 private void decreaseWorkTime() {
		 if(worktime > 0)worktime--;
	 }
	 
	 public void createGui(Player player) {
		 
			player.playSound(player.getLocation(), Sound.BLOCK_LAVA_POP, 1, 1);
			player.openInventory(inv);
		}
	 
	 @EventHandler
	 public static void onClick(InventoryClickEvent event) {
		 Inventory inv = event.getInventory();
		 
		 if(inv.getHolder() instanceof BOLFurnaceHolder) {
	    	   BOLFurnaceHolder holder = (BOLFurnaceHolder) inv.getHolder();
	    	   
	    	   if(event.isShiftClick())event.setCancelled(true);
	    	   
	    	   if(!(event.getClickedInventory() instanceof PlayerInventory) && !Arrays.stream(holder.slots_perm).anyMatch(i -> i == event.getSlot())) {
	    		   event.setCancelled(true); 
	    		   
	    	   }
	    	   else if(event.getSlot() == 24 || event.getSlot() == 42) {
	    		   if (!(event.getCursor() == null && event.getCurrentItem() != null))
	    	        {
	    	            event.setCancelled(true);
	    	        } 
	    	   }
		 }
	 }
	
	
}

class BOLFurnaceHolder implements InventoryHolder{
	
	private BOLFurnace object;
	protected final int[] slots_perm = {
			   12,19,24,30,42};
	
	public BOLFurnaceHolder(BOLFurnace object) {
		this.object = object;
	}
	
	@Override
	public Inventory getInventory() {
		// TODO Auto-generated method stub
		return null;
	}

	public BOLFurnace getObject() {
		return object;
	}

	public void setObject(BOLFurnace object) {
		this.object = object;
	}
	
}
