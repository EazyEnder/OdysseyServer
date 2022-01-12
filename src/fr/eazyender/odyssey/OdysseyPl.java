package fr.eazyender.odyssey;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.eazyender.odyssey.event.TchatEvent;
import fr.eazyender.odyssey.gameplay.magic.MagicHandler;
import fr.eazyender.odyssey.commands.CommandCustomGive;
import fr.eazyender.odyssey.event.PlayerEvent;
import fr.eazyender.odyssey.player.CompassUtils;
import fr.eazyender.odyssey.player.StatsEvent;
import fr.eazyender.odyssey.utils.block.BlockUtils;
import fr.eazyender.odyssey.utils.block.FileTileEntity;
import fr.eazyender.odyssey.utils.world.WorldUtils;
import fr.eazyender.odyssey.utils.zone.ZoneUtils;


public class OdysseyPl extends JavaPlugin{
	
private static OdysseyPl odysseypl;
	
	@Override
	public void onEnable() 
	{
		odysseypl = this;
		
		FileTileEntity.loadFile();
		
		ZoneUtils.initZoneUtils();
		WorldUtils.initWorlds();
		BlockUtils.initBlocks();
		
		
		PluginManager pm = getServer().getPluginManager();
		MagicHandler.init(pm);
		pm.registerEvents(new PlayerEvent(), this);
		pm.registerEvents(new TchatEvent(), this);
		pm.registerEvents(new ZoneUtils(), this);
		pm.registerEvents(new CompassUtils(), this);
		pm.registerEvents(new StatsEvent(), this);
		pm.registerEvents(new BlockUtils(), this);
		
		getCommand("cgive").setExecutor(new CommandCustomGive());
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			PlayerEvent.reload(player);
		}
		
		BlockUtils.initTileEntityLoop();
	}
	@Override
	public void onDisable() 
	{
		for (Player player : Bukkit.getOnlinePlayers()) {
			CompassUtils.compass_player.get(player.getUniqueId()).setVisible(false);
			CompassUtils.title_zone_player.get(player.getUniqueId()).setVisible(false);
		}
		
		FileTileEntity.saveFile();
	}
	
	public static OdysseyPl getOdysseyPlugin() {
		return odysseypl;
	}

}
