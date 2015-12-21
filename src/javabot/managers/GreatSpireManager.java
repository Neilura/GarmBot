package javabot.managers;

import javabot.model.Unit;

public class GreatSpireManager extends BuildingManager {

	public UPGRADE_STATES state = UPGRADE_STATES.NONE;
	public int attackLvl = 0;
	public int defenseLvl = 0;
	
	public boolean attack = false;
	
	public GreatSpireManager(Unit u) {
		super(u);
	}
	
	public void update()
	{
		super.update();
	}

	public void drawDebug()
	{
		bwapi.drawText(unit.getX() - 10, unit.getY() - 8, "Great Spire " + state + " a:" + attackLvl + " d:" + defenseLvl + " id:" + unit.getUpgradingUpgradeID(), false);
	}
}