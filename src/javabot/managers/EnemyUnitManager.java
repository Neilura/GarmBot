package javabot.managers;

import javabot.JNIBWAPI;
import javabot.JavaBot;
import javabot.model.Unit;
import javabot.operators.Operators;
import javabot.util.BWColor;

public class EnemyUnitManager {
	
	public Unit unit;
	public int posX;
	public int posY;
	public int typeID;
	JNIBWAPI bwapi;
	public boolean inRangeDetector;
	public boolean killed;
	public boolean isDangerousAir = false;
	public boolean isDangerousGround = false;
	public int isBroodlinged = 0;
	public int range;
	public int broodlingLevel;
	boolean dead;
	public boolean isAir;
	public boolean isDetector;
	public UnitManager killer;
	
	public int valZ;
	public int valH;
	public int valQ;
	public int valM;
	public int valG;
	public int valD;
	
	public EnemyUnitManager(Unit u) {
		
		unit = u;
		typeID = u.getTypeID();
		bwapi = JavaBot.bwapi;
		posX = unit.getX();
		posY = unit.getY();
		inRangeDetector = false;
		killed = false;
		isBroodlinged = 0;
		range = 0;
		broodlingLevel = Operators.broodlingLevel(typeID);
		isDangerousAir = Operators.isDangerousAir(typeID);
		isDangerousGround = Operators.isDangerousGround(typeID);
		isDetector = Operators.isDetector(typeID);
		range = Operators.getRange(typeID);
		isAir = Operators.isAir(typeID);
		dead = false;
		killer = null;
		JavaBot.armyComposition.setAdaptComposition(this);
	}
	
	public void update()
	{
		if (JavaBot.newAdapt) {
			JavaBot.armyComposition.add(valZ, valH, valQ, valM, valG, valD, 0, 0, 0, 0);
		}
		typeID = unit.getTypeID();
		posX = unit.getX();
		posY = unit.getY();
		
		if (isAir)
			JavaBot.enemyUnitsAirTargetManager.add(this);
		else
			JavaBot.enemyUnitsGroundTargetManager.add(this);
		if (isDangerousAir)
			JavaBot.enemyUnitsAirManager.add(this);
		if (isDangerousGround)
			JavaBot.enemyUnitsGroundManager.add(this);
		if (isDetector)
			JavaBot.enemyUnitsDetectorManager.add(this);
		if (broodlingLevel > 0)
			JavaBot.enemyUnitsBroodlingManager.add(this);

		if (isBroodlinged > 0)isBroodlinged--;
		
		if (!unit.isExists() || !bwapi.isVisible(unit.getTileX(), unit.getTileY())) {
			dead = true;
			JavaBot.enemyUnitsManagerMap.remove(unit.getID());
		}
		
		if (!dead)
			JavaBot.enemyUnitsManagerExisting.add(this);
	}

	public void drawDebug()
	{
		bwapi.drawCircle(posX, posY, 2, BWColor.YELLOW, true, false);
		bwapi.drawCircle(posX, posY, range, BWColor.RED, false, false);
	}
}
