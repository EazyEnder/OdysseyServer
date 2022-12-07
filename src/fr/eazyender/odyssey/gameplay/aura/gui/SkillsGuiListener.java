package fr.eazyender.odyssey.gameplay.aura.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import fr.eazyender.odyssey.gameplay.aura.skills.SkillsDB;
import fr.eazyender.odyssey.gameplay.items.ItemUtils;
import fr.eazyender.odyssey.gameplay.masteries.MasteryDB;
import net.md_5.bungee.api.ChatColor;

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
						if (e.getClick().name().contains("LEFT"))
							SkillsCommand.showSkills(p, MasteryDB.getClass(p.getUniqueId().toString()), 1, slotSkill);
						else if (e.getClick().name().contains("RIGHT")) {
							SkillsDB.setSkill(p.getUniqueId().toString(), slotSkill, "null");
							SkillsCommand.openSkills(p);
						}
					}
					slotSkill++;
				}
			} else {
				if (e.getCurrentItem() != null) {
					
					if (ItemUtils.getInfo(e.getCurrentItem(), "id") != null) {
						String id = ItemUtils.getInfo(e.getCurrentItem(), "id");
						for(String skill : SkillsDB.getSkills(p.getUniqueId().toString()).values()) {
							if (skill.equals(id)) {
								
								SkillsCommand.openSkills(p);
								p.sendMessage(ChatColor.of("#ff0000") + "<!> Vous avez déjà ce sort dans votre barre d'invocation !");
								return;
							}
						}
						
						SkillsDB.setSkill(p.getUniqueId().toString(), holder.getId(), id);
						SkillsCommand.openSkills(p);
					}
						
				}
			}
		}
	}

}
