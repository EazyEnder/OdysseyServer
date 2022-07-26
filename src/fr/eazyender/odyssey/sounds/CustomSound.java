package fr.eazyender.odyssey.sounds;

import fr.eazyender.odyssey.sounds.SoundProps.SoundTrigger;

public class CustomSound {
	
	private String id;
	private float[] volume;
	private float[] pitch;
	private int length;
	private int loop;
	private double chance;
	private double range;
	private SoundTrigger trigger;
	
	/*BLOCK: "MATERIAL;DENSITY;DISTANCE;IFISLOCATED"
	 * BIOME : "NAME"
	 * BUILD : "NAME;DISTANCE" (0 = inside the build)*/
	private String trigger_data;
	
	/*
	 * Time in the world : 
	 * 
	 * -total day : [-6000,18000]
	 * -day : [-6000,6000]
	 * -night : [6000,18000]
	 * 
	 * other : zenith : 0 ; moon_apo : 12000
	 */
	private int[] time_spectre;
	
	public CustomSound(String id, float[] volume, float[] pitch, int length, int loop, double chance, SoundTrigger trigger, String trigger_data, int[] time_spectre, double range) {
		this.id = id;
		this.volume = volume;
		this.pitch = pitch;
		this.length = length;
		this.loop = loop;
		this.chance = chance;
		this.trigger = trigger;
		this.trigger_data = trigger_data;
		this.time_spectre = time_spectre;
		this.range = range;
	}
	
	
	

	public double getRange() {
		return range;
	}




	public int[] getTime_spectre() {
		return time_spectre;
	}



	public String getId() {
		return id;
	}

	public float[] getVolume() {
		return volume;
	}

	public float[] getPitch() {
		return pitch;
	}

	public int getLength() {
		return length;
	}

	public int getLoop() {
		return loop;
	}

	public double getChance() {
		return chance;
	}

	public SoundTrigger getTrigger() {
		return trigger;
	}

	public String getTrigger_data() {
		return trigger_data;
	}
	
	

}
