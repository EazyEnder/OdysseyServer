package fr.eazyender.odyssey.dungeons.gui;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.eazyender.odyssey.dungeons.DungeonInstance;

public class DungeonCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg0 instanceof Player) {
			Player p = (Player) arg0;
			if (arg3.length < 1) {
				DungeonGui.openGui(p, 1);
			} else if (arg3[0].equalsIgnoreCase("quit")) {
				if (DungeonInstance.getInstance(p) != null) {
					DungeonInstance.getInstance(p).quit(p);
					
				}
				
			}
		}
		return false;
	}

}
