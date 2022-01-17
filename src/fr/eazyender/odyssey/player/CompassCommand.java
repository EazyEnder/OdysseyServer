package fr.eazyender.odyssey.player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CompassCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		
		Player p = (Player) sender;
		
		if(args.length > 0) {
			
			int resolution = Integer.parseInt(args[0]);
			
			CompassUtils.compass_resolution.replace(p.getUniqueId(), resolution);
			
		}
		
		return false;
		
	}
	
}
