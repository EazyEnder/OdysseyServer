package fr.eazyender.odyssey.player.harvest;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.gameplay.city.building.BuildManager;
import fr.eazyender.odyssey.gameplay.city.building.IDynamicBuild;
import net.md_5.bungee.api.ChatColor;

public class HarvestHandler implements Listener{
	
	private static List<IHarvestingResource> resources = new ArrayList<IHarvestingResource>();
	private static List<IRegenBlock> regen_blocks = new ArrayList<IRegenBlock>();
	
	public static void initHandler() {
		
		
		
		ItemStack fibre = new ItemStack(Material.RED_DYE, 1);
		ItemMeta fibre_meta = fibre.getItemMeta();
		fibre_meta.setCustomModelData(1);
		fibre_meta.setDisplayName(ChatColor.of(new Color(172, 188, 122)) + "Fibre végétale");
		List<String> fibre_lore = new ArrayList<String>();
		fibre_lore.add(ChatColor.of(new Color(175, 186, 141)) +"Expansion cellulaire possédant d'intéressantes");
		fibre_lore.add(ChatColor.of(new Color(175, 186, 141)) +"propriétés mécaniques et thermiques.");
		fibre_meta.setLore(fibre_lore);
		fibre.setItemMeta(fibre_meta);
		List<Material> leaves = new ArrayList<Material>();
		List<Double> fibre_drops = new ArrayList<Double>();
		leaves.add(Material.OAK_LEAVES); fibre_drops.add(0.5);
		leaves.add(Material.SPRUCE_LEAVES); fibre_drops.add(0.5);
		leaves.add(Material.BIRCH_LEAVES); fibre_drops.add(0.5);
		leaves.add(Material.DARK_OAK_LEAVES); fibre_drops.add(0.5);
		leaves.add(Material.ACACIA_LEAVES); fibre_drops.add(0.5);
		leaves.add(Material.JUNGLE_LEAVES); fibre_drops.add(0.5);
		IHarvestingResource fibre_rs = new IHarvestingResource("fibre", fibre, leaves, fibre_drops, "", 60*10);
		resources.add(fibre_rs);
		
		ItemStack bois = new ItemStack(Material.OAK_LOG, 1);
		ItemMeta bois_meta = bois.getItemMeta();
		bois_meta.setDisplayName(ChatColor.of(new Color(187, 131, 80)) + "Bois");
		List<String> bois_lore = new ArrayList<String>();
		bois_lore.add(ChatColor.of(new Color(157, 86, 67)) +"Matériau d'origine végétale résistant");
		bois_lore.add(ChatColor.of(new Color(157, 86, 67)) +"utile dans beaucoup de cas dont la");
		bois_lore.add(ChatColor.of(new Color(157, 86, 67)) +"construction.");
		bois_meta.setLore(bois_lore);
		bois.setItemMeta(bois_meta);
		List<Material> woods = new ArrayList<Material>();
		List<Double> bois_drops = new ArrayList<Double>();
		woods.add(Material.OAK_LOG); bois_drops.add(1.0);
		woods.add(Material.SPRUCE_LOG); bois_drops.add(1.0);
		woods.add(Material.BIRCH_LOG); bois_drops.add(1.0);
		woods.add(Material.DARK_OAK_LOG); bois_drops.add(1.0);
		woods.add(Material.ACACIA_LOG); bois_drops.add(1.0);
		woods.add(Material.JUNGLE_LOG); bois_drops.add(1.0);
		IHarvestingResource bois_rs = new IHarvestingResource("bois", bois, woods, bois_drops, "", 60*60);
		resources.add(bois_rs);
		
		
		
		
		
		
		
		//----------------------------------------------------------------------
		
		int delay = 30;
		new BukkitRunnable() {

			@Override
			public void run() {
				
				for (IRegenBlock regen : new ArrayList<>(regen_blocks)) {
					
					if(regen.getActual_timer() >= regen.getTimer()) {
						
						regen.getLoc().getWorld().setType(regen.getLoc(), regen.getType());
						regen.getLoc().getWorld().setBlockData(regen.getLoc(), regen.getData());
						
						regen.getLoc().getWorld().playSound(regen.getLoc(), Sound.BLOCK_STONE_PLACE, 1, 1);
						
						regen_blocks.remove(regen);
						
					}else {
						
						regen.setActual_timer(regen.getActual_timer() + delay);
						
					}
					
				}
				
			}
			
		}.runTaskTimer(OdysseyPl.getOdysseyPlugin(), 0, 20*delay);
		
	}
	
	public static void forceRespawnAllRegenBlocks() {
		for (IRegenBlock regen : new ArrayList<>(regen_blocks)) {
			
				regen.getLoc().getWorld().setType(regen.getLoc(), regen.getType());
				regen.getLoc().getWorld().setBlockData(regen.getLoc(), regen.getData());
				
				regen_blocks.remove(regen);
			
		}
	}
	
	@EventHandler
	public static void onPlayerHarvest(BlockBreakEvent event) {
		
		Player player = event.getPlayer();
		
		if(player != null && !player.getGameMode().equals(GameMode.CREATIVE)) {
		
			IDynamicBuild dbuild = BuildManager.getDynamicBuild(player.getWorld(), event.getBlock().getLocation().toVector());
			if(dbuild == null) {
				
				Block block = event.getBlock();
				boolean cancel = true;
				double max_timer = 0;
				List<ItemStack> items = new ArrayList<ItemStack>();
				for (IHarvestingResource res : resources) {
					if(res.getBlocks().contains(block.getType())) {
						Double chance = res.getDrops().get(res.getBlocks().indexOf(block.getType()));
						if(Math.random() <= chance) {
							items.add(res.getResource());
						}
						max_timer = Math.max(max_timer, res.getTimer());
						cancel = false;
					}
				}
				
				if(cancel) {
					event.setCancelled(true);
				}else {
					event.setDropItems(false);
					event.setExpToDrop(0);
				
					for (ItemStack item : items) {
						block.getWorld().dropItem(block.getLocation(), item);
					}
					
					
					IRegenBlock regen = new IRegenBlock(block.getType(),block.getBlockData(), block.getLocation() ,max_timer);
					regen_blocks.add(regen);
				}
			}else {
				event.setCancelled(true);
			}
			
			
		}
		
		
	}
	
	

}
