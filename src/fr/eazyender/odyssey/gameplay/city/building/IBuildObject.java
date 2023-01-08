package fr.eazyender.odyssey.gameplay.city.building;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import fr.eazyender.odyssey.gameplay.city.building.objects.BOContainer;
import fr.eazyender.odyssey.gameplay.city.building.objects.BOLFurnace;
import fr.eazyender.odyssey.gameplay.city.building.objects.BOManager;
import fr.eazyender.odyssey.gameplay.city.building.objects.BOWorkBench;

public class IBuildObject {
	
	protected String name;
	protected IDynamicBuild build_owner;
	protected UUID owner;
	protected Vector pos;
	protected Material icon = Material.PAPER;
	
	public IBuildObject() {}
	
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
	
	public String getName() {
		return name;
	}

	public UUID getOwner() {
		return owner;
	}

	public Vector getPos() {
		return pos;
	}
	
	public Material getIcon() {
		return icon;
	}
	
	public static IBuildObject getObject(IDynamicBuild build, String str) {
		
		IBuildObject obj = null;
		String[] str_a = str.split("!!objtype;");
		String type = str_a[0];
		switch(type) {
		case "CONTAINER": obj = BOContainer.fromString(build, str_a[1]);
			break;
		case "FURNACE": obj = BOLFurnace.fromString(build, str_a[1]);
			break;
		case "WORKBENCH": obj = BOWorkBench.fromString(build, str_a[1]);
			break;
		case "MANAGER": obj = BOManager.fromString(build, str_a[1]);
			break;
		default:
			break;
		}
		
		return obj;
		
	}
	
	public String toString() {return "";}
	
	

}
