package fr.eazyender.odyssey.dungeons;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class DungeonInstance {

	ArrayList<Player> players;
	String dungeonId;
	int id;

	public DungeonInstance(ArrayList<Player> players, String dungeonId, int id) {
		this.players = players;
		this.dungeonId = dungeonId;
		this.id = id;
	}

	public void run() {
		int pos = 0;
		for (Player p : players) {
			p.teleport(DungeonConfig.getStartLoc(dungeonId, id, pos));
			pos++;
		}
	}

}
