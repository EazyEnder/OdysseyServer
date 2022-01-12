package fr.eazyender.odyssey.utils.zone;

import org.bukkit.Location;

public class IZoneCircle extends IZone{
	
	private Location center;
	private double radius;
	
	public IZoneCircle(String name, String type, Location center, double radius) {
		
		super(name,type);
		this.center = center;
		this.radius = radius;
		
	}

	public String getName() {
		return this.name;
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

	public Location getCenter() {
		return center;
	}

	public void setCenter(Location center) {
		this.center = center;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	
	
	
	

}
