package fr.eazyender.odyssey.gameplay.city.building.objects;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.gameplay.city.building.IBuildObject;
import fr.eazyender.odyssey.gameplay.city.building.IDynamicBuild;
import fr.eazyender.odyssey.utils.CharRepo;

import java.awt.Color;
import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockState;
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
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class BOWorkBench extends IBuildObject implements Listener {
  private String name;
  
  private int level;
  
  public BOWorkBench() {}
  
  public BOWorkBench(IDynamicBuild build_owner, UUID owner, Vector pos) {
    super(build_owner, owner, pos);
  }
  
  public void render() {
    ArmorStand as = null;
    if (!this.build_owner.getWorld().getNearbyEntities(new Location(this.build_owner.getWorld(), this.pos.getX(), this.pos.getY(), this.pos.getZ()), 1.0D, 1.0D, 1.0D).isEmpty())
      for (Entity entity : this.build_owner.getWorld().getNearbyEntities(new Location(this.build_owner.getWorld(), this.pos.getX(), this.pos.getY(), this.pos.getZ()), 1.0D, 1.0D, 1.0D)) {
        ArmorStand astand = (ArmorStand)entity;
        if (entity instanceof ArmorStand && astand.getPersistentDataContainer().get(new NamespacedKey((Plugin)OdysseyPl.getOdysseyPlugin(), "workbench"), PersistentDataType.INTEGER) != null)
          as = astand; 
      }  
    if (as == null) {
      as = (ArmorStand)this.build_owner.getWorld().spawnEntity(
          new Location(this.build_owner.getWorld(), this.pos.getX(), this.pos.getY(), this.pos.getZ()), 
          EntityType.ARMOR_STAND);
      as.setGravity(false);
      as.setCanPickupItems(false);
      as.setCustomName("LOGO"+ ChatColor.of(new Color(124, 117, 82)) + this.name);
      as.setCustomNameVisible(true);
      as.setVisible(false);
      as.getPersistentDataContainer().set(new NamespacedKey((Plugin)OdysseyPl.getOdysseyPlugin(), "workbench"), PersistentDataType.INTEGER, Integer.valueOf(1));
    } 
  }
  
  public void trigger(Player player) {
    createGui(player);
  }
  
  public void load(Object obj) {
    BlockState bs = (BlockState)obj;
    if (bs.getType() == Material.CRAFTING_TABLE) {
      this.level = 1;
      this.name = "Etabli : " + this.level;
    } 
  }
  
  public static BOWorkBench fromString(IDynamicBuild build, String str) {
    BOWorkBench object = null;
    String[] str_a = str.split("!!obj;");
    String name = str_a[0];
    UUID owner = UUID.fromString(str_a[1]);
    Vector pos = new Vector(Double.parseDouble(str_a[2]), 
        Double.parseDouble(str_a[3]), 
        Double.parseDouble(str_a[4]));
    int level = Integer.parseInt(str_a[5]);
    object = new BOWorkBench(build, owner, pos);
    object.name = name;
    object.level = level;
    return object;
  }
  
  public String toString() {
    String str = "WORKBENCH!!objtype;";
    str = String.valueOf(str) + this.name + "!!obj;";
    str = String.valueOf(str) + this.owner + "!!obj;";
    str = String.valueOf(str) + this.pos.getX() + "!!obj;" + this.pos.getY() + "!!obj;" + this.pos.getZ() + "!!obj;";
    str = String.valueOf(str) + this.level;
    return str;
  }
  
  public void createGui(Player player) {
    BOWorkBenchHolder holder = new BOWorkBenchHolder(this, 0);
    Inventory inv = Bukkit.createInventory(holder, 54, "§r"+CharRepo.BUILD_WORKBENCH_54);
    player.openInventory(inv);
  }
  
  @EventHandler
  public static void onClick(InventoryClickEvent event) {
    Inventory inv = event.getInventory();
    if (inv.getHolder() instanceof BOWorkBenchHolder) {
      BOWorkBenchHolder holder = (BOWorkBenchHolder)inv.getHolder();
      if (event.isShiftClick())
        event.setCancelled(true); 
      if (!(event.getClickedInventory() instanceof org.bukkit.inventory.PlayerInventory) && !Arrays.stream(holder.slots_perm).anyMatch(i -> (i == event.getSlot())))
        event.setCancelled(true); 
    } 
  }
  
  @EventHandler
  public static void onDragClick(InventoryDragEvent event) {
    Inventory inv = event.getInventory();
    if (inv.getHolder() instanceof BOWorkBenchHolder) {
      BOWorkBenchHolder holder = (BOWorkBenchHolder)inv.getHolder();
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
    if (inv.getHolder() instanceof BOWorkBenchHolder) {
      BOWorkBenchHolder holder = (BOWorkBenchHolder)inv.getHolder();
      for (int i = 0; i < holder.slots_perm.length; i++)
        event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), inv.getItem(holder.slots_perm[i])); 
    } 
  }
}

class BOWorkBenchHolder implements InventoryHolder {
  private BOWorkBench object;
  
  protected final int[] slots_perm = new int[] { 
      2, 3, 4, 5, 6, 7, 
      11, 12, 13, 14, 
      15, 16, 
      20, 21, 22, 23, 24, 25, 
      29, 30, 
      31, 32, 33, 34, 
      38, 39, 40, 41, 42, 43 };
  
  public BOWorkBenchHolder(BOWorkBench object, int page) {
    this.object = object;
  }
  
  public Inventory getInventory() {
    return null;
  }
  
  public BOWorkBench getObject() {
    return this.object;
  }
  
  public void setObject(BOWorkBench object) {
    this.object = object;
  }
}

