package fr.eazyender.odyssey.dungeons;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.eazyender.odyssey.OdysseyPl;
import io.lumine.mythic.api.exceptions.InvalidMobTypeException;
import io.lumine.mythic.bukkit.MythicBukkit;
import net.md_5.bungee.api.ChatColor;

public class DungeonInstance implements Listener {

	ArrayList<Player> players;
	Dungeon dungeon;
	int id;

	ArrayList<Entity> mobs = new ArrayList<>();

	public DungeonInstance(ArrayList<Player> players, Dungeon dungeon, int id) {
		this.players = players;
		this.dungeon = dungeon;
		this.id = id;
		OdysseyPl.getOdysseyPlugin().getServer().getPluginManager().registerEvents(this, OdysseyPl.getOdysseyPlugin());
	}

	public void run() {

		spawnMobs();

		int pos = 0;
		for (Player p : players) {
			p.teleport(DungeonConfig.getStartLoc(dungeon.getId(), id, pos));
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 120, 999, true, false, false));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 999, true, false, false));
			p.playSound(p.getLocation(), dungeon.getSong().split("_")[0], 500f, 1f);
			pos++;
		}
		new BukkitRunnable() {
			int i = 5;

			@SuppressWarnings("deprecation")
			public void run() {
				for (Player p : players) {
					if (i == 5)
						p.sendTitle(ChatColor.of(dungeon.getName().split("_")[0]) + dungeon.getName().split("_")[1],
								null);
					else if (i != 4)
						p.sendTitle(ChatColor.of("#ff4800") + "" + i, null);
				}
				i--;
				if (i == 0)
					this.cancel();
			}
		}.runTaskTimer(OdysseyPl.getOdysseyPlugin(), 0, 20);

	}

	public void stop() {
		HandlerList.unregisterAll(this);
		for (Entity e : mobs)
			if (!e.isDead())
				e.remove();
	}

	public void spawnMobs() {
		for (Entry<Location, String> mob : dungeon.getMobs().entrySet()) {
			try {
				mobs.add(MythicBukkit.inst().getAPIHelper().spawnMythicMob(mob.getValue(), mob.getKey()));
			} catch (InvalidMobTypeException e) {
				e.printStackTrace();
			}
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if (players.contains(e.getPlayer())) {
			players.remove(e.getPlayer());
			if (players.size() == 0) {
				stop();
			}
		}
	}
	

}
