package fr.eazyender.odyssey.gameplay.aura.skills;

import java.util.HashMap;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;


public interface SkillHitActivation {
	
	public static HashMap<LivingEntity, SkillHitActivation> skillsActivation = new HashMap<>();
	
	public void activate(EntityDamageByEntityEvent e, boolean isCrit);
	

}
