package fr.eazyender.odyssey.gameplay.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.gameplay.items.ItemInventoryHolder.State;
import fr.eazyender.odyssey.gameplay.stats.Classe;
import fr.eazyender.odyssey.gameplay.stats.Stat;
import fr.eazyender.odyssey.utils.NBTEditor;
import net.md_5.bungee.api.ChatColor;

public class ItemGuiListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getInventory().getHolder() instanceof ItemInventoryHolder) {
			ItemInventoryHolder holder = (ItemInventoryHolder) e.getInventory().getHolder();
			Player p = (Player) e.getWhoClicked();
			if (holder.getState() == ItemInventoryHolder.State.MAIN) {
				if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
					if (e.getSlot() == 45) {
						ItemCommand.openItemsGui(p, holder.getPage() - 1);
						return;
					}
					if (e.getSlot() == 53) {
						ItemCommand.openItemsGui(p, holder.getPage() + 1);
						return;
					}
					e.setCancelled(true);
					ItemCommand.openItemGui(p, holder.setIdItem(ItemUtils.getInfo(e.getCurrentItem(), "id")));

				}
				return;
			}
			if (holder.getState() == ItemInventoryHolder.State.ITEM) {
				if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
					e.setCancelled(true);
					if (e.getSlot() == 0) {
						ItemCommand.openItemsGui(p, holder.getPage());
						return;
					}
					if (e.getSlot() == 1) {
						ItemUtils.giveItem(p, holder.getIdItem());
						return;
					}
					if (e.getSlot() == 18) {
						ItemCommand.openStatEditor(p, holder, "CustomModelData");
						return;
					}

					if (e.getSlot() == 19) {
						ItemCommand.openTypeEditor(p, holder);
						return;
					}

					if (e.getSlot() == 20) {
						ItemCommand.openRankEditor(p, holder);
						return;
					}

					if (e.getSlot() == 21) {
						ItemCommand.openNameEditor(p, holder);
						return;
					}

					if (e.getSlot() == 22) {
						ItemCommand.openLoreEditor(p, holder);
						return;
					}

					if (e.getSlot() == 23) {

						ItemStack item = ItemDB.getItem(holder.getIdItem());
						ArrayList<String> lore = ItemUtils.buildLore(item);
						ItemMeta meta = item.getItemMeta();
						meta.setLore(lore);
						meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
						item.setItemMeta(meta);
						ItemDB.updateItem(holder.getIdItem(), NBTEditor.getItemNBTTag(item));
						
						
						return;
					}
					
					
					if (e.getSlot() == 24) {
						ItemCommand.openStatEditor(p, holder, "Level");
						return;
					}
					
					
					

					if (e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().hasDisplayName()
							&& Stat.valueOf(e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[0]) != null) {
						ItemCommand.openStatEditor(p, holder,
								e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[0]);
					}

				}
				return;
			}
			if (holder.getState() == ItemInventoryHolder.State.STAT) {

				if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {

					if (e.getSlot() == 0) {
						ItemCommand.openItemGui(p, holder);
						return;
					}

					if (e.getCurrentItem().getType().name().contains("WOOL")) {
						int operation = 0;

						if (e.getSlot() == 3)
							operation = -1;
						if (e.getSlot() == 12)
							operation = -5;
						if (e.getSlot() == 21)
							operation = -10;
						if (e.getSlot() == 30)
							operation = -25;
						if (e.getSlot() == 39)
							operation = -50;
						if (e.getSlot() == 48)
							operation = -100;

						if (e.getSlot() == 5)
							operation = 1;
						if (e.getSlot() == 14)
							operation = 5;
						if (e.getSlot() == 23)
							operation = 10;
						if (e.getSlot() == 32)
							operation = 25;
						if (e.getSlot() == 41)
							operation = 50;
						if (e.getSlot() == 50)
							operation = 100;

						ItemStack is = ItemDB.getItem(holder.getIdItem());

						if (holder.getStat().equals("CustomModelData") || holder.getStat().equals("Level")) {
							is = NBTEditor.set(is, ItemUtils.getNumericInfo(is, holder.getStat()) + operation,
									holder.getStat());
						} else {

							is = NBTEditor.set(is, ItemUtils.getStat(is, Stat.valueOf(holder.getStat())) + operation,
									holder.getStat());
						}
						ItemDB.updateItem(holder.getIdItem(), NBTEditor.getItemNBTTag(is));

						ItemCommand.openStatEditor(p, holder, holder.getStat());

					}
				}

				return;
			}
			if (holder.getState() == ItemInventoryHolder.State.TYPE) {
				if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
					if (e.getSlot() == 0) {
						ItemCommand.openItemGui(p, holder);
						return;
					}
			
					Classe type = Classe.values()[e.getSlot() - 1];

					ItemStack is = ItemDB.getItem(holder.getIdItem());
					is = NBTEditor.set(is, type.name(), "classe");
					ItemDB.updateItem(holder.getIdItem(), NBTEditor.getItemNBTTag(is));

					ItemCommand.openTypeEditor(p, holder);

				}
				return;

			}

			if (holder.getState() == ItemInventoryHolder.State.RANK) {
				if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
					if (e.getSlot() == 0) {
						ItemCommand.openItemGui(p, holder);
						return;
					}

					ItemRank rank = ItemRank.values()[e.getSlot() - 1];

					ItemStack is = ItemDB.getItem(holder.getIdItem());
					is = NBTEditor.set(is, rank.name(), "rank");
					ItemDB.updateItem(holder.getIdItem(), NBTEditor.getItemNBTTag(is));

					ItemCommand.openRankEditor(p, holder);

				}
				return;
			}

		}
	}

	@EventHandler
	public void onDrop(PlayerToggleSneakEvent e) {
		if (ItemCommand.editingLore.containsKey(e.getPlayer()) || ItemCommand.editingName.containsKey(e.getPlayer())) {
			ItemCommand.editingLore.remove(e.getPlayer());
			ItemCommand.editingName.remove(e.getPlayer());
			e.getPlayer().sendMessage("§cCancel !");
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if (ItemCommand.editingName.containsKey(e.getPlayer())) {
			e.setCancelled(true);
			Player p = e.getPlayer();
			String name = ChatColor.translateAlternateColorCodes('&', e.getMessage());
			
			ItemStack item = ItemDB.getItem(ItemCommand.editingName.get(p));
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(name);
			item.setItemMeta(meta);
			ItemDB.updateItem(ItemCommand.editingName.get(p), NBTEditor.getItemNBTTag(item));
			new BukkitRunnable() {
				public void run() {
					ItemCommand.openItemGui(p, new ItemInventoryHolder(State.ITEM, ItemCommand.editingName.get(p)));
					ItemCommand.editingName.remove(p);
				}
			}.runTaskLater(OdysseyPl.getOdysseyPlugin(), 1L);

			p.sendMessage("§aNom enregistré.");
		}
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if (ItemCommand.editingLore.containsKey(e.getPlayer())) {
			if (e.getItemDrop().getItemStack().getType() == Material.WRITABLE_BOOK) {
				
			
				Player p = e.getPlayer();
				
				BookMeta bookMeta = (BookMeta) e.getItemDrop().getItemStack().getItemMeta();
				List<String> description = bookMeta.getPages();

				ItemStack item = ItemDB.getItem(ItemCommand.editingLore.get(p));
				item = NBTEditor.set(item, String.join("", description), "description");
				
				ItemDB.updateItem(ItemCommand.editingLore.get(p), NBTEditor.getItemNBTTag(item));
				ItemCommand.openItemGui(p, new ItemInventoryHolder(State.ITEM, ItemCommand.editingLore.get(p)));
				ItemCommand.editingLore.remove(p);
				e.setCancelled(true);
				p.getInventory().remove(Material.WRITABLE_BOOK);
				p.sendMessage("§aDescription enregistré");
				
				
			}
		}
	}

}
