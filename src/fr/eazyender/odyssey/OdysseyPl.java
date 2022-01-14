package fr.eazyender.odyssey;


import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.eazyender.odyssey.gameplay.magic.MagicHandler;
import fr.eazyender.odyssey.listener.ListenerManager;
import fr.eazyender.odyssey.commands.CommandCustomGive;
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
		
		FileTileEntity.loadFile();
		ZoneUtils.initZoneUtils();
		WorldUtils.initWorlds();
		BlockUtils.initBlocks();
		lManager = new ListenerManager(this);
		sqlManager = new SQLManager(this);
		MagicHandler.init(pm);
		getCommand("cgive").setExecutor(new CommandCustomGive());
		BlockUtils.initTileEntityLoop();
		
		
	}
	@Override
	public void onDisable() 
	{
		CompassUtils.setVisibleFalse();
		FileTileEntity.saveFile();
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
