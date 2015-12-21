package javabot.managers;

import javabot.JavaBot;
import javabot.knowledge.Tech;
import javabot.model.Unit;
import javabot.operators.Operators;
import javabot.types.UpgradeType.UpgradeTypes;

public class HydraDenManager extends BuildingManager {

	public UPGRADE_STATES state = UPGRADE_STATES.NONE;
	public int upgradeToDo = 0;
	
	public HydraDenManager(Unit u) {
		super(u);
		countDontAct = 0;
	}
	
	public void update()
	{
		super.update();
		
		if (upgradeToDo >= 2 || unit.getRemainingBuildTimer() > 0 || JavaBot.knowledge.currentBuildings[Operators.iterHive] == 0) {
			return;
		}
		
		if (countDontAct > 0) countDontAct--;
		
		if (state == UPGRADE_STATES.NONE && unit.getRemainingUpgradeTime() > 0) {
			state = UPGRADE_STATES.EVOLVING;
		}
		
		if (state == UPGRADE_STATES.EVOLVING && unit.getRemainingUpgradeTime() == 0) {
			state = UPGRADE_STATES.NONE;
			if (upgradeToDo == 0)
				Tech.groove = 1;
			if (upgradeToDo == 1)
				Tech.muscular = 2;
			upgradeToDo++;
		}
		
		if (state == UPGRADE_STATES.NONE && countDontAct == 0) {
			if (upgradeToDo == 0) {
				if (upgradeToDo == 0 && Tech.groove == 1) {
					upgradeToDo++;
					return;
				}
				if (upgradeToDo == 1 && Tech.muscular == 1) {
					upgradeToDo++;
					return;
				}
				if (JavaBot.knowledge.realMinerals >= 150 && JavaBot.knowledge.realGas >= 150) {
					BwapiCallsManager.upgrade(unit.getID(), UpgradeTypes.Grooved_Spines.ordinal());
					countDontAct = 20;
				}
			}
			else if (upgradeToDo == 1) {
				if (JavaBot.knowledge.realMinerals >= 150 && JavaBot.knowledge.realGas >= 150) {
					BwapiCallsManager.upgrade(unit.getID(), UpgradeTypes.Muscular_Augments.ordinal());
					countDontAct = 20;
				}
			}
		}
	}

	public void drawDebug()
	{
		if (unit.getRemainingUpgradeTime() > 0)
			bwapi.drawText(unit.getX() - 10, unit.getY() - 8, "Hydralisk Den " + unit.getRemainingUpgradeTime(), false);
		else
			bwapi.drawText(unit.getX() - 10, unit.getY() - 8, "Hydralisk Den", false);
	}
}