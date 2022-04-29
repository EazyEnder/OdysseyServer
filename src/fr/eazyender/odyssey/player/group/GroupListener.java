package fr.eazyender.odyssey.player.group;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class GroupListener implements Listener {

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (PlayerGroup.getGroup(p) != null) {
			PlayerGroup group = PlayerGroup.getGroup(p);
			if (group.getHost() == p) {
				group.dissolve();
			} else {
				group.leave(p);
			}

		}

	}
}
