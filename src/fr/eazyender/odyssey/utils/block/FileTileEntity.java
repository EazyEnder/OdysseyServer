package fr.eazyender.odyssey.utils.block;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.bukkit.Location;

import fr.eazyender.odyssey.OdysseyPl;


public class FileTileEntity {
	
	public static void loadFile() {
        File file = new File(OdysseyPl.getOdysseyPlugin().getDataFolder(), "tile_entity"+".txt");
        
        try {
        	Scanner reader = new Scanner(file.getAbsoluteFile());
        
        while(reader.hasNextLine()) {
        	String data = reader.nextLine();
        	data = data.split("\n")[0];
        	
        	switch(data.split(":")[0]) {
        	default :;
        	/**example : case "catalyseur": data = data.split(":")[1];
        		TileCatalyseur tile = TileCatalyseur.fromString(data);
        		BlockUtils.tile_entity.put(tile.getBlock_location(), tile);
        		break;*/
        	break;
        	}
        }
        
        reader.close();
        } catch (FileNotFoundException  e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
	
	public static void saveFile() {
        File file = new File(OdysseyPl.getOdysseyPlugin().getDataFolder(), "tile_entity"+".txt");
        try {
        FileWriter writer = new FileWriter(file.getAbsoluteFile());
        
        for (Location loc : BlockUtils.tile_entity.keySet()) {
			String str = BlockUtils.tile_entity.get(loc).toString() + "\n";
			writer.write(str);
		}
        
        writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
