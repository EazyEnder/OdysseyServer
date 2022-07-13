package fr.eazyender.odyssey.gameplay.city.building.objects;

import java.util.UUID;

import org.bukkit.block.BlockState;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.util.Vector;

import fr.eazyender.odyssey.gameplay.city.building.BuildManager;
import fr.eazyender.odyssey.gameplay.city.building.IBuildObject;
import fr.eazyender.odyssey.gameplay.city.building.IDynamicBuild;

public class BuildObjectListener implements Listener{
	
	@EventHandler
	public static void onPlayerInteractWithBObjects(PlayerInteractAtEntityEvent event) {
		
		Player player = event.getPlayer();
		if(event.getRightClicked() instanceof ArmorStand) {
			event.setCancelled(true);
			
			IDynamicBuild dbuild = BuildManager.getDynamicBuild(player.getWorld(), event.getRightClicked().getLocation().toVector());
			if(dbuild != null) {
				
				IBuildObject object = BuildManager.getObject(dbuild, event.getRightClicked().getLocation().toVector(), 1);
				if(object != null && (player.getUniqueId().compareTo(object.getOwner()) == 0 || player.getUniqueId().compareTo(dbuild.getOwner()) == 0)) {
					
					object.trigger(player);
				}
			}		
		}
		
	}
	
	public static IBuildObject convertBlockToObjects(BlockState data,IDynamicBuild db, UUID owner, Vector pos) {
		
		
		switch(data.getType()) {
		case CHEST:
			return new BOContainer(db,owner,pos);
		default: return null;
		}
		
	}

}
