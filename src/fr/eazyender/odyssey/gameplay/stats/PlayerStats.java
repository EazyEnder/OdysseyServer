package fr.eazyender.odyssey.gameplay.stats;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.gameplay.aura.AuraHUD;
import fr.eazyender.odyssey.gameplay.items.ItemUtils;
import fr.eazyender.odyssey.gameplay.magic.WandUtils;
import fr.eazyender.odyssey.gameplay.masteries.MasteryDB;
import net.md_5.bungee.api.ChatColor;

public class PlayerStats {

	public static HashMap<Player, PlayerStats> stats = new HashMap<>();
	public static HashMap<Player, Long> warningCooldowns = new HashMap<>();
	
	Player p;
	HashMap<Stat, Integer> playerStats = new HashMap<>();

	public PlayerStats(Player p) {
		this.p = p;
	}

	public int getStat(Stat stat) {
		if (playerStats.containsKey(stat)) {
			return playerStats.get(stat);
		} else {
			return updateStat(stat);
		}
	}
	
	public void warn(Player p) {
		if (!warningCooldowns.containsKey(p) || System.currentTimeMillis() - warningCooldowns.get(p) > 30000){
			p.sendMessage(ChatColor.of("#ff0000") + "<!> Une armure équipée n'est pas de votre classe actuelle, les effets sont annulés.");
			warningCooldowns.put(p, System.currentTimeMillis());
		}
	}
	
	public int updateStat(Stat stat) {
		int sum = 0;
		for (ItemStack armorPiece : p.getInventory().getArmorContents()) {
			if (armorPiece != null)
				if (ItemUtils.getType(armorPiece) == null || ItemUtils.getType(armorPiece).name().equals(MasteryDB.getClass(p.getUniqueId().toString())))
					sum += ItemUtils.getStat(armorPiece, stat);
				else warn(p);
		}
		
		if (p.getInventory().getItemInMainHand() != null && !isArmor(p.getInventory().getItemInMainHand())) 
			if (ItemUtils.getType(p.getInventory().getItemInMainHand()) == null || ItemUtils.getType(p.getInventory().getItemInMainHand()).name().equals(MasteryDB.getClass(p.getUniqueId().toString())))
				sum += ItemUtils.getStat(p.getInventory().getItemInMainHand(), stat);
		if (p.getInventory().getItemInOffHand() != null && p.getInventory().getItemInOffHand().getType() == Material.SHIELD && p.isBlocking())
			if (ItemUtils.getType(p.getInventory().getItemInOffHand()) == null || ItemUtils.getType(p.getInventory().getItemInOffHand()).name().equals(MasteryDB.getClass(p.getUniqueId().toString())))
				sum += ItemUtils.getStat(p.getInventory().getItemInOffHand(), stat);
				

		// Adding default values to sum
		if (stat == Stat.DAMAGE)
			sum += 1;
		if (stat == Stat.POWER)
			sum += 1;
		if (stat == Stat.HEALTH)
			sum += 20;
		if (stat == Stat.MP || stat == Stat.AURA)
			sum += 100;
		if (stat == Stat.REGENMP || stat == Stat.REGENAURA)
			sum += 25;
		if (stat == Stat.CRIT_DAMAGE)
			sum += 150;
		if (stat == Stat.WATER || stat == Stat.FIRE || stat == Stat.EARTH || stat == Stat.WIND || stat == Stat.LIGHT || stat == Stat.SHADOW)
			sum += 100;

		playerStats.put(stat, sum);
		CombatStats.getStats(p).updateStat(stat);
		return sum;
	}

	public void updateStats() {
		for (Stat stat : Stat.values()) {
			updateStat(stat);
		}
	}

	public static PlayerStats getStats(Player p) {
		if (stats.containsKey(p))
			return stats.get(p);
		else {
			PlayerStats pStats = new PlayerStats(p);
			stats.put(p, pStats);
			return pStats;
		}
	}

