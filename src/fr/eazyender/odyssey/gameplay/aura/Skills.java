package fr.eazyender.odyssey.gameplay.aura;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.EulerAngle;

import fr.eazyender.odyssey.gameplay.aura.skills.tank.Taillade;
import fr.eazyender.odyssey.gameplay.items.ItemUtils;
import fr.eazyender.odyssey.gameplay.stats.Classe;
import net.md_5.bungee.api.ChatColor;

public class Skills {
	
	
	public static HashMap<LivingEntity, HashMap<Class<? extends Skill>, Long>> cooldowns = new HashMap<>();
	public static HashMap<LivingEntity, Class<? extends Skill>> combos = new HashMap<>();
	
	
	public static int getRemainingCooldown(LivingEntity p, Skill skill) {
		int cooldownSkill = skill.getCooldown();
		long activeCooldown = cooldowns.get(p).get(skill.getClass());
		return (int) ((cooldownSkill - (System.currentTimeMillis() - activeCooldown)) / 1000);
	}

	public static ItemStack getSkillItem(String id) {
		switch(id) {
		case "Taillade": return ItemUtils.getItem(new ItemStack(Material.YELLOW_DYE), ChatColor.of("#ffb300") + "Taillade", Arrays.asList(
				
				ChatColor.of("#bdb7ae") + "Effectue une taillade puissante", 
				ChatColor.of("#bdb7ae") + "Puissance: " + ChatColor.of("#eaed47") + "200%")
				
				, 2);
		
		
		}
		return null;
	}
	
	public static Skill getSkill(String id, Player p) {

		switch (id) {
			// Tank
			case "Taillade": return (Skill) new Taillade(p);
			
			
			
		}
		return null;
	}

	
	
	public static String[] getSkills(Classe classe, int mastery) {
		switch (classe) {
		case TANK: {
			switch (mastery) {
			case 0:
				return new String[] {"Taillade"};

			}
		}
		case GUERRIER: {
			switch (mastery) {

			}
		}
		case ARCHER: {
			switch (mastery) {

			}
		}
		default:
			break;
		}
		return null;

	}
	
	public static ItemStack getSkillModel(int modelData) {
		ItemStack is = new ItemStack(Material.COAL);
		ItemMeta meta = is.getItemMeta();
		meta.setCustomModelData(modelData);
		is.setItemMeta(meta);
		return is;
	}
	
	public static ArmorStand spawnModel(Location loc, EulerAngle angle) {                 
		ArmorStand stand = (ArmorStand) loc.getWorld().spawn(loc, ArmorStand.class, armorstand->{            
			
			armorstand.setVisible(false);                      
			armorstand.setGravity(false);                          
			armorstand.setMarker(true);                          
			armorstand.setInvulnerable(true);
			armorstand.setHeadPose(angle);
		});
		return stand;
	}
	
	public static boolean isTarget(Player p, Entity e) {
		return true;
	}
	
	
}
