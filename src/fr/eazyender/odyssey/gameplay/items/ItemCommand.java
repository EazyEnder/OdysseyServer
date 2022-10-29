package fr.eazyender.odyssey.gameplay.items;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.eazyender.odyssey.gameplay.items.ItemInventoryHolder.State;
import fr.eazyender.odyssey.gameplay.stats.Stat;
import fr.eazyender.odyssey.utils.NBTEditor;

public class ItemCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {

		Player p = (Player) sender;

		// If args, create item
		if (args.length > 0) {

			if (args[0].equalsIgnoreCase("c")) {

				String id = args[1];
				if (ItemDB.getItem(id) == null) {

					if (p.getInventory().getItemInMainHand() != null
							&& p.getInventory().getItemInMainHand().getType() != Material.AIR) {

						// Add item into DB
						ItemStack is = p.getInventory().getItemInMainHand();
						is = NBTEditor.set(is, id, "id");
						ItemDB.addItem(id, is);
						p.sendMessage("§aItem ajouté !");

					} else {
						p.sendMessage("§cItem invalide");
						return false;
					}
				} else {
					p.sendMessage("§cCet item existe déjà !");
					return false;
				}
			} else if (args[0].equalsIgnoreCase("this")) {
				
				if (ItemUtils.getInfo(p.getInventory().getItemInMainHand(), "id") != null) {
					if (ItemDB.getItem(ItemUtils.getInfo(p.getInventory().getItemInMainHand(), "id")) != null) {
						openItemGui(p,  new ItemInventoryHolder(State.ITEM, 0).setIdItem(ItemUtils.getInfo(p.getInventory().getItemInMainHand(), "id")));
					} else p.sendMessage("§cItem non trouvé dans la bdd");
					
				} else p.sendMessage("§cCet item n'a pas d'id !");
				
			} else {
				String id = args[0];
				if (ItemDB.getItem(id) != null) {
					openItemGui(p,  new ItemInventoryHolder(State.ITEM, 0).setIdItem(id));
				} else
					p.sendMessage("§cCet item n'existe pas !");
				return false;
			}

		}
		// Open gui

		openItemsGui(p, 0);

