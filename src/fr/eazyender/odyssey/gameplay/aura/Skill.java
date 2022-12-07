package fr.eazyender.odyssey.gameplay.aura;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public interface Skill {

	public boolean canCast();
	public int getCooldown();
	public int getRemainingCooldown(LivingEntity p);
	public void setCooldown(int cooldown);
	public ItemStack getItem();
	public boolean launch();
	public String getId();
	public int getAuraCost();

	
	
	
}
