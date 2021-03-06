package fr.eazyender.odyssey.gameplay.aura;

import java.util.ArrayList;

import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.gameplay.stats.Classe;
import net.md_5.bungee.api.ChatColor;

public class Cast {

	public static ArrayList<Player> castAnimation = new ArrayList<>();

	Player p;
	Classe type;
	String pattern = "";
	BukkitTask expired = null;
	long time = 0;

	Cast(Player p, Classe type) {
		this.p = p;
		this.type = type;
		AuraCastListener.casts.put(p, this);
	}

	public Player getPlayer() {
		return p;
	}

	public void setPlayer(Player p) {
		this.p = p;
	}

	public Classe getType() {
		return type;
	}

	public void setType(Classe type) {
		this.type = type;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		if (this.pattern.length() < 10)
			this.pattern = pattern;
	}

	public void animate() {
		this.time = System.currentTimeMillis();

		BossBar bar = AuraHUD.player_bossbars.get(p.getUniqueId());
		bar.setTitle(generateText(false, false));
		if (castAnimation.contains(p)) {
			bar.setColor(BarColor.YELLOW);
			castAnimation.remove(p);
		}
		AuraHUD.player_bossbars.replace(p.getUniqueId(), bar);
		p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 0.3f, 10f);
		if (expired != null)
			expired.cancel();
		expired = new BukkitRunnable() {

			@Override
			public void run() {
				cancelCast();
			}

		}.runTaskLater(OdysseyPl.getOdysseyPlugin(), 40);

	}

	@SuppressWarnings("deprecation")
	public void cast() {
		try {
			Class<? extends Skill> skillClass = Skill.getSkill(this);
			BossBar bar = AuraHUD.player_bossbars.get(p.getUniqueId());
			if (skillClass != null) {
				bar.setTitle(generateText(true, true));
				bar.setColor(BarColor.RED);
				skillClass.newInstance().launch(p, null);
			} else {
				bar.setColor(BarColor.WHITE);
				p.playSound(p.getLocation(), Sound.ITEM_SHIELD_BREAK, 1, 1);
			}
			


			AuraHUD.player_bossbars.replace(p.getUniqueId(), bar);
			castAnimation.add(p);
			if (expired != null) expired.cancel();
			new BukkitRunnable() {

				@Override
				public void run() {
					if (castAnimation.contains(p)) {
						BossBar bar = AuraHUD.player_bossbars.get(p.getUniqueId());
						bar.setTitle("");
						bar.setColor(BarColor.YELLOW);
						AuraHUD.player_bossbars.replace(p.getUniqueId(), bar);
						castAnimation.remove(p);
					}
				}

			}.runTaskLater(OdysseyPl.getOdysseyPlugin(), 20);
			
			
			
			AuraCastListener.casts.remove(p);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}


	}

	public void cancelCast() {

		BossBar bar = AuraHUD.player_bossbars.get(p.getUniqueId());
		bar.setTitle("");
		AuraHUD.player_bossbars.replace(p.getUniqueId(), bar);

		AuraCastListener.casts.remove(p);

	}

	public static char getClick(Action action) {
		if (action.name().contains("LEFT"))
			return 'L';
		else
			return 'R';
	}

	public String generateText(boolean cast, boolean successful) {
		if (!cast)
			return "??f" + getText();
		else {
			if (successful)
				return ChatColor.of("#ff1100") + getText();
			return ChatColor.of("&#6e6b68") + getText();

		}
	}

	public String getText() {
		String text = "";
		for (char character : pattern.toCharArray()) {
			text += character + " - ";
		}
		text = text.substring(0, text.length() - 2);
		return text;

	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