		return false;
	}

	public static void openItemsGui(Player p, int page) {

		Inventory inv = Bukkit.createInventory(new ItemInventoryHolder(State.MAIN, page), 54, "Page " + (page + 1));

		ArrayList<ItemStack> items = ItemDB.getItems();
		if (items != null) {
			int slot = 0;
			for (int i = page * 45; i < (page + 1) * 45; i++) {
				if (items.size() > i) {
					inv.setItem(slot, items.get(i));
				} else
					break;
				slot++;
			}
			if (page > 0)
				inv.setItem(45, ItemUtils.getItem(new ItemStack(Material.ARROW), "Page " + page, null, 0));
			if (items.size() > (page * 45) + 45)
				inv.setItem(53, ItemUtils.getItem(new ItemStack(Material.ARROW), "Page " + (page + 2), null, 0));
		}
		p.openInventory(inv);

	}

	public static void openItemGui(Player p, ItemInventoryHolder holder) {

		Inventory inv = Bukkit.createInventory(holder.setState(State.ITEM).setIdItem(holder.getIdItem()), 36, holder.getIdItem());
		
		ItemStack item = ItemDB.getItem(holder.getIdItem());
		inv.setItem(0, ItemUtils.getItem(new ItemStack(Material.ARROW), "§8Retour", null, 0));
		inv.setItem(1, ItemUtils.getItem(new ItemStack(Material.DISPENSER), "§aGive l'item", null, 0));
		int slot = 2;
		for(Stat stat : Stat.values()) {
			inv.setItem(slot, ItemUtils.getItem(new ItemStack(Material.BOOK), stat.name() + " : " + ItemUtils.getStat(item, stat), null, 0));
			slot++;
		}
		inv.setItem(18, ItemUtils.getItem(new ItemStack(Material.OAK_SIGN), "§8Model data : " + ItemUtils.getNumericInfo(item, "CustomModelData") , null, 0));
		inv.setItem(19, ItemUtils.getItem(new ItemStack(Material.REDSTONE), "§8Type : " + ItemUtils.getInfo(item, "classe") , null, 0));
		inv.setItem(20, ItemUtils.getItem(new ItemStack(Material.BLAZE_ROD), "§8Rank : " + ItemUtils.getInfo(item, "rank") , null, 0));
		inv.setItem(21, ItemUtils.getItem(new ItemStack(Material.SPRUCE_SIGN), "§8Name" , null, 0));
		inv.setItem(22, ItemUtils.getItem(new ItemStack(Material.WRITABLE_BOOK), "§8Description" , null, 0));
		inv.setItem(23, ItemUtils.getItem(new ItemStack(Material.CLOCK), "§6Update Lore" , null, 0));
		inv.setItem(24, ItemUtils.getItem(new ItemStack(Material.EXPERIENCE_BOTTLE), "§aLevel requis" , null, 0));
		
		inv.setItem(35, ItemUtils.getItem(new ItemStack(Material.BARRIER), "§cSupprimer", null, 0));
		p.openInventory(inv);

	}
	
	public static void openStatEditor(Player p, ItemInventoryHolder holder, String stat) {
		ItemStack item = ItemDB.getItem(holder.getIdItem());
		Inventory inv = null;
		if (!stat.equals("CustomModelData") && !stat.equals("Level"))
			inv = Bukkit.createInventory(holder.setState(State.STAT).setStat(stat), 54, "§l" + stat + " : " + ItemUtils.getStat(item, Stat.valueOf(stat)));
		else 
			inv = Bukkit.createInventory(holder.setState(State.STAT).setStat(stat), 54, "§l" + stat + " : " + ItemUtils.getNumericInfo(item, stat));
		
		inv.setItem(0, ItemUtils.getItem(new ItemStack(Material.ARROW), "§8Retour", null, 0));
		
		if (!stat.equals("CustomModelData") && !stat.equals("Level"))
			inv.setItem(4, ItemUtils.getItem(new ItemStack(Material.BOOK), "§l" + stat + " : " + ItemUtils.getStat(item,  Stat.valueOf(stat)), null, 0));
		else inv.setItem(4, ItemUtils.getItem(new ItemStack(Material.BOOK), "§l" + stat + " : " +  ItemUtils.getNumericInfo(item, stat), null, 0));
		
		
		inv.setItem(3, ItemUtils.getItem(new ItemStack(Material.RED_WOOL), "§c-1", null, 0));
		inv.setItem(12, ItemUtils.getItem(new ItemStack(Material.RED_WOOL), "§c-5", null, 0));
		inv.setItem(21, ItemUtils.getItem(new ItemStack(Material.RED_WOOL), "§c-10", null, 0));
		inv.setItem(30, ItemUtils.getItem(new ItemStack(Material.RED_WOOL), "§c-25", null, 0));
		inv.setItem(39, ItemUtils.getItem(new ItemStack(Material.RED_WOOL), "§c-50", null, 0));
		inv.setItem(48, ItemUtils.getItem(new ItemStack(Material.RED_WOOL), "§c-100", null, 0));
		
		inv.setItem(5, ItemUtils.getItem(new ItemStack(Material.LIME_WOOL), "§a+1", null, 0));
		inv.setItem(14, ItemUtils.getItem(new ItemStack(Material.LIME_WOOL), "§a+5", null, 0));
		inv.setItem(23, ItemUtils.getItem(new ItemStack(Material.LIME_WOOL), "§a+10", null, 0));
		inv.setItem(32, ItemUtils.getItem(new ItemStack(Material.LIME_WOOL), "§a+25", null, 0));
		inv.setItem(41, ItemUtils.getItem(new ItemStack(Material.LIME_WOOL), "§a+50", null, 0));
		inv.setItem(50, ItemUtils.getItem(new ItemStack(Material.LIME_WOOL), "§a+100", null, 0));
		
		p.openInventory(inv);
	}
	
	public static void openTypeEditor(Player p, ItemInventoryHolder holder) {
		ItemStack item = ItemDB.getItem(holder.getIdItem());
		Inventory inv = Bukkit.createInventory(holder.setState(State.TYPE), 9, "§lType : "  + ItemUtils.getInfo(item, "classe"));
		inv.setItem(0, ItemUtils.getItem(new ItemStack(Material.ARROW), "§8Retour", null, 0));
		inv.setItem(1, ItemUtils.getItem(new ItemStack(Material.GLASS_BOTTLE), "§eMagie", null, 0));
		inv.setItem(2, ItemUtils.getItem(new ItemStack(Material.IRON_SWORD), "§eGuerrier", null, 0));
		inv.setItem(3, ItemUtils.getItem(new ItemStack(Material.BOW), "§eArcher", null, 0));
		inv.setItem(4, ItemUtils.getItem(new ItemStack(Material.SHIELD), "§eTank", null, 0));
		
		
		p.openInventory(inv);
	}
	
	public static void openRankEditor(Player p, ItemInventoryHolder holder) {
		ItemStack item = ItemDB.getItem(holder.getIdItem());
		Inventory inv = Bukkit.createInventory(holder.setState(State.RANK), 9, "§lRank : "  + ItemUtils.getInfo(item, "rank"));
		inv.setItem(0, ItemUtils.getItem(new ItemStack(Material.ARROW), "§8Retour", null, 0));
		inv.setItem(1, ItemUtils.getItem(new ItemStack(Material.LIGHT_GRAY_WOOL), "§7Commun", null, 0));
		inv.setItem(2, ItemUtils.getItem(new ItemStack(Material.BLUE_WOOL), "§1Rare", null, 0));
		inv.setItem(3, ItemUtils.getItem(new ItemStack(Material.PURPLE_WOOL), "§dEpic", null, 0));
		inv.setItem(4, ItemUtils.getItem(new ItemStack(Material.YELLOW_WOOL), "§eUnique", null, 0));
		inv.setItem(5, ItemUtils.getItem(new ItemStack(Material.ORANGE_WOOL), "§6Légendaire", null, 0));
		
		p.openInventory(inv);
	}
	
	public static HashMap<Player, String> editingName = new HashMap<>();
	public static HashMap<Player, String> editingLore = new HashMap<>();
	
	
	public static void openNameEditor(Player p, ItemInventoryHolder holder) {
		p.closeInventory();
		editingName.put(p, holder.getIdItem());
		p.sendMessage("§aEcrivez un nouveau name dans le chat ! Sneak pour cancel.");
		
	}
	
	
	public static void openLoreEditor(Player p, ItemInventoryHolder holder) {
		p.closeInventory();
		editingLore.put(p, holder.getIdItem());
		p.sendMessage("§aEcrivez une description dans le livre, puis droppez-le ! Sneak pour cancel.");
		p.getInventory().addItem(new ItemStack(Material.WRITABLE_BOOK));
		
	}
	
}
