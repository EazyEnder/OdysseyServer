package fr.eazyender.odyssey.gameplay.aura.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import fr.eazyender.odyssey.gameplay.aura.skills.SkillsDB;
import fr.eazyender.odyssey.gameplay.items.ItemUtils;
import fr.eazyender.odyssey.gameplay.masteries.MasteryDB;
import fr.eazyender.odyssey.gameplay.stats.Classe;

public class SkillsGuiListener implements Listener {
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getInventory().getHolder() instanceof SkillsCommand.SkillsCommandHolder) {
			e.setCancelled(true);
			SkillsCommand.SkillsCommandHolder holder = (SkillsCommand.SkillsCommandHolder)e.getInventory().getHolder();
			Player p = (Player) e.getWhoClicked();
			if (holder.getId() == 0) {
				int slotSkill = 1;
				for(int slotInventory : SkillsCommand.slotSkills) {
					if (slotInventory == e.getSlot()) {
						SkillsCommand.showSkills(p, Classe.valueOf(MasteryDB.getClass(p.getUniqueId().toString())), 1, slotSkill);
					}
					slotSkill++;
				}
			} else {
				if (e.getCurrentItem() != null) {
					if (ItemUtils.getInfo(e.getCurrentItem(), "id") != null) {
						
						SkillsDB.setSkill(p.getUniqueId().toString(), holder.getId(), ItemUtils.getInfo(e.getCurrentItem(), "id"));
						SkillsCommand.openSkills(p);
					}
						
				}
			}
		}
	}

}
