package fr.eazyender.odyssey.gameplay.aura.skills.tank;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.gameplay.aura.BasicSkill;
import fr.eazyender.odyssey.gameplay.aura.Skills;
import fr.eazyender.odyssey.gameplay.stats.DamageHelper;

public class Taillade extends BasicSkill {

	
	
	public Taillade(Player p) {
		super(p, 8000, "Taillade", 10);
	}


	@Override
	public boolean launch() {
		// Effect
		Location skillLoc = p.getLocation().add(p.getLocation().getDirection().multiply(1.35));
		ArmorStand skill = Skills.spawnModel(skillLoc.add(0, -0.8, 0), new EulerAngle(0, Math.toRadians(180), Math.toRadians(-10 + new Random().nextInt(10))));
		
		skill.getEquipment().setHelmet(Skills.getSkillModel(100));
		p.playSound(p.getLocation(), "taillade", 1f, 1f);
		new BukkitRunnable() {
			int i = 1;
			public void run() {
				skill.getEquipment().setHelmet(Skills.getSkillModel(100 + i));
				i++;
				if (i>7) {
					skill.remove();
					this.cancel();
				}
			}
		}.runTaskTimer(OdysseyPl.getOdysseyPlugin(), 1, 1);
		// Hitobx
		boolean isCrit = DamageHelper.rollCrit(p);
		for(Entity e : skill.getLocation().getWorld().getNearbyEntities(skill.getEyeLocation().add(0, 1, 0), 1.5,  0.2, 1.5)) {
			if (e != p && e != skill && e instanceof LivingEntity) {
				if (Skills.isTarget(p, e)) {
					
					DamageHelper.damage(p, DamageHelper.getAuraDamage(p, 200, isCrit), (LivingEntity)e, isCrit );
				}
			}
		}
		
		Skills.combos.put(p, Taillade.class);
		return true;
	}

	
	
}
