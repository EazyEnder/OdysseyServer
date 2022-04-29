package fr.eazyender.odyssey.dungeons.gui;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class DungeonCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg0 instanceof Player) {
			
			
			Player p = (Player) arg0;

			DungeonGui.openGui(p, 1);
		}
		return false;
	}
	
	

}
