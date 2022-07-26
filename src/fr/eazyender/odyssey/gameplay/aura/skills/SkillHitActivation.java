package fr.eazyender.odyssey.gameplay.aura.skills;

import java.util.HashMap;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;


public interface SkillHitActivation {
	
	public static HashMap<LivingEntity, SkillHitActivation> skillsActivation = new HashMap<>();
	
	public void activate(EntityDamageByEntityEvent e);
	
	public class ActivationListener implements Listener {
		
		@EventHandler
		public void onDamage(EntityDamageByEntityEvent e) {
			if (e.getDamager() instanceof Player) {
				Player p = (Player) e.getDamager();
				if (skillsActivation.containsKey(p)) {
					skillsActivation.get(p).activate(e);
					skillsActivation.remove(p);
				}
			}
			
		}
	}

}
