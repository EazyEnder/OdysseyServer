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

import fr.eazyender.odyssey.gameplay.aura.Skill;
import fr.eazyender.odyssey.gameplay.items.ItemUtils;
import fr.eazyender.odyssey.gameplay.masteries.Mastery;
import fr.eazyender.odyssey.gameplay.masteries.MasteryDB;
import fr.eazyender.odyssey.gameplay.stats.Classe;
import net.md_5.bungee.api.ChatColor;

public class SkillsCommand implements CommandExecutor, Listener {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {

		Player p = (Player) arg0;

		if (!MasteryDB.getClass(p.getUniqueId().toString()).equals("null")) {

			Classe classe = Classe.valueOf(MasteryDB.getClass(p.getUniqueId().toString()));
			if (classe != null) {
				showMenu(p, classe, 1);
			}

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

	@SuppressWarnings("deprecation")
	public void showMenu(Player p, Classe classe, int page) {

		Inventory inv = Bukkit.createInventory(new SkillsCommandHolder(), 9 * 6, ChatColor.of("#34eb8c") + "Skills : "
				+ classe.name().substring(0, 1).toUpperCase() + classe.name().substring(1).toLowerCase());

		boolean darkGray = true;
		for (int i = 0; i < 6; i++) {

			for (int j = 0; j < 9; j ++) {
				
				if (darkGray)
					inv.setItem(i * 9 + j,
							ItemUtils.getItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " ", null, 0));
				else
					inv.setItem(i * 9 + j,
							ItemUtils.getItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), " ", null, 0));
				darkGray = !darkGray;
			}
			darkGray = !darkGray;
			
		}
		p.openInventory(inv);
		
		int masteryLvl = (page - 1) * 45;
		for (int i = 0; i < 9; i++) {

			inv.setItem(45 + i, ItemUtils.getItem(new ItemStack(Material.SPRUCE_SIGN),
					ChatColor.of("#42eff5") + "Maîtrise requise : " + masteryLvl, null, 0));

			@SuppressWarnings("rawtypes")
			Class[] skills = Skill.getSkills(classe, masteryLvl);
			if (skills != null) {
				if (skills.length == 1) {
					try {
						if (getMastery(p, classe) >= masteryLvl)
							inv.setItem(i + 18, ((Skill) skills[0].newInstance()).getItem());
						else
							inv.setItem(i + 18, ItemUtils.getItem(new ItemStack(Material.YELLOW_DYE),
									ChatColor.of("#818385") + "?", null, 1));
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}
				} else if (skills.length == 2) {
					try {
						if (getMastery(p, classe) >= masteryLvl) {

							inv.setItem(i + 17, ((Skill) skills[0].newInstance()).getItem());
							inv.setItem(i + 19, ((Skill) skills[1].newInstance()).getItem());
						} else {
							inv.setItem(i + 17, ItemUtils.getItem(new ItemStack(Material.YELLOW_DYE),
									ChatColor.of("#818385") + "?", null, 1));
							inv.setItem(i + 19, ItemUtils.getItem(new ItemStack(Material.YELLOW_DYE),
									ChatColor.of("#818385") + "?", null, 1));
						}
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			masteryLvl += 5;
		}

		p.openInventory(inv);
	}

	public static int getMastery(Player p, Classe classe) {
		switch (classe) {
		case GUERRIER:
			return MasteryDB.getMastery(p.getUniqueId().toString(), Mastery.GUERRIER);
		case TANK:
			return MasteryDB.getMastery(p.getUniqueId().toString(), Mastery.TANK);
		case ARCHER:
			return MasteryDB.getMastery(p.getUniqueId().toString(), Mastery.ARCHER);
		default:
			return 0;
		}
	}

}
