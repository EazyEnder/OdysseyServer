package fr.eazyender.odyssey.gameplay.magic;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.eazyender.odyssey.gameplay.magic.spells.SpellTest;
import fr.eazyender.odyssey.gameplay.magic.spells.SpellTest2;

public class CommandTrySpell implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {

		Player p = (Player) sender;
		
		if(args.length > 0) {
			
			if(args[0].equalsIgnoreCase("fire")) {
				if(args.length > 1) {
					if(Integer.parseInt(args[1]) == 1) {
						SpellTest fb = new SpellTest(); fb.launch(p);
					}else if(Integer.parseInt(args[1]) == 2) {
						SpellTest2 fb = new SpellTest2(); fb.launch(p);
					}else {
						
					}
				}
			}else {
				p.sendMessage("Usage : /tryspell (fire/wind) (spellid)");
			}
			
		}
		
		return false;
	}

}
