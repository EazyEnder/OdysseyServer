package fr.eazyender.odyssey.utils.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import fr.eazyender.odyssey.OdysseyPl;

public class BlockUtils implements Listener{
	
	public static List<IBlock> blocks = new ArrayList<IBlock>();
	
	public static Map<Location, ITileEntity> tile_entity = new ConcurrentHashMap<Location, ITileEntity>();
	
	private static Map<UUID, Boolean> timer_place = new HashMap<UUID, Boolean>();
	
	public static void initTileEntityLoop() {
		new BukkitRunnable() {
		@Override
		public void run() {
			for (Location loc : tile_entity.keySet()) {
				if(loc.isWorldLoaded()) {
					tile_entity.get(loc).loop();
				}
			}
			
		} }.runTaskTimer(OdysseyPl.getOdysseyPlugin(), 0, 10);
	}
	
	public static void initBlocks() {
		
		// NAME - ID - SOURCE - MATERIAL - SOUND - MINING - TOOL
		//Example :blocks.add(new IBlock("§f"+"Relai", 1, 4, Material.NOTE_BLOCK, 2, Sound.BLOCK_WOOD_PLACE, 0, 0));
		
	}
	
	@EventHandler
	public void onPlayerInteractWithBlock(PlayerInteractEvent event) {
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType().equals(Material.NOTE_BLOCK)) {
			//Custom Event
			Block b = event.getClickedBlock();
			NoteBlock nb = (NoteBlock) b.getBlockData();
			event.setCancelled(true);
		}
		
	}
	
	@EventHandler
	public void onPlayerInteractWithNB(NotePlayEvent event) {
		
			event.setCancelled(true);
		
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerPlaceNoteBlock(BlockPlaceEvent event) {
		Block block_placed = event.getBlockPlaced();
		if(block_placed != null && block_placed.getType().equals(Material.NOTE_BLOCK)) {
			event.setCancelled(true);
		}
		
		if(block_placed.getLocation().add(0,1,0).getBlock().getType().equals(Material.NOTE_BLOCK)){
			NoteBlock b = (NoteBlock) block_placed.getLocation().add(0,1,0).getBlock().getBlockData();
			new BukkitRunnable() {
				@Override
				public void run() {
					
					block_placed.getLocation().add(0,1,0).getBlock().setBlockData(b);
					
				}
				
			}.runTaskLater(OdysseyPl.getOdysseyPlugin(), 1);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler()
	public void onPlayerPlaceBlock(PlayerInteractEvent event) {
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			
			if(!timer_place.containsKey(event.getPlayer().getUniqueId()))
			{timer_place.put(event.getPlayer().getUniqueId(), true);}
			
			Block block_placed = null;
			if(event.getBlockFace().equals(BlockFace.UP)) {block_placed = event.getClickedBlock().getLocation().add(0,1,0).getBlock();}
			else if(event.getBlockFace().equals(BlockFace.DOWN)) {block_placed = event.getClickedBlock().getLocation().add(0,-1,0).getBlock();}
			else if(event.getBlockFace().equals(BlockFace.EAST)) {block_placed = event.getClickedBlock().getLocation().add(1,0,0).getBlock();}
			else if(event.getBlockFace().equals(BlockFace.SOUTH)) {block_placed = event.getClickedBlock().getLocation().add(0,0,1).getBlock();}
			else if(event.getBlockFace().equals(BlockFace.NORTH)) {block_placed = event.getClickedBlock().getLocation().add(0,0,-1).getBlock();}
			else if(event.getBlockFace().equals(BlockFace.WEST)) {block_placed = event.getClickedBlock().getLocation().add(-1,0,0).getBlock();}
			
			if(event.getClickedBlock().getType()==Material.NOTE_BLOCK && block_placed != null && block_placed.getType().equals(Material.AIR) && (event.getPlayer().getLocation().getBlockX()!=block_placed.getLocation().getBlockX()||
					event.getPlayer().getLocation().getBlockY()!=block_placed.getLocation().getBlockY()||
					event.getPlayer().getLocation().getBlockZ()!=block_placed.getLocation().getBlockZ()) && timer_place.get(event.getPlayer().getUniqueId()))
				if(event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType().isBlock()) {
					block_placed.setType(event.getPlayer().getItemInHand().getType());
					timer_place.replace(event.getPlayer().getUniqueId(), false);
					new BukkitRunnable() {

						@Override
						public void run() {
							timer_place.replace(event.getPlayer().getUniqueId(), true);
							
						}
						
					}.runTaskLater(OdysseyPl.getOdysseyPlugin(), 4);
				}
			for (IBlock block : blocks) {
				if(event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().hasItemMeta() && event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(block.getName()) & 
						(event.getPlayer().getLocation().getBlockX()!=block_placed.getLocation().getBlockX()||
						event.getPlayer().getLocation().getBlockY()!=block_placed.getLocation().getBlockY()||
						event.getPlayer().getLocation().getBlockZ()!=block_placed.getLocation().getBlockZ())&&
						timer_place.get(event.getPlayer().getUniqueId())) {
					if(block.getMaterial()==Material.NOTE_BLOCK) {
						
					setNoteBlock(block_placed, block.getId());
					
					}
					if(block.getPlace_sound() != null) {
						block_placed.getWorld().playSound(block_placed.getLocation(), block.getPlace_sound(), 1, 1);
					}
					
					if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
					if(event.getPlayer().getItemInHand().getAmount() > 1) {
						event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount()-1);
					}else {
						event.getPlayer().setItemInHand(null);
					}
					
					timer_place.replace(event.getPlayer().getUniqueId(), false);
					new BukkitRunnable() {

						@Override
						public void run() {
							timer_place.replace(event.getPlayer().getUniqueId(), true);
							
						}
						
					}.runTaskLater(OdysseyPl.getOdysseyPlugin(), 4);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void	onGrassDecay(BlockPhysicsEvent event) {
		if(!event.getSourceBlock().getType().equals(Material.NOTE_BLOCK) && event.getSourceBlock().getLocation().add(0,1,0).getBlock().getType().equals(Material.NOTE_BLOCK)) {
			NoteBlock b = (NoteBlock) event.getSourceBlock().getLocation().add(0,1,0).getBlock().getBlockData();
			new BukkitRunnable() {
				@Override
				public void run() {
					
					event.getSourceBlock().getLocation().add(0,1,0).getBlock().setBlockData(b);
					
				}
				
			}.runTaskLater(OdysseyPl.getOdysseyPlugin(), 1);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerBreakBlock(BlockBreakEvent event) {
		Block block_destroyed = event.getBlock();
		@SuppressWarnings("deprecation")
		ItemStack item = event.getPlayer().getItemInHand();
		
		if(block_destroyed.getLocation().add(0,1,0).getBlock().getType().equals(Material.NOTE_BLOCK)){
			NoteBlock b = (NoteBlock) block_destroyed.getLocation().add(0,1,0).getBlock().getBlockData();
			new BukkitRunnable() {
				@Override
				public void run() {
					
					block_destroyed.getLocation().add(0,1,0).getBlock().setBlockData(b);
					
				}
				
			}.runTaskLater(OdysseyPl.getOdysseyPlugin(), 1);
		}
		
		if(block_destroyed.getType().equals(Material.NOTE_BLOCK)) {
		
			IBlock block = null;
			block = getBlockOfANoteBlock(block_destroyed);
			
			if(block != null) {
				
				Material need = getToolNeeded(block.getTool(),block.getMining_level());
				boolean flag = true;
				if(need != null && event.getPlayer() != null) {
					flag = false;
					if(block.getMining_level() > 1) {
						for (int i = block.getMining_level(); i < 6; i++) {
							flag = flag|| (item != null && item.getType()==getToolNeeded(block.getTool(), i));
						}
					}
				}
				
				if(flag) {
					
					
					ItemStack drop = block.getItem();
		
					
					block_destroyed.getWorld().dropItem(block_destroyed.getLocation(), drop);
					
				}
				if(tile_entity.containsKey(block_destroyed.getLocation())) {
					if(tile_entity.get(block_destroyed.getLocation()) instanceof ITileTravel) {
					ITileTravel tile = (ITileTravel) tile_entity.get(block_destroyed.getLocation());
					for (Location l : tile.getOutputs()) {
						if(l != null) {
							if(tile_entity.get(l) != null) {
							ITileTravel tile2 = (ITileTravel) tile_entity.get(l);
							Location[] inputs = tile2.getInputs();
								for (int i = 0; i <inputs.length; i++) {
									if(inputs[i] == tile.getBlock_location()) {
										inputs[i] = null;
									}
								}
								tile2.setInputs(inputs);
								tile_entity.replace(block_destroyed.getLocation(), tile2);
							}
						}
					}
					for (Location l : tile.getInputs()) {
						if(l != null) {
							if(tile_entity.get(l) != null) {
							ITileTravel tile2 = (ITileTravel) tile_entity.get(l);
							Location[] outputs = tile2.getOutputs();
								for (int i = 0; i <outputs.length; i++) {
									if(outputs[i] == tile.getBlock_location()) {
										outputs[i] = null;
									}
								}
								tile2.setOutputs(outputs);
								tile_entity.replace(l, tile2);
							}
						}
					}
					}
					
					tile_entity.remove(block_destroyed.getLocation());
				}
				event.setDropItems(false);
			}
		}
	}
	
	public static Material getToolNeeded(int tool, int mininglevel) {
		Material mat = null;
		
		switch(tool) {
		case 1: switch(mininglevel) {
			case 1: mat = Material.WOODEN_PICKAXE; break;
			case 2: mat = Material.STONE_PICKAXE; break;
			case 3: mat = Material.STONE_PICKAXE; break;
			case 4: mat = Material.IRON_PICKAXE; break;
			case 5: mat = Material.GOLDEN_PICKAXE; break;
			case 6: mat = Material.DIAMOND_PICKAXE; break;
			}
			break;
		case 2:
			switch(mininglevel) {
			case 1: mat = Material.WOODEN_SHOVEL; break;
			case 2: mat = Material.STONE_SHOVEL; break;
			case 3: mat = Material.STONE_SHOVEL; break;
			case 4: mat = Material.IRON_SHOVEL; break;
			case 5: mat = Material.GOLDEN_SHOVEL; break;
			case 6: mat = Material.DIAMOND_SHOVEL; break;
			}
			break;
		case 3:
			switch(mininglevel) {
			case 1: mat = Material.WOODEN_AXE; break;
			case 2: mat = Material.STONE_AXE; break;
			case 3: mat = Material.STONE_AXE; break;
			case 4: mat = Material.IRON_AXE; break;
			case 5: mat = Material.GOLDEN_AXE; break;
			case 6: mat = Material.DIAMOND_AXE; break;
			}
			break;
			
		}
		
		return mat;
	}
	
	public static void setNoteBlock(Block block, int id) {
		block.setType(Material.NOTE_BLOCK);
		NoteBlock note = (NoteBlock) block.getBlockData();
		note.setInstrument(GetInstrumentOfId(id));
		note.setNote(GetNoteOfId(id));
		note.setPowered(true);
		block.setBlockData(note);
			
	}
	
	public static IBlock getBlockOfANoteBlock(Block bl) {
		NoteBlock note = (NoteBlock)bl.getBlockData();
		IBlock block = null;
		for(IBlock b : blocks) {
			if(GetNoteOfId(b.getId()) != null && GetInstrumentOfId(b.getId()) != null)
			if(GetNoteOfId(b.getId()).equals(note.getNote()) && GetInstrumentOfId(b.getId()).equals(note.getInstrument())) {
				block = b;
				return block;
			}
		}
		return null;
	}
	
	public static Instrument GetInstrumentOfId(int id) {
		Instrument inst = null;
		
		id = (id-id%25)/(11);
		
		switch(id) {
		case 0: inst = Instrument.BASS_GUITAR;
			break;
		case 1: inst = Instrument.SNARE_DRUM;
			break;
		case 2: inst = Instrument.STICKS;
			break;
		case 3: inst = Instrument.BASS_DRUM;
			break;
		case 4: inst = Instrument.BELL;
			break;
		case 5: inst = Instrument.FLUTE;
			break;
		case 6: inst = Instrument.CHIME;
			break;
		case 7: inst = Instrument.GUITAR;
			break;
		case 8: inst = Instrument.XYLOPHONE;
			break;
		case 9: inst = Instrument.IRON_XYLOPHONE;
			break;
		case 10: inst = Instrument.COW_BELL;
			break;
		case 11: inst = Instrument.DIDGERIDOO;
			break;
		}
		
		return inst;
	}
	
	public static Note GetNoteOfId(int id) {
		Note note = null;
		
		id = (id%25);
		
		Tone[] tones = {Note.Tone.getById(Tone.F.getId(true)),
				Tone.G,
				Note.Tone.getById(Tone.G.getId(true)),
				Tone.A,
				Note.Tone.getById(Tone.A.getId(true)),
				Tone.B,Tone.C,
				Note.Tone.getById(Tone.C.getId(true)),
				Tone.D,
				Note.Tone.getById(Tone.D.getId(true)),
				Tone.E,Tone.F,
				Note.Tone.getById(Tone.F.getId(true))};
		int o = (id-id%tones.length)/(tones.length);
		int t = (id % tones.length)-1;
		if(t < 0) t = 0;else if(o > 0) t++;
		
		@SuppressWarnings("deprecation")
		Note n = new Note(o, Tone.getById((byte) t), false);
		if(Tone.getById((byte) t).isSharped((byte) t)) {
			n = new Note(o, Tone.getById((byte) t), true);
		}
		note = n;
		
		return note;
	}

}
