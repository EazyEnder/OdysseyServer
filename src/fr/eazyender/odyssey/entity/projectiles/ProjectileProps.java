package fr.eazyender.odyssey.entity.projectiles;

public class ProjectileProps {
	
	public enum ProjectileTrigger {
		PLAYER, TIMER, BLOCK, ENTITY
	}
	
	public enum ProjectileSource {
		ATTRACTIVE, REPULSION, REFLECTIVE, NONE
	}
	
	public enum ProjectileExtendedType {
		FOLLOW_CAM, STATIC, FOLLOW_TARGET, NONE
	}
	
	public enum System{
		CARTESIAN, SPHERIC, CYLINDRIC
	}

}
