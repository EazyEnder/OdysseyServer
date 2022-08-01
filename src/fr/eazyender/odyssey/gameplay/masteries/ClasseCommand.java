package fr.eazyender.odyssey.gameplay.masteries;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.eazyender.odyssey.gameplay.items.ItemUtils;
import net.md_5.bungee.api.ChatColor;

public class ClasseCommand implements CommandExecutor, Listener {

	public static HashMap<Player, Long> cooldowns = new HashMap<>();

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {

		Player p = (Player) arg0;
		Inventory inv = Bukkit.createInventory(new ClasseCommandHolder(), 9,
				ChatColor.of("#034efc") + "Changement de classe");

		inv.setItem(0, ItemUtils.getItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), " ", null, 0));
		inv.setItem(1, ItemUtils.getItem(new ItemStack(Material.BARRIER), "§cFermer", null, 0));
		inv.setItem(2, ItemUtils.getItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), " ", null, 0));
		inv.setItem(3, getGlow(p, ItemUtils.getItem(new ItemStack(Material.IRON_SWORD), "§cGuerrier", null, 0), 1));
		inv.setItem(4, getGlow(p, ItemUtils.getItem(new ItemStack(Material.GLASS_BOTTLE), "§bMage", null, 0), 2));
		inv.setItem(5, getGlow(p, ItemUtils.getItem(new ItemStack(Material.BOW), "§fArcher", null, 0), 3));
		inv.setItem(6, getGlow(p, ItemUtils.getItem(new ItemStack(Material.SHIELD), "§eTank", null, 0), 4));
		inv.setItem(7, ItemUtils.getItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), " ", null, 0));
		inv.setItem(8, ItemUtils.getItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), " ", null, 0));

		p.openInventory(inv);

		return false;
	}

	public ItemStack getGlow(Player p, ItemStack is, int classeNum) {
		String classe = MasteryDB.getClass(p.getUniqueId().toString());
		if (classe == "null")
			return is;

		boolean glow = false;
		if (classeNum == 1)
			if (classe.equals("GUERRIER"))
				glow = true;
		if (classeNum == 2)
			if (classe.equals("MAGE"))
				glow = true;
		if (classeNum == 3)
			if (classe.equals("ARCHER"))
				glow = true;
		if (classeNum == 4)
			if (classe.equals("TANK"))
				glow = true;
		ItemStack isRenvoi = is.clone();
		ItemMeta meta = isRenvoi.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		isRenvoi.setItemMeta(meta);
		if (glow) {
			
			isRenvoi.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1);
			return isRenvoi;
		} else
			return isRenvoi;

	}

	class ClasseCommandHolder implements InventoryHolder {

		@Override
		public Inventory getInventory() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getInventory().getHolder() instanceof ClasseCommandHolder) {
			Player p = (Player)e.getWhoClicked();
			if (e.getCurrentItem() == null)
				return;
			e.setCancelled(true);
			if (e.getSlot() == 1)
				p.closeInventory();
			if (e.getSlot() == 0 || e.getSlot() == 2 || e.getSlot() == 7 || e.getSlot() == 8)
				return;
			if (e.getCurrentItem().getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) != 0)
				return;

			if (!cooldowns.containsKey((Player) e.getWhoClicked())
					|| System.currentTimeMillis() - cooldowns.get(p) > 60000) {
				if (e.getSlot() == 3)
					MasteryDB.setClass(p.getUniqueId().toString(), "GUERRIER");
				if (e.getSlot() == 4)
					MasteryDB.setClass(p.getUniqueId().toString(), "MAGE");
				if (e.getSlot() == 5)
					MasteryDB.setClass(p.getUniqueId().toString(), "ARCHER");
				if (e.getSlot() == 6)
					MasteryDB.setClass(p.getUniqueId().toString(), "TANK");
				p.setLevel(MasteryDB.getMastery(p.getUniqueId().toString(), Mastery.valueOf(MasteryDB.getClass(p.getUniqueId().toString()))));
				p.setExp(MasteryDB.getXp(p, Mastery.valueOf(MasteryDB.getClass(p.getUniqueId().toString()))));
				cooldowns.put((Player) e.getWhoClicked(), System.currentTimeMillis());
			} else {
				e.getWhoClicked().sendMessage(
						ChatColor.of("#FF0000") + "Tu dois attendre avant de pouvoir changer de classe à nouveau !");
				return;
			}

			e.getWhoClicked().closeInventory();
			e.getWhoClicked().sendMessage(ChatColor.of("#fc03eb") + "Vous avez changé de classe !");

		}
	}

}
