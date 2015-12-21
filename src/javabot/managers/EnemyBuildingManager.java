package javabot.managers;

import javabot.JNIBWAPI;
import javabot.JavaBot;
import javabot.knowledge.TileGraph;
import javabot.model.Unit;
import javabot.operators.Operators;
import javabot.types.UnitType.UnitTypes;
import javabot.util.BWColor;

public class EnemyBuildingManager {
	
	public Unit unit;
	public int posX;
	public int posY;
	public int typeID;
	JNIBWAPI bwapi;
	int range;
	boolean dead;
	BaseManager base;
	public int timerDestroy;
	public boolean isDangerousAir = false;
	public boolean isDangerousGround = false;
	public boolean isDetector = false;
	public boolean destroy = false;
	public int timerDestroyCreep;
	
	public EnemyBuildingManager(Unit u) {
		
		unit = u;
		typeID = u.getTypeID();
		timerDestroyCreep = 0;
		
		if (typeID == UnitTypes.Zerg_Creep_Colony.ordinal()) {
			destroy = true;
			timerDestroyCreep = 1000;
		}
		
		bwapi = JavaBot.bwapi;
		posX = unit.getX();
		posY = unit.getY();
		dead = false;
		timerDestroy = 20;
		
		isDangerousAir = Operators.isDangerousAir(typeID);
		isDangerousGround = Operators.isDangerousGround(typeID);
		isDetector = Operators.isDetector(typeID);
		range = Operators.getRange(typeID);
		
		
		int distMax = Integer.MAX_VALUE;
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
	
	public void update()
	{
		posX = unit.getX();
		posY = unit.getY();
		
		timerDestroyCreep--;
		if (timerDestroy <= 0) {
			dead = true;
		}
		
		if (unit.isLifted())
			JavaBot.enemyBuildingsAirTargetManager.add(this);
		if (isDangerousAir)
			JavaBot.enemyBuildingsAirManager.add(this);
		if (isDangerousGround)
			JavaBot.enemyBuildingsGroundManager.add(this);
		if (isDetector)
			JavaBot.enemyBuildingsDetectorManager.add(this);
		
		if (unit.getTypeID() == UnitTypes.Terran_Command_Center.ordinal()) {
			updateEnemyCommandCenter();
		}
		
		
		if (bwapi.isVisible(posX / 32, posY / 32)
				&& bwapi.isVisible(1+posX / 32, posY / 32)
				&& bwapi.isVisible(posX / 32, 1+posY / 32)
				&& bwapi.isVisible(1+posX / 32, 1+posY / 32))
		{
			timerDestroy--;
			if (timerDestroy <= 0) {
				dead = true;
			}
		}
		if (!dead)
			JavaBot.enemyBuildingsManagerExisting.add(this);
	}
	
	private void updateEnemyCommandCenter() {
		JavaBot.enemyCommandCenters.add(new EnemyCommandCenterManager(unit));
	}

	public void fillGraph() {
		TileGraph.fillBuilding(unit);
	}

	public void drawDebug()
	{
		if (isDangerousAir)
			bwapi.drawCircle(unit.getX(), unit.getY(), 3, BWColor.RED, true, false);
		
		bwapi.drawCircle(unit.getX(), unit.getY(), range, BWColor.RED, false, false);
	}
}
