package javabot.managers;

import javabot.JavaBot;
import javabot.model.Unit;
import javabot.operators.Operators;
import javabot.types.UnitType.UnitTypes;

public class CreepColonyManager extends BuildingManager {
	
	public CreepColonyManager(Unit u) {
		super(u);
	}
	
	public void update() {
		super.update();
		
		if (JavaBot.knowledge.currentBuildings[Operators.iterPool] > 0 && JavaBot.repartition.getCreepMinerals() >= 50 && (base == null || (base.timerTryCreep == 0 && base.nbSunkenColony < 2))) {
			if (BwapiCallsManager.morph(unit.getID(), UnitTypes.Zerg_Sunken_Colony.ordinal()))
			if (base != null)
				base.timerTryCreep = 10;
		}
		else if (JavaBot.knowledge.currentBuildings[Operators.iterEvolutionChamber] > 0 && JavaBot.repartition.getCreepMinerals() >= 50 && (base == null || (base.timerTryCreep == 0 && base.nbSporeColony < 1))) {
			if (BwapiCallsManager.morph(unit.getID(), UnitTypes.Zerg_Spore_Colony.ordinal()))
			if (base != null)
				base.timerTryCreep = 10;
		}
	}
	
	public void drawDebug() {
		bwapi.drawText(unit.getX() - 10, unit.getY() - 8, "Creep Colony", false);
	}
}
