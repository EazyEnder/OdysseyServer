package fr.eazyender.odyssey.gameplay.magic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import fr.eazyender.odyssey.gameplay.magic.spells.ISpell;
import fr.eazyender.odyssey.utils.maths.ISphericPosition;

public class IRune {
	
	private String name;
	private int element;
	private ISpell spell;
	private List<ISphericPosition> positions;
	private double precision, tolerance;
	
	public IRune(String name, int element, ISpell spell, List<ISphericPosition> positions, double precision, double tolerance) {
		this.name = name;
		this.element = element;
		this.spell = spell;
		this.positions = positions;
		this.precision = precision;
		this.tolerance = tolerance;
	}
	
	public boolean verifyRune(List<ISphericPosition> pos2) {
		boolean isIt = true;
		
		//GET DISTANCE OF ALL POINT TO THE ORIGIN
		List<double[]> position_distance_to_origin = new ArrayList<double[]>();
		for (ISphericPosition p : positions) {
			position_distance_to_origin.add(p.getDistanceToAnotherPoint(positions.get(0)));
		}
		List<double[]> position2_distance_to_origin = new ArrayList<double[]>();
		for (ISphericPosition p : pos2) {
			position2_distance_to_origin.add(p.getDistanceToAnotherPoint(pos2.get(0)));
		}
		
		List<Boolean> key_valide = new CopyOnWriteArrayList<Boolean>();
		List<Boolean> obj_valide = new CopyOnWriteArrayList<Boolean>();
		int i = 0, j= 0;
		
		for (double[] distance : position_distance_to_origin) {
			key_valide.add(false);
			j = 0;
			for (double[] distance2 : position2_distance_to_origin) {
				if(obj_valide.size() < position2_distance_to_origin.size())
				obj_valide.add(false);
				if(Math.abs(distance[0]-distance2[0]) <= tolerance && Math.abs(distance[1]-distance2[1]) <= tolerance) {
					key_valide.set(i, true);
				}
				if(Math.abs(distance[0]-distance2[0]) <= 0.5 && Math.abs(distance[1]-distance2[1]) <= precision) {
					if(!obj_valide.get(j)) {obj_valide.set(j, true);}
				}
				j++;
			}
			i++;
		}
		
		for (Boolean boolean1 : key_valide) {
			if(!boolean1) isIt = false;
		}
		for (Boolean boolean1 : obj_valide) {
			if(!boolean1) isIt = false;
		}
		
		return isIt;
	}
	
//	public boolean verifyRune(List<ISphericPosition> pos2) {
//		boolean isIt = true;
//		List<Vector> p_trs = new CopyOnWriteArrayList<Vector>();
//		for (Vector vector : positions) {
//			Vector inter = vector.clone().subtract(positions.get(0).clone());
//			p_trs.add(inter);
//		}
//		List<Vector> trs = new CopyOnWriteArrayList<Vector>();
//		Vector origin = pos2.get(0).clone();
//		//float angle = (float) Math.acos((origin.getX()*positions.get(0).getX()*+origin.getZ()*positions.get(0).getZ())/(Math.sqrt(origin.getX()*origin.getX()+origin.getZ()*origin.getZ())+Math.sqrt(positions.get(0).getX()*positions.get(0).getX()+positions.get(0).getZ()*positions.get(0).getZ())));
//		
//		
//		
//		
//		//System.out.println(angle);
//		//origin.rotateAroundY(angle);
//		for (Vector vector : pos2) {
//			//Vector inter = vector.rotateAroundY(angle);
//			Vector inter = vector.clone().subtract(origin.clone());
//			trs.add(inter);
//		}
//		
//		
//		
//		List<Boolean> key_valide = new CopyOnWriteArrayList<Boolean>();
//		List<Boolean> obj_valide = new CopyOnWriteArrayList<Boolean>();
//		int i = 0, j= 0;
//		
//		for (Vector vector : p_trs) {
//			key_valide.add(false);
//			j = 0;
//			for (Vector vector2 : trs) {
//				if(obj_valide.size() < trs.size())
//				obj_valide.add(false);
//				if(vector.distance(vector2) <= tolerance) {
//					key_valide.set(i, true);
//				}
//				if(vector.distance(vector2) <= 1) {
//					if(!obj_valide.get(j)) {obj_valide.set(j, true);}
//				}
//				j++;
//			}
//			i++;
//		}
//		
//		
//		for (Boolean boolean1 : key_valide) {
//			if(!boolean1) isIt = false;
//		}
//		for (Boolean boolean1 : obj_valide) {
//			System.out.print(boolean1);
//			if(!boolean1) isIt = false;
//		}
//		
//		return isIt;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getElement() {
		return element;
	}

	public void setElement(int element) {
		this.element = element;
	}

	public ISpell getSpell() {
		return spell;
	}

	public void setSpell(ISpell spell) {
		this.spell = spell;
	}

	public List<ISphericPosition> getPositions() {
		return positions;
	}

	public void setPositions(List<ISphericPosition> positions) {
		this.positions = positions;
	}

	public double getPrecision_number() {
		return precision;
	}

	public void setPrecision_number(double precision_number) {
		this.precision = precision_number;
	}

	public double getTolerance() {
		return tolerance;
	}

	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}
	
	
	

}
