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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.player.group.PlayerGroup;
import fr.eazyender.odyssey.utils.zone.IZone;
import fr.eazyender.odyssey.utils.zone.ZoneUtils;
import net.md_5.bungee.api.ChatColor;

public class CompassUtils implements Listener{
	
	public static Map<UUID, BossBar> compass_player = new HashMap<UUID, BossBar>();
	public static Map<UUID, BossBar> title_zone_player = new HashMap<UUID, BossBar>();
	protected static Map<UUID, Integer> compass_resolution = new HashMap<UUID, Integer>();
	
	private int refresh = 2;
	
	public CompassUtils() {
		
		new BukkitRunnable() {

			@Override
			public void run() {
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					
					if(!compass_resolution.containsKey(player.getUniqueId())) {
						compass_resolution.put(player.getUniqueId(), 18);
					}
					
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
		for (int i = -compass_resolution.get(player.getUniqueId())/2; i < compass_resolution.get(player.getUniqueId())/2; i++) {
			String point = ChatColor.of("#D8A97F") + " \u2219 ";
			boolean flag = true; 
			
			switch(Math.round(yaw + (i*(180/compass_resolution.get(player.getUniqueId()))))) {
			case -90 : point = ChatColor.of("#AB8662") + " §lE§r ";flag = false; break; 
			case -45 : point = ChatColor.of("#D8A97F") + "SE ";flag = false; break; 
			case 0 : point = ChatColor.of("#AB8662") + " §lS§r ";flag = false; break; 
			case 45 : point = ChatColor.of("#D8A97F") + " SW ";flag = false; break; 
			case 90 : point = ChatColor.of("#AB8662") + " §lW§r ";flag = false; break; 
			case 135 : point = ChatColor.of("#D8A97F") + " NW ";flag = false; break; 
			case 180 : point = ChatColor.of("#AB8662") + " §lN§r ";flag = false; break; 
			case 225 : point = ChatColor.of("#D8A97F") + " NE ";flag = false; break;
			case 270 : point = ChatColor.of("#AB8662") + " §lE§r ";flag = false; break; 
			case 315 : point = ChatColor.of("#D8A97F") + " SE ";flag = false; break; 
			case 360 : point = ChatColor.of("#AB8662") + " §lS§r ";flag = false; break; 
			case 360 + 45 : point = ChatColor.of("#D8A97F") + "SW ";flag = false; break; 
			case 360 + 90 : point = ChatColor.of("#AB8662") + " §lW§r ";flag = false; break; 
			}
			
			if(flag) {
				
				int rad = 500;
				List<IZone> zones = ZoneUtils.getZoneInRadius(player, null, rad);
				boolean flag2 = true;
				//Opti todo : if distance instant after loop
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
						if(angle % (180/compass_resolution.get(player.getUniqueId())) != 0) {
					    	double t = angle / (180/compass_resolution.get(player.getUniqueId()));
					    	angle = (180/compass_resolution.get(player.getUniqueId())) * Math.round(t);
					    }
						
						if(Math.round(yaw + (i*(180/compass_resolution.get(player.getUniqueId())))) == angle || angle-360==Math.round(yaw + (i*(180/compass_resolution.get(player.getUniqueId())))) || angle+360==Math.round(yaw + (i*(180/compass_resolution.get(player.getUniqueId()))))){
							if(player.getLocation().distance(zone.getCenter()) < rad/3)
							point = ZoneUtils.getLogoOfZone(zone.getType(),1);
							else if(player.getLocation().distance(zone.getCenter()) < rad/2)
								point = ZoneUtils.getLogoOfZone(zone.getType(),2);
							else if(player.getLocation().distance(zone.getCenter()) <= rad)
								point = ZoneUtils.getLogoOfZone(zone.getType(),3);
						}
						
					}
				}
				if(PlayerGroup.getGroup(player) !=null)
				for (Player p : PlayerGroup.getGroup(player).getPlayers()) {
					
						Vector vector_member = p.getLocation().toVector().clone().subtract(player.getLocation().toVector());
						vector_member.normalize();
						Vector vector_north = new Vector(0,0,-1).normalize();
						
						
						
						double scalar_product = vector_member.getX()*vector_north.getX()+vector_member.getZ()*vector_north.getZ();
						double costeta = scalar_product/(Math.sqrt((vector_member.getX()*vector_member.getX())+(vector_member.getZ()*vector_member.getZ())) * Math.sqrt((vector_north.getX()*vector_north.getX())+(vector_north.getZ()*vector_north.getZ())));
						double teta = Math.acos(costeta)/(Math.PI/180);
					
						if(p.getLocation().getX() < player.getLocation().getX()) {
							teta = -teta;
						}
						
						double angle = 180.0F + teta;
						if(angle % (180/compass_resolution.get(player.getUniqueId())) != 0) {
					    	double t = angle / (180/compass_resolution.get(player.getUniqueId()));
					    	angle = (180/compass_resolution.get(player.getUniqueId())) * Math.round(t);
					    }
						
						if(Math.round(yaw + (i*(180/compass_resolution.get(player.getUniqueId())))) == angle || angle-360==Math.round(yaw + (i*(180/compass_resolution.get(player.getUniqueId())))) || angle+360==Math.round(yaw + (i*(180/compass_resolution.get(player.getUniqueId()))))){
							if(player.getLocation().distance(p.getLocation()) < rad/3)
								point = "\uEfc5";
							else if(player.getLocation().distance(p.getLocation()) < rad/2)
								point = "\uEfc6";
							else if(player.getLocation().distance(p.getLocation()) <= rad)
								point = "\uEfc7";
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
	    
	    if(calcYaw % (180/compass_resolution.get(player.getUniqueId())) != 0) {
	    	double t = calcYaw / (180/compass_resolution.get(player.getUniqueId()));
	    	calcYaw = (180/compass_resolution.get(player.getUniqueId())) * Math.round(t);
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

