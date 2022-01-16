package fr.eazyender.odyssey.gameplay.aura;

import org.bukkit.plugin.PluginManager;

import fr.eazyender.odyssey.OdysseyPl;

public class AuraHandler {
	
	public static void init(PluginManager pm) {
		AuraHUD.initHUD();
		pm.registerEvents(new AuraHUD(), OdysseyPl.getOdysseyPlugin());
	}
}
