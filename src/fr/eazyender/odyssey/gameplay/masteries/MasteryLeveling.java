package fr.eazyender.odyssey.gameplay.masteries;

import java.util.Random;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import fr.eazyender.odyssey.dungeons.DungeonInstance;
import fr.eazyender.odyssey.gameplay.stats.Classe;
import fr.eazyender.odyssey.player.group.PlayerGroup;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import net.md_5.bungee.api.ChatColor;

public class MasteryLeveling implements Listener {

	@EventHandler
	public void onKill(MythicMobDeathEvent e) { 
		if (ExpHandler.xpMobs.containsKey(e.getMobType().getInternalName())) {
			if (e.getKiller() instanceof Player) {
				Player killer = (Player) e.getKiller();
				Random r = new Random();
				int percentageVariance = -10 + r.nextInt(20);
				double xpToGive = ExpHandler.xpMobs.get(e.getMobType().getInternalName()) + ((double)percentageVariance / 100) * ExpHandler.xpMobs.get(e.getMobType().getInternalName());
				
				
				
				// Dungeons -> everyone gets the xp
				if (DungeonInstance.getInstance(killer) != null) {
					for(Player p : DungeonInstance.getInstance(killer).getPlayers()) {
						if (MasteryDB.getClass(p.getUniqueId().toString()) != null && MasteryDB.getClass(p.getUniqueId().toString()) != Classe.MAGE)
							giveXp(p, xpToGive,MasteryDB.getClass(p.getUniqueId().toString()).getMastery());
					}
				} else {
					if (MasteryDB.getClass(killer.getUniqueId().toString()) != null && MasteryDB.getClass(killer.getUniqueId().toString()) != Classe.MAGE)
						giveXp(killer, xpToGive, MasteryDB.getClass(killer.getUniqueId().toString()).getMastery());
					// Groupe ? 25% to every other player
					if (PlayerGroup.getGroup(killer) != null) {
						for(Player p : PlayerGroup.getGroup(killer).getMembers()) {
							if (p != killer) {
								if (MasteryDB.getClass(p.getUniqueId().toString()) != null && MasteryDB.getClass(p.getUniqueId().toString()) != Classe.MAGE)
									giveXp(p, xpToGive * 0.25, MasteryDB.getClass(p.getUniqueId().toString()).getMastery());
							}
						}
					} 
					
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void giveXp(Player p, double xp, Mastery mastery) {
		int currentLevel = MasteryDB.getMastery(p.getUniqueId().toString(), mastery);
		double currentXp = p.getExp() * getXpNeeded(currentLevel);

		if (currentXp + xp >= getXpNeeded(currentLevel)) {
			while (currentXp + xp >= getXpNeeded(currentLevel)) {

				xp -= getXpNeeded(currentLevel) - currentXp;
				currentLevel += 1;
				p.sendTitle(ChatColor.of("#34eb5b") + "LEVEL UP !", null);
				p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);

				currentXp = 0;
			}
			
			p.setExp((float) (xp / getXpNeeded(currentLevel)) );
			MasteryDB.setMastery(p.getUniqueId().toString(), mastery, currentLevel);
			MasteryDB.setXp(p, mastery, (float) (xp / getXpNeeded(currentLevel)));
		} else {
			p.setExp((float) (p.getExp() + (xp / getXpNeeded(currentLevel) )));
			MasteryDB.setXp(p, mastery, (float) (p.getExp() + (xp / getXpNeeded(currentLevel) )));
		}
		p.setLevel(currentLevel);

	}

	public int getXpNeeded(int level) {
		return 150;
	}
}
 