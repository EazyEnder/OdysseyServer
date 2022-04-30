package fr.eazyender.odyssey.dungeons.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import fr.eazyender.odyssey.dungeons.Dungeon;
import fr.eazyender.odyssey.dungeons.DungeonInstance;
import fr.eazyender.odyssey.player.group.PlayerGroup;
import net.md_5.bungee.api.ChatColor;

public class DungeonGuiListener implements Listener {
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getInventory().getHolder() instanceof DungeonGuiHolder) {
			DungeonGuiHolder holder = (DungeonGuiHolder) e.getInventory().getHolder();
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			if (holder.getState() == DungeonGuiState.MAINPAGE)
				DungeonGui.openDungeon((Player) e.getWhoClicked(), "lul", holder);
			else if (holder.getState() == DungeonGuiState.STARTING) {
				if (e.getSlot() == 15) {
					DungeonGui.openGui(p, holder.getPage());
				}
				if (e.getSlot() == 16) {
					int idDj = getId(holder.getDungeon());
					if (idDj != 0)
						new DungeonInstance(PlayerGroup.getGroup(p).getPlayers(), holder.getDungeon(), getId(holder.getDungeon())).run();
					else {
						p.closeInventory();
						p.sendMessage(ChatColor.of("#ff0000") + "Nous n'avons pas pu trouvé d'instances pour vous .. réessayez plus tard !");
					}
				}
				
			}
		}
	}
	
	public int getId(Dungeon dungeon) {
		int i = 1;
		while(dungeon.getOffsets().containsKey(i)) {
			if (!isUsed(dungeon.getName(), i)) {
				return i;
			}
			i++;
		}
		return 0;
	}
	
	public boolean isUsed(String dungeon, int id) {
		for(DungeonInstance instance : DungeonInstance.instances) {
			if (instance.getDungeon().getName().equals(dungeon) && instance.getId() == id) {
				return true;
			}
		}
		return false;
	}

}
