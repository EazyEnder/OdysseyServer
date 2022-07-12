package fr.eazyender.odyssey.player.harvest;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public class IRegenBlock {
	
	private double timer;
	private double actual_timer = 0;
	private Material type;
	private BlockData data;
	private Location loc;
	
	public IRegenBlock(Material type, BlockData data,Location loc,double timer) {
		this.type = type;
		this.data = data;
		this.loc = loc;
		this.timer = timer;
		this.actual_timer = 0;
	}
	
	

	public Material getType() {
		return type;
	}



	public void setType(Material type) {
		this.type = type;
	}



	public BlockData getData() {
		return data;
	}



	public void setData(BlockData data) {
		this.data = data;
	}



	public double getTimer() {
		return timer;
	}

	public void setTimer(double timer) {
		this.timer = timer;
	}

	public double getActual_timer() {
		return actual_timer;
	}

	public void setActual_timer(double actual_timer) {
		this.actual_timer = actual_timer;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}
	
	

}
