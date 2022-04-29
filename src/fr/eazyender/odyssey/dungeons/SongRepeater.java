package fr.eazyender.odyssey.dungeons;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SongRepeater extends BukkitRunnable {

	ArrayList<Player> players;
	Dungeon dungeon;
	
	
	public SongRepeater(ArrayList<Player> players, Dungeon dungeon) {
		this.players = players;
		this.dungeon = dungeon;
	}


	@Override
	public void run() {
		for(Player p : players)
			p.playSound(p.getLocation(), dungeon.getSong().split("_")[0], 500f, 1f);
		
	}

}
