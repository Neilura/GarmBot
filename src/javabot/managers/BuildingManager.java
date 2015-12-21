package javabot.managers;

import javabot.JavaBot;
import javabot.knowledge.TileGraph;
import javabot.model.Unit;
import javabot.operators.Operators;

public class BuildingManager extends UnitManager {
	
	public BuildingManager(Unit u) {
		super(u);
		
		int distMax = 999999;
		BaseManager bestBase = null;
		for (BaseManager manager : JavaBot.bases)
		{
			int dist = (manager.posX - posX) * (manager.posX - posX) + (manager.posY - posY) * (manager.posY - posY);
			if (dist < distMax)
			{
				distMax = dist;
				bestBase = manager;
			}
		}
		base = bestBase;
		isABuilding = true;
	}
	
	public void update()
	{
		super.update();
		
		TileGraph.fillBuilding(unit);
		
		if (base != null)
		{
			if (!dead && !base.buildings.contains(unit))
			{
				base.buildings.add(unit);
			}
		}
		else
		{
			int distMax = 999999;
			BaseManager bestBase = null;
			for (BaseManager manager : JavaBot.bases)
			{
				int dist = (manager.posX - posX) * (manager.posX - posX) + (manager.posY - posY) * (manager.posY - posY);
				if (dist < distMax)
				{
					distMax = dist;
					bestBase = manager;
				}
			}
			base = bestBase;
		}
		if (!dead)
			JavaBot.buildingsExisting.add(this);
	}

	public void drawDebug()
	{
		if (unit.isConstructing())
		{
			bwapi.drawText(unit.getX() - 10, unit.getY() - 8, "BUILDING " + Operators.stringFromType(typeID), false);
		}
		else
			bwapi.drawText(unit.getX() - 10, unit.getY() - 8, "BUILDING " + Operators.stringFromType(typeID), false);
	}

	
}
