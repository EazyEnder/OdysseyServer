package fr.eazyender.odyssey.dungeons;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.bukkit.scheduler.BukkitTask;

import fr.eazyender.odyssey.OdysseyPl;
import io.lumine.mythic.api.exceptions.InvalidMobTypeException;
import io.lumine.mythic.bukkit.MythicBukkit;
import net.md_5.bungee.api.ChatColor;
import nl.pim16aap2.bigDoors.BigDoors;
import nl.pim16aap2.bigDoors.Door;

public class DungeonInstance implements Listener {

	HashMap<Player, Location> previousLoc = new HashMap<>();
	
	ArrayList<Player> players;
	Dungeon dungeon;
	int id;
	BukkitTask songRepeater;
	

	HashMap<String, Door> doors = new HashMap<>();
	ArrayList<Entity> mobs = new ArrayList<>();

	public DungeonInstance(ArrayList<Player> players, Dungeon dungeon, int id) {
		this.players = players;
		this.dungeon = dungeon;
		this.id = id;
		OdysseyPl.getOdysseyPlugin().getServer().getPluginManager().registerEvents(this, OdysseyPl.getOdysseyPlugin());
	}

	public void run() {

		setupDoors();
		spawnMobs();
		songRepeater = new SongRepeater(this).runTaskTimer(OdysseyPl.getOdysseyPlugin(), 0, Integer.parseInt(dungeon.getSong().split("_")[1]) * 20);

		int pos = 0;
		for (Player p : players) {
			previousLoc.put(p, p.getLocation().clone());
			p.teleport(DungeonConfig.getStartLoc(dungeon.getId(), id, pos));
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 160, 999, true, false, false));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 160, 999, true, false, false));
			
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
		}.runTaskTimer(OdysseyPl.getOdysseyPlugin(), 40, 20);

	}
	
	
	public void stop() {
		HandlerList.unregisterAll(this);
		for (Entity e : mobs)
			if (!e.isDead())
				e.remove();
		for(String doorName : dungeon.getDoors())
			if (getDoor(doorName).isOpen())
				BigDoors.get().toggleDoor(getDoor(doorName).getDoorUID());
	}

	public void setupDoors() {
		for(String doorName : dungeon.getDoors()) {
			Door door = getDoor(doorName + id);
			if (door == null) {
				
				Door copyFrom = getDoor(doorName);
				door = new Door(copyFrom.getPlayerUUID(), copyFrom.getPlayerName(), copyFrom.getPrimeOwner(), copyFrom.getWorld(), 
						DungeonConfig.applyOffset(copyFrom.getMinimum(), dungeon.getOffsets().get(id)), DungeonConfig.applyOffset(copyFrom.getMaximum(), dungeon.getOffsets().get(id)),
						DungeonConfig.applyOffset(copyFrom.getEngine(), dungeon.getOffsets().get(id)), copyFrom.getName() + id, false, -1L, false, copyFrom.getPermission(), copyFrom.getType(), copyFrom.getEngSide(), DungeonConfig.applyOffset(copyFrom.getPowerBlockLoc(), dungeon.getOffsets().get(id)),
						copyFrom.getOpenDir(), copyFrom.getAutoClose(), copyFrom.notificationEnabled());
				BigDoors.get().getCommander().addDoor(door);
				
			}
			
			doors.put(doorName, door);
		}
	}
	
	public Door getDoor(String doorName) {
		for(Door door : BigDoors.get().getCommander().getDoorsInWorld(dungeon.getStartLocs().get(0).getWorld())) {
			if (door.getName().equals(doorName) ) {
				return door;
			}
		}
		return null;
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
			e.getPlayer().teleport(previousLoc.get(e.getPlayer()));
			if (players.size() == 0) {
				stop();
			}
		}
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public Dungeon getDungeon() {
		return dungeon;
	}

	
	 
	

}
