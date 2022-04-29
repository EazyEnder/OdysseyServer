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
				Player p = (Player) sender;
				if (PlayerGroup.getGroup(p) == null) {
					if (args.length == 1) {
						String name = args[0];
						Player target = Bukkit.getPlayer(name);

						if (target != null) {
							if (CommandGroup.requests.containsKey(p)) {
								if (CommandGroup.requests.get(p).containsKey(target)) {
									if (PlayerGroup.getGroup(target) != null && PlayerGroup.getGroup(target) == CommandGroup.requests.get(p).get(target)) {
										if (PlayerGroup.getGroup(target).getPlayers().size() < 16) {

											PlayerGroup.getGroup(target).getMembers().add(p);
											p.sendMessage(
													srv_msg + "Vous avez rejoint le groupe de " + name + ".");
											target.sendMessage(srv_msg + p.getName() + " a rejoint votre groupe.");

											p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
											target.playSound(target.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);

											CommandGroup.requests.remove(p);

										}  else
											p.sendMessage(srv_msg + "Le groupe est plein !");
									} else
										p.sendMessage(srv_msg + "Ce joueur n'est maintenant dans aucun groupe.");

								} else
									p.sendMessage(srv_msg + "Vous n'avez aucune demande venant de ce joueur.");
							} else
								p.sendMessage(srv_msg + "Vous n'avez aucune demande.");
						} else
							p.sendMessage(srv_msg + "Joueur invalide");
					} else {
						p.sendMessage(srv_msg + "Usage : /accept <player>");
					}
				} else
					p.sendMessage(srv_msg + "Vous êtes déja dans un groupe, faites /group leave pour le quitter.");
			}
			return true;
		}
		return false;
	}

	
}
