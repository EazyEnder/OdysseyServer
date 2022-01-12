package fr.eazyender.odyssey.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ParticleUtils {
	
	public static void drawLineOfParticle(DustOptions option, double len, Location target1, Location target2, boolean damage) {
		Location d1 = target1.clone().add(0.5,0.5,0.5);
		Location d2 = target2.clone().add(0.5,0.5,0.5);
		Vector t1 = d2.toVector().clone().subtract(d1.toVector()).normalize().multiply(0.2);
		double length = 0.0D;
		for (double k = 0; length < d1.distance(d2);) {
			Vector v0 = d1.toVector();
			for (double j = 0; j < k; j+=len) {
			v0.add(t1.clone().multiply(1/0.2).multiply(len));
			}
			if(damage) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if(p.getLocation().distance(v0.toLocation(d1.getWorld())) < 1) {
						p.damage(1);
					}
				}
			}
			target1.getWorld().spawnParticle(Particle.REDSTONE, v0.getX(),v0.getY(), v0.getZ() , 0, 0D, 0D, 0D, option);
			k += len;
			length += len;
		}
	}
	
	public static void drawCuboidParticle(DustOptions option, Location target, int intensity, int radius) {
		for (int i = 0; i < intensity; i++) {
			target.getWorld().spawnParticle(Particle.REDSTONE, target.getX()+Math.random()*radius,target.getY()+Math.random()*radius, target.getZ()-Math.random()*radius, 0, 0D, 0D, 0D, option);
			target.getWorld().spawnParticle(Particle.REDSTONE, target.getX()-Math.random()*radius,target.getY()+Math.random()*radius, target.getZ()-Math.random()*radius, 0, 0D, 0D, 0D, option);
			target.getWorld().spawnParticle(Particle.REDSTONE, target.getX()+Math.random()*radius,target.getY()+Math.random()*radius, target.getZ()+Math.random()*radius, 0, 0D, 0D, 0D, option);
			target.getWorld().spawnParticle(Particle.REDSTONE, target.getX()-Math.random()*radius,target.getY()+Math.random()*radius, target.getZ()+Math.random()*radius, 0, 0D, 0D, 0D, option);
		}
	}

}
