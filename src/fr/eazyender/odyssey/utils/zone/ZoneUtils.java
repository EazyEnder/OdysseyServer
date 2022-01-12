package fr.eazyender.odyssey.utils.zone;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class ZoneUtils implements Listener{
	
	public static List<IZone> zones_map = new ArrayList<IZone>();
	
	public static void initZoneUtils() {
		
		/*ADD ZONES OF THE WORLD**/
		zones_map.add(new IZoneCircle("Test","Mine",new Location(Bukkit.getWorld("world"),19712.39, 4.00, 20106.44),10));
		
		
	}
	
	public static String getLogoOfZone(String type,int size) {	
		String logo ="X";
		
		switch(type) {
		case "Quest":if(size==1)logo="\uEfa1";else if(size==2)logo="\uEfa2";else if(size==3)logo="\uEfa3";break;
		case "Mine":if(size==1)logo="\uEfc1";else if(size==2)logo="\uEfc2";else if(size==3)logo="\uEfc3";break;
		}
		
		return logo;
	}
	
	public static String getImgOfZone(String type) {	
		String logo ="";
		
		if(type != null) {
		switch(type) {
		case "Mine":logo="\uEfc4";break;
		}
		}else {logo = "";}
		
		return logo;
	}
	
	@EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
		
		Player player = e.getPlayer();
		
		IZone zone = getZoneOfPlayer(player);
		
	}
	
	public static List<IZone> getZoneInRadius(Player player, String type, int radius){
		List<IZone> zones = new ArrayList<IZone>();
		
		Location p_loc = player.getLocation();
		IZone playerzone = getZoneOfPlayer(player);
		
		for (IZone zone : zones_map) {
			if(zone != playerzone) {
				if(zone.getCenter().distance(p_loc) <= radius) {
					if(type != null) {if(zone.getType()==type) {zones.add(zone);}}
					else {zones.add(zone);}
				}
			}
		}
		
		return zones;
	}
	
	public static IZone getZoneOfPlayer(Player player) {
		boolean flag = true;
		IZone playerzone = null;
		for (IZone zone : zones_map) {
			
			if(flag && zone instanceof IZoneSquare) {
				IZoneSquare zone_s = (IZoneSquare) zone;
				if(player.getLocation().getX() >= zone_s.getPosition()[0].getX()
						&& player.getLocation().getY() >= zone_s.getPosition()[0].getY()
						&& player.getLocation().getZ() >= zone_s.getPosition()[0].getZ()
						&& player.getLocation().getX() <= zone_s.getPosition()[1].getX()
						&& player.getLocation().getY() <= zone_s.getPosition()[1].getY()
						&& player.getLocation().getZ() <= zone_s.getPosition()[1].getZ()) {
					flag = false;
					playerzone = zone_s;
				}	
			}
			
			if(flag && zone instanceof IZoneCircle) {
				IZoneCircle zone_c = (IZoneCircle) zone;
				if(player.getLocation().distance(zone_c.getCenter()) <= zone_c.getRadius()) {
					if(playerzone != null && playerzone instanceof IZoneCircle) {
						IZoneCircle zone_c2 = (IZoneCircle) playerzone;
						if(player.getLocation().distance(zone_c.getCenter()) < player.getLocation().distance(zone_c2.getCenter())) {
							playerzone = zone_c;
						}
					}else {
					playerzone = zone_c;
					}
				}	
			}
			
		}
		
		return playerzone;
	}

}
