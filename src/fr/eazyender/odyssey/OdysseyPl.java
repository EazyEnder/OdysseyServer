package fr.eazyender.odyssey;



import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.eazyender.odyssey.gameplay.aura.AuraHUD;
import fr.eazyender.odyssey.gameplay.aura.AuraHandler;
import fr.eazyender.odyssey.gameplay.aura.gui.SkillsCommand;
import fr.eazyender.odyssey.gameplay.city.building.BuildManager;
import fr.eazyender.odyssey.gameplay.city.building.BuildingCommand;
import fr.eazyender.odyssey.gameplay.items.ItemCommand;
import fr.eazyender.odyssey.gameplay.magic.CommandTrySpell;
import fr.eazyender.odyssey.gameplay.magic.MagicHandler;
import fr.eazyender.odyssey.gameplay.magic.WandUtils;
import fr.eazyender.odyssey.gameplay.masteries.ClasseCommand;
import fr.eazyender.odyssey.gameplay.masteries.ExpHandler;
import fr.eazyender.odyssey.gameplay.stats.PlayerStats;
import fr.eazyender.odyssey.listener.ListenerManager;
import fr.eazyender.odyssey.listener.TchatListener;
import fr.eazyender.odyssey.dungeons.Dungeon;
import fr.eazyender.odyssey.dungeons.DungeonStaffCommand;
import fr.eazyender.odyssey.dungeons.gui.DungeonCommand;
import fr.eazyender.odyssey.entity.EntityManager;
import fr.eazyender.odyssey.player.CompassCommand;
import fr.eazyender.odyssey.player.CompassUtils;
import fr.eazyender.odyssey.player.group.CommandAccept;
import fr.eazyender.odyssey.player.group.CommandGroup;
import fr.eazyender.odyssey.player.harvest.HarvestHandler;
import fr.eazyender.odyssey.sounds.SoundsManager;
import fr.eazyender.odyssey.sql.SQLManager;
import fr.eazyender.odyssey.utils.ResourceItems;
import fr.eazyender.odyssey.utils.block.BlockUtils;
import fr.eazyender.odyssey.utils.block.FileTileEntity;
import fr.eazyender.odyssey.utils.world.WorldUtils;
import fr.eazyender.odyssey.utils.zone.ZoneUtils;


public class OdysseyPl extends JavaPlugin{
	
private static OdysseyPl odysseypl;
	
	ListenerManager lManager;
	SQLManager sqlManager;
	
	@Override
	public void onEnable() 
	{
		odysseypl = this;
		PluginManager pm = getServer().getPluginManager();
		saveDefaultConfig();
		ConfigurationSerialization.registerClass(Dungeon.class, "Dungeon");
		
		FileTileEntity.loadFile();
		ZoneUtils.initZoneUtils();
		WorldUtils.initWorlds();
		BlockUtils.initBlocks();
		MagicHandler.init(pm);
		AuraHandler.init(pm);
		PlayerStats.init();
		ExpHandler.init();
		EntityManager.initLoop(3);
		lManager = new ListenerManager(this);
		sqlManager = new SQLManager(this);
		getCommand("item").setExecutor(new ItemCommand());
		getCommand("tryspell").setExecutor(new CommandTrySpell());
		getCommand("accept").setExecutor(new CommandAccept());
		getCommand("group").setExecutor(new CommandGroup());
		getCommand("compass").setExecutor(new CompassCommand());
		getCommand("dstaff").setExecutor(new DungeonStaffCommand());
		getCommand("dj").setExecutor(new DungeonCommand());
		getCommand("build").setExecutor(new BuildingCommand());
		getCommand("classe").setExecutor(new ClasseCommand());
		getCommand("skills").setExecutor(new SkillsCommand());
		BlockUtils.initTileEntityLoop();
		BuildManager.initBuildManager();
		HarvestHandler.initHandler();
		SoundsManager.initSounds(20);
		ResourceItems.initItems();
		
		
		
	}
	@Override
	public void onDisable() 
	{
		CompassUtils.setVisibleFalse();
		WandUtils.setVisibleFalse();
		AuraHUD.setVisibleFalse();
		FileTileEntity.saveFile();
		TchatListener.runRunnables();
		HarvestHandler.forceRespawnAllRegenBlocks();
		BuildManager.saveFile();
	}
	
	public static OdysseyPl getOdysseyPlugin() {
		return odysseypl;
	}
	
	public ListenerManager getListenerManager() {
		return lManager;
	}
	public SQLManager getSqlManager() {
		return sqlManager;
	}

	
}
