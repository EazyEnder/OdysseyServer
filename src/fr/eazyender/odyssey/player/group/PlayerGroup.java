package fr.eazyender.odyssey.player.group;

import java.util.ArrayList;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PlayerGroup {

	public static ArrayList<PlayerGroup> groups = new ArrayList<>();

	Player host;
	ArrayList<Player> members = new ArrayList<Player>();

	public PlayerGroup(Player host) {
		this.host = host;
		groups.add(this);
	}

	public PlayerGroup(Player host, ArrayList<Player> members) {
		this.host = host;
		this.members = members;
	}

	public Player getHost() {
		return host;
	}

	public ArrayList<Player> getMembers() {
		return members;
	}

	public ArrayList<Player> getPlayers() {
		ArrayList<Player> players = new ArrayList<>();
		players.addAll(members);
		players.add(host);
		return players;
	}

	public static PlayerGroup getGroup(Player p) {
		for (PlayerGroup group : groups) {
			if (group.getPlayers().contains(p))
				return group;
		}
		return null;
	}

	public void leave(Player p) {
		if (getHost() == p) {
			dissolve();
			return;
		} else {
			members.remove(p);
			for (Player player : getPlayers()) {
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
				player.sendMessage(CommandGroup.srv_msg + "Le joueur " + p.getName() + "  a quitté le groupe.");
			}
		}

	}

	public void dissolve() {
		for (Player member : getMembers()) {
			member.playSound(member.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			member.sendMessage(CommandGroup.srv_msg + "Le chef a quitté le groupe, vous avez donc été exclu.");
		}
		PlayerGroup.groups.remove(this);
	}

}
