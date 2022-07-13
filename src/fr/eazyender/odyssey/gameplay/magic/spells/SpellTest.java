package fr.eazyender.odyssey.gameplay.magic.spells;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import fr.eazyender.odyssey.entity.projectiles.ProjectileProps;
import fr.eazyender.odyssey.entity.projectiles.ProjectilesManager;
import fr.eazyender.odyssey.gameplay.magic.WandUtils;

public class SpellTest extends ISpell{

	public static int basicCooldown = 1 * 1000;
	public static int basicCost = 0;

	private Player sender;

	public SpellTest() {
		super(basicCooldown);
	}	

	public void launch(Player player) {

		if(WandUtils.player_mana.get(player.getUniqueId()) >= basicCost) {
			if (super.launch(player, SpellTest.class)) {

				this.sender = player;
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 1);

				ProjectilesManager.createProjectile(this, player, player.getLocation(), new Vector(0,-0.5,0), player.getTargetBlock(null, 40).getLocation().toVector().clone().subtract(player.getEyeLocation().toVector()).normalize().multiply(3), 
						ProjectileProps.ProjectileTrigger.ENTITY, 2.5D, ProjectileProps.ProjectileSource.NONE, new Double[] {0D,0D}, ProjectileProps.ProjectileExtendedType.NONE);


				player.setVelocity(player.getVelocity().clone().add(player.getTargetBlock(null, 40).getLocation().toVector().clone().normalize().multiply(-0.5)));
				WandUtils.player_mana.replace(player.getUniqueId(), WandUtils.player_mana.get(player.getUniqueId())-basicCost);
			}
		}
	}

	@Override
	public void draw(Location pos, Vector velocity, double time) {
		
		Particle.DustOptions dustOptions = new Particle.DustOptions(Color.NAVY, 4.0F);
		pos.getWorld().spawnParticle(Particle.REDSTONE, pos , 5, 0D, 0D, 0D, 0, dustOptions, true);
		pos.getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, pos.getX(), pos.getY(), pos.getZ(),0, 0D, 0D, 0D);

	}

	@Override
	public void trigger(Location pos, Object tr) {

		if(tr instanceof LivingEntity) {
			LivingEntity entity =(LivingEntity) tr;
			entity.damage(5);
		}

	}

}

