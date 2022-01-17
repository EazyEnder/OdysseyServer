package fr.eazyender.odyssey.gameplay.aura;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.gameplay.items.ItemType;
import fr.eazyender.odyssey.gameplay.items.ItemUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Cast {

	Player p;
	ItemType type;
	String pattern = "";
	BukkitTask expired = null;
	
	Cast(Player p, ItemType type) {
		this.p = p;
		this.type = type;
	}

	public Player getPlayer() {
		return p;
	}

	public void setPlayer(Player p) {
		this.p = p;
	}

	public ItemType getType() {
		return type;
	}

	public void setType(ItemType type) {
		this.type = type;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	public void animate() {
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(generateText(false, false)));
		
		if (expired != null) expired.cancel();
		expired = new BukkitRunnable() {

			@Override
			public void run() {
				cancel();
			}
			
		}.runTaskLater(OdysseyPl.getOdysseyPlugin(), 40);
		
	}
	
	public void cast() {
		
	}
	
	public void cancel() {
		AuraCastListener.casts.remove(this);
		
	}
	
	public static char getClick(Action action) {
		if (action.name().contains("LEFT")) return 'L';
		else return 'R';
	}
	
	public String generateText(boolean cast, boolean successful) {
		if (!cast) return "§f" + getText();
		else {
			if (successful) return ItemUtils.translateHexCodes("&#ffac54" + getText());
			else return ItemUtils.translateHexCodes("&#6e6b68" + getText());
		}
	}
	
	public String getText() {
		String text = "";
		for(char character : pattern.toCharArray()) {
			text += character + " - ";
		}
		text = text.substring(0,text.length() - 2);
		return text;
		
	}
	
	
}
