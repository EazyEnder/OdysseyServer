package fr.eazyender.odyssey.gameplay.magic.spells.fire;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import fr.eazyender.odyssey.OdysseyPl;
import fr.eazyender.odyssey.gameplay.magic.WandUtils;
import fr.eazyender.odyssey.gameplay.magic.spells.ColorUtils;
import fr.eazyender.odyssey.gameplay.magic.spells.ISpell;

public class SpellFirebolt extends ISpell{
	
	List<BukkitRunnable> brun = new ArrayList<BukkitRunnable>();
	public static int basicCooldown = 5 * 1000;
	public static int basicCost = 30;
	
	public SpellFirebolt() {
		super(basicCooldown);
	}

   public void launch(Player player) {
	   
	   if(WandUtils.player_mana.get(player.getUniqueId()) >= basicCost) {
       if (super.launch(player, SpellFirebolt.class)) {
                	
    	  player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 1);
    	   launchSpell(player,player.getEyeLocation(), player.getTargetBlock(null, 40).getLocation(), ColorUtils.fire);
    	   WandUtils.player_mana.replace(player.getUniqueId(), WandUtils.player_mana.get(player.getUniqueId())-basicCost);
	   }
	   }
   }
   
   private void launchSpell(Player player, Location target, Location target2, Color color) {
	   
	   
	    double distance = target.distance(target2);
	    Vector v = target.toVector();
	    Vector v2 = target2.toVector();
	    Vector vector = v2.clone().subtract(v).normalize().multiply(0.25D);
	    
	    player.setVelocity(vector.clone().multiply(-5));
	    
	    
	    double length = 0.0D;
	    for (int i = 0; length < distance;) {
	    	int z = i;
	    	double length_copy = length;
	    	brun.add(new BukkitRunnable() {
				@Override
				public void run() {
					final int w = z;
					Vector v1 = target.toVector();
					for (int j = 0; j < w; j++) {
						v1.add(vector);
					}
					boolean collide = false;
					Location l = new Location(target.getWorld(),v1.getX(), v1.getBlockY(), v1.getBlockZ());
					for (int j = 0; j < target.getWorld().getEntities().size(); j++) {
						if(!(target.getWorld().getEntities().get(j).equals(player))  && target.getWorld().getEntities().get(j) instanceof LivingEntity) {
							if(l.distance(target.getWorld().getEntities().get(j).getLocation()) < 2.5) {
								collide = true;
							}
						}
					}
					if(!collide) {
					  Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1.0F);
					  for (int j = 0; j < 360; j=j+40) {
						 target.getWorld().spawnParticle(Particle.FLAME, v1.getX() + Math.cos(j) * 0.5, v1.getY() + Math.sin(j) * 0.5, v1.getZ() + Math.cos(j) * 0.5, 1, 0.0D, 0.0D, 0.0D, 0.1D);
					  }
					  
					 
					  
					  int nbrcircle = 2;
					  for (double i = 0; i <= Math.PI; i += Math.PI / nbrcircle) {
						   double radius = Math.sin(i) * 0.25;
						   double y = Math.cos(i) * 0.25;
						   for (double a = 0; a < Math.PI * 2; a+= Math.PI / nbrcircle) {
						      double x = Math.cos(a) * radius;
						      double z = Math.sin(a) * radius;
						      v1.add(new Vector(x, y, z));
						      target.getWorld().spawnParticle(Particle.REDSTONE, v1.getX(), v1.getY(), v1.getZ() , 0, 10D, 0D, 0D, dustOptions);
						      v1.subtract(new Vector(x, y, z));
						      
						   }
						   
						}
					}else {
						
						  target.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, v1.getX(), v1.getY(), v1.getZ() , 0, 0D, 0D, 0D);
						  
						 
							  target.getWorld().spawnParticle(Particle.LAVA, v1.getX(), v1.getY(), v1.getZ() , 25, 0.25D, 0.25D, 0.25D);
						  
						  for (int j = 0; j < target.getWorld().getEntities().size(); j++) {
								if(!(target.getWorld().getEntities().get(j).equals(player)) && target.getWorld().getEntities().get(j) instanceof LivingEntity) {
									if(l.distance(target.getWorld().getEntities().get(j).getLocation()) < 2.5) {
										LivingEntity entity = (LivingEntity)target.getWorld().getEntities().get(j);
										entity.damage(8, player);
									}
								}
							}
						  
						  
						  for (int j = 0; j < brun.size(); j++) {
								brun.get(j).cancel();
							}
						  
						
					}
					  			
				}
			});   	
	        
	    	i++;
	        length += 0.25D;
	    }
	    for (int j = 0; j < brun.size(); j++) {
			brun.get(j).runTaskLater(OdysseyPl.getOdysseyPlugin(), (int)(Math.log(j)*10));
		}
	    
	  }

}
