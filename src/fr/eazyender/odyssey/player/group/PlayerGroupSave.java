package fr.eazyender.odyssey.player.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class PlayerGroupSave implements Listener{

    public static PlayerGroupSave playerGrp;

    private static Map<UUID, PlayerGroup> groups = new HashMap<UUID, PlayerGroup>();
    
    @EventHandler
	  public void onPlayerJoin(PlayerJoinEvent e) { 
		
		Player player = e.getPlayer();
		getPlayerGroup().loadPlayer(player);
		
    }
    
    @EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) { 
    	Player player = e.getPlayer();
    	if(PlayerGroup.aGroupContainPlayer(player.getUniqueId()))
		{
			PlayerGroup group = PlayerGroup.getGroupOfAPlayer(player);
			for (int i = 0; i < group.getGroup().size(); i++) {
				Player member = Bukkit.getPlayer(group.getGroup().get(i));
				if(member.getUniqueId() == player.getUniqueId()) {
					List<UUID> newGroup = group.getGroup();
					newGroup.remove(group.getGroup().get(i));
					PlayerGroup.getGroupOfAPlayer(player).setGroup(newGroup);
					for (Player mate : group.getPlayers()) {
						mate.playSound(mate.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
						mate.sendMessage(CommandGroup.srv_msg + "Le joueur " + player.getName() + " s'est déconnecté et a donc quitté le groupe");
					}
					
				}
			}
		}else if(!getPlayerGroup().getGroup(player).getGroup().isEmpty()) {
			PlayerGroup group = getPlayerGroup().getGroup(player);
			group.setGroup(new ArrayList<UUID>());
			for (UUID mUUID : group.getGroup()) {
				Player member = Bukkit.getPlayer(mUUID);
				member.playSound(member.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
				member.sendMessage(CommandGroup.srv_msg + "Le chef du groupe " + player.getName() + " s'est déconnecté, vous avez donc été exclu du groupe.");
			}
		}
    	PlayerGroupSave.getPlayerGroup().unloadPlayer(player);
    }

    public PlayerGroupSave() {
    	playerGrp = this;
        for (Player ps : Bukkit.getOnlinePlayers()) {
            loadPlayer(ps);
        }
    }

    public static void createPlayerGroup(Player p) {
        groups.put(p.getUniqueId(), new PlayerGroup(p));
    }

    public void loadPlayer(Player p) {
        	createPlayerGroup(p);
    }
    
    public List<PlayerGroup> getAllGroup() {
    	List<PlayerGroup> newgroups = new ArrayList<PlayerGroup>();
    	  for (Player ps : Bukkit.getOnlinePlayers()) {
    		  newgroups.add(getGroup(ps));
    	  }
          return newgroups;
    }

    public PlayerGroup getGroup(Player p) {
        if (playerExist(p)) {
            return groups.get(p.getUniqueId());
        }else {System.out.println("Player doesn't exist");
            return null;}
    }
    public void setGroup(Player p, PlayerGroup newValue) {
        if (playerExist(p)) {
            groups.replace(p.getUniqueId(), newValue);
        }else {System.out.println("Player doesn't exist");}
    }
    public void unloadPlayer(Player p) {
        if (playerExist(p)) {
            groups.remove(p.getUniqueId());
        }
    }

    public static PlayerGroupSave getPlayerGroup() { return playerGrp;  }

    public boolean playerExist(Player p) { return groups.containsKey(p.getUniqueId()); }


}