	public static HashMap<Player, PlayerStats> getStats() {
		return stats;
	}

	public Player getPlayer() {
		return p;
	}

	public HashMap<Stat, Integer> getPlayerStats() {
		return playerStats;
	}
	
	public static HashMap<Player, ItemStack> itemHeld = new HashMap<>();
	public static HashMap<Player, ArrayList<ItemStack>> armor = new HashMap<>();

	public static void init() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			itemHeld.put(p,ItemUtils.cloneIfNotNull( p.getInventory().getItemInMainHand()));
			armor.put(p, ItemUtils.cloneIfNotNull(p.getInventory().getArmorContents()));
		}
		new BukkitRunnable() {
			@SuppressWarnings("unchecked")
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) {
					
						if (!isSame(itemHeld.get(p), p.getInventory().getItemInMainHand())) {
							if (itemHeld.get(p) != null && !isArmor(itemHeld.get(p))) {
								AuraHUD.setPlayerAura(p, AuraHUD.getPlayerAura(p) - ItemUtils.getStat(itemHeld.get(p), Stat.AURA));
								WandUtils.setMana(p, WandUtils.getMana(p) - ItemUtils.getStat(itemHeld.get(p), Stat.MP));
							}
							if (p.getInventory().getItemInMainHand() != null && !isArmor(p.getInventory().getItemInMainHand())) {
								AuraHUD.setPlayerAura(p, AuraHUD.getPlayerAura(p) + ItemUtils.getStat(p.getInventory().getItemInMainHand(), Stat.AURA));
								WandUtils.setMana(p, WandUtils.getMana(p) + ItemUtils.getStat(p.getInventory().getItemInMainHand(), Stat.MP));
							}
							
							itemHeld.put(p, p.getInventory().getItemInMainHand());
							
						}
					
						
						
					int slot = 0;
					for(ItemStack armorPiece : (ArrayList<ItemStack>) armor.get(p).clone()) {
						if (!isSame(armorPiece, p.getInventory().getArmorContents()[slot])) {
							if (armorPiece != null) {
								AuraHUD.setPlayerAura(p, AuraHUD.getPlayerAura(p) - ItemUtils.getStat(armorPiece, Stat.AURA));
								WandUtils.setMana(p, WandUtils.getMana(p) - ItemUtils.getStat(armorPiece, Stat.MP));
							}
							if (p.getInventory().getArmorContents()[slot] != null) {
								AuraHUD.setPlayerAura(p, AuraHUD.getPlayerAura(p) + ItemUtils.getStat(p.getInventory().getArmorContents()[slot], Stat.AURA));
								WandUtils.setMana(p, WandUtils.getMana(p) + ItemUtils.getStat(p.getInventory().getArmorContents()[slot], Stat.MP));
							}
							ArrayList<ItemStack> armorNew = armor.get(p);
							armorNew.set(slot, p.getInventory().getArmorContents()[slot]);
							armor.replace(p, armorNew);
						}
						slot++;
					}
					
					PlayerStats.getStats(p).updateStats();
				}
			}
		}.runTaskTimer(OdysseyPl.getOdysseyPlugin(), 0, 1);
	}
	
	public static boolean isSame(ItemStack is, ItemStack is2) {
		if (is == null && is2 != null) return false;
		if (is != null && is2 == null) return false;
		if (is == null && is2 == null) return true;
		if (is.isSimilar(is2)) return true;
		return false;
	}
	
    public static boolean isArmor(final ItemStack itemStack) {
        if (itemStack == null)
            return false;
        final String typeNameString = itemStack.getType().name();
        if (typeNameString.endsWith("_HELMET")
                || typeNameString.endsWith("_CHESTPLATE")
                || typeNameString.endsWith("_LEGGINGS")
                || typeNameString.endsWith("_BOOTS")) {
            return true;
            }

        return false;
    }
	
}
