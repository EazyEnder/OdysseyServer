package fr.eazyender.odyssey.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.utils.TextUtils;

@SuppressWarnings("deprecation")
public class TchatListener implements Listener {

	public static Map<String, String> emotes = new HashMap<String, String>();

	// 0 = INFO; 1 = ALL; 2 = LOCAL/RP
	public static Map<UUID, Integer> player_tchat = new HashMap<UUID, Integer>();
	
	public static Map<UUID, BukkitRunnable> bubbles = new HashMap<UUID, BukkitRunnable>();
	
	public TchatListener() {

		emotes.put(":small_earth:", "\uEff1");
		emotes.put(":big_earth:", "\uEff2");

	}

	@EventHandler()
	public void onPlayerSendMessage(PlayerChatEvent event) {
		if (!event.isCancelled()) {

			Player p = event.getPlayer();
			if (!player_tchat.containsKey(p.getUniqueId())) {
				player_tchat.put(p.getUniqueId(), 2);
			}
			String msg_init = event.getMessage();
			String msg = msg_init;

			for (String emote_id : emotes.keySet()) {
				if (msg_init.contains(emote_id)) {
					msg = msg.replaceAll(emote_id, emotes.get(emote_id));
				}
			}
			int tchat = player_tchat.get(p.getUniqueId());
			if (tchat == 0) {
				p.sendMessage(TextUtils.server + "Vous avez enlevé les messages, faites /t all.");
				event.setCancelled(true);
			} else if (tchat == 1) {
				for (UUID uuid : getTchatPlayerList(1)) {
					Bukkit.getPlayer(uuid).sendMessage(TextUtils.hrp + " " + p.getDisplayName() + ": " + msg);
				}
			p.sendMessage(TextUtils.hrp + p.getDisplayName() + ": "+msg);
			event.setCancelled(true);
		}else if(tchat == 2) {
			if(bubbles.containsKey(p.getUniqueId())) {bubbles.get(p.getUniqueId()).runTask(OdysseyPl.getOdysseyPlugin()); bubbles.remove(p.getUniqueId());}
			createBubble(p,msg);
			for (Player p2 : Bukkit.getOnlinePlayers()) {
				if(p2!=p && p2.getLocation().getWorld()==p.getLocation().getWorld()) {
					if(p.getLocation().distance(p2.getLocation()) <= 50) {
						p2.sendMessage(TextUtils.local + p.getDisplayName() + ": "+msg);
						}
					}
				}
				p.sendMessage(TextUtils.local + " " + p.getDisplayName() + ": " + msg);
				event.setCancelled(true);
			}
		}
	}

	public static List<UUID> getTchatPlayerList(int tchat) {
		List<UUID> players = new ArrayList<UUID>();
		for (UUID uuid : player_tchat.keySet()) {
			if (player_tchat.get(uuid) == tchat) {
				players.add(uuid);
			}
		}
		return players;
	}
	
	public static void createBubble(Player player,String text) {
		
		ArmorStand as = (ArmorStand) player.getWorld().spawn(player.getLocation().add(new Vector(0,0.5,0)), ArmorStand.class, armorstand -> {
			
		
		armorstand.setGravity(false);
		armorstand.setCanPickupItems(false);
		armorstand.setCustomName("\uEAD4");
		armorstand.setMarker(true);
		armorstand.setCustomNameVisible(true);
		armorstand.setVisible(false);});
		
		ArmorStand as1 = (ArmorStand) player.getWorld().spawn(player.getLocation().add(new Vector(0,0.5,0)), ArmorStand.class, armorstand -> {
			
			
			armorstand.setGravity(false);
			armorstand.setCanPickupItems(false);
			armorstand.setCustomName(text);
			armorstand.setMarker(true);
			armorstand.setCustomNameVisible(true);
			armorstand.setVisible(false);});
		
		player.addPassenger(as);
		player.addPassenger(as1);
		
		BukkitRunnable brun = new BukkitRunnable() {
			
			@Override
			public void run() {
				
							
				as.remove();
				as1.remove();
				
			}
			
		};
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				
				if(as != null && as1 != null) {			
				as.remove();
				as1.remove();
				}

			}
			
		}.runTaskLater(OdysseyPl.getOdysseyPlugin(), 4*20);
		
		if(!bubbles.containsKey(player.getUniqueId())) bubbles.put(player.getUniqueId(), brun);
		
	}

	public static void runRunnables() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(TchatListener.bubbles.containsKey(p.getUniqueId())) TchatListener.bubbles.get(p.getUniqueId()).run();
		}
	}
	
}
