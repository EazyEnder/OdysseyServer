package fr.eazyender.odyssey.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.eazyender.odyssey.player.StatsEvent;


public class PlayerEvent implements Listener{
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) { 
		StatsEvent.initPlayerStats(e.getPlayer());
	}
	
	public static void reload(Player player) {
		StatsEvent.initPlayerStats(player);
	}

}
