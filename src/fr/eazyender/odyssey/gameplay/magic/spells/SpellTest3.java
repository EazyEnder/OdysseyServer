package fr.eazyender.odyssey.gameplay.magic.spells;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import fr.eazyender.odyssey.entity.aoe.AOEProps;
import fr.eazyender.odyssey.entity.aoe.AoeManager;
import fr.eazyender.odyssey.gameplay.magic.WandUtils;
import fr.eazyender.odyssey.utils.ParticleUtils;

public class SpellTest3 extends ISpell{

	public static int basicCooldown = 1 * 1000;
	public static int basicCost = 0;
	
	private static Double size = 5.0;

	private Player sender;

	public SpellTest3() {
		super(basicCooldown);
	}	

	public void launch(Player player) {

		if(WandUtils.player_mana.get(player.getUniqueId()) >= basicCost) {
			if (super.launch(player, SpellTest3.class)) {

				this.sender = player;
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 1);

				AoeManager.createAoe(this, sender, player.getTargetBlock(null, 40).getLocation().add(0,1,0), size,
						new Vector(0,0,0), new Vector(0.1,0,0), 10.0, AOEProps.Source.REFLECTIVE, new Double[]{2.5, 1.0}, AOEProps.ExtendedType.STATIC, AOEProps.Shape.CIRCLE);


				WandUtils.player_mana.replace(player.getUniqueId(), WandUtils.player_mana.get(player.getUniqueId())-basicCost);
			}
		}
	}

	@Override
	public void draw(Location pos, Vector velocity, double time) {
		
		Particle.DustOptions dustOptions = new Particle.DustOptions(Color.OLIVE, 2.0F);
		ParticleUtils.drawPlanParticle(dustOptions, pos, 15, size);

	}

	@Override
	public void trigger(Location pos, Object tr) {

		if(tr instanceof List<?>) {
			
		}

	}

}
