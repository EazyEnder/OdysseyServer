package fr.eazyender.odyssey.entity.projectiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import fr.eazyender.odyssey.gameplay.magic.spells.ISpell;

public class ProjectilesManager {
	
	public static HashMap<ISpell,IProjectile> projectiles = new HashMap<ISpell,IProjectile>();
	
	public static void refreshProjectiles() {
		List<ISpell> spells_to_remove = new ArrayList<ISpell>();
		for (ISpell spell : projectiles.keySet()) {
			IProjectile proj = projectiles.get(spell);
			if(proj.updateProjectile() != null) {
				spells_to_remove.add(spell);
			}
		}
		for (ISpell s : spells_to_remove) {
			projectiles.remove(s);
		}
	}
	
	public static void createProjectile(ISpell spell, Player sender, Location position, Vector force, Vector velocity,ProjectileProps.ProjectileTrigger trigger, Double trigger_info, ProjectileProps.ProjectileSource source, Double[] source_info, ProjectileProps.ProjectileExtendedType type) {
		IProjectile projectile = new IProjectile(spell, sender,position,force,velocity,trigger,trigger_info,source,source_info[0], source_info[1], type);
		
		projectiles.put(spell, projectile);
	}

}
