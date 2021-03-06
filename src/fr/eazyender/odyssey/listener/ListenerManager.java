package fr.eazyender.odyssey.listener;


import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.dungeons.gui.DungeonGuiListener;
import fr.eazyender.odyssey.gameplay.aura.AuraCastListener;
import fr.eazyender.odyssey.gameplay.aura.gui.SkillsCommand;
import fr.eazyender.odyssey.gameplay.city.building.HammerHandler;
import fr.eazyender.odyssey.gameplay.city.building.objects.BuildObjectListener;
import fr.eazyender.odyssey.gameplay.items.ItemGuiListener;
import fr.eazyender.odyssey.gameplay.masteries.ClasseCommand;
import fr.eazyender.odyssey.player.CompassUtils;
import fr.eazyender.odyssey.player.StatsListener;
import fr.eazyender.odyssey.player.group.GroupListener;
import fr.eazyender.odyssey.player.harvest.HarvestHandler;
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
	HammerHandler hammerHandler;
	GroupListener playerGroupHandler;
	DungeonGuiListener dungeonGuiListener;
	ClasseCommand classeCommand;
	HarvestHandler harvestListener;
	SkillsCommand skillsCommand;
	BuildObjectListener bolistener;
	

	public ListenerManager(OdysseyPl pl) {
		this.pl = pl;
		pl.getServer().getPluginManager().registerEvents(chatListener = new TchatListener(), pl);
		pl.getServer().getPluginManager().registerEvents(zUtils = new ZoneUtils(), pl);
		pl.getServer().getPluginManager().registerEvents(cUtils = new CompassUtils(), pl);
		pl.getServer().getPluginManager().registerEvents(sListener = new StatsListener(), pl);
		pl.getServer().getPluginManager().registerEvents(bUtils = new BlockUtils(), pl);
		pl.getServer().getPluginManager().registerEvents(auraListener = new AuraCastListener(), pl);
		pl.getServer().getPluginManager().registerEvents(itemGuiListener = new ItemGuiListener(), pl);
		pl.getServer().getPluginManager().registerEvents(hammerHandler = new HammerHandler(), pl);
		pl.getServer().getPluginManager().registerEvents(playerGroupHandler = new GroupListener(), pl);
		pl.getServer().getPluginManager().registerEvents(dungeonGuiListener = new DungeonGuiListener(), pl);
		pl.getServer().getPluginManager().registerEvents(harvestListener = new HarvestHandler(), pl);
		pl.getServer().getPluginManager().registerEvents(classeCommand = new ClasseCommand(), pl);
		pl.getServer().getPluginManager().registerEvents(skillsCommand = new SkillsCommand(), pl);
		pl.getServer().getPluginManager().registerEvents(bolistener = new BuildObjectListener(), pl);
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
