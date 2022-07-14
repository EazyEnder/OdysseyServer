package fr.eazyender.odyssey.gameplay.aura;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import fr.eazyender.odyssey.gameplay.items.ItemUtils;
import fr.eazyender.odyssey.gameplay.masteries.MasteryDB;
import fr.eazyender.odyssey.gameplay.stats.Classe;
import net.md_5.bungee.api.ChatColor;

public class AuraCastListener implements Listener {

	public static HashMap<Player, Cast> casts = new HashMap<>();

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getItem() != null && (ItemUtils.getType(e.getItem()) == Classe.GUERRIER
				|| ItemUtils.getType(e.getItem()) == Classe.ARCHER
				|| ItemUtils.getType(e.getItem()) == Classe.TANK)) {

			if (e.getAction().name().contains("LEFT") || e.getAction().name().contains("RIGHT")) {
				Cast cast = casts.get(e.getPlayer());
				if (cast != null) {
					if (cast.getTime() == 0 || System.currentTimeMillis() - cast.getTime() > 250) {
						cast.setPattern(cast.getPattern() + Cast.getClick(e.getAction()));
						cast.animate();
						e.setCancelled(true);
					}
				} else {
					if (ItemUtils.getType(e.getItem()) == Classe.GUERRIER && e.getAction().name().contains("LEFT"))
						return;
					if (ItemUtils.getType(e.getItem()) == Classe.TANK && e.getAction().name().contains("LEFT"))
						return;
					if (ItemUtils.getType(e.getItem()) == Classe.ARCHER && e.getAction().name().contains("RIGHT"))
						return;

					if (ItemUtils.getType(e.getItem()).name()
							.equals(MasteryDB.getClass(e.getPlayer().getUniqueId().toString()))) {

						cast = new Cast(e.getPlayer(), ItemUtils.getType(e.getItem()));
						cast.setPattern("" + Cast.getClick(e.getAction()));
						cast.animate();
						e.setCancelled(true);
					} else {
						e.getPlayer().sendMessage(
								ChatColor.of("#FF0000") + "Tu ne peux pas utiliser cet item avec ta classe actuelle !");
					}

				}
			}

		}
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			ItemStack item = p.getInventory().getItemInMainHand();
			if (item != null && (ItemUtils.getType(item) == Classe.GUERRIER
					|| ItemUtils.getType(item) == Classe.ARCHER || ItemUtils.getType(item) == Classe.TANK)) {
					Cast cast = casts.get(p);
					if (cast != null) {
						cast.setPattern(cast.getPattern() + "L");
						cast.animate();
				}
			}
		}
	}

	@EventHandler
	public void onSneak(PlayerToggleSneakEvent e) {
		if (casts.containsKey(e.getPlayer())) {
			casts.get(e.getPlayer()).cast();
		}
	}

}
