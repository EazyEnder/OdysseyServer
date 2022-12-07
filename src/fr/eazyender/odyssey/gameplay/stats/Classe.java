package fr.eazyender.odyssey.gameplay.stats;

import fr.eazyender.odyssey.gameplay.masteries.Mastery;

public enum Classe {

	MAGE, GUERRIER, ARCHER, TANK;

	public Mastery getMastery() {
		switch (this) {
		case GUERRIER:
			return Mastery.GUERRIER;
		case ARCHER:
			return Mastery.ARCHER;
		case TANK:
			return Mastery.TANK;
		default:
			return null;
		}
	}

}
