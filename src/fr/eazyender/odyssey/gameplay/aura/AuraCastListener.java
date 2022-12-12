package fr.eazyender.odyssey.gameplay.aura;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.gameplay.aura.skills.SkillHitActivation;
import fr.eazyender.odyssey.gameplay.aura.skills.SkillsDB;
import fr.eazyender.odyssey.gameplay.items.ItemUtils;
import fr.eazyender.odyssey.gameplay.masteries.MasteryDB;
import fr.eazyender.odyssey.gameplay.stats.Classe;
import fr.eazyender.odyssey.gameplay.stats.CombatStats;
import fr.eazyender.odyssey.gameplay.stats.DamageHelper;
import net.md_5.bungee.api.ChatColor;

public class AuraCastListener implements Listener {

	HashMap<Player, Long> GlobalCooldown = new HashMap<>();
	HashMap<Player, BukkitRunnable> castInformations = new HashMap<>();
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			ItemStack item = p.getInventory().getItemInMainHand();
			boolean isCrit = DamageHelper.rollCrit(p);
			if (item != null && (ItemUtils.getClass(item) == Classe.GUERRIER
					|| ItemUtils.getClass(item) == Classe.ARCHER || ItemUtils.getClass(item) == Classe.TANK)) {

				if (!SkillHitActivation.skillsActivation.containsKey(p)) {
					e.setDamage(
							DamageHelper
									.getBasicAttackDamage(CombatStats.getStats(p),
											MasteryDB.getMastery(p.getUniqueId().toString(),
													MasteryDB.getClass(p.getUniqueId().toString()).getMastery()),
											isCrit));
				} else {
					SkillHitActivation.skillsActivation.get(p).activate(e, isCrit);
				}
			} else
				e.setDamage(DamageHelper.getBasicAttackDamage(CombatStats.getStats(p),
						MasteryDB.getMastery(p.getUniqueId().toString(),
								MasteryDB.getClass(p.getUniqueId().toString()).getMastery()),
						isCrit));
			if (!(e.getEntity() instanceof Player))
				DamageHelper.animateDamage(p, (LivingEntity) e.getEntity(), (int) e.getDamage(), isCrit);
		}
	}
	

	ArrayList<Player> cancelNext = new ArrayList<>();

	@EventHandler
	public void onHeld(PlayerItemHeldEvent e) {
		// Check sneak, player has class and not mage, item class is player's class
		if (e.getPlayer().isSneaking() && MasteryDB.getClass(e.getPlayer().getUniqueId().toString()) != null && MasteryDB.getClass(e.getPlayer().getUniqueId().toString()) != Classe.MAGE && ItemUtils.getClass(e.getPlayer().getInventory().getItemInMainHand()) == MasteryDB.getClass(e.getPlayer().getUniqueId().toString())) {
			if (!cancelNext.contains(e.getPlayer())) {
				Skill skill = Skills.getSkill(
						SkillsDB.getSkill(e.getPlayer().getUniqueId().toString(), e.getNewSlot() + 1), e.getPlayer());
				castSkill(e.getPlayer(), skill, e.getPlayer().getInventory().getItem(e.getPreviousSlot()));
				cancelNext.add(e.getPlayer());
				// fix a bug where if cancelled, the event is called another time

				e.setCancelled(true);
			} else
				cancelNext.remove(e.getPlayer());
		}
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if (e.getPlayer().isSneaking() && MasteryDB.getClass(e.getPlayer().getUniqueId().toString()) != null && MasteryDB.getClass(e.getPlayer().getUniqueId().toString()) != Classe.MAGE && ItemUtils.getClass(e.getItemDrop().getItemStack()) == MasteryDB.getClass(e.getPlayer().getUniqueId().toString())) {
			Skill skill = Skills.getSkill(SkillsDB.getSkill(e.getPlayer().getUniqueId().toString(),
					e.getPlayer().getInventory().getHeldItemSlot() + 1), e.getPlayer());
			castSkill(e.getPlayer(), skill, e.getItemDrop().getItemStack());
			e.setCancelled(true);

		}
	}
	
	@EventHandler
	public void onHandSwap(PlayerSwapHandItemsEvent e) {
		if (e.getPlayer().isSneaking() && MasteryDB.getClass(e.getPlayer().getUniqueId().toString()) != null && MasteryDB.getClass(e.getPlayer().getUniqueId().toString()) != Classe.MAGE && ItemUtils.getClass(e.getPlayer().getInventory().getItemInMainHand()) == MasteryDB.getClass(e.getPlayer().getUniqueId().toString())) {
			Skill skill = Skills.getSkill(SkillsDB.getSkill(e.getPlayer().getUniqueId().toString(),
					10), e.getPlayer());
			castSkill(e.getPlayer(), skill, e.getMainHandItem());
			e.setCancelled(true);
		}
	}

	enum CastState {
		SUCCESS, NOAURA, ONCOOLDOWN, NOTFOUND
	}

	public void castSkill(Player p, Skill skill, ItemStack item) {
		if (skill != null) {
			if (skill.canCast()) {
				if (AuraHUD.getPlayerAura(p) >= skill.getAuraCost()) {
					if (!GlobalCooldown.containsKey(p) || System.currentTimeMillis() - GlobalCooldown.get(p) >= 1000) {
						AuraHUD.setPlayerAura(p, AuraHUD.getPlayerAura(p) - skill.getAuraCost());
						skill.launch();
						animateBossBar(p, skill, CastState.SUCCESS);
						GlobalCooldown.put(p, System.currentTimeMillis());
						p.setCooldown(item.getType(), 20);
					}
				} else {
					animateBossBar(p, skill, CastState.NOAURA);
				}
			} else
				animateBossBar(p, skill, CastState.ONCOOLDOWN);
		} else
			animateBossBar(p, null, CastState.NOTFOUND);
	}

	public void animateBossBar(Player p, Skill skill, CastState state) {
		BossBar bar = AuraHUD.player_bossbars.get(p.getUniqueId());
		switch (state) {
		case SUCCESS: {
			bar.setTitle(ChatColor.of("#5aa832") + skill.getId());
			bar.setColor(BarColor.GREEN);
			break;
		}
		
		case NOAURA: {
			bar.setTitle(ChatColor.of("#de1b14") + "Aura insuffisante !");
			bar.setColor(BarColor.RED);
			break;
		}
		case NOTFOUND: {
			bar.setTitle("Non trouvé");
			bar.setColor(BarColor.WHITE);
			break;
		}
		case ONCOOLDOWN: {
			bar.setTitle(ChatColor.of("#9716c9") + skill.getId() + " - " + skill.getRemainingCooldown(p) + "s");
			bar.setColor(BarColor.PURPLE);
			break;
		}
		}
		if (castInformations.containsKey(p)) castInformations.get(p).cancel();
		castInformations.put(p, new BukkitRunnable() {
			int i = 0;
			public void run() {
				if (i > 3 && (state == CastState.NOAURA || state == CastState.NOTFOUND || state == CastState.SUCCESS))
				{
					AuraHUD.player_bossbars.get(p.getUniqueId()).setColor(BarColor.YELLOW);
					AuraHUD.player_bossbars.get(p.getUniqueId()).setTitle("");
					this.cancel();
				}
				if (state == CastState.ONCOOLDOWN) {
					AuraHUD.player_bossbars.get(p.getUniqueId()).setTitle(ChatColor.of("#9716c9") + skill.getId() + " - " + skill.getRemainingCooldown(p) + "s");
					if ( skill.getRemainingCooldown(p) <= 0) {
						AuraHUD.player_bossbars.get(p.getUniqueId()).setColor(BarColor.YELLOW);
						AuraHUD.player_bossbars.get(p.getUniqueId()).setTitle("");
						this.cancel();
					}
				}
				
				i++;
			}
			
		});
		castInformations.get(p).runTaskTimer(OdysseyPl.getOdysseyPlugin(), 0, 20);
		

	}

}
