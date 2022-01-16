package fr.eazyender.odyssey.entity.projectiles;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import fr.eazyender.odyssey.entity.EntityManager;
import fr.eazyender.odyssey.entity.aoe.AOEProps;
import fr.eazyender.odyssey.entity.aoe.AoeManager;
import fr.eazyender.odyssey.entity.aoe.IAoe;
import fr.eazyender.odyssey.gameplay.magic.spells.ISpell;

public class IProjectile {
	
	private ISpell spell;
	private Player sender;
	public Location position;
	//SI > per second
	private Vector force;
	private Vector velocity;
	private ProjectileProps.ProjectileTrigger trigger;
	//timer or distance
	private Double trigger_info;
	public ProjectileProps.ProjectileSource source;
	//distance of repulsion / attraction
	public Double source_distance;
	public Double source_intensity;
	private ProjectileProps.ProjectileExtendedType type;
	
	private ProjectileProps.System system;
	
	private double timer;
	
	public IProjectile(ISpell spell, Player sender, Location position, Vector force, Vector velocity,ProjectileProps.ProjectileTrigger trigger, Double trigger_info, ProjectileProps.ProjectileSource source, Double source_distance, Double source_intensity, ProjectileProps.ProjectileExtendedType type) {
		
		this.spell = spell;
		this.sender = sender;
		this.position = position;
		this.force = force;
		this.velocity = velocity;
		this.trigger = trigger;
		this.trigger_info = trigger_info;
		this.source = source;
		this.source_distance = source_distance;
		this.source_intensity = source_intensity;
		this.type = type;
		
	}
	
	public Object updateProjectile() {
		
		//Draw part
		spell.draw(this.position, this.velocity, this.timer);
		
		timer+=new Double(EntityManager.getPeriod())/20;
		
		//Refresh part
		long delta_t = EntityManager.getPeriod();

		Vector updated_force = this.force.clone();
		
		if(this.type== null || (this.type!=null && !this.type.equals(ProjectileProps.ProjectileExtendedType.STATIC))) {
			for (IProjectile proj : ProjectilesManager.projectiles.values()) {
				if(proj != this) {
					
					if(proj.source.equals(ProjectileProps.ProjectileSource.ATTRACTIVE) && this.position.distance(proj.position)<proj.source_distance) {
						updated_force = updated_force.add(proj.position.toVector().clone().subtract(this.position.toVector().clone()).normalize().multiply((Double)(1/this.position.distance(proj.position))).multiply(10*proj.source_intensity));
					}else if(proj.source.equals(ProjectileProps.ProjectileSource.REPULSION) && this.position.distance(proj.position)<proj.source_distance) {
						updated_force = updated_force.add(proj.position.toVector().clone().subtract(this.position.toVector().clone()).normalize().multiply((Double)(1/this.position.distance(proj.position))).multiply(-10*proj.source_intensity));
					}
					
				}
			}
			for (IAoe aoe : AoeManager.aoes.values()) {
				
					if(aoe.source.equals(AOEProps.Source.ATTRACTIVE) && this.position.distance(aoe.position)<aoe.source_distance) {
						updated_force = updated_force.add(aoe.position.toVector().clone().subtract(this.position.toVector().clone()).normalize().multiply((Double)(1/this.position.distance(aoe.position))).multiply(10*aoe.source_intensity));
					}else if(aoe.source.equals(AOEProps.Source.REPULSION) && this.position.distance(aoe.position)<aoe.source_distance) {
						updated_force = updated_force.add(aoe.position.toVector().clone().subtract(this.position.toVector().clone()).normalize().multiply((Double)(1/this.position.distance(aoe.position))).multiply(-10*aoe.source_intensity));
					}else if(aoe.source.equals(AOEProps.Source.REFLECTIVE) && Math.abs(this.position.getY()-aoe.position.getY()) < aoe.source_distance && (Math.abs(this.position.getX()-aoe.position.getX()) <= aoe.size && Math.abs(this.position.getZ()-aoe.position.getZ()) <= aoe.size)) {
						this.velocity.setY(this.velocity.getY()*-1).add(aoe.velocity).multiply(aoe.source_intensity);
					}
				
			}
		}
		
		updated_force.multiply((double)(delta_t)/20.0);
		
		if(this.system==null || this.system.equals(ProjectileProps.System.CARTESIAN)) {
			this.velocity.add(updated_force);
		}else if(this.system.equals(ProjectileProps.System.SPHERIC)){
			//= need to change base
			
			//ISphericPosition p = new ISphericPosition(
				//	
				//	);
		}
		
		this.position.add(this.velocity);
		
		if(this.position.distance(this.sender.getLocation()) > 100) {return new Object();}
		
		//Trigger part
		Object trig = null;
		if(trigger.equals(ProjectileProps.ProjectileTrigger.BLOCK)  || trigger.equals(ProjectileProps.ProjectileTrigger.TIMER) || trigger.equals(ProjectileProps.ProjectileTrigger.ENTITY)) {
			
			if(!position.getBlock().getType().isAir()) {trig = position.getBlock();}
			
			if(trigger.equals(ProjectileProps.ProjectileTrigger.TIMER)) {
				if(timer > trigger_info) {
					trig = timer;
				}
			}
			
			if(trigger.equals(ProjectileProps.ProjectileTrigger.ENTITY)) {
			  for (int j = 0; j < position.getWorld().getEntities().size(); j++) {
					if(!(position.getWorld().getEntities().get(j).equals(sender)) &&  !(position.getWorld().getEntities().get(j) instanceof ArmorStand) && position.getWorld().getEntities().get(j) instanceof LivingEntity) {
						if(position.distance(position.getWorld().getEntities().get(j).getLocation()) < trigger_info) {
							 trig =  position.getWorld().getEntities().get(j);
						}
					}
				}
			}
		}
		
		if(trig != null)
		spell.trigger(this.position, trig);
		return trig;
	}

}
