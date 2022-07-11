package fr.eazyender.odyssey.gameplay.city.building;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import fr.eazyender.odyssey.OdysseyPl;

public class BuildManager {

	public static List<IBuild> builds = new ArrayList<IBuild>();
	public static List<IDynamicBuild> dynamic_builds = new ArrayList<IDynamicBuild>();
	
	public static void initBuildManager()
	{
		List<Double> shop_work_time = new ArrayList<Double>();
		shop_work_time.add(20.0);
		shop_work_time.add(10.0);
		builds.add(new IBuild("Magasin","village/shop","plain",new double[]{17,7,21},"t",new ArrayList<List<ItemStack>>(),shop_work_time));
	}
	
	public static void initLoopDynamicBuild() {
		
		new BukkitRunnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
			
		}.runTaskTimer(OdysseyPl.getOdysseyPlugin(), 0, 20);
		
	}
}
