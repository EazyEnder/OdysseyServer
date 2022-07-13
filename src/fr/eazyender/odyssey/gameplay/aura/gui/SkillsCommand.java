package fr.eazyender.odyssey.gameplay.aura.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import fr.eazyender.odyssey.gameplay.items.ItemUtils;
import fr.eazyender.odyssey.gameplay.masteries.MasteryDB;
import net.md_5.bungee.api.ChatColor;

public class SkillsCommand implements CommandExecutor, Listener {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {

		Player p = (Player) arg0;
		String classe = MasteryDB.getClass(p.getUniqueId().toString());
		if (classe != null) {
			showMenu(p, classe, 1);
		} else
			p.sendMessage(ChatColor.of("#FF0000") + "Tu n'as aucune classe !");

		return false;
	}

	class SkillsCommandHolder implements InventoryHolder {

		@Override
		public Inventory getInventory() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getInventory().getHolder() instanceof SkillsCommandHolder) {
			e.setCancelled(true);

		}
	}

	public void showMenu(Player p, String classe, int page) {
		Inventory inv = Bukkit.createInventory(new SkillsCommandHolder(), 9 * 6, ChatColor.of("#34eb8c")
				+ "Skills : " + classe.substring(0, 1).toUpperCase() + classe.substring(1).toLowerCase());
		
		boolean darkGray= true;
		for (int i = 0; i < 6; i++) {
			
			for (int j = 0; j < 9; j++) {
				if (darkGray)
					inv.setItem(i*9 + j, ItemUtils.getItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " ", null, 0));
				else inv.setItem(i*9 + j, ItemUtils.getItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), " ", null, 0));
			}
			darkGray = !darkGray;
		}
		
		p.openInventory(inv);
	}
	
}
