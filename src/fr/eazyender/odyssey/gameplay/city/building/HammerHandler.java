package fr.eazyender.odyssey.gameplay.city.building;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.eazyender.odyssey.utils.ItemTools;
import fr.eazyender.odyssey.utils.TextUtils;

public class HammerHandler implements Listener{
	
	public static Map<UUID, IBuildingSettings> settings = new HashMap<UUID, IBuildingSettings>();
	public static Map<UUID, IBuildUnderConstruction> in_works = new HashMap<UUID, IBuildUnderConstruction>();
	
	
	@EventHandler
	public void onHammerUsed(PlayerInteractEvent event) {
		
		Player player = event.getPlayer();
		
		if(player.getInventory().getItemInMainHand().isSimilar(ItemTools.getHammer())){
			
			//Right click = set Position
			if(!player.isSneaking() && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) {
				if(!settings.containsKey(player.getUniqueId()))settings.put(player.getUniqueId(), new IBuildingSettings(player.getUniqueId(), player.getLocation().toVector().clone(), 0, null));
				
				settings.get(player.getUniqueId()).refreshSettings(0, BuildManager.builds.get(0), true);
			}
			//Left click = Rotate
			else if(!player.isSneaking() && (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR)) {
				if(settings.containsKey(player.getUniqueId())) {
					settings.get(player.getUniqueId()).refreshSettings((settings.get(player.getUniqueId()).getRotationY() + 90) % 360, null, false);
				}else {
					player.sendMessage(TextUtils.aide + "Vous ne pouvez pas faire la rotation d'un bâtiment (Aucun bâtiment en cours de placement).");
				}
			}
			
			//RIGHT CLICK = Place
			else if(player.isSneaking() && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) {
				if(settings.containsKey(player.getUniqueId())) {
					settings.get(player.getUniqueId()).beginConstruction();
					settings.get(player.getUniqueId()).task.cancel();
					settings.remove(player.getUniqueId());
				}else {
					player.sendMessage(TextUtils.aide + "Vous ne pouvez pas placer un bâtiment (Aucun bâtiment en cours de placement).");
				}
			}
			
			event.setCancelled(true);
			
		}
		
	}
	

}
