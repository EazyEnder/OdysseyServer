package fr.eazyender.odyssey.gameplay.city.building;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.structure.Palette;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;
import org.bukkit.util.Vector;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.utils.TextUtils;

public class IBuildingSettings {
	
	private UUID playerid;
	
	private Vector pos = new Vector(0,0,0);
	private int rotationY = 0;
	private IBuild build;
	
	public BukkitTask task;
	private String state;
	
	
	public IBuildingSettings(UUID playerid, Vector pos,int rotationY,IBuild build) {
		this.playerid = playerid;
		this.pos = pos;
		this.rotationY = rotationY;
		this.build = build;
	}
	
	public IBuildingSettings refreshSettings(int rotationY,IBuild build, boolean refreshpos) {
		this.rotationY = rotationY;
		if(build != null)this.build = build;
		refreshPlacement(refreshpos);
		
		return this;
	}
	
	public boolean beginConstruction() {
		
		Player player = Bukkit.getPlayer(playerid);
		
		//Check State
		if(!this.state.equalsIgnoreCase("GOOD")) {
			player.sendMessage(TextUtils.aide + "Vous ne pouvez pas construire ce bâtiment §l(Endroit invalide + " + this.state +  ").§r");
			return false;
		}
		
		//Check items
		if(!build.getUpgrade().isEmpty())
		for (ItemStack require : build.getUpgrade().get(0)) {
			
			if(!player.getInventory().contains(require)) {
				player.sendMessage(TextUtils.aide + "Vous ne pouvez pas construire ce bâtiment §l(pas les ressources nécessaires).§r");
				return false;
			}
			
		}
		
		
		if(!HammerHandler.in_works.containsKey(playerid)) {
			
			
			IDynamicBuild dynamicbuild = new IDynamicBuild(player.getUniqueId(),
						getPos(),
						getRotationY(),
						player.getWorld(),
						getBuild(),
						0);
			
			BuildManager.dynamic_builds.add(dynamicbuild);
			
			HammerHandler.in_works.put(playerid, new IBuildUnderConstruction(playerid, Bukkit.getPlayer(playerid).getWorld(), pos, rotationY, build, dynamicbuild, 1));
			
			//Remove items
			if(!build.getUpgrade().isEmpty())
			for (ItemStack require : build.getUpgrade().get(0)) {
				
				if(player.getInventory().contains(require)) {
					player.getInventory().remove(require);
				}
				
			}
			
			
			
			
			String shem_name = build.getSchem_path() + "_begin";
			
			gen(shem_name);
		}else {
			player.sendMessage(TextUtils.aide + "Vous avez déjà une construction en cours.");
		}
		
		return true;
		
		
	}
	
