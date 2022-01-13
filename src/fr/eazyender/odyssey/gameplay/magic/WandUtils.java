package fr.eazyender.odyssey.gameplay.magic;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.gameplay.stats.CombatStats;
import fr.eazyender.odyssey.gameplay.stats.Stat;
import fr.eazyender.odyssey.utils.item.IWand;
import fr.eazyender.odyssey.utils.zone.IZone;
import fr.eazyender.odyssey.utils.zone.ZoneUtils;
import net.md_5.bungee.api.ChatColor;

public class WandUtils implements Listener {

	public static List<IWand> wands = new ArrayList<IWand>();

	public static HashMap<UUID, Integer> element_choose = new HashMap<UUID, Integer>();
	public static Map<UUID, BossBar> element = new HashMap<UUID, BossBar>();

	public static Map<UUID, Double> player_mana = new HashMap<UUID, Double>();

	public static void initWand() {

		initHud();

		int[] basic_wand_color = { 24, 38, 72 };
		List<String> basic_wand_lore = new ArrayList<String>();
		basic_wand_lore.add(ChatColor.of(new Color(basic_wand_color[0], basic_wand_color[1], basic_wand_color[2]))
				+ "Type : §r§f" + " Un bâton simple mais utilisable");
		basic_wand_lore.add(ChatColor.of(new Color(basic_wand_color[0], basic_wand_color[1], basic_wand_color[2]))
				+ "Bonus : §r§f" + "...");
		wands.add(new IWand(
				ChatColor.of(new Color(basic_wand_color[0], basic_wand_color[1], basic_wand_color[2]))
						+ "Bâton magique en bois",
				basic_wand_lore, 1, basic_wand_color, 0, new double[] { 1.0, 1.0, 1.0, 1.0 }));

	}

	public static void initHud() {
		new BukkitRunnable() {

			@SuppressWarnings("deprecation")
			@Override
			public void run() {

				for (Player player : Bukkit.getOnlinePlayers()) {
					CombatStats stats = CombatStats.getStats(player);
					if (!player_mana.containsKey(player.getUniqueId())) {
						player_mana.put(player.getUniqueId(), (double)stats.getStat(Stat.MP));
					} else {
						if (player_mana.get(player.getUniqueId()) < stats.getStat(Stat.MP)) {
							if (player_mana.get(player.getUniqueId()) + ((double)stats.getStat(Stat.REGENMP) / 100) >= stats
									.getStat(Stat.MP)) {
								player_mana.replace(player.getUniqueId(), (double)stats.getStat(Stat.MP));
							} else {
								player_mana.replace(player.getUniqueId(),
										player_mana.get(player.getUniqueId()) + ((double)stats.getStat(Stat.REGENMP) / 100));
							}
						}
					}

					if (player.getItemInHand() != null && player.getItemInHand().getType().equals(Material.STICK)
							&& player.getItemInHand().hasItemMeta()
							&& player.getItemInHand().getItemMeta().getCustomModelData() > 0) {
						IWand wand = null;
						for (IWand w : WandUtils.wands) {
							if (w.getCustomModelData() == player.getItemInHand().getItemMeta().getCustomModelData()) {
								wand = w;
							}
						}

						if (wand != null) {

							if (!element_choose.containsKey(player.getUniqueId())) {
								element_choose.put(player.getUniqueId(), 1);
							}

							if (!element.containsKey(player.getUniqueId())) {
								BossBar bossBar = Bukkit.createBossBar("----", BarColor.BLUE, BarStyle.SOLID);
								bossBar.addPlayer(player);
								bossBar.setProgress((double) (0));
								bossBar.setVisible(true);
								element.put(player.getUniqueId(), bossBar);
							}

							BossBar e = element.get(player.getUniqueId());
							e.setTitle(getASCIElement(element_choose.get(player.getUniqueId())));
							e.setProgress(player_mana.get(player.getUniqueId()) / (double)stats.getStat(Stat.MP));
							e.setVisible(true);
							element.replace(player.getUniqueId(), e);

						} else {
							if (element.containsKey(player.getUniqueId())) {
								BossBar e = element.get(player.getUniqueId());
								e.setVisible(false);
								element.replace(player.getUniqueId(), e);
							}
						}
					} else {
						if (element.containsKey(player.getUniqueId())) {
							BossBar e = element.get(player.getUniqueId());
							e.setVisible(false);
							element.replace(player.getUniqueId(), e);
						}
					}

				}
			}
		}.runTaskTimer(OdysseyPl.getOdysseyPlugin(), 0, 5);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerChangeElement(PlayerToggleSneakEvent e) {
		Player p = e.getPlayer();
		if ((!LaunchMagicUtils.player_runes.containsKey(p.getUniqueId())
				|| LaunchMagicUtils.player_runes.get(p.getUniqueId()).isEmpty()) && p.getItemInHand() != null
				&& p.getItemInHand().getType().equals(Material.STICK) && p.getItemInHand().hasItemMeta()
				&& p.getItemInHand().getItemMeta().getCustomModelData() > 0) {
			IWand wand = null;
			for (IWand w : WandUtils.wands) {
				if (w.getCustomModelData() == p.getItemInHand().getItemMeta().getCustomModelData()) {
					wand = w;
				}
			}

			if (wand != null) {

				changeElement(p);

			}
		}
	}

	public static String getASCIElement(int element) {
		String ascii = "";

		switch (element) {
		case 1:
			ascii = "\uEAC1";
			break;
		case 2:
			ascii = "\uEAC2";
			break;
		case 3:
			ascii = "\uEAC3";
			break;
		case 4:
			ascii = "\uEAC4";
			break;
		case 5:
			ascii = "\uEAC5";
			break;
		}

		return ascii;
	}

	public static void changeElement(Player p) {
		if (!p.isSneaking()) {
			if (element_choose.get(p.getUniqueId()) >= 5) {
				element_choose.replace(p.getUniqueId(), 1);
			} else {
				element_choose.replace(p.getUniqueId(), element_choose.get(p.getUniqueId()) + 1);
			}
		}
	}

	public static org.bukkit.Color getColorOfElement(Player p) {
		org.bukkit.Color color = null;
		if (element_choose.containsKey(p.getUniqueId())) {

			int element = element_choose.get(p.getUniqueId());

			switch (element) {
			case 1:
				color = org.bukkit.Color.fromRGB(92, 195, 239);
				break;
			case 2:
				color = org.bukkit.Color.fromRGB(159, 142, 126);
				break;
			case 3:
				color = org.bukkit.Color.fromRGB(138, 47, 28);
				break;
			case 4:
				color = org.bukkit.Color.fromRGB(192, 227, 135);
				break;
			case 5:
				color = org.bukkit.Color.fromRGB(255, 240, 163);
				break;
			}

		}

		return color;
	}

}
