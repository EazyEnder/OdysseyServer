package fr.eazyender.odyssey.gameplay.aura.skills.tank;

import java.util.Arrays;

import org.bukkit.entity.LivingEntity;

import fr.eazyender.odyssey.gameplay.aura.Skill;
import net.md_5.bungee.api.ChatColor;

public class SlashVertical extends Skill {

	
	public SlashVertical() {
		super(2500, 2, ChatColor.of("#ffb300") + "Slash Vertical", Arrays.asList(ChatColor.of("#bdb7ae") + "Effectue un slash vertical", 
				ChatColor.of("#bdb7ae") + "Puissance: " + ChatColor.of("#eaed47") + "100", 
				ChatColor.of("#bdb7ae") + "Patterne: " + ChatColor.of("#eaed47") + "R-L-R"));
	}

	@Override
	public boolean launch(LivingEntity p, Class<? extends Skill> skill) {
		if (super.launch(p, SlashVertical.class)) {
			//blabla
		}
		return true;
	}
	
}
