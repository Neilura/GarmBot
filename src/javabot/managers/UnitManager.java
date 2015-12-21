package javabot.managers;

import java.awt.Point;
import java.util.ArrayList;

import javabot.*;
import javabot.managers.BaseManager.BaseState;
import javabot.model.Unit;
import javabot.operators.Operators;
import javabot.types.UnitType.UnitTypes;

public class UnitManager {
	
	public enum UPGRADE_STATES {
		NONE,
		EVOLVING,
		FINISHED
	};
	
	public Unit unit;
	public int posX;
	public int posY;
	public int typeID;

	JNIBWAPI bwapi;

	int typeBeingConstructed;
	boolean dead;
	public boolean follow;
	public boolean explore;
	public boolean authorizeAttack;
	
	boolean isABuilding;
	BaseManager base;
	public int count;
	public int countResetAction;
	public int countMaxResetAction;
	public int countDontAct;
	public int nbNemesis = 0;
	public int hp;
	
	public int radiusDetection = 260;
	public int safeMarge = 0;
	public ArrayList<UnitManager> nearestUnits;
	public ArrayList<EnemyUnitManager> nearestEnemyUnits;
	public boolean attacking;
	public Point retreatPoint;
	
	public UnitManager(Unit u) {
		unit = u;
		typeID = u.getTypeID();
		bwapi = JavaBot.bwapi;

		posX = unit.getX();
		posY = unit.getY();
		
		dead = false;
		explore = false;
		authorizeAttack = true;
		
		isABuilding = false;
		base = null;
		follow = true;
		count = 0;
		countResetAction = 0;
		countMaxResetAction = (int) (Math.random() * 200 + 1100);
		countDontAct = 0;
		
		nearestUnits = new ArrayList<UnitManager>();
		nearestEnemyUnits = new ArrayList<EnemyUnitManager>();
		attacking = false;
		retreatPoint = new Point(posX, posY);
		
		int distMax = Integer.MAX_VALUE;
		BaseManager bestBase = null;
		for (BaseManager manager : JavaBot.bases)
		{
			int dist = (int) Math.sqrt((manager.posX - posX) * (manager.posX - posX) + (manager.posY - posY) * (manager.posY - posY));
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
		
		hp = unit.getHitPoints();
		
		if (dead == false && (!unit.isExists() || typeID != unit.getTypeID()))
		{
			if (!isABuilding)
			{
				JavaBot.UnitsMap.remove(unit.getID());
			}
			else
			{
				if (base != null)
					base.buildings.remove(unit);
				JavaBot.BuildingsMap.remove(unit.getID());
			}
			dead = true;
		}
		
		if (count > 0)
			count--;
		if (countDontAct > 0)
			countDontAct--;
		if (unit.isIdle())
			count = 0;
		
		if (!dead) {
			if (!isABuilding)
				JavaBot.unitsExisting.add(this);
		}
	}

	public void drawDebug()
	{
		bwapi.drawText(unit.getX() - 10, unit.getY() - 8, "DefaultUnit", false);
	}

	public static UnitManager chooseCorrectManager(Unit u) {
		
		if (u.getTypeID() == UnitTypes.Zerg_Larva.ordinal()) {
			return new LarvaManager(u);
		}
		
		if (u.getTypeID() == UnitTypes.Zerg_Drone.ordinal()) {
			return new DroneManager(u);
		}
		
		if (u.getTypeID() == UnitTypes.Zerg_Zergling.ordinal()) {
			return new ZerglingManager(u);
		}
		
		if (u.getTypeID() == UnitTypes.Zerg_Hydralisk.ordinal()) {
			return new HydraliskManager(u);
		}
		
		if (u.getTypeID() == UnitTypes.Zerg_Queen.ordinal()) {
			return new QueenManager(u);
		}
		
		if (u.getTypeID() == UnitTypes.Zerg_Mutalisk.ordinal()) {
			return new MutaliskManager(u);
		}
		
		if (u.getTypeID() == UnitTypes.Zerg_Broodling.ordinal()) {
			return new BroodlingManager(u);
		}
		
		if (u.getTypeID() == UnitTypes.Zerg_Guardian.ordinal()) {
			return new GuardianManager(u);
		}
		
		if (u.getTypeID() == UnitTypes.Zerg_Devourer.ordinal()) {
			return new DevourerManager(u);
		}
		
		if (u.getTypeID() == UnitTypes.Zerg_Overlord.ordinal()) {
			return new UnitManager(u);
		}
		
		return new UnitManager(u);
	}
	
	public static BuildingManager chooseCorrectBuildingManager(Unit u) {
		
		int typeId = u.getTypeID();
		
		if (typeId == UnitTypes.Zerg_Hatchery.ordinal()) {
			JavaBot.repartition.reduceExpandMinerals(300);
			return new HatcheryManager(u);
		}
		
		if (typeId == UnitTypes.Zerg_Spawning_Pool.ordinal()) {
			JavaBot.repartition.reduceTechMinerals(200);
			return new SpawningPoolManager(u);
		}
		
		if (typeId == UnitTypes.Zerg_Extractor.ordinal()) {
			JavaBot.repartition.reduceTechMinerals(50);
			return new BuildingManager(u);
		}
		
		if (typeId == UnitTypes.Zerg_Hydralisk_Den.ordinal()) {
			JavaBot.repartition.reduceTechMinerals(100);
			return new HydraDenManager(u);
		}
		
		if (typeId == UnitTypes.Zerg_Lair.ordinal()) {
			JavaBot.repartition.reduceTechMinerals(150);
			return new LairManager(u);
		}
		
		if (typeId == UnitTypes.Zerg_Creep_Colony.ordinal()) {
			JavaBot.repartition.reduceCreepMinerals(75);
			return new CreepColonyManager(u);
		}
		
		if (typeId == UnitTypes.Zerg_Sunken_Colony.ordinal()) {
			JavaBot.repartition.reduceSunkenMinerals(50);
			return new BuildingManager(u);
		}
		
		if (typeId == UnitTypes.Zerg_Spore_Colony.ordinal()) {
			JavaBot.repartition.reduceSunkenMinerals(50);
			return new BuildingManager(u);
		}
		
		if (typeId == UnitTypes.Zerg_Queens_Nest.ordinal()) {
			JavaBot.repartition.reduceTechMinerals(150);
			return new QueenNestManager(u);
		}
		
		if (typeId == UnitTypes.Zerg_Spire.ordinal()) {
			JavaBot.repartition.reduceTechMinerals(200);
			return new SpireManager(u);
		}
		
		if (typeId == UnitTypes.Zerg_Greater_Spire.ordinal()) {
			JavaBot.repartition.reduceTechMinerals(100);
			return new GreatSpireManager(u);
		}
		
		if (typeId == UnitTypes.Zerg_Evolution_Chamber.ordinal()) {
			JavaBot.repartition.reduceTechMinerals(75);
			return new EvoChamberManager(u);
		}
		
		if (typeId == UnitTypes.Zerg_Infested_Command_Center.ordinal()) {
			return new InfestedCommandCenterManager(u);
		}
		
		return new BuildingManager(u);
	}
	
	public void attackRandom() {
		countResetAction++;
		
		if (!authorizeAttack && countDontAct >= 5) {
			authorizeAttack = true;
			countDontAct = 0;
		}
		
		if (!authorizeAttack && countDontAct < 5) {
			countDontAct++;
		}
		
		if (authorizeAttack) {
			if (countResetAction > countMaxResetAction) {
				if (BwapiCallsManager.stop(unit.getID())) {
					countResetAction = 0;
					authorizeAttack = false;
					explore = false;
					countDontAct = 0;
				}
			}
			else if ((explore || unit.isIdle()) && JavaBot.enemyBuildingsManager.size() > 0) {
				
				int iterBuilding = (int) (Math.random() * JavaBot.enemyBuildingsManager.size());
				if (iterBuilding >= JavaBot.enemyBuildingsManager.size()) iterBuilding--;
				if (iterBuilding < 0) iterBuilding = 0;
				
				EnemyBuildingManager buildingToAttack = JavaBot.enemyBuildingsManager.get(iterBuilding);
				
				if (BwapiCallsManager.attack(unit.getID(), buildingToAttack.posX, buildingToAttack.posY)) {
					explore = false;
					countResetAction = 0;
					authorizeAttack = false;
					countDontAct = 0;
				}
			}
			else if (unit.isIdle())
			{
				if (Math.random() < 0.5f) {
					for (BaseManager baseM : JavaBot.bases) {
						if (baseM.state != BaseState.FRIENDLY && baseM.baseLocation.isStartLocation() && Math.random() < 0.3) {
							if (BwapiCallsManager.attack(unit.getID(), baseM.posX, baseM.posY)) {
								explore = true;
								countResetAction = 0;
								authorizeAttack = false;
								countDontAct = 0;
							}
							break;
						}
					}
				}
				else {
					int randX = (int) (Math.random() * ((double)bwapi.getMap().getWidth() * 32));
					int randY = (int) (Math.random() * ((double)bwapi.getMap().getHeight() * 32));
					
					if (BwapiCallsManager.attack(unit.getID(), randX, randY)) {
						explore = true;
						countResetAction = 0;
						authorizeAttack = false;
						countDontAct = 0;
					}
				}
			}
		}
	}
	
	public boolean nearbyDetector() {
		for (EnemyUnitManager enemy : JavaBot.enemyUnitsDetectorManager) {
			if (Operators.distance(posX, posY, enemy.posX, enemy.posY) < enemy.range) {
				return true;
			}
		}
		for (EnemyBuildingManager enemyB : JavaBot.enemyBuildingsDetectorManager) {
			if (Operators.distance(posX, posY, enemyB.posX, enemyB.posY) < enemyB.range) {
				return true;
			}
		}
		return false;
	}
	
	public boolean nearbyGroundEnemy(int safeMarge) {
		for (EnemyUnitManager enemy : JavaBot.enemyUnitsGroundManager) {
			if (Operators.distance(posX, posY, enemy.posX, enemy.posY) < enemy.range + safeMarge) {
				return true;
			}
		}
		for (EnemyBuildingManager enemyB : JavaBot.enemyBuildingsGroundManager) {
			if (Operators.distance(posX, posY, enemyB.posX, enemyB.posY) < enemyB.range + safeMarge) {
				return true;
			}
		}
		return false;
	}
	
	public boolean nearbyAirEnemy() {
		for (EnemyUnitManager enemy : JavaBot.enemyUnitsAirManager) {
			if (Operators.distance(posX, posY, enemy.posX, enemy.posY) < enemy.range) {
				return true;
			}
		}
		for (EnemyBuildingManager enemyB : JavaBot.enemyBuildingsAirManager) {
			if (Operators.distance(posX, posY, enemyB.posX, enemyB.posY) < enemyB.range) {
				return true;
			}
		}
		return false;
	}
	
	public static EnemyBuildingManager existsBuilding(int x, int y, ArrayList<EnemyBuildingManager> enemyBuildingsManager) {
		for (EnemyBuildingManager building : enemyBuildingsManager) {
			if (building.posX == x && building.posY == y) return building;
		}
		return null;
	}
}
