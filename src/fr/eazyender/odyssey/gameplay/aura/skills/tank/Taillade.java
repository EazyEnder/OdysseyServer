package fr.eazyender.odyssey.gameplay.aura.skills.tank;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.eazyender.odyssey.gameplay.aura.BasicSkill;
import fr.eazyender.odyssey.gameplay.aura.Skills;

public class Taillade extends BasicSkill {

	
	
	public Taillade(Player p) {
		super(p, 8000, "Taillade", 10);
	}


	@Override
	public boolean launch() {
		// Effect
		Bukkit.broadcastMessage("Lancé Taillade");
		
		Skills.combos.put(p, Taillade.class);
		return true;
	}

	
	
}
