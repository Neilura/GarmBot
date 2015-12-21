package javabot.managers;

import javabot.model.Unit;
import javabot.util.BWColor;

public class EnemyCommandCenterManager extends UnitManager {

	public boolean targetable = false;
	
	public EnemyCommandCenterManager(Unit u) {
		super(u);
		
		targetable = unit.getHitPoints() <= 750 && unit.getRemainingBuildTimer() == 0 && unit.getRemainingBuildTimer() == 0;
	}
	
	public void drawDebug()
	{
		bwapi.drawText(unit.getX() - 130, unit.getY() - 8, "InfestTarget:"+targetable+""+unit.getRemainingBuildTimer()+":"+unit.getHitPoints(), false);
		if (targetable)
			bwapi.drawCircle(posX, posY, 1800, BWColor.WHITE, false, false);
	}
}
