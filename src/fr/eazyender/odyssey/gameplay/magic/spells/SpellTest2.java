package fr.eazyender.odyssey.gameplay.magic.spells;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import fr.eazyender.odyssey.entity.projectiles.ProjectileProps;
import fr.eazyender.odyssey.entity.projectiles.ProjectilesManager;
import fr.eazyender.odyssey.gameplay.magic.WandUtils;

public class SpellTest2 extends ISpell{

	public static int basicCooldown = 1 * 1000;
	public static int basicCost = 0;
	
	private FallingBlock fb = null;

	private Player sender;

	public SpellTest2() {
		super(basicCooldown);
	}	

	public void launch(Player player) {

		if(WandUtils.player_mana.get(player.getUniqueId()) >= basicCost) {
			if (super.launch(player, SpellTest.class)) {

				this.sender = player;
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 1);

				ProjectilesManager.createProjectile(this, player, player.getLocation(), new Vector(0,0,0), new Vector(0,0,0), 
						ProjectileProps.ProjectileTrigger.TIMER, 10D, ProjectileProps.ProjectileSource.ATTRACTIVE, new Double[] {100D,2D}, ProjectileProps.ProjectileExtendedType.STATIC);


				player.setVelocity(player.getVelocity().clone().add(player.getTargetBlock(null, 40).getLocation().toVector().clone().normalize().multiply(-0.5)));
				WandUtils.player_mana.replace(player.getUniqueId(), WandUtils.player_mana.get(player.getUniqueId())-basicCost);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void draw(Location pos, Vector velocity, double time) {
		
		if(fb !=null)fb.remove();
		fb = pos.getWorld().spawnFallingBlock(pos, Material.REDSTONE_BLOCK, (byte) 0);
		fb.setGravity(false);
		fb.setHurtEntities(false);
		fb.setDropItem(false);
		fb.setInvulnerable(true);
		fb.setGlowing(true);
		
		

	}

	@Override
	public void trigger(Location pos, Object tr) {

		Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 2.0F);
		pos.getWorld().spawnParticle(Particle.REDSTONE, pos.add(-0.5, -0.5, -0.5) , 20, 1D, 1D, 1D, 0, dustOptions, true);
		if(fb !=null)fb.remove();

	}

}


