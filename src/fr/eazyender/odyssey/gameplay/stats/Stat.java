package fr.eazyender.odyssey.gameplay.stats;


import net.md_5.bungee.api.ChatColor;

public enum Stat {
	
	HEALTH("§7Vie"),DEFENSE("§7Défense"),DAMAGE("§7Dégats"),AURA("§6Aura"),REGENAURA("§6Regen Aura"),POWER("§7Dégats magiques"),MP("§bMana"),REGENMP("§bRegen Mana"),
	FIRE(ChatColor.RED + "Element Feu"), WATER(ChatColor.of("#2990f0") + "Element Eau"), WIND(ChatColor.of("#c7edea") + "Element Vent"), EARTH(ChatColor.of("#8c4414") + "Element Terre"), LIGHT(ChatColor.of("#fff673") + "Element Lumière"), SHADOW(ChatColor.of("#4a4d54") + "Element Ombres");

	String showing;
	
	 Stat(String showing) {
		this.showing = showing;
	 }

	public String getShowing() {
		return showing;
	}
	 
	 
	
}
