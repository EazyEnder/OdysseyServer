package fr.eazyender.odyssey.gameplay.stats;

import java.util.HashMap;

import org.bukkit.entity.Player;


public class CombatStats {
	
	public static HashMap<Player, CombatStats> stats = new HashMap<>();
	Player p;
	HashMap<Stat, Integer> combatStats = new HashMap<>();
	HashMap<Stat, Integer> multiplicators = new HashMap<>();
	
	
	public CombatStats(Player p) {
		this.p = p;
	}
	
	public int getStat(Stat stat) {
		if (combatStats.containsKey(stat)) {
			return combatStats.get(stat);
		} else {
			return updateStat(stat);
		}
	}
	
	public int updateStat(Stat stat) {
		int multiplicator = 1;
		if (multiplicators.containsKey(stat)) multiplicator = multiplicators.get(stat);
		combatStats.put(stat, PlayerStats.getStats(p).getStat(stat) * multiplicator);
		return combatStats.get(stat);
	}
	
	public static CombatStats getStats(Player p) {
		if (stats.containsKey(p))
			return stats.get(p);
		else {
			CombatStats cStats = new CombatStats(p);
			stats.put(p, cStats);
			return cStats;
		}
	}

	public HashMap<Stat, Integer> getCombatStats() {
		return combatStats;
	}

	public HashMap<Stat, Integer> getMultiplicators() {
		return multiplicators;
	}

	public static HashMap<Player, CombatStats> getStats() {
		return stats;
	}
	
	

}
