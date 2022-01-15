package fr.eazyender.odyssey.gameplay.magic.spells;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class ISpell {
	
    public static HashMap<LivingEntity, HashMap<Class<? extends ISpell>, Long>> cooldowns = new HashMap<>();

    public int cooldown;

    public ISpell(int cooldown) {
        this.cooldown = cooldown;
    }

    public boolean launch(LivingEntity p, Class<? extends ISpell> spell) {
        if (!cooldowns.containsKey(p) || !cooldowns.get(p).containsKey(spell)
                || System.currentTimeMillis() - cooldowns.get(p).get(spell) > getCooldown()) {
            HashMap<Class<? extends ISpell>, Long> newCooldown = new HashMap<>();
            if (cooldowns.get(p) != null)
                newCooldown = cooldowns.get(p);
            newCooldown.put(spell, System.currentTimeMillis());
            cooldowns.put(p, newCooldown);
            return true;
        } else
            return false;
    }
    
    public void draw(Location position, Vector velocity, double time) {}
    
    public void trigger(Location position, Object tr) {}

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    
    public static int getRemainingCooldown(LivingEntity p, ISpell spell) {
        int cooldownSkill = spell.getCooldown();
        long activeCooldown = cooldowns.get(p).get(spell.getClass());
        return (int) ((cooldownSkill - (System.currentTimeMillis() - activeCooldown)) / 1000);
    }
	
	

}
