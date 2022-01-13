package fr.eazyender.odyssey.gameplay.stats;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.eazyender.odyssey.utils.NBTEditor;


public class PlayerStats {
	
	public static HashMap<Player, PlayerStats> stats = new HashMap<>();

	
	Player p;
	HashMap<Stat, Integer> playerStats = new HashMap<>();
	

	public PlayerStats(Player p) {
		this.p = p;
	}
	
	public int getStat(Stat stat) {
		if (playerStats.containsKey(stat)) {
			return playerStats.get(stat);
		} else {
			return updateStat(stat);
		}
	}


	public int updateStat(Stat stat) {
		int sum = 0;
		for(ItemStack armorPiece : p.getInventory().getArmorContents()) {
			//sum += NBTEditor.getInt(armorPiece, stat.name());
		}
		//sum += NBTEditor.getInt(p.getInventory().getItemInMainHand(), stat.name());
		
		// Adding default values to sum
		if (stat == Stat.DAMAGE) sum += 1;
		if (stat == Stat.POWER) sum += 1;
		if (stat == Stat.HEALTH) sum += 20;
		if (stat == Stat.MP) sum += 100;
		if (stat == Stat.REGENMP) sum += 25;
		
		
		
		
		
		playerStats.put(stat, sum);
		CombatStats.getStats(p).updateStat(stat);
		return sum;
	}

	public void updateStats() {
		for(Stat stat : Stat.values()) {
			updateStat(stat);
		}
	}


	public static PlayerStats getStats(Player p) {
		if (stats.containsKey(p))
			return stats.get(p);
		else {
			PlayerStats pStats = new PlayerStats(p);
			stats.put(p, pStats);
			return pStats;
		}
	}

	public static HashMap<Player, PlayerStats> getStats() {
		return stats;
	}


	public Player getPlayer() {
		return p;
	}


	public HashMap<Stat, Integer> getPlayerStats() {
		return playerStats;
	}

	
	
	
	
	
}
