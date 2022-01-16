package fr.eazyender.odyssey;



import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.eazyender.odyssey.gameplay.aura.AuraHUD;
import fr.eazyender.odyssey.gameplay.aura.AuraHandler;
import fr.eazyender.odyssey.gameplay.items.ItemCommand;
import fr.eazyender.odyssey.gameplay.magic.CommandTrySpell;
import fr.eazyender.odyssey.gameplay.magic.MagicHandler;
import fr.eazyender.odyssey.gameplay.magic.WandUtils;
import fr.eazyender.odyssey.gameplay.stats.PlayerStats;
import fr.eazyender.odyssey.listener.ListenerManager;
import fr.eazyender.odyssey.listener.TchatListener;
import fr.eazyender.odyssey.entity.EntityManager;
import fr.eazyender.odyssey.player.CompassUtils;
import fr.eazyender.odyssey.sql.SQLManager;
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
		
		FileTileEntity.loadFile();
		ZoneUtils.initZoneUtils();
		WorldUtils.initWorlds();
		BlockUtils.initBlocks();
		PlayerStats.init();
		EntityManager.initLoop(3);
		lManager = new ListenerManager(this);
		sqlManager = new SQLManager(this);
		MagicHandler.init(pm);
		AuraHandler.init(pm);
		getCommand("item").setExecutor(new ItemCommand());
		getCommand("tryspell").setExecutor(new CommandTrySpell());
		BlockUtils.initTileEntityLoop();
		
		
	}
	@Override
	public void onDisable() 
	{
		CompassUtils.setVisibleFalse();
		WandUtils.setVisibleFalse();
		AuraHUD.setVisibleFalse();
		FileTileEntity.saveFile();
		TchatListener.runRunnables();
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
