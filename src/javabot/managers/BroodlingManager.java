package javabot.managers;

import javabot.JavaBot;
import javabot.model.Unit;

public class BroodlingManager extends UnitManager {

	
	public BroodlingManager(Unit u) {
		super(u);
		explore = true;
	}
	
	public void update()
	{
		super.update();
		
		/*if (true)
			return;*/
		
		attackRandom();
	}
	
	public void drawDebug()
	{
		if (!JavaBot.RELEASE)
			bwapi.drawText(unit.getX() - 10, unit.getY() - 8, "Broodling", false);
	}
}
