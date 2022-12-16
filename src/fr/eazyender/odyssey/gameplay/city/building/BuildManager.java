package fr.eazyender.odyssey.gameplay.city.building;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.gameplay.city.building.objects.BOContainer;
import fr.eazyender.odyssey.gameplay.city.building.objects.BOLFurnace;
import fr.eazyender.odyssey.gameplay.city.building.objects.BOWorkBench;

public class BuildManager {

	public static List<IBuild> builds = new ArrayList<IBuild>();
	public static List<IDynamicBuild> dynamic_builds = new ArrayList<IDynamicBuild>();
	
	public static void initBuildManager()
	{
		List<Double> shop_work_time = new ArrayList<Double>();
		shop_work_time.add(20.0);
		shop_work_time.add(10.0);
		builds.add(new IBuild("Magasin","village/shop","plain",new double[]{17,7,21},"METIERS",new ArrayList<List<ItemStack>>(),shop_work_time));
		
		loadFile();
		
		OdysseyPl.getOdysseyPlugin().getServer().getPluginManager().registerEvents(new BOContainer(), OdysseyPl.getOdysseyPlugin());
		OdysseyPl.getOdysseyPlugin().getServer().getPluginManager().registerEvents(new BOLFurnace(), OdysseyPl.getOdysseyPlugin());
		OdysseyPl.getOdysseyPlugin().getServer().getPluginManager().registerEvents(new BOWorkBench(), OdysseyPl.getOdysseyPlugin());
		
		initLoopDynamicBuild();
	}
	
	public static IBuild getBuildByName(String name) {
		for (IBuild build : builds) {
			if(build.getName().equalsIgnoreCase(name))return build;
		}
		return null;
	}
	
	public static List<IDynamicBuild> getDynamicBuild(UUID player) {
		
		List<IDynamicBuild> dbuilds = new ArrayList<IDynamicBuild>();
		
		for (IDynamicBuild dbuild : dynamic_builds) {
			if(dbuild.getOwner().compareTo(player) == 0) dbuilds.add(dbuild);
		}
		
		return dbuilds;
	}
	
	public static IDynamicBuild getDynamicBuild(World world, Vector pos) {
		
		for (IDynamicBuild dbuild : dynamic_builds) {
			IBuild build = dbuild.getBuild();
			BoundingBox area = new BoundingBox(dbuild.getPos().getX(),dbuild.getPos().getY() - 1,dbuild.getPos().getZ(),
					dbuild.getPos().getX() + ((build.getSize()[0]) * Math.cos(dbuild.getRotationY() * (Math.PI/180)) + (build.getSize()[2]) * -Math.sin(dbuild.getRotationY()* (Math.PI/180) )),
					dbuild.getPos().getY() + build.getSize()[1],
					dbuild.getPos().getZ() + (Math.sin(dbuild.getRotationY()* (Math.PI/180)) * (build.getSize()[0]) + Math.cos(dbuild.getRotationY()* (Math.PI/180)) * (build.getSize()[2])) );
			
			if(area.contains(pos)) {return dbuild;}
			
		}
		
		
		return null;
	}
	
	public static IBuildObject getObject(IDynamicBuild dbuild,Vector pos, double range) {
		
		for (IBuildObject object : dbuild.getObjects()) {
			if(object.getPos().distance(pos) <= range) return object;
		}
		
		return null;
	}
	
	public static void loadFile() {
        File file = new File(OdysseyPl.getOdysseyPlugin().getDataFolder(), "/builds"+".txt");
        
        try {
        	Scanner reader = new Scanner(file.getAbsoluteFile());
        
        while(reader.hasNextLine()) {
        	String data = reader.nextLine();
        	data = data.split("\n")[0];
        	
        	dynamic_builds.add(IDynamicBuild.fromString(data));
        }
        
        reader.close();
        } catch (FileNotFoundException  e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
	
	public static void saveFile() {
        File file = new File(OdysseyPl.getOdysseyPlugin().getDataFolder(), "/builds"+".txt");
        
        //file.delete();
        
        try {
        FileWriter writer = new FileWriter(file.getAbsoluteFile());
        
        for (IDynamicBuild build : dynamic_builds) {
			String str = build.toString() + "\n";
			writer.write(str);
		}
        
        writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
	
	public static void initLoopDynamicBuild() {
		
		new BukkitRunnable() {

			@Override
			public void run() {
				
				for (IDynamicBuild dbuild : dynamic_builds) {
					dbuild.loop();
				}
				
			}
			
		}.runTaskTimer(OdysseyPl.getOdysseyPlugin(), 0, 20);
		
	}
}
