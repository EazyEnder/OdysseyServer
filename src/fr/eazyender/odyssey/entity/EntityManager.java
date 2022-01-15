package fr.eazyender.odyssey.entity;

import org.bukkit.scheduler.BukkitRunnable;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.entity.projectiles.ProjectilesManager;

public class EntityManager {
	
	private static int period = 2;
	
	//Refresh all customs entities
	public static void initLoop(int i) {
		
		period = i;
		
		 new BukkitRunnable(){

			@Override
			public void run() {
				
				ProjectilesManager.refreshProjectiles();
				
			}
		 }.runTaskTimer(OdysseyPl.getOdysseyPlugin(), 0, period);
		
	}

	public static int getPeriod() {
		return period;
	}

	public static void setPeriod(int period) {
		EntityManager.period = period;
	}

}
