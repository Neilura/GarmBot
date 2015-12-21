package javabot.managers;

import javabot.JavaBot;
import javabot.knowledge.Tech;
import javabot.model.Unit;
import javabot.types.TechType.TechTypes;
import javabot.types.UnitType.UnitTypes;

public class HatcheryManager extends BuildingManager {

	public UPGRADE_STATES state = UPGRADE_STATES.NONE;
	
	public HatcheryManager(Unit u) {
		super(u);
	}
	
	public void update() {
		super.update();
		
		if (unit.getRemainingBuildTimer() > 0 && unit.getHitPoints() < 100) {
			bwapi.cancelConstruction(unit.getID());
		}
		
		if (base != null)
			base.timerRebuild = 5;
		
		if (state == UPGRADE_STATES.NONE && unit.getRemainingResearchTime() > 0) {
			state = UPGRADE_STATES.EVOLVING;
			if (Tech.burrow == 0)
				Tech.burrow = 1;
		}
		
		if (state == UPGRADE_STATES.EVOLVING && unit.getRemainingResearchTime() == 0) {
			state = UPGRADE_STATES.NONE;
			Tech.burrow = 2;
		}
		
		if (Tech.burrow == 0) {
			if (JavaBot.knowledge.realMinerals >= 100 && JavaBot.knowledge.realGas >= 100) {
				BwapiCallsManager.research(unit.getID(), TechTypes.Burrowing.ordinal());
				countDontAct = 5;
			}
		}
		else if (base != null && base.requestLair && countDontAct == 0 && base.blockLair == 0 && (Tech.burrow == 2 || JavaBot.currFrame >= 3000)) {
			if (JavaBot.knowledge.realMinerals >= 150 && JavaBot.knowledge.realGas >= 100) {
				BwapiCallsManager.morph(unit.getID(), UnitTypes.Zerg_Lair.ordinal());
				base.blockLair = 5;
			}
			else
				JavaBot.requestsManager.requestRessources(150, 100);
		}
	}
	
	public void drawDebug()
	{
		bwapi.drawText(unit.getX() - 10, unit.getY() - 8, "Hatchery  tech = " + Tech.burrow + ", time : " + unit.getRemainingResearchTime(), false);
	}
}
