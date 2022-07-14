package fr.eazyender.odyssey.gameplay.aura;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import fr.eazyender.odyssey.gameplay.aura.skills.tank.SlashVertical;
import fr.eazyender.odyssey.gameplay.items.ItemUtils;
import fr.eazyender.odyssey.gameplay.stats.Classe;

public class Skill {

	public static HashMap<LivingEntity, HashMap<Class<? extends Skill>, Long>> cooldowns = new HashMap<>();

	public int cooldown;
	
	int modelData;
	String name;
	List<String> lore;
	

	public Skill(int cooldown, int modelData, String name, List<String> lore) {
		this.cooldown = cooldown;
		this.modelData = modelData;
		this.name = name;
		this.lore = lore;
	}

	public boolean launch(LivingEntity p, Class<? extends Skill> spell) {
		if (!cooldowns.containsKey(p) || !cooldowns.get(p).containsKey(spell)
				|| System.currentTimeMillis() - cooldowns.get(p).get(spell) > getCooldown()) {
			HashMap<Class<? extends Skill>, Long> newCooldown = new HashMap<>();
			if (cooldowns.get(p) != null)
				newCooldown = cooldowns.get(p);
			newCooldown.put(spell, System.currentTimeMillis());
			cooldowns.put(p, newCooldown);
			return true;
		} else
			return false;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public static int getRemainingCooldown(LivingEntity p, Skill skill) {
		int cooldownSkill = skill.getCooldown();
		long activeCooldown = cooldowns.get(p).get(skill.getClass());
		return (int) ((cooldownSkill - (System.currentTimeMillis() - activeCooldown)) / 1000);
	}

	@SuppressWarnings("serial")
	public static Class<? extends Skill> getSkill(Cast cast) {

		switch (cast.getType()) {
		case TANK: {
			return new HashMap<String, Class<? extends Skill>>() {
				{
					put("RLR", SlashVertical.class);
				}
			}.get(cast.getPattern());
		}

		case GUERRIER: {
			return new HashMap<String, Class<? extends Skill>>() {
				{
				}
			}.get(cast.getPattern());

		}
		case ARCHER: {

			return new HashMap<String, Class<? extends Skill>>() {
				{
				}
			}.get(cast.getPattern());
		}
		default:
			break;
		}

		return null;
	}

	@SuppressWarnings("rawtypes")
	public static Class[] getSkills(Classe classe, int mastery) {
		switch (classe) {
			case TANK: {
				switch (mastery) {
					case 0:
						return new Class[] { SlashVertical.class };

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
	
	public ItemStack getItem() {
		return ItemUtils.getItem(new ItemStack(Material.YELLOW_DYE), name, lore, modelData);
	}

}
