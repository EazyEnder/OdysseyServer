package fr.eazyender.odyssey.gameplay.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.eazyender.odyssey.gameplay.stats.Stat;
import fr.eazyender.odyssey.utils.NBTEditor;

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
						ItemCommand.openStatEditor(p, holder, null);
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
					if (e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().hasDisplayName()
							&& Stat.valueOf(e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[0]) != null) {
						ItemCommand.openStatEditor(p, holder,
								Stat.valueOf(e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[0]));
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
						
						if (holder.getStat() == null) {
							is = NBTEditor.set(is, ItemUtils.getNumericInfo(is, "CustomModelData") + operation,
									"CustomModelData");
						} else {
						
						is = NBTEditor.set(is, ItemUtils.getStat(is, holder.getStat()) + operation,
								holder.getStat().name());
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
					ItemType type = ItemType.values()[e.getSlot() - 1];
					
					ItemStack is = ItemDB.getItem(holder.getIdItem());
					is = NBTEditor.set(is, type.name(), "type");
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

}
