package fr.eazyender.odyssey.entity.aoe;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import fr.eazyender.odyssey.entity.EntityManager;
import fr.eazyender.odyssey.entity.projectiles.IProjectile;
import fr.eazyender.odyssey.entity.projectiles.ProjectileProps;
import fr.eazyender.odyssey.entity.projectiles.ProjectilesManager;
import fr.eazyender.odyssey.gameplay.magic.spells.ISpell;

public class IAoe {
	
	private ISpell spell;
	private Player sender;
	public Location position;
	public double size;
	//SI > per second
	private Vector force;
	public Vector velocity;
	//timer or distance
	private Double decay_timer;
	public AOEProps.Source source;
	//distance of repulsion / attraction
	public Double source_distance, source_intensity;
	private AOEProps.ExtendedType type;
	
	private AOEProps.Shape shape;
	
	private double timer;
	
	public IAoe(ISpell spell, Player sender, Location position, double size, Vector force, Vector velocity, Double decay_timer, AOEProps.Source source, Double source_distance, Double source_intensity, AOEProps.ExtendedType type, AOEProps.Shape shape) {
		
		this.spell = spell;
		this.sender = sender;
		this.position = position;
		this.size = size;
		this.force = force;
		this.velocity = velocity;
		this.decay_timer = decay_timer;
		this.source = source;
		this.source_distance = source_distance;
		this.source_intensity = source_intensity;
		this.type = type;
		this.shape = shape;
		
	}
	
	public boolean updateAoe() {
		
		//Draw part
		spell.draw(this.position, this.velocity, this.timer);
		
		timer+=new Double(EntityManager.getPeriod())/20;
		
		//Refresh part
		long delta_t = EntityManager.getPeriod();

		Vector updated_force = this.force.clone();
		
		if(this.type== null || (this.type!=null && !this.type.equals(AOEProps.ExtendedType.STATIC)))
			for (IProjectile proj : ProjectilesManager.projectiles.values()) {	
					if(proj.source.equals(ProjectileProps.ProjectileSource.ATTRACTIVE) && this.position.distance(proj.position)<proj.source_distance) {
						updated_force = updated_force.add(proj.position.toVector().clone().subtract(this.position.toVector().clone()).normalize().multiply((Double)(1/this.position.distance(proj.position))).multiply(10*proj.source_intensity));
					}else if(proj.source.equals(ProjectileProps.ProjectileSource.REPULSION) && this.position.distance(proj.position)<proj.source_distance) {
						updated_force = updated_force.add(proj.position.toVector().clone().subtract(this.position.toVector().clone()).normalize().multiply((Double)(1/this.position.distance(proj.position))).multiply(-10*proj.source_intensity));
					}
			}
		
		this.velocity.setY(0);
		updated_force.multiply((double)(delta_t)/20.0).setY(0);
		
		this.velocity.add(updated_force);
		
		this.position.add(this.velocity);
		
		if(this.position.distance(this.sender.getLocation()) > 50) {return true;}
		
		List<LivingEntity> trig = new ArrayList<LivingEntity>();
			
			
			
			
			  for (int j = 0; j < position.getWorld().getEntities().size(); j++) {
					if(!(position.getWorld().getEntities().get(j).equals(sender)) &&  !(position.getWorld().getEntities().get(j) instanceof ArmorStand) && position.getWorld().getEntities().get(j) instanceof LivingEntity) {
						if(position.distance(position.getWorld().getEntities().get(j).getLocation()) < size) {
							 trig.add((LivingEntity) position.getWorld().getEntities().get(j));
						}
					}
				}
			
		
		spell.trigger(this.position, trig);
		if(timer > this.decay_timer) {
			return true;
		}
		return false;
	}

}
