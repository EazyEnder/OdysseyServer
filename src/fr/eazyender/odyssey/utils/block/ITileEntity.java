package fr.eazyender.odyssey.utils.block;

import org.bukkit.Location;

public abstract class ITileEntity {

	protected int tick_loop;
	protected Location block_location;
	
	public ITileEntity(Location block_location, int tick_loop) {
		this.tick_loop = tick_loop;
		this.block_location = block_location;
	}

	public abstract  int getTick_loop();

	public abstract  void setTick_loop(int tick_loop);

	public abstract  Location getBlock_location();

	public abstract  void setBlock_location(Location block_location);
	
	public abstract void loop();
	
	public abstract String toString();
	
	
	
	
	

}
