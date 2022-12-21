package fr.eazyender.odyssey.gameplay.city.building;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.structure.Palette;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.gameplay.city.building.objects.BuildObjectListener;
import fr.eazyender.odyssey.gameplay.city.building.objects.IBuildObjectLoop;
import fr.eazyender.odyssey.utils.TextUtils;

public class IDynamicBuild {
	
	private UUID owner;
	private List<IBuildObject> objects = new ArrayList<IBuildObject>();
	private Vector pos;
	int rotationY;
	private IBuild build;
	private World world;
	private int tier;
	
	private Double hp;
	//Coefficient btw 0 -> 1
	private Double armor;
	//in second
	private int decay_time;
	//Tier 0 = Full integrity
	private int decay_tier;
	private int max_decay_tier = 3;
	
	private List<UUID> permission_players = new ArrayList<UUID>();
	//private ICity city; 
	

	public IDynamicBuild(UUID owner, Vector pos, int rotationY, World world, IBuild build, int tier) {
		this.owner = owner;
		this.pos = pos;
		this.rotationY = rotationY;
		this.build = build;
		this.world = world;
		this.tier = tier;
	}
	
	public void loop() {
		for (IBuildObject object : objects) {
			if(object instanceof IBuildObjectLoop) {
				((IBuildObjectLoop)(object)).loop();
			}
		}
		
		if(tier > 0)this.decay();
	}
	
	public boolean upgrade(Palette pal) {
		tier++;
		
		return upgrade_components(pal);
	}
	
	public boolean upgrade_components(Palette pal) {
		
		//REMOVE ARMOR STAND
		BoundingBox area = new BoundingBox(pos.getX(),pos.getY(),pos.getZ(),
				pos.getX() + ((build.getSize()[0]) * Math.cos(rotationY * (Math.PI/180)) + (build.getSize()[2]) * -Math.sin(rotationY* (Math.PI/180) )),
				pos.getY() + build.getSize()[1],
				pos.getZ() + (Math.sin(rotationY* (Math.PI/180)) * (build.getSize()[0]) + Math.cos(rotationY* (Math.PI/180)) * (build.getSize()[2])) );
		
		for (Entity entity : world.getNearbyEntities(area)) {
			if(entity instanceof ArmorStand && !entity.getPersistentDataContainer().isEmpty()) {
				ArmorStand holo = (ArmorStand)entity;
				holo.remove();
			}
		}
		
		//REBUILD OBJECT
		List<IBuildObject> new_objects = new ArrayList<IBuildObject>();
		 for (BlockState data : pal.getBlocks()) {
			 Vector new_pos = data.getLocation().toVector().rotateAroundY(-rotationY * Math.PI/180).add(pos).add(new Vector(0,-1,0));
			IBuildObject obj =  BuildObjectListener.convertBlockToObjects(data, this, owner,new_pos.clone().add(new Vector(0,-0.7,0)));
			if(obj != null) {
				obj.load(data);
				obj.render();
				world.getBlockAt(new_pos.toLocation(world)).setType(Material.AIR);
				
				for (IBuildObject object : objects) {
					if(object.name != null && obj.name != null && object.name.equalsIgnoreCase(obj.name)) {
						obj = object;
						break;
					}
				}
				new_objects.add(obj);
			}
         }
		 objects = new_objects;
		
		
		return true;
	}
	
	public boolean destroy() {
		
		upgrade_components(gen(build.getSchem_path() + "_" + tier +  "_ruin"));
		BuildManager.dynamic_builds.remove(this);
		BuildManager.ruins.add(new IRuin(pos,rotationY,world,build,tier));
		
		return true;
	}
	
	public boolean decay() {
		
		decay_time++;
		
		if(decay_tier < max_decay_tier && decay_time >= build.getDecay_time().get(decay_tier)) {

			
			decay_tier++;
			decay_time = 0;
			
			//OPTION CREATE RUIN
			if(decay_tier == max_decay_tier) {
				destroy();
				return false;
			}
			
			//REGEN BUILD
			upgrade_components(gen(build.getSchem_path() + "_" + tier +  "_decay_" + decay_tier));
			
			//CHANGE STATS OF BUILD
			
			return true;
		}
		
		return false;
	}
	
	public boolean launch_upgrade() {
		return false;
	}
	
	@Override
	public String toString() {
		
		String str = owner.toString();
		str += "!!UUID;";
		
		str += pos.getX() + "!!Base;" + pos.getY() + "!!Base;" + pos.getZ() + "!!Base;";
		str += rotationY + "!!Base;";
		str += world.getName() + "!!Base;";
		str += build.getName() + "!!Base;";
		str += tier + "!!Base;";
		str += decay_tier + "!!Base;";
		str += decay_time + "!!Base;";
		
		for (IBuildObject obj : objects) {
			if(objects.indexOf(obj) != 0 )str += "!!delObjects;";
			str += obj.toString();
		}
	
		return str;
	}
	
	public static IDynamicBuild fromString(String str) {
		
		IDynamicBuild dynamicbuild = null;
		
		String[] str_a = str.split("!!UUID;");
		UUID owner = UUID.fromString(str_a[0]);
		
		String[] str_b = str_a[1].split("!!Base;");
		Vector pos = new Vector(Double.parseDouble(str_b[0]),
				Double.parseDouble(str_b[1]),
				Double.parseDouble(str_b[2]));
		
		int rotationY = Integer.parseInt(str_b[3]);
		World world = Bukkit.getWorld(str_b[4]);
		IBuild build = BuildManager.getBuildByName(str_b[5]);
		int tier = Integer.parseInt(str_b[6]);
		int decay_tier = Integer.parseInt(str_b[7]);
		int decay_time = Integer.parseInt(str_b[8]);
		
		dynamicbuild = new IDynamicBuild(owner,pos,rotationY,world,build,tier);
		
		List<IBuildObject> objects = new ArrayList<IBuildObject>();
		if(str_b.length > 9) {
		String[] str_c = str_b[9].split("!!delObjects;");
		for (String str_d : str_c) {
			objects.add(IBuildObject.getObject(dynamicbuild, str_d));
		}
		}
		
		dynamicbuild.objects = objects;
		
		dynamicbuild.decay_tier = decay_tier;
		dynamicbuild.decay_time = decay_time;
		
		return dynamicbuild;
	}
	
	private Palette gen(String sc) {
		
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
            return blockPalette;
			
		} catch (IOException e) {
			e.printStackTrace();
			Bukkit.getPlayer(owner).sendMessage(TextUtils.aide + "La structure n'a pas réussi à être générée.");
			return null;
		}
		
  
	}
	
	public void load() {
		
	}

	public UUID getOwner() {
		return owner;
	}

	public List<IBuildObject> getObjects() {
		return objects;
	}

	public Vector getPos() {
		return pos;
	}

	public int getRotationY() {
		return rotationY;
	}

	public IBuild getBuild() {
		return build;
	}

	public World getWorld() {
		return world;
	}

	public int getTier() {
		return tier;
	}
	
	

	public int getDecay_time() {
		return decay_time;
	}

	public void setDecay_time(int decay_time) {
		this.decay_time = decay_time;
	}

	public int getDecay_tier() {
		return decay_tier;
	}

	public void setDecay_tier(int decay_tier) {
		this.decay_tier = decay_tier;
	}

	public List<UUID> getPermission_players() {
		return permission_players;
	}
	
	
	
	
	
}
