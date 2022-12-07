package fr.eazyender.odyssey.gameplay.aura;

import java.util.HashMap;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.eazyender.odyssey.gameplay.masteries.Mastery;
import fr.eazyender.odyssey.gameplay.masteries.MasteryDB;
import fr.eazyender.odyssey.gameplay.stats.CombatStats;

public class BasicSkill implements Skill {

	public int cooldown;

	String id;
	public Player p;
	Mastery mastery;
	CombatStats stats;
	int auraCost;

	public BasicSkill(Player p, int cooldown, String id, int auraCost) {
		this.p = p;
		this.cooldown = cooldown;
		mastery = MasteryDB.getClass(p.getUniqueId().toString()).getMastery();
		stats = CombatStats.getStats(p);
		this.id = id;
		this.auraCost = auraCost;
	}

	public boolean canCast() {
		if (!Skills.cooldowns.containsKey(p) || !Skills.cooldowns.get(p).containsKey(this.getClass())
				|| System.currentTimeMillis() - Skills.cooldowns.get(p).get(this.getClass()) > getCooldown()) {
			HashMap<Class<? extends Skill>, Long> newCooldown = new HashMap<>();
			if (Skills.cooldowns.get(p) != null)
				newCooldown = Skills.cooldowns.get(p);
			newCooldown.put(this.getClass(), System.currentTimeMillis());
			Skills.cooldowns.put(p, newCooldown);
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


	public int getRemainingCooldown(LivingEntity p) {
		int cooldownSkill = getCooldown();
		long activeCooldown = Skills.cooldowns.get(p).get(this.getClass());
		return (int) ((cooldownSkill - (System.currentTimeMillis() - activeCooldown)) / 1000);
	}
	
	
	public ItemStack getItem() {
		return Skills.getSkillItem(id);
	}

	public Mastery getMastery() {
		return mastery;
	}

	public void setMastery(Mastery mastery) {
		this.mastery = mastery;
	}

	public CombatStats getStats() {
		return stats;
	}

	public void setStats(CombatStats stats) {
		this.stats = stats;
	}

	@Override
	public boolean launch() {
		
		return false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getAuraCost() {
		return auraCost;
	}

	

}
