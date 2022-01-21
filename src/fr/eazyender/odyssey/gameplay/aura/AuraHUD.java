package fr.eazyender.odyssey.gameplay.aura;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.gameplay.items.ItemType;
import fr.eazyender.odyssey.gameplay.items.ItemUtils;
import fr.eazyender.odyssey.gameplay.stats.CombatStats;
import fr.eazyender.odyssey.gameplay.stats.Stat;

public class AuraHUD implements Listener {

	public static Map<UUID, BossBar> player_bossbars = new HashMap<>();
	public static Map<UUID, Double> player_aura = new HashMap<UUID, Double>();

	public static void initHUD() {
		new BukkitRunnable() {

			@SuppressWarnings("deprecation")
			@Override
			public void run() {

				for (Player player : Bukkit.getOnlinePlayers()) {
					CombatStats stats = CombatStats.getStats(player);
					if (!player_aura.containsKey(player.getUniqueId())) {
						player_aura.put(player.getUniqueId(), (double) stats.getStat(Stat.AURA));
					if (player_aura.get(player.getUniqueId()) < 0)
						player_aura.replace(player.getUniqueId(), (double)0);
					} else {
						if (player_aura.get(player.getUniqueId()) < stats.getStat(Stat.AURA)) {
							if (player_aura.get(player.getUniqueId())
									+ ((double) stats.getStat(Stat.REGENAURA) / 100) >= stats.getStat(Stat.AURA)) {
								player_aura.replace(player.getUniqueId(), (double) stats.getStat(Stat.AURA));
							} else {
								player_aura.replace(player.getUniqueId(), player_aura.get(player.getUniqueId())
										+ ((double) stats.getStat(Stat.REGENAURA) / 100));
							}
						}
						else if (player_aura.get(player.getUniqueId()) > stats.getStat(Stat.AURA)) {
							player_aura.put(player.getUniqueId(), (double) stats.getStat(Stat.AURA));
						}
					}

					if (player.getItemInHand() != null) {
						ItemType type = ItemUtils.getType(player.getItemInHand());
						if (type != null
								&& (type == ItemType.ARCHER || type == ItemType.GUERRIER || type == ItemType.TANK)) {
							if (!player_bossbars.containsKey(player.getUniqueId())) {
								BossBar bossBar = Bukkit.createBossBar("", BarColor.YELLOW, BarStyle.SOLID);
										bossBar.addPlayer(player);
								bossBar.setProgress((double) (0));
								bossBar.setVisible(true);
								player_bossbars.put(player.getUniqueId(),
										bossBar);
							}
							BossBar e = player_bossbars.get(player.getUniqueId());
							if (player_aura.get(player.getUniqueId()) / (double) stats.getStat(Stat.AURA) <= 1)
								e.setProgress(
										player_aura.get(player.getUniqueId()) / (double) stats.getStat(Stat.AURA));
							e.setVisible(true);
							player_bossbars.replace(player.getUniqueId(), e);
							return;
						}

					}
					if (player_bossbars.containsKey(player.getUniqueId())) {
						BossBar e = player_bossbars.get(player.getUniqueId());
						e.setVisible(false);
						player_bossbars.replace(player.getUniqueId(), e);
						if (AuraCastListener.casts.containsKey(player)) AuraCastListener.casts.get(player).cancelCast();
					}

				}

			}
		}.runTaskTimer(OdysseyPl.getOdysseyPlugin(), 0, 5);

	}

	public static double getPlayerAura(Player p) {
		return player_aura.get(p.getUniqueId());
	}

	public static void setPlayerAura(Player p, double aura) {
		player_aura.put(p.getUniqueId(), aura);
	}

	public static void setVisibleFalse(Player player) {
		if (player_bossbars.containsKey(player.getUniqueId())) {
			BossBar e = player_bossbars.get(player.getUniqueId());
			e.setVisible(false);
			player_bossbars.replace(player.getUniqueId(), e);
		}
	}

	public static void setVisibleFalse() {
		for (Player p : Bukkit.getOnlinePlayers())
			setVisibleFalse(p);
	}

}
