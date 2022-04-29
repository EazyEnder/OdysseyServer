package fr.eazyender.odyssey.dungeons;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class DungeonStaffCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {

		if (args.length > 0) {

			if (args[0].equals("create")) {

				File dungeonFile = new File("plugins/OdysseyPlugin/Dungeons/" + args[1] + ".yml");
				if (!dungeonFile.exists()) {
					dungeonFile.getParentFile().mkdirs();
					try {
						dungeonFile.createNewFile();
						Dungeon dungeon = new Dungeon(args[1], new HashMap<>(), new HashMap<>(), new HashMap<>(), null,
								null, new ArrayList<>());
						refreshDungeonYaml(dungeon);
					} catch (IOException e) {

						e.printStackTrace();
					}
					sender.sendMessage("§aDonjon crée !");

				} else
					sender.sendMessage("§cCe donjon existe déjà !");
			}
			if (args[0].equals("delete") || args[0].equals("remove")) {
				File dungeonFile = new File("plugins/OdysseyPlugin/Dungeons/" + args[1] + ".yml");
				if (dungeonFile.exists()) {
					dungeonFile.delete();
					sender.sendMessage("§aDonjon supprimé !");
				} else
					sender.sendMessage("§cDonjon non trouvé !");
			}
			// /dstaff startloc <dj> <pos>
			if (args[0].equals("startloc") && sender instanceof Player) {
				Player p = (Player) sender;
				Dungeon d = DungeonConfig.getDungeon(args[1]);
				int pos = Integer.parseInt(args[2]);
				d.getStartLocs().put(pos, p.getLocation());
				p.sendMessage(
						"§dDonjon " + args[1] + " | pos : " + pos + " -> new location : " + p.getLocation().toString());

				refreshDungeonYaml(d);
			}
			// /dstaff startloc <dj> <id>
			if (args[0].equals("offset") && sender instanceof Player) {
				Player p = (Player) sender;
				Dungeon d = DungeonConfig.getDungeon(args[1]);
				int idOffset = Integer.parseInt(args[2]);
				double xOffset = p.getLocation().getX() - d.getStartLocs().get(0).getX();
				double zOffset = p.getLocation().getZ() - d.getStartLocs().get(0).getZ();
				String offset = xOffset + "," + zOffset;
				d.getOffsets().put(idOffset, offset);
				sender.sendMessage("§dDonjon " + args[1] + " | id : " + idOffset + " -> nouvel offset -> §6" + offset);
				refreshDungeonYaml(d);
			}
			;
			if (args[0].equals("mob") && sender instanceof Player) {
				Player p = (Player) sender;
				Dungeon d = DungeonConfig.getDungeon(args[1]);
				d.getMobs().put(p.getLocation(), args[2]);
				sender.sendMessage("§dDonjon " + args[1] + " | mob : §6" + args[2] + " -> new location : §6"
						+ p.getLocation().toString());
				refreshDungeonYaml(d);

			}
			if (args[0].equals("test")) {
				ArrayList<Player> players = new ArrayList<>();
				players.add((Player) sender);

				DungeonInstance instance = new DungeonInstance(players, DungeonConfig.getDungeon("lul"), 1);
				instance.run();
			}

		} else
			sender.sendMessage("§d/dstaff create/startloc/mob/offset");

		return false;
	}

	void refreshConfig(String id, FileConfiguration config) {
		try {
			config.save(new File("plugins/OdysseyPlugin/Dungeons/" + id + ".yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void refreshDungeonYaml(Dungeon d) {
		File fileConfig = new File("plugins/OdysseyPlugin/Dungeons/" + d.getId() + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(fileConfig);
		config.set(d.getId(), d);
		refreshConfig(d.getId(), config);

	}

}
