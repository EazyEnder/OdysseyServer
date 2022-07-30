package fr.eazyender.odyssey.gameplay.city.building;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.eazyender.odyssey.utils.ItemTools;
import fr.eazyender.odyssey.utils.TextUtils;

public class BuildingCommand  implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		
		Player player = (Player) sender;
		
		if(args.length > 0) {
			
			if(args[0].equalsIgnoreCase("hammer")) {
			
				ItemStack hammer = ItemTools.getHammer();
				
				if(!player.getInventory().contains(hammer)) {
					player.getInventory().addItem(hammer);
					player.sendMessage(TextUtils.aide + "Vous avez obtenu un maillet de construction. Pour vous en servir prenez le en main et sélectionner le bâtiment que vous voulez puis visualisez la zone avec les particules avant de commencer sa construction.");
					player.sendMessage(TextUtils.aide + TextUtils.mouse_rc + " Changer la position");
					player.sendMessage(TextUtils.aide + TextUtils.mouse_lc + " Tourner le bâtiment");
					player.sendMessage(TextUtils.aide + TextUtils.keyboard_ctrl + TextUtils.mouse_lc + " Sélectionner un bâtiment");
					player.sendMessage(TextUtils.aide + TextUtils.keyboard_ctrl + TextUtils.mouse_lc + " Placer le bâtiment");
				}else {
					player.sendMessage(TextUtils.aide + "Vous avez déjà un maillet de construction dans votre inventaire.");
				}
				
			}else {
				player.sendMessage(TextUtils.server + "Usage : /bd <hammer/>");
			}
			
			
		}else {
			player.sendMessage(TextUtils.server + "Usage : /bd <hammer/>");
		}
		
		return false;
		
	}
}
