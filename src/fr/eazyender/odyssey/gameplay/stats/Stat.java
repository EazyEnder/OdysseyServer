package fr.eazyender.odyssey.gameplay.stats;


import net.md_5.bungee.api.ChatColor;

public enum Stat {
	
	HEALTH("§7Vie"),DEFENSE("§7Défense"),DAMAGE("§7Dégâts"), CRIT_CHANCE("§7Chances de coup critique"), CRIT_DAMAGE("§7Dégâts de coup critique"), AURA("§6Aura"),REGENAURA("§6Regen Aura"),POWER("§7Dégâts magiques"),MP("§bMana"),REGENMP("§bRegen Mana"),
	FIRE(ChatColor.RED + "Element Feu"), WATER(ChatColor.of("#2990f0") + "Element Eau"), WIND(ChatColor.of("#c7edea") + "Element Vent"), EARTH(ChatColor.of("#8c4414") + "Element Terre"), LIGHT(ChatColor.of("#fff673") + "Element Lumière"), SHADOW(ChatColor.of("#4a4d54") + "Element Ombres");

	String showing;
	
	 Stat(String showing) {
		this.showing = showing;
	 }

	public String getShowing() {
		return showing;
	}
	 
	 
	
}
