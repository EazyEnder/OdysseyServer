package fr.eazyender.odyssey.gameplay.aura;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import fr.eazyender.odyssey.gameplay.aura.skills.SkillHitActivation;
import fr.eazyender.odyssey.gameplay.aura.skills.SkillsDB;
import fr.eazyender.odyssey.gameplay.items.ItemUtils;
import fr.eazyender.odyssey.gameplay.masteries.MasteryDB;
import fr.eazyender.odyssey.gameplay.stats.Classe;
import fr.eazyender.odyssey.gameplay.stats.CombatStats;
import fr.eazyender.odyssey.gameplay.stats.DamageHelper;
import fr.eazyender.odyssey.gameplay.stats.Stat;
public class AuraCastListener implements Listener {


	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			ItemStack item = p.getInventory().getItemInMainHand();
			boolean isCrit = DamageHelper.isCrit(p);
			if (item != null && (ItemUtils.getClass(item) == Classe.GUERRIER || ItemUtils.getClass(item) == Classe.ARCHER
					|| ItemUtils.getClass(item) == Classe.TANK)) {

				if (!SkillHitActivation.skillsActivation.containsKey(p)) {

					if (!isCrit)
						e.setDamage(DamageHelper.applyVariation(CombatStats.getStats(p).getStat(Stat.DAMAGE)));
					else
						e.setDamage(DamageHelper.applyVariation((int) (CombatStats.getStats(p).getStat(Stat.DAMAGE)
								* ((double) CombatStats.getStats(p).getStat(Stat.CRIT_DAMAGE)) / 100)));
				} else {
					SkillHitActivation.skillsActivation.get(p).activate(e, isCrit);
				}
			} else e.setDamage(DamageHelper.applyVariation(CombatStats.getStats(p).getStat(Stat.DAMAGE)));
			if (!(e.getEntity() instanceof Player))
				DamageHelper.animateDamage(p, (LivingEntity) e.getEntity(), (int) e.getDamage(), isCrit);
		}
	}

	
	
	ArrayList<Player> cancelNext = new ArrayList<>();
	@EventHandler
	public void onHeld(PlayerItemHeldEvent e) {
		if (e.getPlayer().isSneaking() && (MasteryDB.getClass(e.getPlayer().getUniqueId().toString()).equals("GUERRIER")  || MasteryDB.getClass(e.getPlayer().getUniqueId().toString()).equals("ARCHER") || MasteryDB.getClass(e.getPlayer().getUniqueId().toString()).equals("TANK"))) {
			if (!cancelNext.contains(e.getPlayer())) {
			Skill skill = Skills.getSkill(SkillsDB.getSkill(e.getPlayer().getUniqueId().toString(), e.getNewSlot() + 1), e.getPlayer());
			if (skill.canCast()) skill.launch();
			
			cancelNext.add(e.getPlayer());
			// fix a bug where if cancelled, the event is called another time
			
			e.setCancelled(true);
			} else cancelNext.remove(e.getPlayer());
		}
	}

}
