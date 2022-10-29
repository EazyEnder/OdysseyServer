package fr.eazyender.odyssey.gameplay.magic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.gameplay.items.ItemUtils;
import fr.eazyender.odyssey.gameplay.magic.spells.ISpell;
import fr.eazyender.odyssey.gameplay.magic.spells.SpellTest;
import fr.eazyender.odyssey.gameplay.magic.spells.fire.SpellFirebolt;
import fr.eazyender.odyssey.gameplay.masteries.MasteryDB;
import fr.eazyender.odyssey.gameplay.stats.Classe;
import fr.eazyender.odyssey.utils.maths.ISphericPosition;
import net.md_5.bungee.api.ChatColor;

public class LaunchMagicUtils implements Listener {

	public static Map<UUID, List<ISphericPosition>> player_runes = new ConcurrentHashMap<UUID, List<ISphericPosition>>();
	public static Map<UUID, Boolean> isInCasting = new ConcurrentHashMap<UUID, Boolean>();
	private static Map<UUID, BukkitRunnable> player_runes_cooldown = new HashMap<UUID, BukkitRunnable>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerDraw(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

			if (p.getItemInHand() != null
					&& ItemUtils.getClass(p.getItemInHand()) != null && ItemUtils.getClass(p.getItemInHand()) == Classe.MAGE) {
				if (!MasteryDB.getClass(p.getUniqueId().toString()).equals("MAGE")) {
					event.setCancelled(true);
					p.sendMessage(ChatColor.of("#FF0000") + "Tu ne peux pas utiliser cet item avec ta classe actuelle !");
					return;
					
				}
				

				if (!player_runes.containsKey(p.getUniqueId())) {
					player_runes.put(p.getUniqueId(), new CopyOnWriteArrayList<ISphericPosition>());

					new BukkitRunnable() {

						@Override
						public void run() {
							if (p.isOnline() && player_runes.containsKey(p.getUniqueId())) {
								Particle.DustOptions dustOptions = new Particle.DustOptions(Color.WHITE, 0.75F);
								if (WandUtils.getColorOfElement(p) != null)
									dustOptions = new Particle.DustOptions(WandUtils.getColorOfElement(p), 0.75F);
								for (Iterator<ISphericPosition> iterator = player_runes.get(p.getUniqueId())
										.iterator(); iterator.hasNext();) {
									ISphericPosition v = iterator.next();
									p.getWorld().spawnParticle(Particle.REDSTONE,
											v.getCartesianLocation(p.getEyeLocation(), p.getWorld()).getX(),
											v.getCartesianLocation(p.getEyeLocation(), p.getWorld()).getY(),
											v.getCartesianLocation(p.getEyeLocation(), p.getWorld()).getZ(), 0, 0D, 0D,
											0D, dustOptions);
								}

							} else {
								this.cancel();
							}

						}

					}.runTaskTimer(OdysseyPl.getOdysseyPlugin(), 1, 5);
				}

				BukkitRunnable task = new BukkitRunnable() {
					@Override
					public void run() {
						launchSpell(p);
					}
				};
				if (player_runes_cooldown.containsKey(p.getUniqueId())) {
					player_runes_cooldown.get(p.getUniqueId()).cancel();
					player_runes_cooldown.replace(p.getUniqueId(), task);
					player_runes_cooldown.get(p.getUniqueId()).runTaskLater(OdysseyPl.getOdysseyPlugin(), 10);
				} else {
					player_runes_cooldown.put(p.getUniqueId(), task);
					player_runes_cooldown.get(p.getUniqueId()).runTaskLater(OdysseyPl.getOdysseyPlugin(), 10);
				}

				ISphericPosition target = new ISphericPosition((-p.getLocation().getPitch()) * (Math.PI / 180),
						calcYaw(p.getLocation().getYaw()) * (Math.PI / 180), 1);
				boolean flag = false;
				List<ISphericPosition> v = player_runes.get(p.getUniqueId());
				for (Iterator<ISphericPosition> iterator = v.iterator(); iterator.hasNext();) {
					ISphericPosition position2 = iterator.next();
					if (target != position2
							&& target.getCartesianLocation(p.getEyeLocation(), p.getWorld())
									.distance(position2.getCartesianLocation(p.getEyeLocation(), p.getWorld())) < 0.1
							&& !flag) {
						flag = true;
					}
				}
				if (!flag) {
					v.add(target);
				}
				player_runes.replace(p.getUniqueId(), v);

				event.setCancelled(true);

			}
		}
	}

	private static double calcYaw(double yaw) {
		double y = yaw;

		if (y < 0) {
			y = 180 + Math.abs((-180 - y));
		}
		y += 90;

		return y;
	}

	private static void launchSpell(Player player) {

		// RuneUtils.makeRune(player_runes.get(player.getUniqueId()));

		ISpell spell = RuneUtils.getSpell(WandUtils.element_choose.get(player.getUniqueId()),
				player_runes.get(player.getUniqueId()));

		if (spell != null) {
			for (Iterator<ISphericPosition> iterator = player_runes.get(player.getUniqueId()).iterator(); iterator
					.hasNext();) {
				ISphericPosition v = iterator.next();
				player.spawnParticle(Particle.ENCHANTMENT_TABLE,
						v.getCartesianLocation(player.getEyeLocation(), player.getWorld()).getX(),
						v.getCartesianLocation(player.getEyeLocation(), player.getWorld()).getY(),
						v.getCartesianLocation(player.getEyeLocation(), player.getWorld()).getZ(), 0, 0D, 0D, 0D);
			}

			if (spell instanceof SpellTest) {
				SpellTest fb = new SpellTest();
				if (WandUtils.player_mana.get(player.getUniqueId()) >= SpellFirebolt.basicCost) {
					fb.launch(player);
				} else
					for (Iterator<ISphericPosition> iterator = player_runes.get(player.getUniqueId())
							.iterator(); iterator.hasNext();) {
						ISphericPosition v = iterator.next();
						player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
						player.spawnParticle(Particle.ASH,
								v.getCartesianLocation(player.getEyeLocation(), player.getWorld()).getX(),
								v.getCartesianLocation(player.getEyeLocation(), player.getWorld()).getY(),
								v.getCartesianLocation(player.getEyeLocation(), player.getWorld()).getZ(), 0,
								Math.random() * 2, Math.random() * 2, Math.random() * 2);
						player.spawnParticle(Particle.ASH,
								v.getCartesianLocation(player.getEyeLocation(), player.getWorld()).getX(),
								v.getCartesianLocation(player.getEyeLocation(), player.getWorld()).getY(),
								v.getCartesianLocation(player.getEyeLocation(), player.getWorld()).getZ(), 0,
								Math.random() * 2, Math.random() * 2, Math.random() * 2);
						player.spawnParticle(Particle.ASH,
								v.getCartesianLocation(player.getEyeLocation(), player.getWorld()).getX(),
								v.getCartesianLocation(player.getEyeLocation(), player.getWorld()).getY(),
								v.getCartesianLocation(player.getEyeLocation(), player.getWorld()).getZ(), 0,
								Math.random() * 2, Math.random() * 2, Math.random() * 2);
					}
			}

			player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1);
		} else {
			player.playSound(player.getLocation(), Sound.BLOCK_BASALT_BREAK, 1, 1);
		}

		player_runes.remove(player.getUniqueId());

	}

}