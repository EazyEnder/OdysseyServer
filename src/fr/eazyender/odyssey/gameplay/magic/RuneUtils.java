package fr.eazyender.odyssey.gameplay.magic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.gameplay.magic.spells.ISpell;
import fr.eazyender.odyssey.gameplay.magic.spells.SpellTest;
import fr.eazyender.odyssey.utils.maths.ISphericPosition;

public class RuneUtils {

	public static List<IRune> runes = new ArrayList<IRune>();
	
	public static void initRunes() {
		
		List<ISphericPosition> rune_firebolt_pos = new CopyOnWriteArrayList<ISphericPosition>();
		rune_firebolt_pos.add(new ISphericPosition(0.04132581443595394,7.309022175904876,1.0));
		rune_firebolt_pos.add(new ISphericPosition(0.04859746760149602,1.626974112093464,1.0));
		rune_firebolt_pos.add(new ISphericPosition(0.07041243125931144,2.089946558108327,1.0));
		rune_firebolt_pos.add(new ISphericPosition(0.05344524053971327,1.8305844953388182,1.0));
		rune_firebolt_pos.add(new ISphericPosition(0.05344524053971327,7.614424967599676,1.0));
		rune_firebolt_pos.add(new ISphericPosition(-0.08956394670499261,1.9517519333508073,1.0));
		rune_firebolt_pos.add(new ISphericPosition(-0.3004420549532809,1.6487225508194872,1.0));
		runes.add(new IRune("Boule de feu",3,new SpellTest(),rune_firebolt_pos,0.2,0.2));
		
	}
	
	public static void makeRune(List<ISphericPosition> vector) {
		Random rdm = new Random();
        File file = new File(OdysseyPl.getOdysseyPlugin().getDataFolder(), "locations_" + vector.size() + "_" + rdm.nextInt() + ".txt");
        try {
        FileWriter writer = new FileWriter(file.getAbsoluteFile());
        
        for (ISphericPosition pos : vector) {
			String str = "rune_firebolt_pos.add(new ISphericPosition("+pos.getPhi() + "," + pos.getTeta() + "," + pos.getRayon() + "));" + "\n";
			writer.write(str);
		}
        
        writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
	}
	
	public static ISpell getSpell(int element, List<ISphericPosition> pos) {
		for (IRune rune : runes) {
			if(element==rune.getElement()) {
				if(rune.verifyRune(pos)) {
					return rune.getSpell();
				}
			}
		}
		return null;
	}
	
}
