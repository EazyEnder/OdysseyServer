package fr.eazyender.odyssey.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class InteractionsManager implements Listener{
	
	@EventHandler(priority = EventPriority.LOWEST)
	public static void blockPlaceEvent(BlockPlaceEvent event) {
		if(!event.getPlayer().isOp()) event.setCancelled(true);
	}

}
