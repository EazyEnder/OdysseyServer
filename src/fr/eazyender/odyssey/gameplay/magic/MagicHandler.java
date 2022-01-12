package fr.eazyender.odyssey.gameplay.magic;

import org.bukkit.plugin.PluginManager;

import fr.eazyender.odyssey.OdysseyPl;

public class MagicHandler {
	
	public static void init(PluginManager pm) {
		WandUtils.initWand();
		RuneUtils.initRunes();
		pm.registerEvents(new LaunchMagicUtils(), OdysseyPl.getOdysseyPlugin());
		pm.registerEvents(new WandUtils(), OdysseyPl.getOdysseyPlugin());
	}

}
