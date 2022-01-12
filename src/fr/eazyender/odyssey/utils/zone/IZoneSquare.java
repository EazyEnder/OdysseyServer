package fr.eazyender.odyssey.utils.zone;

import org.bukkit.Location;

public class IZoneSquare extends IZone {
	
	private Location[] loc = {null,null};
	
	public IZoneSquare(String name, String type, Location minLoc, Location maxLoc) {
		super(name,type);
		this.loc[0] = minLoc;this.loc[1] = maxLoc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Location[] getPosition() {
		return loc;
	}

	public void setPosition(Location[] position) {
		this.loc = position;
	}
	
	public Location getCenter() {
		double x = (loc[0].getX()+((loc[1].getX()-loc[0].getX())/2));
		double z = (loc[0].getZ()+((loc[1].getZ()-loc[0].getZ())/2));
		Location l = new Location(null,x,0,z);
		return l;
	}
	

}
