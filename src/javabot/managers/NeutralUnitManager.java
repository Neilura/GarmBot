package javabot.managers;

import javabot.JNIBWAPI;
import javabot.JavaBot;
import javabot.model.Unit;

public class NeutralUnitManager {

	JNIBWAPI bwapi;
	public boolean parasited;
	public int timeParasite;
	public Unit unit;
	public int ID;
	public boolean dead;
	
	public NeutralUnitManager(Unit u) {
		unit = u;
		timeParasite = 0;
		parasited = false;
		bwapi = JavaBot.bwapi;
		ID = u.getID();
		dead = false;
	}
	
	public void update()
	{
		if (!unit.isExists() || unit.isParasited() || !bwapi.isVisible(unit.getTileX(), unit.getTileY())) {
			dead = true;
		}
		if (unit.isParasited())
			parasited = true;
		if (timeParasite > 0) timeParasite--;
		
		if (!dead)
			JavaBot.neutralUnitsMapExists.put(ID, this);
	}

	public void drawDebug()
	{
		if (unit != null)
			bwapi.drawText(unit.getX() - 10, unit.getY() - 8, "Neutral, parasited " + parasited + ", timer " + timeParasite, false);
	}
}
