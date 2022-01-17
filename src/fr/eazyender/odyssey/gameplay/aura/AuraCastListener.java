package fr.eazyender.odyssey.gameplay.aura;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.eazyender.odyssey.gameplay.items.ItemType;
import fr.eazyender.odyssey.gameplay.items.ItemUtils;

public class AuraCastListener implements Listener {
	
	public static ArrayList<Cast> casts = new ArrayList<>();
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getItem() != null && (ItemUtils.getType(e.getItem()) == ItemType.GUERRIER || ItemUtils.getType(e.getItem()) == ItemType.ARCHER || ItemUtils.getType(e.getItem()) == ItemType.TANK)) {
			
			if (e.getAction().name().contains("LEFT") || e.getAction().name().contains("RIGHT")) {
				Cast cast = getCast(e.getPlayer());
				if (cast != null) {
					
				} else {
					if (ItemUtils.getType(e.getItem()) == ItemType.GUERRIER && e.getAction().name().contains("LEFT")) return;
					if (ItemUtils.getType(e.getItem()) == ItemType.TANK && e.getAction().name().contains("LEFT")) return;
					if (ItemUtils.getType(e.getItem()) == ItemType.ARCHER && e.getAction().name().contains("RIGHT")) return;
					
					cast = new Cast(e.getPlayer(), ItemUtils.getType(e.getItem()));
					cast.setPattern("" + Cast.getClick(e.getAction()));
					cast.animate();
					
					
				}
			}
			
		}
	}
	
	public static Cast getCast(Player p) {
		for(Cast cast : casts) {
			if (cast.getPlayer() == p) return cast;
		}
		return null;
	}

}
