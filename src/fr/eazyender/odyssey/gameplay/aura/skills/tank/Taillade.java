package fr.eazyender.odyssey.gameplay.aura.skills.tank;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.eazyender.odyssey.gameplay.aura.BasicSkill;
import fr.eazyender.odyssey.gameplay.aura.Skills;

public class Taillade extends BasicSkill {

	
	
	public Taillade(Player p) {
		super(p, 10000, "Taillade");
	}


	@Override
	public boolean launch() {
		// Effect
		Bukkit.broadcastMessage("Lancé");
		
		Skills.combos.put(p, Taillade.class);
		return true;
	}

	
	
}
