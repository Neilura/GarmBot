package javabot.managers;

import javabot.model.Unit;

public class HiveManager extends BuildingManager {

	public HiveManager(Unit u) {
		super(u);
	}
	
	public void update() {
		super.update();
		
		if (base != null)
			base.timerRebuild = 5;
	}
	
	public void drawDebug()
	{
		bwapi.drawText(unit.getX() - 10, unit.getY() - 8, "Hive", false);
	}
}