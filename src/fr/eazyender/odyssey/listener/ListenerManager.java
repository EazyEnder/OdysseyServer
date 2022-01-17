package fr.eazyender.odyssey.listener;


import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.gameplay.aura.AuraCastListener;
import fr.eazyender.odyssey.gameplay.items.ItemGuiListener;
import fr.eazyender.odyssey.player.CompassUtils;
import fr.eazyender.odyssey.player.StatsListener;
import fr.eazyender.odyssey.utils.block.BlockUtils;
import fr.eazyender.odyssey.utils.zone.ZoneUtils;

public class ListenerManager {

	OdysseyPl pl;

	TchatListener chatListener;
	ZoneUtils zUtils;
	CompassUtils cUtils;
	StatsListener sListener;
	BlockUtils bUtils;
	AuraCastListener auraListener;
	ItemGuiListener itemGuiListener;
	
	

	public ListenerManager(OdysseyPl pl) {
		this.pl = pl;
		pl.getServer().getPluginManager().registerEvents(chatListener = new TchatListener(), pl);
		pl.getServer().getPluginManager().registerEvents(zUtils = new ZoneUtils(), pl);
		pl.getServer().getPluginManager().registerEvents(cUtils = new CompassUtils(), pl);
		pl.getServer().getPluginManager().registerEvents(sListener = new StatsListener(), pl);
		pl.getServer().getPluginManager().registerEvents(bUtils = new BlockUtils(), pl);
		pl.getServer().getPluginManager().registerEvents(auraListener = new AuraCastListener(), pl);
		pl.getServer().getPluginManager().registerEvents(itemGuiListener = new ItemGuiListener(), pl);
	}

	public OdysseyPl getPl() {
		return pl;
	}

	public TchatListener getChatListener() {
		return chatListener;
	}

	public ZoneUtils getZoneUtils() {
		return zUtils;
	}

	public CompassUtils getCompassUtils() {
		return cUtils;
	}

	public StatsListener getStatsListener() {
		return sListener;
	}

	public BlockUtils getBlockUtils() {
		return bUtils;
	}

	
	
}
