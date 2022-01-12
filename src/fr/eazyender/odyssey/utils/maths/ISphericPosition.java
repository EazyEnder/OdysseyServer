package fr.eazyender.odyssey.utils.maths;

import org.bukkit.Location;
import org.bukkit.World;

public class ISphericPosition {
	
	//latitude
	private double phi;
	//longitude
	private double teta;
	private double rayon;
	
	public ISphericPosition(double phi, double teta, double rayon) {
		this.phi = phi;
		this.teta = teta;
		this.rayon = rayon;
	}
	
	public double[] getDistanceToAnotherPoint(ISphericPosition pos2) {
		
		double phi2 = pos2.getPhi();
		double teta2 = pos2.getTeta();
		
		double delta_phi = Math.abs(phi2-phi);
		if(teta - phi2 < 0 || phi2 - teta > 0) {delta_phi = Math.abs(2*Math.PI-(phi2-phi2));}
		double delta_teta = Math.abs(teta2-teta);
		if(teta - teta2 < 0 || teta2 - teta > 0) {delta_teta = Math.abs(2*Math.PI-(teta2-teta));}
		
		
		return new double[] {delta_teta,delta_phi};
	}
	
	public Location getCartesianLocation(Location center, World world) {
		Location loc = null;
		
		double posX = rayon*Math.cos(phi)*Math.cos(teta);
		double posZ = rayon*Math.cos(phi)*Math.sin(teta);
		double posY = rayon*Math.sin(phi);
		
		loc = new Location(world, center.getX()+posX,
				center.getY()+posY,
				center.getZ()+posZ);
		
		return loc;
	}

	public double getPhi() {
		return phi;
	}

	public void setPhi(double phi) {
		this.phi = phi;
	}

	public double getTeta() {
		return teta;
	}

	public void setTeta(double teta) {
		this.teta = teta;
	}

	public double getRayon() {
		return rayon;
	}

	public void setRayon(double rayon) {
		this.rayon = rayon;
	}
	
	

}
