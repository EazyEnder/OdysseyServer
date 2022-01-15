package fr.eazyender.odyssey.gameplay.items;

import net.md_5.bungee.api.ChatColor;

public enum ItemRank {

	COMMUN(ChatColor.of("#c7c0bd")), RARE(ChatColor.of("#6e94e6")), EPIC(ChatColor.of("#5316d9")), UNIQUE(ChatColor.of("#d97816")), LEGENDAIRE(ChatColor.of("#ff0000"));
	
	ChatColor color;
	
	ItemRank(ChatColor color) {
		this.color = color;
	}
	
	ChatColor getColor(){
		return color;
	}

}
