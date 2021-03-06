package fr.eazyender.odyssey.gameplay.city.building;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import fr.eazyender.odyssey.gameplay.city.building.objects.BOContainer;

public class IBuildObject {
	
	protected IDynamicBuild build_owner;
	protected UUID owner;
	protected Vector pos;
	
	public IBuildObject() {
		
	}
	
	public IBuildObject(IDynamicBuild build_owner, UUID owner, Vector pos) {
		this.build_owner = build_owner;
		this.owner = owner;
		this.pos = pos;
	}

	public void render() {}
	
	public void trigger(Player player) {}

	public void load(Object obj) {}

	public IDynamicBuild getBuild_owner() {
		return build_owner;
	}

	public UUID getOwner() {
		return owner;
	}

	public Vector getPos() {
		return pos;
	}
	
	public static IBuildObject getObject(IDynamicBuild build, String str) {
		
		IBuildObject obj = null;
		String[] str_a = str.split("!!objtype;");
		String type = str_a[0];
		switch(type) {
		case "CONTAINER": obj = BOContainer.fromString(build, str_a[1]);
			break;
		default:
			break;
		}
		
		return obj;
		
	}
	
	public String toString() {return "";}
	
	

}
