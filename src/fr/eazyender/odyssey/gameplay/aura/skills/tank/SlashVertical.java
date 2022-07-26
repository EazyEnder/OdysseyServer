package fr.eazyender.odyssey.gameplay.aura.skills.tank;

import java.util.Arrays;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import fr.eazyender.odyssey.gameplay.aura.Skill;
import fr.eazyender.odyssey.gameplay.aura.skills.SkillHitActivation;
import fr.eazyender.odyssey.gameplay.stats.DamageHelper;
import net.md_5.bungee.api.ChatColor;

public class SlashVertical extends Skill implements SkillHitActivation {

	final static int power = 100;
	
	public SlashVertical() {
		super(10000, 2, ChatColor.of("#ffb300") + "Slash Vertical", Arrays.asList(ChatColor.of("#bdb7ae") + "Effectue un slash vertical", 
				ChatColor.of("#bdb7ae") + "Puissance: " + ChatColor.of("#eaed47") + power, 
				ChatColor.of("#bdb7ae") + "Patterne: " + ChatColor.of("#eaed47") + "R-L-R"));
	}


	public boolean launch(Player p) {
			
		skillsActivation.put(p, this);
		combos.put(p, SlashVertical.class);

		return true;
	}

	@Override
	public void activate(EntityDamageByEntityEvent e) {
		Player p = (Player) e.getDamager();
		e.setDamage(DamageHelper.getAuraDamage(p, power, 1, DamageHelper.isCrit(p)));
		boolean isCrit = DamageHelper.isCrit(p);
		DamageHelper.animateDamage(p, (LivingEntity) e.getEntity(), DamageHelper.getAuraDamage(p, power, 1, isCrit), isCrit);
		
	}
	
	
}
