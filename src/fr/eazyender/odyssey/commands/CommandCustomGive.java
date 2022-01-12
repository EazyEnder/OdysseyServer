package fr.eazyender.odyssey.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.eazyender.odyssey.gameplay.magic.WandUtils;
import fr.eazyender.odyssey.utils.block.BlockUtils;

public class CommandCustomGive implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if (cmd.getName().equalsIgnoreCase("cgive")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if(player.isOp()) {
                	
                	if(args.length <= 0) {
    					player.sendMessage("/cgive <id>...");
    				}
                	if(args.length >= 1) {
                		int id = Integer.parseInt(args[0]);
                		if(args.length <= 1) {
        					player.sendMessage("/cgive <id> <block/item>");
        				}
        				if(args.length >= 2) {
        					if(args[1].equals("block")) {
        						
        						player.getInventory().addItem(BlockUtils.blocks.get(id).getItem());
        						
        					}else if(args[1].equals("item")) {
        						if(args.length == 2) {
        	    					player.sendMessage("/cgive <id> <item> <generic/wand>");
        	    				}
        	    				if(args.length >= 3) {
        	    					if(args[2].equals("generic")) {
        	    						
        	    					}else if(args[2].equals("wand")) {
        	    						player.getInventory().addItem(WandUtils.wands.get(id).getItemStack());
        	    					}
        	    				}
        					}
        				}
                	}
          	
                }

                return true;
            }
        }

        return false;
    }

    }
