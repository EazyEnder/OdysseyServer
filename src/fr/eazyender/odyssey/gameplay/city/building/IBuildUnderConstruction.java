package fr.eazyender.odyssey.gameplay.city.building;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.structure.Palette;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.utils.TextUtils;
import net.md_5.bungee.api.ChatColor;

public class IBuildUnderConstruction {
	
	private UUID owner;
	private World world;
	private Vector pos;
	private int rotationY;
	private IBuild build;
	private int next_tier;
	
	private ArmorStand holo;
	private double construction_time = 0;
	
	
	
	public IBuildUnderConstruction(UUID owner, World world, Vector pos, int rotationY, IBuild build, int next_tier) {
		this.owner = owner;
		this.world = world;
		this.pos = pos;
		this.rotationY = rotationY;
		this.build = build;
		this.next_tier = next_tier;
		
		Location holo_loc = new Location(world,
				pos.getX() + ((build.getSize()[0]) * Math.cos(rotationY * (Math.PI/180)) + (build.getSize()[2]) * -Math.sin(rotationY* (Math.PI/180) )) / 2,
				pos.getY() + 4,
				pos.getZ() + (Math.sin(rotationY* (Math.PI/180)) * (build.getSize()[0]) + Math.cos(rotationY* (Math.PI/180)) * (build.getSize()[2])) / 2
				);
		
		holo = createHologram(holo_loc, getBar());
		
		new BukkitRunnable() {

			@Override
			public void run() {
				
				if(construction_time >= build.getWork_time().get(next_tier)) {
					finishBuildConstruction();
					holo.remove();
					this.cancel();
				}
				
				construction_time++;
				holo.setCustomName(getBar());
				
			}
			
		}.runTaskTimer(OdysseyPl.getOdysseyPlugin(), 20, 20);
		
		
	}
	
	private String getBar() {
		
		int resolution = 20;
		String bar = "§r<";
		
		double avancement_percent = construction_time / build.getWork_time().get(next_tier);
		
		//H : 0 -> 100 (red to green)
		
		bar += ChatColor.of(Color.getHSBColor((float) (avancement_percent * 100 / 360.0), (float)(44.0/100.0) , (float)(74.0/100.0))) + "§l";
		
		if((int)Math.floor(avancement_percent * resolution) > 0 && resolution - (int)Math.floor(avancement_percent * resolution) > 0)
		bar += "X".repeat((int)Math.floor(avancement_percent * resolution)) + "§r§f" + "-".repeat(resolution - (int)Math.floor(avancement_percent * resolution));
	
		bar += "§r>";
		return bar;
	}
	
	private static ArmorStand createHologram(Location lc, String txt) {
		
		
		ArmorStand as = (ArmorStand) lc.getWorld().spawnEntity(lc, EntityType.ARMOR_STAND);
		as.setGravity(false);
		as.setCanPickupItems(false);
		as.setCustomName(txt);
		as.setCustomNameVisible(true);
		as.setVisible(false);
		
		as.getPersistentDataContainer().set(new NamespacedKey(OdysseyPl.getOdysseyPlugin(), "loadingbar_construction"), PersistentDataType.STRING, "loadingbar_construction");
		
		return as;
		
	}
	
	private void gen(String sc) {
			
			File src = new File(OdysseyPl.getOdysseyPlugin().getDataFolder(),sc + ".nbt");
		
		StructureManager manager = Bukkit.getStructureManager();
		try {
			Structure structure = manager.loadStructure(src);
			if(rotationY == 0)structure.place(new Location(world, pos.getX(), pos.getY()-1, pos.getZ()), false, StructureRotation.NONE, Mirror.NONE, 0, 1.0f, new Random());
			else if(rotationY == 90)structure.place(new Location(world, pos.getX(), pos.getY()-1, pos.getZ()), false, StructureRotation.CLOCKWISE_90, Mirror.NONE, 0, 1.0f, new Random());
			else if(rotationY == 180)structure.place(new Location(world, pos.getX(), pos.getY()-1, pos.getZ()), false, StructureRotation.CLOCKWISE_180, Mirror.NONE, 0, 1.0f, new Random());
			else if(rotationY == 270)structure.place(new Location(world, pos.getX(), pos.getY()-1, pos.getZ()), false, StructureRotation.COUNTERCLOCKWISE_90, Mirror.NONE, 0, 1.0f, new Random());
		
			
			Palette blockPalette = structure.getPalettes().get(0);
	        for (BlockState data : blockPalette.getBlocks()) {
	        	if(data.getType().equals(Material.STRUCTURE_BLOCK)) {
	        		world.getBlockAt(data.getLocation().add(pos).add(new Vector(0,-1,0))).setType(Material.AIR);
	        	}
	        }
			
		} catch (IOException e) {
			e.printStackTrace();
			Bukkit.getPlayer(owner).sendMessage(TextUtils.aide + "La structure n'a pas réussi à être générée.");
			}
			
	  
	}
	
	public String toString() {
		String str = "";
		str += "owner:";
		str += world.getName() + ",";
		str += pos.getX() + ",";
		str += pos.getY() + ",";
		str += pos.getZ() + ",";
		str += rotationY+ ",";
		str += build.getName() + ",";
		str += next_tier + ",";
		str += construction_time;
		
		return str;
	}
	
	public void finishBuildConstruction() {
		
		String shem_name = build.getSchem_path() + "_" + next_tier;
		gen(shem_name);
		
		Player player = Bukkit.getPlayer(owner);
		if(player != null && player.isOnline()) {
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);
			player.sendMessage(TextUtils.aide + "La construction de votre bâtiment " + build.getName() + " est finie.");
		}
		
	}
	
	public void pauseBuildConstruction() {
		
	}
	
	public void resumeBuildConstruction() {
		
		//Load data from file
		
		//Load holo from ancient armor stand
		BoundingBox area = new BoundingBox(pos.getX(),pos.getY(),pos.getZ(),
				pos.getX() + ((build.getSize()[0]) * Math.cos(rotationY * (Math.PI/180)) + (build.getSize()[2]) * -Math.sin(rotationY* (Math.PI/180) )),
				pos.getY() + build.getSize()[1],
				pos.getZ() + (Math.sin(rotationY* (Math.PI/180)) * (build.getSize()[0]) + Math.cos(rotationY* (Math.PI/180)) * (build.getSize()[2])) );
		
		for (Entity entity : world.getNearbyEntities(area)) {
			if(entity instanceof ArmorStand && entity.getPersistentDataContainer().has(new NamespacedKey(OdysseyPl.getOdysseyPlugin(), "loadingbar_construction"), PersistentDataType.STRING)) {
				holo = (ArmorStand)entity;
				break;
			}
		}
		
	}
	

}
