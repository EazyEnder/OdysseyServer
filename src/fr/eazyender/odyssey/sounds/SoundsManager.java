package fr.eazyender.odyssey.sounds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.sounds.SoundProps.SoundTrigger;

public class SoundsManager implements Listener{
	
	private static List<CustomSound> sounds = new ArrayList<CustomSound>();
	private static Map<UUID, PlayerSounds> player_sounds = new HashMap<UUID, PlayerSounds>();
	
	public static void initSounds(int task_timer) {
		
		
		CustomSound forest_ambiance = new CustomSound("forest_ambiance",new float[] {1.0f,1.0f}, new float[] {1f,1f}, 90, 85, 1.0, SoundTrigger.BIOME, "PLAINS",new int[] {-6000,6000}, 0);
		sounds.add(forest_ambiance);
		
		CustomSound nightingale = new CustomSound("nightingale",new float[] {0.1f,0.3f}, new float[] {0.95f,1.05f}, 4, 4, 0.08, SoundTrigger.BIOME, "PLAINS",new int[] {-6000,6000}, 50);
		sounds.add(nightingale);
		
		CustomSound woodpecker = new CustomSound("woodpecker",new float[] {0.025f,0.2f}, new float[] {0.85f,1.1f}, 2, 2, 0.007, SoundTrigger.BIOME, "PLAINS",new int[] {-6000,6000}, 50);
		sounds.add(woodpecker);
		
		CustomSound pigeon_wings = new CustomSound("pigeon_wings",new float[] {0.6f,0.9f}, new float[] {0.87f,1.1f}, 2, 2, 0.03, SoundTrigger.BIOME, "PLAINS",new int[] {-6000,6000}, 30);
		sounds.add(pigeon_wings);
		
		
		initLoop(task_timer);
	}
	
	private static void initLoop(int task_timer) {
		
		new BukkitRunnable() {

			@Override
			public void run() {
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					if(!player_sounds.containsKey(player.getUniqueId()))player_sounds.put(player.getUniqueId(), new PlayerSounds());
					
					PlayerSounds psounds = player_sounds.get(player.getUniqueId());
					
					for (CustomSound customSound : sounds) {
						if(!psounds.getSounds().containsKey(customSound.getId()) && Math.random() <= customSound.getChance()) {
							
							boolean flag = false;
							
							long timeOfDay = player.getWorld().getTime();
							int[] time_spectre = customSound.getTime_spectre();
							if(time_spectre[0] < 0) flag = timeOfDay >= (time_spectre[0] + 24000) || timeOfDay <= time_spectre[1];
							else flag = timeOfDay > time_spectre[0] && timeOfDay <= time_spectre[1];
							if(flag) {
								
								boolean play = false;
							
								switch(customSound.getTrigger()) {
								case BIOME:
									
									String biome_name = player.getLocation().getBlock().getBiome().name();
									String biome_sound = customSound.getTrigger_data().split(";")[0];
									
									if(biome_sound.equalsIgnoreCase(biome_name)) play = true;
									
									break;
								case BLOCK:
									break;
									
								default:
									break;
								}
								
								if(play) {
									
									if(customSound.getRange() <= 0) {
										player.playSound(player.getLocation(), customSound.getId()
												, customSound.getVolume()[0] + ((float)Math.random() * (customSound.getVolume()[1] - customSound.getVolume()[0]))
												, customSound.getPitch()[0] + ((float)Math.random() * (customSound.getPitch()[1] - customSound.getPitch()[0])));
									}else {
										Location sound_location = player.getLocation().clone();
										sound_location.add(new Vector((Math.random() * 2 - 0.5) * customSound.getRange(), Math.random() * customSound.getRange() , (Math.random() * 2 - 0.5) * customSound.getRange()));
										player.playSound(sound_location, customSound.getId()
												, customSound.getVolume()[0] + ((float)Math.random() * (customSound.getVolume()[1] - customSound.getVolume()[0]))
												, customSound.getPitch()[0] + ((float)Math.random() * (customSound.getPitch()[1] - customSound.getPitch()[0])));
									}
									psounds.getSounds().put(customSound.getId(), 0);
									
								}
							}
							
						}else {
							if(psounds.getSounds().get(customSound.getId()) == null) {psounds.getSounds().remove(customSound.getId());
								psounds.getSounds().put(customSound.getId(),0);
							}
							psounds.getSounds().replace(customSound.getId(), psounds.getSounds().get(customSound.getId()) + (task_timer/20));
							
							if(psounds.getSounds().get(customSound.getId()) >= customSound.getLoop())psounds.getSounds().remove(customSound.getId());
						}
					}
				}
				
			}
			
		}.runTaskTimer(OdysseyPl.getOdysseyPlugin(), 0, task_timer);
		
	}
	
	@EventHandler()
	public static void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		if(player_sounds.containsKey(player.getUniqueId())) {
			player_sounds.get(player.getUniqueId()).getSounds().clear();
			player_sounds.remove(player.getUniqueId());
		}
	}
	
	

}
