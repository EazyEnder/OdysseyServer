package fr.eazyender.odyssey.gameplay.city.building;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.World;
import org.bukkit.util.Vector;

public class IDynamicBuild {
	
	private UUID owner;
	//private List<IBuildObject> objects = new ArrayList<IBuildObject>();
	private Vector pos;
	private int rotationY;
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
	
	public boolean upgrade() {
		return false;
	}
	
	public void save() {
		
	}
	
	public void load() {
		
	}
	
}
