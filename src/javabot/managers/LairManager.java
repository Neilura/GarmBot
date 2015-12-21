package javabot.managers;

import javabot.JavaBot;
import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;

public class LairManager extends BuildingManager {

	public LairManager(Unit u) {
		super(u);
	}
	
	public void update() {
		super.update();
		
		if (base != null)
			base.timerRebuild = 5;
		
		if (base != null && base.requestHive) {
			if (JavaBot.knowledge.realMinerals >= 200 && JavaBot.knowledge.realGas >= 150) {
				if (BwapiCallsManager.morph(unit.getID(), UnitTypes.Zerg_Hive.ordinal()))
					base.timerTryMacro = 20;
			}
			else
				JavaBot.requestsManager.requestRessources(200, 150);
		}
	}
	
	public void drawDebug()
	{
		bwapi.drawText(unit.getX() - 10, unit.getY() - 8, "Lair", false);
	}
}
