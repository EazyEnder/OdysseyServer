package fr.eazyender.odyssey.gameplay.city.building;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.structure.Palette;
import org.bukkit.util.Vector;

import fr.eazyender.odyssey.gameplay.city.building.objects.BuildObjectListener;

public class IDynamicBuild {
	
	private UUID owner;
	private List<IBuildObject> objects = new ArrayList<IBuildObject>();
	private Vector pos;
	int rotationY;
	private IBuild build;
	private World world;
	private int tier;
	
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
		
	}
	
	public boolean upgrade_components(Palette pal) {
		
		tier++;
		
		 for (BlockState data : pal.getBlocks()) {
			 Vector new_pos = data.getLocation().toVector().rotateAroundY(-rotationY * Math.PI/180).add(pos).add(new Vector(0,-1,0));
			IBuildObject obj =  BuildObjectListener.convertBlockToObjects(data, this, owner,new_pos.clone().add(new Vector(0,-0.7,0)));
			if(obj != null) {
				obj.load(data);
				obj.render();
				world.getBlockAt(new_pos.toLocation(world)).setType(Material.AIR);
				
				objects.add(obj);
			}
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
		
		dynamicbuild = new IDynamicBuild(owner,pos,rotationY,world,build,tier);
		
		List<IBuildObject> objects = new ArrayList<IBuildObject>();
		if(str_b.length > 7) {
		String[] str_c = str_b[7].split("!!delObjects;");
		for (String str_d : str_c) {
			objects.add(IBuildObject.getObject(dynamicbuild, str_d));
		}
		}
		
		dynamicbuild.objects = objects;
		
		return dynamicbuild;
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

	public List<UUID> getPermission_players() {
		return permission_players;
	}
	
	
	
	
	
}
