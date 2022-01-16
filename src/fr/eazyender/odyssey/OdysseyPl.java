package fr.eazyender.odyssey;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.eazyender.odyssey.gameplay.items.ItemCommand;
import fr.eazyender.odyssey.gameplay.magic.CommandTrySpell;
import fr.eazyender.odyssey.gameplay.magic.MagicHandler;
import fr.eazyender.odyssey.gameplay.magic.WandUtils;
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
		EntityManager.initLoop(3);
		lManager = new ListenerManager(this);
		sqlManager = new SQLManager(this);
		MagicHandler.init(pm);
		getCommand("item").setExecutor(new ItemCommand());
		getCommand("tryspell").setExecutor(new CommandTrySpell());
		BlockUtils.initTileEntityLoop();
		
		
	}
	@Override
	public void onDisable() 
	{
		CompassUtils.setVisibleFalse();
		FileTileEntity.saveFile();
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			WandUtils.setVisibleFalse(player);
			if(TchatListener.bubbles.containsKey(player.getUniqueId())) TchatListener.bubbles.get(player.getUniqueId()).run();
			}
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
