package fr.eazyender.odyssey.gameplay.aura;

import java.util.HashMap;

import org.bukkit.entity.LivingEntity;

public class Skill {
	
	public static HashMap<String, Skill> patterns = new HashMap<>();
	
    public static HashMap<LivingEntity, HashMap<Class<? extends Skill>, Long>> cooldowns = new HashMap<>();
    

    public int cooldown;
    public String pattern;

    public Skill(String pattern, int cooldown) {
        this.cooldown = cooldown;
        this.pattern = pattern;
        patterns.put(pattern, this);
    }

    public boolean launch(LivingEntity p, Class<? extends Skill> spell) {
        if (!cooldowns.containsKey(p) || !cooldowns.get(p).containsKey(spell)
                || System.currentTimeMillis() - cooldowns.get(p).get(spell) > getCooldown()) {
            HashMap<Class<? extends Skill>, Long> newCooldown = new HashMap<>();
            if (cooldowns.get(p) != null)
                newCooldown = cooldowns.get(p);
            newCooldown.put(spell, System.currentTimeMillis());
            cooldowns.put(p, newCooldown);
            return true;
        } else
            return false;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    
    public static int getRemainingCooldown(LivingEntity p, Skill skill) {
        int cooldownSkill = skill.getCooldown();
        long activeCooldown = cooldowns.get(p).get(skill.getClass());
        return (int) ((cooldownSkill - (System.currentTimeMillis() - activeCooldown)) / 1000);
    }

    public static Skill getSkill(String pattern) {
    	return patterns.get(pattern);
    }
  

	
}
