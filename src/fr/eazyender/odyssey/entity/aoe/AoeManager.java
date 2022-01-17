package fr.eazyender.odyssey.entity.aoe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import fr.eazyender.odyssey.gameplay.magic.spells.ISpell;

public class AoeManager {
	
public static HashMap<ISpell,IAoe> aoes = new HashMap<ISpell,IAoe>();
	
	public static void refreshAoes() {
		List<ISpell> spells_to_remove = new ArrayList<ISpell>();
		for (ISpell spell : aoes.keySet()) {
			IAoe aoe = aoes.get(spell);
			if(aoe.updateAoe()) {
				spells_to_remove.add(spell);
			}
		}
		for (ISpell s : spells_to_remove) {
			aoes.remove(s);
		}
	}
	
	public static void createAoe(ISpell spell, Player sender, Location position, double size, Vector force, Vector velocity, Double decay_timer, AOEProps.Source source, Double[] source_info, AOEProps.ExtendedType type, AOEProps.Shape shape) {
		IAoe aoe = new IAoe(spell, sender,position,size,force,velocity,decay_timer,source,source_info[0], source_info[1], type, shape);
		
		aoes.put(spell, aoe);
	}

}
