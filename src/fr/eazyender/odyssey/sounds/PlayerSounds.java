package fr.eazyender.odyssey.sounds;

import java.util.HashMap;
import java.util.Map;

public class PlayerSounds {
	
	//String = id / Integer = time_in_second
	private Map<String,Integer> sounds_on_played = new HashMap<String,Integer>();
	
	public PlayerSounds() {}
	
	public Map<String,Integer> getSounds(){
		return sounds_on_played;
	}

}
