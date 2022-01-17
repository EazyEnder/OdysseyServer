package fr.eazyender.odyssey.player;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.gameplay.aura.AuraCastListener;
import fr.eazyender.odyssey.gameplay.items.ItemUtils;
import fr.eazyender.odyssey.gameplay.stats.PlayerStats;
import fr.eazyender.odyssey.utils.world.IWorld;
import fr.eazyender.odyssey.utils.world.WorldUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class StatsListener implements Listener {

	public static Map<UUID, Double> thirst_player = new HashMap<UUID, Double>();
	public static Map<UUID, Double> temp_player = new HashMap<UUID, Double>();

	private static HashMap<UUID, Integer> temp_timer = new HashMap<UUID, Integer>();
	private static HashMap<UUID, Integer> temp_timerInSituation = new HashMap<UUID, Integer>();
	private static HashMap<UUID, Integer> temp_timerInShadow = new HashMap<UUID, Integer>();
	private static HashMap<UUID, Integer> temp_timerWithBlock = new HashMap<UUID, Integer>();

	private static Map<UUID, BukkitTask> decay_timer = new HashMap<UUID, BukkitTask>();

	public StatsListener() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			StatsListener.reload(player);
		}
	}

	public static void initPlayerStats(Player player) {

		if (decay_timer.containsKey(player.getUniqueId())) {
			decay_timer.get(player.getUniqueId()).cancel();
		}

		if (!thirst_player.containsKey(player.getUniqueId()))
			thirst_player.put(player.getUniqueId(), 100.0);

		if (!temp_player.containsKey(player.getUniqueId()))
			temp_player.put(player.getUniqueId(), 0.0);
		if (!temp_timer.containsKey(player.getUniqueId()))
			temp_timer.put(player.getUniqueId(), 2);

		new BukkitRunnable() {

			@Override
			public void run() {

				if (!player.isOnline()) {
					if (decay_timer.containsKey(player.getUniqueId())) {
						BukkitTask task = new BukkitRunnable() {
							@Override
							public void run() {
								if (!player.isOnline() && temp_player.containsKey(player.getUniqueId())) {
									temp_player.remove(player.getUniqueId());
									temp_timer.remove(player.getUniqueId());
									thirst_player.remove(player.getUniqueId());
									decay_timer.remove(player.getUniqueId());
								}
							}

						}.runTaskLater(OdysseyPl.getOdysseyPlugin(), 20 * 60 * 60);
						decay_timer.put(player.getUniqueId(), task);
					}
					this.cancel();
				}

				IWorld world = WorldUtils.getWorldOfPlayer(player);

				if (world != null) {

					// TEMPERATURE
					double temperature = calcTemp(player, world);

					// SOIF
					double water = thirst_player.get(player.getUniqueId());
					double deltawater = 0;
					if (player.getGameMode() != GameMode.CREATIVE) {
						// EFFET
						if (water <= 0) {
							player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, 1, true));
						}

						if (temperature > 60) {
							deltawater -= 0.5;
						} else if (temperature > 45) {
							deltawater -= 0.2;
						} else if (temperature > 30) {
							deltawater -= 0.1;
						} else {
							deltawater -= 0.05;
						}

						if (water + deltawater <= 0) {
							water = 0;
						}

					}
					water += deltawater;
					thirst_player.replace(player.getUniqueId(), water);

					// Action Bar
					DecimalFormat df = new DecimalFormat("###");
					if (AuraCastListener.getCast(player) == null) {
						String action_bar = "";
						
							String temp_color = ""+ChatColor.of(new Color(106, 176, 76));
							if(temperature > 0 && temperature <= 20) {temp_color = ""+ChatColor.of(new Color(250, 211, 144));}
							else if(temperature > 20 && temperature <= 30) {temp_color = ""+ChatColor.of(new Color(246, 185, 59));}
							else if(temperature > 30 && temperature <= 45) {temp_color = ""+ChatColor.of(new Color(250, 152, 58));}
							else if(temperature > 45 && temperature <= 60) {temp_color = ""+ChatColor.of(new Color(229, 80, 57));}
							else if(temperature > 60) {temp_color = ""+ChatColor.of(new Color(160, 19, 19));}
							else if(temperature > -10 && temperature <= 0) {temp_color = ""+ChatColor.of(new Color(130, 204, 221));}
							else if(temperature > -20 && temperature <= -10) {temp_color = ""+ChatColor.of(new Color(106, 137, 204));}
							else if(temperature > -35 && temperature <= -20) {temp_color = ""+ChatColor.of(new Color(74, 105, 189));}
							else if(temperature > -50 && temperature <= -35) {temp_color = ""+ChatColor.of(new Color(30, 55, 153));}
							else if(temperature <= 50) {temp_color = ""+ChatColor.of(new Color(12, 36, 97));}
							String tempRound = df.format(temperature);
						
						if(temperature <= 0) {
						action_bar += "\uEAA3" + temp_color + " §l" + tempRound + "§C§r ";
						}else {
						action_bar += "\uEAA2" + temp_color + " §l" + tempRound + "§C§r ";
						}
						
							String water_color = ""+ChatColor.of(new Color(106, 176, 76));
							if(water > 80 && water <= 100) {water_color = ""+ChatColor.of(new Color(186, 220, 88));}
							else if(water > 60 && water <= 80) {water_color = ""+ChatColor.of(new Color(246, 229, 141));}
							else if(water > 40 && water <= 60) {water_color = ""+ChatColor.of(new Color(240, 147, 43));}
							else if(water > 20 && water < 40) {water_color = ""+ChatColor.of(new Color(235, 77, 75));}
							else if(water <= 20) {water_color = ""+ChatColor.of(new Color(160, 19, 19));}
							String water_round = df.format(water);
							action_bar += "\uEAA4" + water_color + " §l" + water_round + "%§r ";
					
							player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(action_bar));
					}
				}
			}

		}.runTaskTimer(OdysseyPl.getOdysseyPlugin(), 0, 10);

	}

	@EventHandler
	public void onPlayerDrink(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getClickedBlock() != null) {
			if (event.getClickedBlock().getLocation().add(0, 1, 0).getBlock().getType().equals(Material.WATER)) {
				double water = thirst_player.get(player.getUniqueId());
				if (water + 20 >= 100) {
					water = 100;
				} else {
					water += 20;
				}
				thirst_player.replace(player.getUniqueId(), water);
				player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_DRINK, 1, 1);
			}
		}
	}

	private static double calcTemp(Player player, IWorld world) {
		Double temperature = temp_player.get(player.getUniqueId());
		if (temp_timer.get(player.getUniqueId()) <= 0) {
			Player p = player;
			/** TEMPERATURE EFFECTS */

			if (temperature <= -5 && temperature > -10) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 0));
			} else if (temperature <= -10 && temperature > -15) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 0));
			} else if (temperature <= -15 && temperature > -20) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 0));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0));
			} else if (temperature <= -20 && temperature > -25) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 1));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 1));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
			} else if (temperature <= -25 && temperature > -30) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 2));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
			} else if (temperature <= -30 && temperature > -40) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 3));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 3));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 3));
				p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0));
			} else if (temperature <= -40 && temperature > -45) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 3));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 3));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 3));
				p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 0));
			} else if (temperature <= -45 && temperature > -50) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 3));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 3));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 3));
				p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0));
				p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 1));
			} else if (temperature <= -50) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 3));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 3));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 3));
				p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1));
				p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 2));
			}

			else if (temperature >= 30 && temperature < 40) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 0));
			} else if (temperature >= 40 && temperature < 45) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 1));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 1));
			} else if (temperature >= 45 && temperature < 50) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 1));
			} else if (temperature >= 50 && temperature < 60) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 1));
				p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 0));
			} else if (temperature >= 60 && temperature < 65) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 3));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 2));
				p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0));
				p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 0));
			} else if (temperature >= 65 && temperature < 70) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 3));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 2));
				p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 2));
				p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 1));
			} else if (temperature >= 70) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 4));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 4));
				p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 2));
				p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 2));
			}

			/** TEMPERATURE LOAD */
			float deltaValue = 0;
			/** EN FONCTION DU TEMPS DU JOUR */
			long timeOfDay = player.getWorld().getTime();
			float difference = 0;
			if (timeOfDay >= 24000) {
				timeOfDay = 0;
				player.getWorld().setTime(0);
			}
			if ((timeOfDay > 0 && timeOfDay <= 6000) || timeOfDay > 18000) {
				// VERS zenith
				if (timeOfDay > 18000) {

					if (timeOfDay > 21000) {
						timeOfDay = 24000 - (timeOfDay);
						difference = (float) Math.sqrt(2 * ((12000 - timeOfDay) * (12000 - timeOfDay))) * 0.001f - 12;
					} else {
						timeOfDay = 6000 + (6000 - (24000 - (timeOfDay)));
						difference = (float) Math.sqrt(2 * ((12000 - timeOfDay) * (12000 - timeOfDay))) * 0.003f - 4;
					}
				}
			} else if (timeOfDay > 6000 && timeOfDay <= 18000) {
				// VERS apogee lune
				if (timeOfDay > 6000 && timeOfDay <= 12000) {
					timeOfDay = 12000 - timeOfDay;
				} else if (timeOfDay > 12000 && timeOfDay <= 18000) {
					timeOfDay = 24000 - (timeOfDay);
					if (timeOfDay > 15000) {
						difference = (float) Math.sqrt(2 * ((12000 - timeOfDay) * (12000 - timeOfDay))) * 0.003f + 12;
					} else {
						difference = (float) Math.sqrt(2 * ((12000 - timeOfDay) * (12000 - timeOfDay))) * 0.001f + 12;
					}
				}
			}

			double x = player.getLocation().getX(), y = player.getLocation().getY(), z = player.getLocation().getZ();

			/** EN FONCTION DE LA PROFONDEUR ET ALTITUDE */
			float tempProfondeur = 0;
			if (y > world.getSurface() + 130) {
				tempProfondeur = -30;
			} else if (y <= world.getSurface() + 130 && y > world.getSurface() + 100) {
				tempProfondeur = -20;
			} else if (y <= world.getSurface() + 100 && y > world.getSurface() + 75) {
				tempProfondeur = -10;
			} else if (y <= world.getSurface() + 75 && y > world.getSurface() + 50) {
				tempProfondeur = -7;
			} else if (y <= world.getSurface() + 50 && y > world.getSurface() + 25) {
				tempProfondeur = -5;
			}

			else if (y <= world.getSurface() - 20 && y > world.getSurface() - 40) {
				tempProfondeur = 5;
			} else if (y <= world.getSurface() - 40 && y > world.getSurface() - 55) {
				tempProfondeur = 10;
			} else if (y <= world.getSurface() - 55 && y > world.getSurface() - 70) {
				tempProfondeur = 20;
			} else if (y <= world.getSurface() - 70 && y > world.getSurface() - 90) {
				tempProfondeur = 40;
			} else if (y <= world.getSurface() - 90) {
				tempProfondeur = 55;
			}

			/** EN FONCTION DE BLOCKS ADJACENTS */
			float tempBlock = 0;
			int radius = 4;
			int noFoundBlock = 0;
			boolean blockFound = false;
			for (int X = -radius; X <= radius; X++) {
				for (int Z = -radius; Z <= radius; Z++) {
					for (int Y = -radius; Y <= radius; Y++) {
						Block b = player.getWorld().getBlockAt(X + (int) x, Y + (int) y, Z + (int) z);
						if (b.getType().equals(Material.LAVA) && !blockFound) {
							if (!temp_timerWithBlock.containsKey(player.getUniqueId())) {
								temp_timerWithBlock.put(player.getUniqueId(), 1);
							}
							tempBlock += temp_timerWithBlock.get(player.getUniqueId()) * 1f;
							blockFound = true;
							if (tempBlock >= 20) {
							} else {
								temp_timerWithBlock.replace(player.getUniqueId(),
										temp_timerWithBlock.get(player.getUniqueId()) + 1);
							}
						} else if (b.getType().equals(Material.FIRE) && !blockFound) {
							if (!temp_timerWithBlock.containsKey(player.getUniqueId())) {
								temp_timerWithBlock.put(player.getUniqueId(), 1);
							}
							tempBlock += temp_timerWithBlock.get(player.getUniqueId()) * 1f;
							blockFound = true;
							if (tempBlock >= 10) {
							} else {
								temp_timerWithBlock.replace(player.getUniqueId(),
										temp_timerWithBlock.get(player.getUniqueId()) + 1);
							}
						} else {
							noFoundBlock++;
						}
					}
				}
			}
			if (noFoundBlock >= (2 * radius + 1) * (2 * radius + 1) * (2 * radius + 1)) {
				if (temp_timerWithBlock.containsKey(player.getUniqueId())) {
					if (temp_timerWithBlock.get(player.getUniqueId()) >= 1) {
						tempBlock -= temp_timerWithBlock.get(player.getUniqueId()) * 1f;
						temp_timerWithBlock.replace(player.getUniqueId(),
								temp_timerWithBlock.get(player.getUniqueId()) - 1);
					} else {
						temp_timerWithBlock.remove(player.getUniqueId());
					}
				}
			}

			/** EN FONCTION DE L OMBRE */
			float tempShadow = 0;
			if (player.getWorld().getBlockAt((int) x, (int) y, (int) z).getLightLevel() <= 6) {
				if (!temp_timerInShadow.containsKey(player.getUniqueId())) {
					temp_timerInShadow.put(player.getUniqueId(), 1);
				}
				tempShadow -= temp_timerInShadow.get(player.getUniqueId()) * 0.1f;
				if (tempShadow <= -5) {
				} else {
					temp_timerInShadow.replace(player.getUniqueId(), temp_timerInShadow.get(player.getUniqueId()) + 1);
				}
			} else {
				if (temp_timerInShadow.containsKey(player.getUniqueId())) {
					if (temp_timerInShadow.get(player.getUniqueId()) >= 1) {
						tempShadow -= temp_timerInShadow.get(player.getUniqueId()) * 0.1f;
						temp_timerInShadow.replace(player.getUniqueId(),
								temp_timerInShadow.get(player.getUniqueId()) - 1);
					} else {
						temp_timerInShadow.remove(player.getUniqueId());
					}
				}
			}

			/** EN FONCTION DE LA SITUATION */
			float tempSituation = 0;
			/** DANS L EAU **/
			if (player.getLocation().getBlock().getType().equals(Material.WATER)
					|| player.getLocation().add(0, 1, 0).getBlock().getType().equals(Material.WATER)) {
				if (!temp_timerInSituation.containsKey(player.getUniqueId())) {
					temp_timerInSituation.put(player.getUniqueId(), 1);
				}
				tempSituation -= temp_timerInSituation.get(player.getUniqueId()) * 0.1f;
				if (tempSituation <= -5) {
				} else {
					temp_timerInSituation.replace(player.getUniqueId(),
							temp_timerInSituation.get(player.getUniqueId()) + 1);
				}
				/** DANS LA LAVE **/
			} else if (player.getLocation().getBlock().getType().equals(Material.LAVA)
					|| player.getLocation().add(0, 1, 0).getBlock().getType().equals(Material.LAVA)) {
				if (!temp_timerInSituation.containsKey(player.getUniqueId())) {
					temp_timerInSituation.put(player.getUniqueId(), 30);
				}
				tempSituation += temp_timerInSituation.get(player.getUniqueId()) * 0.1f;
				if (tempSituation >= 70) {
				} else {
					temp_timerInSituation.replace(player.getUniqueId(),
							temp_timerInSituation.get(player.getUniqueId()) + 30);
				}
			} else {
				if (temp_timerInSituation.containsKey(player.getUniqueId())) {
					if (temp_timerInSituation.get(player.getUniqueId()) >= 1) {
						tempSituation -= temp_timerInSituation.get(player.getUniqueId()) * 0.1f;
						temp_timerInSituation.replace(player.getUniqueId(),
								temp_timerInSituation.get(player.getUniqueId()) - 1);
					} else {
						temp_timerInSituation.remove(player.getUniqueId());
					}
				}
			}

			double tempCycle = (0.001f * timeOfDay) - difference;
			/** EN FONCTION DU BIOME */
			String biomeStr = player.getLocation().getBlock().getBiome().name();
			if (world.getTemp().containsKey(biomeStr)) {
				double temperatureBiome = (double) world.getTemp().get(biomeStr);
				double SommeT = tempSituation + temperatureBiome + tempCycle + tempShadow + tempBlock + tempProfondeur;
				if (temperature != SommeT) {
					if (temperature > SommeT) {

						if (temperature - (SommeT) >= 1)
							deltaValue--;
						else {
							deltaValue -= temperature - (SommeT);
						}
					} else if (temperature < SommeT) {
						if (SommeT - temperature >= 1)
							deltaValue++;
						else {
							deltaValue += SommeT - temperature;
						}

					}
				}

			} else {
				deltaValue += 10;
			}

			DecimalFormat df = new DecimalFormat("###.##");
			String tempRound = df.format(temperature + deltaValue);
			tempRound = tempRound.replace(",", ".");
			temperature = (Double.valueOf(tempRound));
			temp_player.replace(player.getUniqueId(), temp_player.get(player.getUniqueId()) + deltaValue);
			temp_timer.replace(player.getUniqueId(), 2);
		} else {
			temp_timer.replace(player.getUniqueId(), temp_timer.get(player.getUniqueId()) - 1);
		}
		return temperature;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		initPlayerStats(e.getPlayer());
		PlayerStats.itemHeld.put(e.getPlayer(), ItemUtils.cloneIfNotNull(e.getPlayer().getInventory().getItemInMainHand()));
		PlayerStats.armor.put(e.getPlayer(), ItemUtils.cloneIfNotNull( e.getPlayer().getInventory().getArmorContents()));

	}

	public static void reload(Player player) {
		initPlayerStats(player);
	}

}
