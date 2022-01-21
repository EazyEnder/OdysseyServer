package fr.eazyender.odyssey.player.group;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CommandAccept implements CommandExecutor {

	private String srv_msg = "§f[§e§lGroupe§r§f] : ";
	
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		
		  if (cmd.getName().equalsIgnoreCase("accept")) {
	            if (sender instanceof Player) {
	                Player player = (Player) sender;
	                	 if(PlayerGroupSave.getPlayerGroup().getGroup(player).getGroup().isEmpty() &&
     							!PlayerGroup.aGroupContainPlayer(player.getUniqueId())) {
						if(args.length >= 1) {
							String name = args[0];
							Player target = Bukkit.getPlayer(name);
							if(target != null) {
								if(CommandGroup.requests.containsKey(player.getUniqueId())) {
									if(CommandGroup.requests.get(player.getUniqueId()).contains(target.getUniqueId())) {
										
										if(!(PlayerGroupSave.getPlayerGroup().getGroup(target).getGroup().size() >= 4)) {
											
											List<UUID> members = PlayerGroupSave.getPlayerGroup().getGroup(target).getGroup();
											members.add(player.getUniqueId());
											PlayerGroupSave.getPlayerGroup().getGroup(target).setGroup(members);
											
											player.sendMessage(srv_msg + "Vous avez rejoint le groupe de " + name + ".");
											target.sendMessage(srv_msg + player.getName() + " a rejoint votre groupe.");
											
											player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
											target.playSound(target.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
											
											CommandGroup.requests.remove(player.getUniqueId());
											
										}else player.sendMessage(srv_msg + "Le groupe possède déja 5 joueurs.");
										
									}else player.sendMessage(srv_msg + "Vous n'avez aucune demande venant de ce joueur.");
								}else player.sendMessage(srv_msg + "Vous n'avez aucune demande.");
							}else player.sendMessage(srv_msg + "Joueur invalide");
						}else {
							player.sendMessage(srv_msg + "Usage : /group yes <player>");
						}
					}else player.sendMessage(srv_msg + "Vous êtes déja dans un groupe, faites /group leave pour le quitter.");
	            }
	            return true;
		  }
		return false;
	}

}
