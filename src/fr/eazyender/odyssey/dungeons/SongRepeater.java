package fr.eazyender.odyssey.dungeons;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SongRepeater extends BukkitRunnable {

	DungeonInstance instance;
	
	
	public SongRepeater(DungeonInstance instance) {
		this.instance = instance;
	}


	@Override
	public void run() {
		for(Player p : instance.getPlayers())
			p.playSound(p.getLocation(), instance.getDungeon().getSong().split("_")[0], 500f, 1f);
		
	}

}
