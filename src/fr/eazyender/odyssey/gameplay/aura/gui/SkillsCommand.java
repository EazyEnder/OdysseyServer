package fr.eazyender.odyssey.gameplay.aura.gui;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.eazyender.odyssey.gameplay.aura.Skills;
import fr.eazyender.odyssey.gameplay.aura.skills.SkillsDB;
import fr.eazyender.odyssey.gameplay.items.ItemUtils;
import fr.eazyender.odyssey.gameplay.masteries.Mastery;
import fr.eazyender.odyssey.gameplay.masteries.MasteryDB;
import fr.eazyender.odyssey.gameplay.stats.Classe;
import fr.eazyender.odyssey.utils.NBTEditor;
import net.md_5.bungee.api.ChatColor;

public class SkillsCommand implements CommandExecutor, Listener {

	public static final int[] slotSkills = { 20, 21, 22, 23, 24, 29, 30, 31, 32, 33 };

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {

		Player p = (Player) arg0;

		if (!MasteryDB.getClass(p.getUniqueId().toString()).equals("null")) {

			Classe classe = MasteryDB.getClass(p.getUniqueId().toString());
			if (classe != null && classe != Classe.MAGE) {
				openSkills(p);
			}

		} else
			p.sendMessage(ChatColor.of("#FF0000") + "Tu n'as aucune classe !");
		return false;
	}

	static class SkillsCommandHolder implements InventoryHolder {

		int id;
		
		@Override
		public Inventory getInventory() {
			// TODO Auto-generated method stub
			return null;
		
		}

		public int getId() {
			return id;
		}

		public SkillsCommandHolder setId(int id) {
			this.id = id;
			return this;
		}

		
		
	}


	public static void openSkills(Player p) {
		Inventory inv = Bukkit.createInventory(new SkillsCommandHolder(), 9 * 6,
				ChatColor.of("#34eb8c") + "Gestion des sorts");
		for (int i = 0; i < 6; i++) {
			inv.setItem(i * 9, ItemUtils.getItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), " ", null, 0));
			inv.setItem(i * 9 + 1, ItemUtils.getItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), " ", null, 0));
			inv.setItem(i * 9 + 7, ItemUtils.getItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), " ", null, 0));
			inv.setItem(i * 9 + 8, ItemUtils.getItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), " ", null, 0));
		}
		for (int i = 0; i < 6; i++) {
			inv.setItem(i * 9 + 2, ItemUtils.getItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " ", null, 0));
			inv.setItem(i * 9 + 3, ItemUtils.getItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " ", null, 0));
			inv.setItem(i * 9 + 4, ItemUtils.getItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " ", null, 0));
			inv.setItem(i * 9 + 5, ItemUtils.getItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " ", null, 0));
			inv.setItem(i * 9 + 6, ItemUtils.getItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " ", null, 0));
		}
		inv.setItem(4,
				ItemUtils.getItem(new ItemStack(Material.PAPER), ChatColor.of("#34eb8c") + "Sorts", Arrays.asList(

						"§f       " + ChatColor.of("#F2EA74") + "Equipez vos sorts en cliquant sur vos emplacements",
						"§f             " + ChatColor.of("#F2EA74") + "de sorts puis sur le sort souhaité",
						ChatColor.of("#F2EA74") + "",
						"       " + ChatColor.of("#F2EA74")
								+ "Lancez un sort avec une combinaison Sneak + (Raccourci Hotbar)",
						ChatColor.of("#F2EA74") + "",
						"       " + ChatColor.of("#F2EA74") + "Pour lancer le sort qui correspond à votre slot hotbar ",
						"           " + ChatColor.of("#F2EA74") + "en cours d'utilisation, utilisez Sneak + Drop",
						ChatColor.of("#F2EA74") + "", ChatColor.of("#F2EA74")
								+ "Il existe également un emplacement de sort Sneak + Changement de Main"),
						0));

		for (int i = 0; i < 5; i++)
			inv.setItem(i + 11, ItemUtils.getItem(new ItemStack(Material.OAK_SIGN),
					ChatColor.of("#34abeb") + "Hotbar Slot " + (i + 1), null, 0));
		for (int i = 0; i < 4; i++)
			inv.setItem(i + 38, ItemUtils.getItem(new ItemStack(Material.OAK_SIGN),
					ChatColor.of("#34abeb") + "Hotbar Slot " + (i + 6), null, 0));

		// Set skills
		int slot = 1;
		for (int i : slotSkills) {

			if (SkillsDB.getSkill(p.getUniqueId().toString(), slot).equals("null"))
				inv.setItem(i, ItemUtils.getItem(new ItemStack(Material.LIME_WOOL),
						ChatColor.of("#2ffa44") + "[✓] Ajouter un sort", null, 0));
			else
				inv.setItem(i, addSkillLore(Skills.getSkillItem(SkillsDB.getSkill(p.getUniqueId().toString(), slot))));
			slot++;
		}

		inv.setItem(42, ItemUtils.getItem(new ItemStack(Material.OAK_SIGN),
				ChatColor.of("#34abeb") + "Changement de Main", null, 0));

		p.openInventory(inv);
	}

	public static void showSkills(Player p, Classe classe, int page, int slotSkill) {

		Inventory inv = Bukkit.createInventory(new SkillsCommandHolder().setId(slotSkill), 9 * 6, ChatColor.of("#34eb8c") + "Sorts : "
				+ classe.name().substring(0, 1).toUpperCase() + classe.name().substring(1).toLowerCase());

		boolean darkGray = true;
		for (int i = 0; i < 6; i++) {

			for (int j = 0; j < 9; j++) {

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

			String[] skills = Skills.getSkills(classe, masteryLvl);
			if (skills != null) {
				if (skills.length == 1) {

					if (getMastery(p, classe) >= masteryLvl)
						inv.setItem(i + 18, addNBTId(Skills.getSkillItem(skills[0]), skills[0]));
					else
						inv.setItem(i + 18, ItemUtils.getItem(new ItemStack(Material.YELLOW_DYE),
								ChatColor.of("#818385") + "?", null, 1));

				} else if (skills.length == 2) {

					if (getMastery(p, classe) >= masteryLvl) {

						inv.setItem(i + 17, addNBTId(Skills.getSkillItem(skills[0]), skills[0]));
						inv.setItem(i + 19, addNBTId(Skills.getSkillItem(skills[1]), skills[1]));
					} else {
						inv.setItem(i + 17, ItemUtils.getItem(new ItemStack(Material.YELLOW_DYE),
								ChatColor.of("#818385") + "?", null, 1));
						inv.setItem(i + 19, ItemUtils.getItem(new ItemStack(Material.YELLOW_DYE),
								ChatColor.of("#818385") + "?", null, 1));
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

	public static ItemStack addNBTId(ItemStack is, String id) {
		if (is == null) return null;
		ItemStack spell = is.clone();
		spell = NBTEditor.set(spell, id, "id");
		return spell;
	}
	
	public static ItemStack addSkillLore(ItemStack skillItem) {
		ItemStack newIs = skillItem.clone();
		ArrayList<String> lore = (ArrayList<String>) skillItem.getItemMeta().getLore();
		lore.add(" ");
		lore.add(ChatColor.of("#4f5f82") + "Clic Gauche pour changer");
		lore.add(ChatColor.of("#4f5f82") + "Clic Droit pour retirer");
		ItemMeta meta = newIs.getItemMeta();
		meta.setLore(lore);
		newIs.setItemMeta(meta);
		return newIs;
		
	}
}