	public String verifyPlacement() {
		
		Player player = Bukkit.getPlayer(playerid);
		
		//Schem bloc orientation (inverse d'un trièdre direct pour le Z)
		/**    N
		 *     ^		  SIZE Y
		 * X--------|		 
		 * |--------|		 O	> SIZE X (positive X)
		 * |--------|		 V
		 * |--------|	SIZE Z (positive Z)
 		 */
		String state = "GOOD";
		
		//Verify if the area can host a build
		
		//Verify all blocks
		
		List<Material> mask_blocks = new ArrayList<Material>();
		mask_blocks.add(Material.AIR);
		mask_blocks.add(Material.GRASS);
		mask_blocks.add(Material.TALL_GRASS);
		mask_blocks.add(Material.OAK_LEAVES);
		mask_blocks.add(Material.ACACIA_LEAVES);
		mask_blocks.add(Material.SPRUCE_LEAVES);
		mask_blocks.add(Material.BIRCH_LEAVES);
		mask_blocks.add(Material.DARK_OAK_LEAVES);
		mask_blocks.add(Material.JUNGLE_LEAVES);
		
		int gradient_sup_max = -999;
		int gradient_sup_min = pos.getBlockY();
		for (double x = 0; x < Math.abs((build.getSize()[0]-0.5) * Math.cos(rotationY * (Math.PI/180)) + (build.getSize()[2]-0.5) * -Math.sin(rotationY* (Math.PI/180) )); x++) {
			if(Math.round(Math.sin(rotationY * (Math.PI/180))) != 0) x *=  -Math.sin(rotationY * (Math.PI/180));
			if(Math.cos(rotationY * (Math.PI/180)) == -1)x *= -1; 
			for (double z = 0; z < Math.abs(Math.sin(rotationY* (Math.PI/180)) * (build.getSize()[0]-0.5) + Math.cos(rotationY* (Math.PI/180)) * (build.getSize()[2]-0.5)) ; z++) {
				if(Math.round(Math.sin(rotationY * (Math.PI/180))) != 0)z *=  Math.sin(rotationY * (Math.PI/180));
				if(Math.cos(rotationY * (Math.PI/180)) == -1) z *= -1;
				for (double y = pos.getY(); y < pos.getY() + build.getSize()[1]; y++) {
					Block bc = player.getWorld().getBlockAt(new Location(player.getWorld(),pos.getX()+x,y,pos.getZ()+z));
					
					if(!mask_blocks.contains(bc.getType())) {
						gradient_sup_max = Math.max(bc.getY(), gradient_sup_max);
						gradient_sup_min = Math.min(bc.getY(), gradient_sup_min);
					}
				}
				if(Math.round(Math.sin(rotationY * (Math.PI/180))) != 0)z /=  Math.sin(rotationY * (Math.PI/180));
				if(Math.cos(rotationY * (Math.PI/180)) == -1) z /= -1;
			}
			if(Math.round(Math.sin(rotationY * (Math.PI/180))) != 0) x /=  -Math.sin(rotationY * (Math.PI/180));
			if(Math.cos(rotationY * (Math.PI/180)) == -1)x /= -1; 
		}
		
		int gradient_sup = Math.abs(gradient_sup_max - gradient_sup_min);
		if(gradient_sup >= 2 && gradient_sup_max != -999) return "TERRAIN_BLOCK";
		
		int gradient_inf_max = pos.getBlockY();
		int gradient_inf_min = 999;
		for (double x = 0; x < Math.abs((build.getSize()[0]-0.5) * Math.cos(rotationY * (Math.PI/180)) + (build.getSize()[2]-0.5) * -Math.sin(rotationY* (Math.PI/180) )); x++) {
			if(Math.round(Math.sin(rotationY * (Math.PI/180))) != 0) x *=  -Math.sin(rotationY * (Math.PI/180));
			if(Math.cos(rotationY * (Math.PI/180)) == -1)x *= -1; 
			for (double z = 0; z < Math.abs(Math.sin(rotationY* (Math.PI/180)) * (build.getSize()[0]-0.5) + Math.cos(rotationY* (Math.PI/180)) * (build.getSize()[2]-0.5)) ; z++) {
				if(Math.round(Math.sin(rotationY * (Math.PI/180))) != 0)z *=  Math.sin(rotationY * (Math.PI/180));
				if(Math.cos(rotationY * (Math.PI/180)) == -1) z *= -1;
				for (double y = pos.getY(); y > pos.getY() - 3; y--) {
					Block bc = player.getWorld().getBlockAt(new Location(player.getWorld(),pos.getX()+x,y,pos.getZ()+z));
					
					if(mask_blocks.contains(bc.getType())) {
						gradient_inf_max = Math.max(bc.getY(), gradient_inf_max);
						gradient_inf_min = Math.min(bc.getY(), gradient_inf_min);
					}
				}
				if(Math.round(Math.sin(rotationY * (Math.PI/180))) != 0)z /=  Math.sin(rotationY * (Math.PI/180));
				if(Math.cos(rotationY * (Math.PI/180)) == -1) z /= -1;
			}
			if(Math.round(Math.sin(rotationY * (Math.PI/180))) != 0) x /=  -Math.sin(rotationY * (Math.PI/180));
			if(Math.cos(rotationY * (Math.PI/180)) == -1)x /= -1; 
		}
		
		int gradient_inf = Math.abs(gradient_inf_max - gradient_inf_min);
		if(gradient_inf >= 2 && gradient_inf_min != 999) return "TERRAIN_BLOCK";
		
		//Verify if it's too close to another build
		
		//Verify if it isn't in a city
		
		
		
		return state;
		
	}
	
	public void drawParticle() {
		
		if(task != null)task.cancel();
		
		Player player = Bukkit.getPlayer(playerid);
		
		//Schem bloc orientation (inverse d'un trièdre direct pour le Z)
		/**    N
		 *     ^		  SIZE Y
		 * X--------|		 
		 * |--------|		 O	> SIZE X (positive X)
		 * |--------|		 V
		 * |--------|	SIZE Z (positive Z)
 		 */
		
		Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(90, 153, 90), 2.5F);
		if(state.equalsIgnoreCase("TERRAIN_BLOCK")) dustOptions = new Particle.DustOptions(Color.fromRGB(142, 57, 57), 2.5F);
		else if(state.equalsIgnoreCase("CITY_BLOCK")) dustOptions = new Particle.DustOptions(Color.fromRGB(146, 115, 181), 2.5F);
		else if(state.equalsIgnoreCase("INVALID_ZONE")) dustOptions = new Particle.DustOptions(Color.fromRGB(10, 10, 10), 2.5F);
		else if(state.equalsIgnoreCase("BUILD_AROUND")) dustOptions = new Particle.DustOptions(Color.fromRGB(206, 163, 82), 2.5F);
		
