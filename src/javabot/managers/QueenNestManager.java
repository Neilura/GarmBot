package javabot.managers;

import javabot.JavaBot;
import javabot.knowledge.Tech;
import javabot.model.Unit;
import javabot.types.TechType.TechTypes;

public class QueenNestManager extends BuildingManager {

	public UPGRADE_STATES state = UPGRADE_STATES.NONE;
	public int upgradeToDo = 0;
	
	public QueenNestManager(Unit u) {
		super(u);
	}
	
	public void update()
	{
		super.update();
		
		if (upgradeToDo >= 1 || Tech.broodlings == 1 || unit.getRemainingBuildTimer() > 0) return;
		
		if (state == UPGRADE_STATES.NONE && unit.getRemainingResearchTime() > 0) {
			state = UPGRADE_STATES.EVOLVING;
		}
		
		if (state == UPGRADE_STATES.EVOLVING && unit.getRemainingResearchTime() == 0) {
			state = UPGRADE_STATES.NONE;
			if (upgradeToDo == 0)
				Tech.broodlings = 1;
			upgradeToDo++;
		}
		
		if (state == UPGRADE_STATES.NONE) {
			if (upgradeToDo == 0) {
					if (upgradeToDo == 0 && Tech.broodlings == 1) {
						upgradeToDo++;
						return;
					}
				if (JavaBot.knowledge.realMinerals >= 100 && JavaBot.knowledge.realGas >= 100) {
					if (JavaBot.strategy.nbQueensTotal > 0)
						BwapiCallsManager.research(unit.getID(), TechTypes.Spawn_Broodlings.ordinal());
				}
				else {
					JavaBot.requestsManager.requestRessources(100, 100);
				}
			}
		}
	}

	public void drawDebug()
	{
		if (unit.getRemainingResearchTime() > 0)
			bwapi.drawText(unit.getX() - 10, unit.getY() - 8, "QueenNest " + unit.getRemainingResearchTime(), false);
		else
			bwapi.drawText(unit.getX() - 10, unit.getY() - 8, "QueenNest", false);
	}
}
