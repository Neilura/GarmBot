package javabot.managers;

import javabot.JavaBot;
import javabot.model.Unit;
import javabot.operators.Operators;
import javabot.types.UnitType.UnitTypes;

public class SpireManager extends BuildingManager {

	public SpireManager(Unit u) {
		super(u);
	}
	
	public void update() {
		super.update();
		
		if (base != null && JavaBot.knowledge.requestedBuildings[Operators.iterGreatSpire] > 0 && JavaBot.strategy.nbMutalisksTotal >= 2) {
			if (JavaBot.knowledge.realMinerals >= 200 && JavaBot.knowledge.realGas >= 150)
				BwapiCallsManager.morph(unit.getID(), UnitTypes.Zerg_Greater_Spire.ordinal());
			else
				JavaBot.requestsManager.requestRessources(200, 150);
		}
	}
	
	public void drawDebug()
	{
		bwapi.drawText(unit.getX() - 10, unit.getY() - 8, "Spire", false);
	}
}