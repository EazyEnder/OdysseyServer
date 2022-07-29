package fr.eazyender.odyssey.gameplay.city.building.objects;

import java.util.UUID;

import org.bukkit.util.Vector;

import fr.eazyender.odyssey.gameplay.city.building.IBuildObject;
import fr.eazyender.odyssey.gameplay.city.building.IDynamicBuild;

public class IBuildObjectLoop extends IBuildObject{
	
	public IBuildObjectLoop() {}
	
	public IBuildObjectLoop(IDynamicBuild build_owner, UUID owner, Vector pos) {
		super(build_owner, owner, pos);
	}

	public void loop() {}
	

}
