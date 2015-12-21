package javabot.managers;

import javabot.model.Unit;

public class InfestedCommandCenterManager extends BuildingManager  {

	public InfestedCommandCenterManager(Unit u) {
		super(u);
	}
	
	public void update() {
		super.update();
		
		if (!unit.isLifted()) {
			bwapi.lift(unit.getID());
		}
		else {
			if (unit.isIdle()) {
				int randX = (int) (Math.random() * ((double)bwapi.getMap().getWidth() * 32));
				int randY = (int) (Math.random() * ((double)bwapi.getMap().getHeight() * 32));
				
				bwapi.move(unit.getID(), randX, randY);
			}
		}
	}
	
	public void drawDebug()
	{
		bwapi.drawText(unit.getX() - 10, unit.getY() - 8, "Infested" + unit.isLifted(), false);
	}
}
