package fr.eazyender.odyssey.utils.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class WorldUtils {
	
	public static List<IWorld> worlds = new ArrayList<IWorld>();
	
	public static void initWorlds() {
		
		//MONDE TEST
		Map<String, Double> temp = new HashMap<String, Double>();
		temp.put("PLAINS", 10.0);
		List<String> tree_schem = new ArrayList<String>();
		worlds.add(new IWorld("Floor 0","floor0",4,tree_schem,null,temp));
		
		for (IWorld world : worlds) {
			World w = Bukkit.getWorld(world.getId());
		}
		
	}
	
	public static IWorld getWorldOfPlayer(Player player) {
		IWorld world = null;
		String id = player.getWorld().getName();
		for (IWorld w : WorldUtils.worlds) {
			if(world == null) {
				if(w.getId().equals(id)) {
					world = w;
				}
			}
		}
		return world;
	}
	
	@EventHandler
	public static void whenPlayerTeleport(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		IWorld world = getWorldOfPlayer(player);
		
		for (IWorldTeleporter tp : world.getTp()) {
			if(tp.getCenter_p().distance(player.getLocation()) <= tp.getSize()) {
				
				if(Math.abs(player.getLocation().getY() - tp.getCenter_p().getY()) < 1.5) {
					Location offset = player.getLocation().add(tp.getCenter_p().multiply(-1));
					Vector velocity = player.getVelocity();
					
					Location goal = new Location(Bukkit.getWorld(tp.getOut().getId()),0.0,0.0,0.0);
					goal = goal.add(tp.getCenter_o());
					goal = goal.add(offset);
					
					player.teleport(goal);
					player.setVelocity(velocity);
				}
				
			}
		}
		
	}

}
