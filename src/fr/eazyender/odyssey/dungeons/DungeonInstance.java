package fr.eazyender.odyssey.dungeons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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

	public static ArrayList<DungeonInstance> instances = new ArrayList<>();

	boolean running = true;

	HashMap<Player, Location> previousLoc = new HashMap<>();

	ArrayList<Player> players;
	Dungeon dungeon;
	int id;
	BukkitTask songRepeater;
	int time = 0;

	HashMap<String, Door> doors = new HashMap<>();
	ArrayList<Entity> mobs = new ArrayList<>();

	public DungeonInstance(ArrayList<Player> players, Dungeon dungeon, int id) {
		this.players = players;
		this.dungeon = dungeon;
		this.id = id;
		OdysseyPl.getOdysseyPlugin().getServer().getPluginManager().registerEvents(this, OdysseyPl.getOdysseyPlugin());
		instances.add(this);
	}

	public void run() {

		setupDoors();
		spawnMobs();

		int pos = 0;
		for (Player p : players) {
			previousLoc.put(p, p.getLocation().clone());
			p.teleport(DungeonConfig.getStartLoc(dungeon.getId(), id, pos));
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 160, 999, true, false, false));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 160, 999, true, false, false));

			pos++;
		}

		// Tp and effects
		new BukkitRunnable() {
			int i = 5;

			@SuppressWarnings("deprecation")
			public void run() {
				for (Player p : players) {
					if (i == 5) {
						songRepeater = new SongRepeater(players, dungeon).runTaskTimer(OdysseyPl.getOdysseyPlugin(), 0,
								Integer.parseInt(dungeon.getSong().split("_")[1]) * 20);
						p.sendTitle(ChatColor.of(dungeon.getName().split("_")[0]) + dungeon.getName().split("_")[1],
								null);
					} else if (i != 4)
						p.sendTitle(ChatColor.of("#ff4800") + "" + i, null);
				}
				i--;
				if (i == 0)
					this.cancel();
			}
		}.runTaskTimer(OdysseyPl.getOdysseyPlugin(), 40, 20);
		// Time to clear dungeon : 1h
		new BukkitRunnable() {
			public void run() {
				if (!running)
					this.cancel();

				time++;
				if (time > 3600)
					stop();
			}
		}.runTaskTimer(OdysseyPl.getOdysseyPlugin(), 0, 20);

	}

	public void stop() {
		running = false;
		instances.remove(this);
		HandlerList.unregisterAll(this);
		for (Player player : players) {
			player.teleport(previousLoc.get(player));
		}
		for (Entity e : mobs)
			if (!e.isDead())
				e.remove();
		for (String doorName : dungeon.getDoors())
			if (getDoor(doorName).isOpen())
				BigDoors.get().toggleDoor(getDoor(doorName).getDoorUID());
	}
	
	@SuppressWarnings("deprecation")
	public void quit(Player p) {
		players.remove(p);
		p.teleport(previousLoc.get(p));
		if (players.size() == 0) {
			stop();
		}
		for (Player pl : players) {
			if (pl.getGameMode() != GameMode.SPECTATOR)
				return;
		}
		for (Player pl : players) {
			pl.sendTitle(ChatColor.of("#ff0000") + "Vous avez échoué", null);
			Bukkit.getScheduler().runTaskLater(OdysseyPl.getOdysseyPlugin(), () -> stop(), 60);
		}
	}

	public void setupDoors() {
		for (String doorName : dungeon.getDoors()) {
			Door door = getDoor(doorName + id);
			if (door == null) {

				Door copyFrom = getDoor(doorName);
				door = new Door(copyFrom.getPlayerUUID(), copyFrom.getPlayerName(), copyFrom.getPrimeOwner(),
						copyFrom.getWorld(),
						DungeonConfig.applyOffset(copyFrom.getMinimum(), dungeon.getOffsets().get(id)),
						DungeonConfig.applyOffset(copyFrom.getMaximum(), dungeon.getOffsets().get(id)),
						DungeonConfig.applyOffset(copyFrom.getEngine(), dungeon.getOffsets().get(id)),
						copyFrom.getName() + id, false, -1L, false, copyFrom.getPermission(), copyFrom.getType(),
						copyFrom.getEngSide(),
						DungeonConfig.applyOffset(copyFrom.getPowerBlockLoc(), dungeon.getOffsets().get(id)),
						copyFrom.getOpenDir(), copyFrom.getAutoClose(), copyFrom.notificationEnabled());
				BigDoors.get().getCommander().addDoor(door);

			}

			doors.put(doorName, door);
		}
	}

	public Door getDoor(String doorName) {
		for (Door door : BigDoors.get().getCommander().getDoorsInWorld(dungeon.getStartLocs().get(0).getWorld())) {
			if (door.getName().equals(doorName)) {
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
			quit(e.getPlayer());
		}
	}

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.DIAMOND_BLOCK) {
				players.remove(e.getPlayer());
				e.getPlayer().teleport(previousLoc.get(e.getPlayer()));
				if (players.size() == 0) {
					stop();
				}

			}
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if (players.contains(e.getPlayer())) {
			if (e.getMessage().startsWith("/"))
				e.setCancelled(true);
			e.getPlayer()
					.sendMessage(ChatColor.of("#ff0000") + "Tu ne peux pas effectuer de commandes durant un donjon !");
		}
	}

	@SuppressWarnings("deprecation")
	public void die(Player p) {
		p.setGameMode(GameMode.SPECTATOR);
		for (Player player : players) {
			if (player.getGameMode() != GameMode.SPECTATOR) {
				p.sendTitle(ChatColor.of("#ff0000") + "Tu es mort !", null);
				new BukkitRunnable() {
					double lowestDistance = 0;

					public void run() {
						if (!running)
							this.cancel();

						for (Player player : players) {
							if (player.getGameMode() != GameMode.SPECTATOR) {
								if (lowestDistance == -1
										|| player.getLocation().distance(p.getLocation()) < lowestDistance)
									lowestDistance = player.getLocation().distance(p.getLocation());
							}
						}
						if (lowestDistance > 20) {
							for (Player player : players) {
								if (player.getGameMode() != GameMode.SPECTATOR) {
									p.teleport(player);
									p.sendMessage(
											ChatColor.of("#ff0000") + "Ne t'aventure pas trop loin de tes équipiers !");
								}
							}

						}
					}
				}.runTaskTimer(OdysseyPl.getOdysseyPlugin(), 0, 5);
				return;
			}
		}
		p.sendTitle(ChatColor.of("#ff0000") + "Vous avez échoué", null);
		Bukkit.getScheduler().runTaskLater(OdysseyPl.getOdysseyPlugin(), () -> stop(), 60);

	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public Dungeon getDungeon() {
		return dungeon;
	}

	public static DungeonInstance getInstance(Player p) {
		for (DungeonInstance instance : instances) {
			if (instance.getPlayers().contains(p))
				return instance;
		}
		return null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	
}
