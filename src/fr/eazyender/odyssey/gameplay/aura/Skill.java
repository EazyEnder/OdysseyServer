package fr.eazyender.odyssey.gameplay.aura;

import org.bukkit.inventory.ItemStack;

public interface Skill {

	public boolean canCast();
	public int getCooldown();
	public void setCooldown(int cooldown);
	public ItemStack getItem();
	public boolean launch();
	

	
	
	
}
