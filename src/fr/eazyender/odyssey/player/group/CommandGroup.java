package fr.eazyender.odyssey.player.group;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGroup implements CommandExecutor {

	public static Map<Player, HashMap<Player, PlayerGroup>> requests = new HashMap<Player, HashMap<Player, PlayerGroup>>();
	public static final String srv_msg = "§f[§e§lGroupe§r§f] : ";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

		if (cmd.getName().equalsIgnoreCase("group")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (args.length >= 1) {

					if (PlayerGroup.getGroup(p) == null && !args[0].equalsIgnoreCase("invite")) {
						p.sendMessage(srv_msg + "Vous n'êtes pas dans un groupe !");
						return false;
					}
					if (args[0].equalsIgnoreCase("invite")) {

						if (args.length >= 2) {

							String name = args[1];
							Player target = Bukkit.getPlayer(name);

							if (target != null) {
								if (target != p) {
									if (PlayerGroup.getGroup(target) == null) {

										PlayerGroup group = new PlayerGroup(p);
										HashMap<Player, PlayerGroup> asks = new HashMap<Player, PlayerGroup>();
										if (requests.containsKey(target)) {
											asks = requests.get(target);
											asks.put(p, group);
										} else {
											asks.put(p, group);
										}
										requests.put(target, asks);
										p.sendMessage(srv_msg + "Vous avez invité " + target.getName());
										target.sendMessage(srv_msg + p.getName()
												+ " vous a invité dans son groupe ! Pour accepter, faites : /accept <player>");
										p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
										target.playSound(target.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
									} else
										p.sendMessage(srv_msg + "Ce joueur appartient déja à un groupe.");
								} else
									p.sendMessage(srv_msg + "Vous ne pouvez pas vous inviter vous-même !");

							} else
								p.sendMessage(srv_msg + "Le joueur n'est pas valide.");

						} else
							p.sendMessage(srv_msg + "Usage : /group invite <Player>");
						return false;

					}

					PlayerGroup group = PlayerGroup.getGroup(p);
					if (args[0].equalsIgnoreCase("leave")) {
						group.leave(p);
						p.sendMessage(srv_msg + "Vous avez quitté le groupe.");

					}

					if (args[0].equalsIgnoreCase("list")) {

						String members = "";
						for (int i = 0; i < group.getMembers().size(); i++) {
							members = ", " + members + group.getMembers().get(i).getName();

						}
						p.sendMessage(srv_msg + "Votre groupe est composé de : (Leader)" + group.getHost().getName()
								+ members);

					}

					if (args[0].equalsIgnoreCase("kick")) {
						if (args.length >= 2) {
							String name = args[1];
							if (Bukkit.getPlayer(name) != null) {
								Player target = Bukkit.getPlayer(name);
								if (group.getMembers().contains(target)) {

									group.getMembers().remove(target);
									p.sendMessage(srv_msg + "Vous avez exclu le joueur " + name + ".");

								} else
									p.sendMessage(srv_msg + "Ce joueur n'est pas dans votre groupe.");

							} else
								p.sendMessage(srv_msg + "Ce joueur n'existe pas.");

						} else {
							p.sendMessage(srv_msg + "Usage : /group kick <player>");
						}
					}
				} else
					p.sendMessage(srv_msg + "/group invite/kick/list");
			}

		}
		return false;
	}

}
