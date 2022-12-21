package fr.eazyender.odyssey.utils.world;

import org.bukkit.Location;

public class IWorldTeleporter {
	
	private IWorld parent, out;
	private Location center_p, center_o;
	private Double size;
	
	public IWorldTeleporter(IWorld parent, IWorld out, Location center_p, Location center_o, Double size) {
		this.parent = parent;
		this.out = out;
		this.center_p = center_p;
		this.center_o = center_o;
		this.size = size;
	}

	public IWorld getParent() {
		return parent;
	}

	public void setParent(IWorld parent) {
		this.parent = parent;
	}

	public IWorld getOut() {
		return out;
	}

	public void setOut(IWorld out) {
		this.out = out;
	}

	public Location getCenter_p() {
		return center_p;
	}

	public void setCenter_p(Location center_p) {
		this.center_p = center_p;
	}

	public Location getCenter_o() {
		return center_o;
	}

	public void setCenter_o(Location center_o) {
		this.center_o = center_o;
	}

	public Double getSize() {
		return size;
	}

	public void setSize(Double size) {
		this.size = size;
	}
	
	

}
