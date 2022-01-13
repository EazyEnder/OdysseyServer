package fr.eazyender.odyssey.listener;

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
public class TchatListener implements Listener{
	
	public static Map<String,String> emotes = new HashMap<String, String>();
	
	//0 = INFO; 1 = ALL; 2 = LOCAL/RP 
	public static Map<UUID, Integer> player_tchat = new HashMap<UUID, Integer>();
	
	public TchatListener() {
		
		emotes.put(":small_earth:", "\uEff1");
		emotes.put(":big_earth:", "\uEff2");
		
	}
	
	@EventHandler()
	public void onPlayerSendMessage(PlayerChatEvent event)  {
		
		Player p = event.getPlayer();
		if(!player_tchat.containsKey(p.getUniqueId())) {player_tchat.put(p.getUniqueId(), 2);}
		String msg_init = event.getMessage();
		String msg = msg_init;
		
		for (String emote_id : emotes.keySet()) {
			if(msg_init.contains(emote_id)) {
				msg = msg.replaceAll(emote_id, emotes.get(emote_id));
			}
		}
		
		int tchat = player_tchat.get(p.getUniqueId());
		if(tchat == 0) {
			p.sendMessage(TextUtils.server + "Vous avez enlevé les messages, faites /t all.");
			event.setCancelled(true);
		}else if(tchat == 1) {
			for (UUID uuid : getTchatPlayerList(1)) {
				Bukkit.getPlayer(uuid).sendMessage(TextUtils.hrp + p.getDisplayName() + ": "+msg);
			}
			p.sendMessage(TextUtils.hrp + p.getDisplayName() + ": "+msg);
			event.setCancelled(true);
		}else if(tchat == 2) {
			createBubble(p,msg);
			for (Player p2 : Bukkit.getOnlinePlayers()) {
				if(p2!=p && p2.getLocation().getWorld()==p.getLocation().getWorld()) {
					if(p.getLocation().distance(p2.getLocation()) <= 50) {
						p2.sendMessage(TextUtils.local + p.getDisplayName() + ": "+msg);
					}
				}
			}
			p.sendMessage(TextUtils.local + p.getDisplayName() + ": "+msg);
			event.setCancelled(true);
		}
		
	}
	
	public static List<UUID> getTchatPlayerList(int tchat){
		List<UUID> players = new ArrayList<UUID>();
		for (UUID uuid : player_tchat.keySet()) {
			if(player_tchat.get(uuid) == tchat) {players.add(uuid);}
		}
		return players;
	}
	
	public static void createBubble(Player player,String text) {
		ArmorStand as = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().add(new Vector(0,0.5,0)), EntityType.ARMOR_STAND);
		as.setGravity(false);
		as.setCanPickupItems(false);
		as.setCustomName("\uEAD4");
		as.setCustomNameVisible(true);
		as.setVisible(false);
		ArmorStand as1 = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().add(new Vector(0,0.5,0)), EntityType.ARMOR_STAND);
		as1.setGravity(false);
		as1.setCanPickupItems(false);
		as1.setCustomName(text);
		as1.setCustomNameVisible(true);
		as1.setVisible(false);
		
		int timermax = 20*5;
		
		new BukkitRunnable() {

			int timer = 0;
			
			@Override
			public void run() {
				
							
				as.teleport(player.getLocation().add(new Vector(0,0.5,0)));
				as1.teleport(player.getLocation().add(new Vector(0,0.5,0)));
				if(timer>=timermax) {
				as.remove();
				as1.remove();
				this.cancel();
				}else {
				timer+=2;
				}
				
			}
			
		}.runTaskTimer(OdysseyPl.getOdysseyPlugin(),0,4);
	}

}
