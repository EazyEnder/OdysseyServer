package fr.eazyender.odyssey.utils.zone;

import org.bukkit.Location;

public abstract class IZone {
	
	protected String name;
	protected String type;
	
	public IZone(String name, String type) {
		
		this.name = name;
		this.type = type;
	}

	public abstract String getName();

	public abstract void setName(String name);

	public abstract String getType();

	public abstract void setType(String type);

	public abstract Location getCenter();


}
