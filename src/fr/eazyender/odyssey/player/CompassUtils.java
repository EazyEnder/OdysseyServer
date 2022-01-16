package fr.eazyender.odyssey.player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.utils.zone.IZone;
import fr.eazyender.odyssey.utils.zone.ZoneUtils;

public class CompassUtils implements Listener{
	
	public static Map<UUID, BossBar> compass_player = new HashMap<UUID, BossBar>();
	public static Map<UUID, BossBar> title_zone_player = new HashMap<UUID, BossBar>();
	
	private static int resolution = 18;
	private int refresh = 2;
	
	public CompassUtils() {
		
		new BukkitRunnable() {

			@Override
			public void run() {
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					if(!compass_player.containsKey(player.getUniqueId())) {
						BossBar bossBar = Bukkit.createBossBar("----", BarColor.PURPLE, BarStyle.SOLID);
						bossBar.addPlayer(player);
						bossBar.setProgress((double)(0));
						bossBar.setVisible(true);
						compass_player.put(player.getUniqueId(), bossBar);
					}
					if(!title_zone_player.containsKey(player.getUniqueId())) {
						BossBar bossBar = Bukkit.createBossBar("----", BarColor.PURPLE, BarStyle.SOLID);
						bossBar.addPlayer(player);
						bossBar.setProgress((double)(0));
						bossBar.setVisible(true);
						title_zone_player.put(player.getUniqueId(), bossBar);
					}
					
					BossBar compass = compass_player.get(player.getUniqueId());
					compass.setTitle(getCompassString(player));
					compass_player.replace(player.getUniqueId(), compass);
					
					BossBar title_zone = title_zone_player.get(player.getUniqueId());
					IZone pzone = ZoneUtils.getZoneOfPlayer(player);
					if(pzone != null) {title_zone.setVisible(true);
					title_zone.setTitle(ZoneUtils.getImgOfZone(pzone.getType()) + " §f§l" + pzone.getName());}
					else {title_zone.setVisible(false);}
					title_zone_player.replace(player.getUniqueId(), title_zone);
					
				}
				
			}
			
		}.runTaskTimer(OdysseyPl.getOdysseyPlugin(), 0, refresh);
		
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) { 
		Player player = e.getPlayer();
		if(!compass_player.containsKey(player.getUniqueId())) {
			BossBar bossBar = Bukkit.createBossBar("----", BarColor.PURPLE, BarStyle.SOLID);
			bossBar.addPlayer(player);
			bossBar.setProgress((double)(0));
			bossBar.setVisible(true);
			compass_player.put(player.getUniqueId(), bossBar);
		}
		if(!title_zone_player.containsKey(player.getUniqueId())) {
			BossBar bossBar = Bukkit.createBossBar("----", BarColor.PURPLE, BarStyle.SOLID);
			bossBar.addPlayer(player);
			bossBar.setProgress((double)(0));
			bossBar.setVisible(true);
			title_zone_player.put(player.getUniqueId(), bossBar);
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) { 
		Player player = e.getPlayer();
		if(compass_player.containsKey(player.getUniqueId())) {
			compass_player.remove(player.getUniqueId());
		}
		if(title_zone_player.containsKey(player.getUniqueId())) {
			title_zone_player.remove(player.getUniqueId());
		}
	}
	
	public static String getCompassString(Player player) {
		String compass = "\uEfb1";
		float yaw = calcYaw(player);
		for (int i = -resolution/2; i < resolution/2; i++) {
			String point = " \u2219 ";
			boolean flag = true; 
			
			switch(Math.round(yaw + (i*(180/resolution)))) {
			case -90 : point = " §lE§r ";flag = false; break; 
			case -45 : point = "SE ";flag = false; break; 
			case 0 : point = " §lS§r ";flag = false; break; 
			case 45 : point = "SW ";flag = false; break; 
			case 90 : point = " §lW§r ";flag = false; break; 
			case 135 : point = "NW ";flag = false; break; 
			case 180 : point = " §lN§r ";flag = false; break; 
			case 225 : point = "NE ";flag = false; break;
			case 270 : point = " §lE§r ";flag = false; break; 
			case 315 : point = "SE ";flag = false; break; 
			case 360 : point = " §lS§r ";flag = false; break; 
			case 360 + 45 : point = "SW ";flag = false; break; 
			case 360 + 90 : point = " §lW§r ";flag = false; break; 
			}
			
			if(flag) {
				
				int rad = 100;
				List<IZone> zones = ZoneUtils.getZoneInRadius(player, null, rad);
				boolean flag2 = true;
				for (IZone zone : zones) {
					if(flag2) {
					
						Vector vector_zone = zone.getCenter().toVector().clone().subtract(player.getLocation().toVector());
						vector_zone.normalize();
						Vector vector_north = new Vector(0,0,-1).normalize();
						
						
						
						double scalar_product = vector_zone.getX()*vector_north.getX()+vector_zone.getZ()*vector_north.getZ();
						double costeta = scalar_product/(Math.sqrt((vector_zone.getX()*vector_zone.getX())+(vector_zone.getZ()*vector_zone.getZ())) * Math.sqrt((vector_north.getX()*vector_north.getX())+(vector_north.getZ()*vector_north.getZ())));
						double teta = Math.acos(costeta)/(Math.PI/180);
					
						if(zone.getCenter().getX() < player.getLocation().getX()) {
							teta = -teta;
						}
						
						double angle = 180.0F + teta;
						if(angle % (180/resolution) != 0) {
					    	double t = angle / (180/resolution);
					    	angle = (180/resolution) * Math.round(t);
					    }
						
						if(Math.round(yaw + (i*(180/resolution))) == angle || angle-360==Math.round(yaw + (i*(180/resolution))) || angle+360==Math.round(yaw + (i*(180/resolution)))){
							if(player.getLocation().distance(zone.getCenter()) < rad/3)
							point = ZoneUtils.getLogoOfZone(zone.getType(),1);
							else if(player.getLocation().distance(zone.getCenter()) < rad/2)
								point = ZoneUtils.getLogoOfZone(zone.getType(),2);
							else if(player.getLocation().distance(zone.getCenter()) <= rad)
								point = ZoneUtils.getLogoOfZone(zone.getType(),3);
						}
						
					}
				}
				
				
			}
			
			compass += point;
			
		}
		
		return compass;
	}
	
	public static float calcYaw(Player player)
	  {
	    Location playerLoc = player.getLocation();
	    float calcYaw = playerLoc.getYaw();

	    if (calcYaw < 0.0F) {
	    	calcYaw += 360.0F;
	    }else if(calcYaw > 360F) {
	    	calcYaw -= 360.0F;
	    }
	    
	    if(calcYaw % (180/resolution) != 0) {
	    	double t = calcYaw / (180/resolution);
	    	calcYaw = (180/resolution) * Math.round(t);
	    }
	    
	    return calcYaw;
	  }

	public static void setVisibleFalse() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			compass_player.get(player.getUniqueId()).setVisible(false);
			title_zone_player.get(player.getUniqueId()).setVisible(false);
		}
	}
	
}