		final Particle.DustOptions dop = dustOptions;
		
		task = new BukkitRunnable() {
			
			public void cancelTask() {
				HammerHandler.settings.remove(playerid);
				this.cancel();
			}
			
			@Override
			public void run() {
				
				if(!player.isOnline() || player.getLocation().toVector().distance(pos) > 25) {
					cancelTask();
				}
				
				Particle.DustOptions dustOptions = dop;
				
				//Draw outline cube
				for (double x = 0; x < Math.abs((build.getSize()[0]-0.5) * Math.cos(rotationY * (Math.PI/180)) + (build.getSize()[2]-0.5) * -Math.sin(rotationY* (Math.PI/180) )); x++) {
					double y = pos.getY();
					double z = pos.getZ();
					if(Math.round(Math.sin(rotationY * (Math.PI/180))) != 0) x *=  -Math.sin(rotationY * (Math.PI/180));
					if(Math.cos(rotationY * (Math.PI/180)) == -1)x *= -1; 
					player.getWorld().spawnParticle(Particle.REDSTONE, new Location(player.getWorld(),pos.getX()+x,y,z) , 1, 0D, 0D, 0D, 1, dustOptions, true);
					
					y += build.getSize()[1];
					player.getWorld().spawnParticle(Particle.REDSTONE, new Location(player.getWorld(),pos.getX()+x,y,z) , 1, 0D, 0D, 0D, 1, dustOptions, true);
					
					z += (Math.sin(rotationY* (Math.PI/180)) * (build.getSize()[0]-0.5) + Math.cos(rotationY* (Math.PI/180)) * (build.getSize()[2]-0.5));
					player.getWorld().spawnParticle(Particle.REDSTONE, new Location(player.getWorld(),pos.getX()+x,y,z) , 1, 0D, 0D, 0D, 1, dustOptions, true);
					
					y-= build.getSize()[1];
					player.getWorld().spawnParticle(Particle.REDSTONE, new Location(player.getWorld(),pos.getX()+x,y,z) , 1, 0D, 0D, 0D, 1, dustOptions, true);
					if(Math.round(Math.sin(rotationY * (Math.PI/180))) != 0)x /=  -Math.sin(rotationY * (Math.PI/180));
					if(Math.cos(rotationY * (Math.PI/180)) == -1) x *= -1;
				}
				
				for (double z = 0; z < Math.abs(Math.sin(rotationY* (Math.PI/180)) * (build.getSize()[0]-0.5) + Math.cos(rotationY* (Math.PI/180)) * (build.getSize()[2]-0.5)) ; z++) {
					double y = pos.getY();
					double x = pos.getX();
					if(Math.round(Math.sin(rotationY * (Math.PI/180))) != 0)z *=  Math.sin(rotationY * (Math.PI/180));
					if(Math.cos(rotationY * (Math.PI/180)) == -1) z *= -1;
					player.getWorld().spawnParticle(Particle.REDSTONE, new Location(player.getWorld(),x,y,z+pos.getZ()) , 1, 0D, 0D, 0D, 1, dustOptions, true);
					
					y += build.getSize()[1];
					player.getWorld().spawnParticle(Particle.REDSTONE, new Location(player.getWorld(),x,y,z+pos.getZ()) , 1, 0D, 0D, 0D, 1, dustOptions, true);
					
					x += ((build.getSize()[0]-0.5) * Math.cos(rotationY * (Math.PI/180)) + (build.getSize()[2]-0.5) * -Math.sin(rotationY* (Math.PI/180) )) ;
					player.getWorld().spawnParticle(Particle.REDSTONE, new Location(player.getWorld(),x,y,z+pos.getZ()) , 1, 0D, 0D, 0D, 1, dustOptions, true);
					
					y-= build.getSize()[1];
					player.getWorld().spawnParticle(Particle.REDSTONE, new Location(player.getWorld(),x,y,z+pos.getZ()) , 1, 0D, 0D, 0D, 1, dustOptions, true);
					if(Math.round(Math.sin(rotationY * (Math.PI/180))) != 0)z /=  Math.sin(rotationY * (Math.PI/180));
					if(Math.cos(rotationY * (Math.PI/180)) == -1) z *= -1;
				}
				
				for (double y = pos.getY(); y < pos.getY() + build.getSize()[1]; y++) {
					double z = pos.getZ();
					double x = pos.getX();
					player.getWorld().spawnParticle(Particle.REDSTONE, new Location(player.getWorld(),x,y,z) , 1, 0D, 0D, 0D, 1, dustOptions, true);
					
					z += (Math.sin(rotationY* (Math.PI/180)) * (build.getSize()[0]-0.5) + Math.cos(rotationY* (Math.PI/180)) * (build.getSize()[2]-0.5));
					player.getWorld().spawnParticle(Particle.REDSTONE, new Location(player.getWorld(),x,y,z) , 1, 0D, 0D, 0D, 1, dustOptions, true);
					
					x += ((build.getSize()[0]-0.5) * Math.cos(rotationY * (Math.PI/180)) + (build.getSize()[2]-0.5) * -Math.sin(rotationY* (Math.PI/180) ));
					player.getWorld().spawnParticle(Particle.REDSTONE, new Location(player.getWorld(),x,y,z) , 1, 0D, 0D, 0D, 1, dustOptions, true);
					
					z -= (Math.sin(rotationY* (Math.PI/180)) * (build.getSize()[0]-0.5) + Math.cos(rotationY* (Math.PI/180)) * (build.getSize()[2]-0.5));
					player.getWorld().spawnParticle(Particle.REDSTONE, new Location(player.getWorld(),x,y,z) , 1, 0D, 0D, 0D, 1, dustOptions, true);
				}
				
				
			}
			
		}.runTaskTimer(OdysseyPl.getOdysseyPlugin(), 0, 10);
		
		
		
		
	}
	
	public void refreshPlacement(boolean poschange) {
		Player player = Bukkit.getPlayer(playerid);
		
		HashSet<Material> filter = new HashSet<>();
		filter.add(Material.AIR);
		
		if(poschange) {
		pos = player.getTargetBlock(filter, 25).getLocation().toVector().clone();
		pos.add(new Vector(0.5,1,0.5));
		}
		
		this.state = verifyPlacement();
		drawParticle();
	}

	public int getRotationY() {
		return rotationY;
	}
	
	
	
	public UUID getPlayerid() {
		return playerid;
	}

	public void setPlayerid(UUID playerid) {
		this.playerid = playerid;
	}

	public Vector getPos() {
		return pos;
	}

	public void setPos(Vector pos) {
		this.pos = pos;
	}

	public IBuild getBuild() {
		return build;
	}

	public void setBuild(IBuild build) {
		this.build = build;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setRotationY(int rotationY) {
		this.rotationY = rotationY;
	}

	private void gen(String sc) {
		
		File src = new File(OdysseyPl.getOdysseyPlugin().getDataFolder(),sc + ".nbt");
		
		StructureManager manager = Bukkit.getStructureManager();
		try {
			Structure structure = manager.loadStructure(src);
			if(rotationY == 0)structure.place(new Location(Bukkit.getPlayer(playerid).getWorld(), pos.getX(), pos.getY()-1, pos.getZ()), false, StructureRotation.NONE, Mirror.NONE, 0, 1.0f, new Random());
			else if(rotationY == 90)structure.place(new Location(Bukkit.getPlayer(playerid).getWorld(), pos.getX(), pos.getY()-1, pos.getZ()), false, StructureRotation.CLOCKWISE_90, Mirror.NONE, 0, 1.0f, new Random());
			else if(rotationY == 180)structure.place(new Location(Bukkit.getPlayer(playerid).getWorld(), pos.getX(), pos.getY()-1, pos.getZ()), false, StructureRotation.CLOCKWISE_180, Mirror.NONE, 0, 1.0f, new Random());
			else if(rotationY == 270)structure.place(new Location(Bukkit.getPlayer(playerid).getWorld(), pos.getX(), pos.getY()-1, pos.getZ()), false, StructureRotation.COUNTERCLOCKWISE_90, Mirror.NONE, 0, 1.0f, new Random());
		
			
			Palette blockPalette = structure.getPalettes().get(0);
            for (BlockState data : blockPalette.getBlocks()) {
            	if(data.getType().equals(Material.STRUCTURE_BLOCK)) {
            		Bukkit.getPlayer(playerid).getWorld().getBlockAt(data.getLocation().add(pos).add(new Vector(0,-1,0))).setType(Material.AIR);
            	}
            }
			
		} catch (IOException e) {
			e.printStackTrace();
			Bukkit.getPlayer(playerid).sendMessage(TextUtils.aide + "La structure n'a pas réussi à être générée.");
		}
		
  
	}
	
	
	

}
