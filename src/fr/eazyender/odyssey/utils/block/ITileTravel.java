package fr.eazyender.odyssey.utils.block;

import org.bukkit.Location;

public abstract class ITileTravel extends ITileEntity{
	
	protected String type;
	protected double unit;
	protected Location[] inputs= {null,null,null,null};
	protected Location[] outputs= {null,null,null,null};

	public ITileTravel(Location block_location, int tick_loop, String type, double unit, Location[] inputs, Location[] outputs) {
		super(block_location, tick_loop);
		this.inputs = inputs;
		this.outputs = outputs;
		this.unit = unit;
		this.type = type;
	}
	
	public abstract  int getTick_loop();

	public abstract  void setTick_loop(int tick_loop);

	public abstract  Location getBlock_location();

	public abstract  void setBlock_location(Location block_location);
	
	public abstract void loop();
	
	public abstract String toString();
	
	public abstract Location[] getInputs();

	public abstract void setInputs(Location[] inputs);

	public abstract Location[] getOutputs();

	public abstract void setOutputs(Location[] outputs);
	
	public abstract int getFreeOutput();
	
	public abstract int getFreeInput();

	public abstract String getType();

	public abstract void setType(String type);

	public abstract double getUnit();

	public abstract void setUnit(double unit);
	
	

}
