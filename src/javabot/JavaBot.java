package javabot;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import javabot.knowledge.ArmyComposition;
import javabot.knowledge.GlobalKnowledge;
import javabot.knowledge.Repartition;
import javabot.knowledge.RequestsManager;
import javabot.knowledge.Strategy;
import javabot.knowledge.Strategy.RaceType;
import javabot.knowledge.Tech;
import javabot.knowledge.TileGraph;
import javabot.managers.BaseManager;
import javabot.managers.BuildingManager;
import javabot.managers.BwapiCallsManager;
import javabot.managers.ControlTower;
import javabot.managers.EnemyBuildingManager;
import javabot.managers.EnemyCommandCenterManager;
import javabot.managers.EnemyUnitManager;
import javabot.managers.LarvaManager;
import javabot.managers.NeutralUnitManager;
import javabot.managers.UnitManager;
import javabot.model.*;
import javabot.operators.Operators;
import javabot.types.UnitType.UnitTypes;

public class JavaBot implements BWAPIEventListener {
	
	public static int homePositionX = -1;
	public static int homePositionY = -1;
	public static int currFrame = 0;
	
	public static final boolean RELEASE = true;
	
	//public static boolean canExpand = true;
	
	public static boolean newAdapt = false;
	
	public static HashMap<Integer, UnitManager> UnitsMap;
	public static HashMap<Integer, BuildingManager> BuildingsMap;
	public static HashMap<Integer, NeutralUnitManager> neutralUnitsMap;
	public static HashMap<Integer, NeutralUnitManager> neutralUnitsMapExists;
	
	public static ArrayList<UnitManager> units;
	public static ArrayList<UnitManager> unitsExisting;
	public static ArrayList<BuildingManager> buildings;
	public static ArrayList<BuildingManager> buildingsExisting;
	public static ArrayList<EnemyBuildingManager> enemyBuildingsManager;
	public static ArrayList<EnemyBuildingManager> enemyBuildingsManagerExisting;
	
	public static ArrayList<EnemyUnitManager> enemyUnitsManager;
	public static ArrayList<EnemyUnitManager> enemyUnitsManagerExisting;
	
	public static ArrayList<EnemyBuildingManager> enemyBuildingsGroundManager;
	public static ArrayList<EnemyBuildingManager> enemyBuildingsAirManager;
	public static ArrayList<EnemyBuildingManager> enemyBuildingsDetectorManager;
	public static ArrayList<EnemyBuildingManager> enemyBuildingsAirTargetManager;
	public static ArrayList<EnemyCommandCenterManager> enemyCommandCenters;
	
	public static ArrayList<EnemyUnitManager> enemyUnitsGroundManager;
	public static ArrayList<EnemyUnitManager> enemyUnitsAirManager;
	public static ArrayList<EnemyUnitManager> enemyUnitsAirTargetManager;
	public static ArrayList<EnemyUnitManager> enemyUnitsGroundTargetManager;
	public static ArrayList<EnemyUnitManager> enemyUnitsDetectorManager;
	public static ArrayList<EnemyUnitManager> enemyUnitsBroodlingManager;
	
	
	public static HashMap<Integer, EnemyUnitManager> enemyUnitsManagerMap;
	
	public static ArrayList<BaseManager> bases;
	
	public static RequestsManager requestsManager;
	public static Strategy strategy;
	public static ArmyComposition armyComposition;
	
	public static JNIBWAPI bwapi;
	public static GlobalKnowledge knowledge;
	public static Repartition repartition;
	
	long updateUnitsTime;
	long updateBasesTime;
	long updateEnemyUnitsTime;
	
	long timeBefore = 0;
	long timeAfter = 0;
	long totalTime = 0;
	String perf = "";
	
	public static void main(String[] args) {
		new JavaBot();
	}
	public JavaBot() {
		bwapi = new JNIBWAPI(this);
		bwapi.start();
	} 
	public void connected() {
		bwapi.loadTypeData();
	}
	
	public void gameStarted() {
		
		BwapiCallsManager.init();
		bwapi.enableUserInput();
		
		bwapi.setGameSpeed(20);
		
		bwapi.loadMapData(true);
		
		int hatchery = getNearestUnit(UnitTypes.Zerg_Hatchery.ordinal(), 0, 0);
		if (hatchery != -1) {
			homePositionX = bwapi.getUnit(hatchery).getX();
			homePositionY = bwapi.getUnit(hatchery).getY();
		}
		
		Operators.init();
		knowledge = new GlobalKnowledge();
		
		requestsManager = new RequestsManager();
		strategy = new Strategy();
		armyComposition = new ArmyComposition();
		TileGraph.init(bwapi, homePositionX, homePositionY);
		
		repartition = new Repartition();
		
		units = new ArrayList<UnitManager>();
		unitsExisting = new ArrayList<UnitManager>();
		buildings = new ArrayList<BuildingManager>();
		buildingsExisting = new ArrayList<BuildingManager>();
		
		UnitsMap = new HashMap<Integer, UnitManager>();
		BuildingsMap = new HashMap<Integer, BuildingManager>();
		neutralUnitsMap = new HashMap<Integer, NeutralUnitManager>();
		neutralUnitsMapExists = new HashMap<Integer, NeutralUnitManager>();
		
		bases = new ArrayList<BaseManager>();

		enemyBuildingsManager = new ArrayList<EnemyBuildingManager>();
		enemyBuildingsManagerExisting = new ArrayList<EnemyBuildingManager>();
		
		enemyUnitsManager = new ArrayList<EnemyUnitManager>();
		enemyUnitsManagerExisting = new ArrayList<EnemyUnitManager>();
		
		enemyUnitsManagerMap = new HashMap<Integer, EnemyUnitManager>();

		enemyBuildingsGroundManager = new ArrayList<EnemyBuildingManager>();
		enemyBuildingsAirManager = new ArrayList<EnemyBuildingManager>();
		enemyBuildingsAirTargetManager = new ArrayList<EnemyBuildingManager>();
		enemyBuildingsDetectorManager = new ArrayList<EnemyBuildingManager>();
		enemyCommandCenters = new ArrayList<EnemyCommandCenterManager>();
		
		enemyUnitsGroundManager = new ArrayList<EnemyUnitManager>();
		enemyUnitsAirManager = new ArrayList<EnemyUnitManager>();
		enemyUnitsAirTargetManager = new ArrayList<EnemyUnitManager>();
		enemyUnitsGroundTargetManager = new ArrayList<EnemyUnitManager>();
		enemyUnitsDetectorManager = new ArrayList<EnemyUnitManager>();
		enemyUnitsBroodlingManager = new ArrayList<EnemyUnitManager>();

		ArrayList<BaseLocation> locations = bwapi.getMap().getBaseLocations();
		
		for (BaseLocation base : locations) {
			bases.add(new BaseManager(base));
		}
		
		if (bwapi.getMap().getName().equals("TestsUnitaires.scx")) {
			Tech.burrow = 2;
		}
		
		BaseManager.setScoresExpand(bases, locations, homePositionX, homePositionY, bwapi);
		
		ControlTower.bwapi = bwapi;
		
		strategy.enemyRace = RaceType.Terran;
		for (Player p : bwapi.getEnemies()) {
			if (p.getRaceID() == 0) {
				if (!RELEASE)
				System.out.println("ZERG opponent");
				strategy.enemyRace = RaceType.Zerg;
			}
			if (p.getRaceID() == 1) {
				if (!RELEASE)
				System.out.println("TERRAN opponent");
				strategy.enemyRace = RaceType.Terran;
			}
			if (p.getRaceID() == 2) {
				if (!RELEASE)
				System.out.println("PROTOSS opponent");
				strategy.enemyRace = RaceType.Protoss;
			}
			if (p.getRaceID() == 6) {
				if (!RELEASE)
				System.out.println("RANDOM opponent");
			}
		}
		strategy.adaptArmyComposition();
		
		
	}

	public void gameUpdate() {
		
		try{
			
		if (!RELEASE)
			totalTime = System.nanoTime();
		
		currFrame = bwapi.getFrameCount();
		
		if (currFrame == 0) {
			firstFrame();
			return;
		}
		
		newAdapt = false;
		if (currFrame % 20 == 0) {
			newAdapt = true;
			armyComposition.reset();
		}
			
		if (LarvaManager.blockMorph > 0)
			LarvaManager.blockMorph--;
		
		repartition.update();
		
		knowledge.update();
		
		for (Unit unit : bwapi.getMyUnits()) {
			knowledge.addUnit(unit);
			
			if (Operators.isAbuilding(unit.getTypeID()))
			{
				if (!BuildingsMap.containsKey(unit.getID())) {
					BuildingManager newManager = UnitManager.chooseCorrectBuildingManager(unit);
					
					BuildingsMap.put(newManager.unit.getID(), newManager);
					buildings.add(newManager);
				}
			}
			else
			{
				if (!UnitsMap.containsKey(unit.getID())) {
					UnitManager newManager = UnitManager.chooseCorrectManager(unit);
					
					UnitsMap.put(newManager.unit.getID(), newManager);
					units.add(newManager);
				}
			}
		}
		
		enemyBuildingsGroundManager.clear();
		enemyBuildingsAirManager.clear();
		enemyBuildingsAirTargetManager.clear();
		enemyBuildingsDetectorManager.clear();
		
		enemyBuildingsManagerExisting = new ArrayList<EnemyBuildingManager>();
		for (EnemyBuildingManager manager : enemyBuildingsManager)
		{
			manager.update();
		}
		enemyBuildingsManager = enemyBuildingsManagerExisting;
		
		enemyCommandCenters.clear();
		for (Unit unit : bwapi.getEnemyUnits()) {
			if (unit.getTypeID() == UnitTypes.Terran_Command_Center.ordinal())
				enemyCommandCenters.add(new EnemyCommandCenterManager(unit));
			if (Operators.isAbuildingAll(unit.getTypeID()))
			{
				EnemyBuildingManager exists = BuildingManager.existsBuilding(unit.getX(), unit.getY(), enemyBuildingsManager);
				if (exists != null) {
					exists.timerDestroy = 20;
				}
				else if (unit.getTypeID() != UnitTypes.Zerg_Creep_Colony.ordinal()) {
					enemyBuildingsManager.add(new EnemyBuildingManager(unit));
				}
			}
			else if (!enemyUnitsManagerMap.containsKey(unit.getID()))
			{
				EnemyUnitManager enemy = new EnemyUnitManager(unit);
				enemyUnitsManagerMap.put(unit.getID(), enemy);
				enemyUnitsManager.add(enemy);
			}
		}
		
		for (Unit unit : bwapi.getNeutralUnits()) {
			if (Operators.isCritter(unit.getTypeID()) && !unit.isParasited() && !neutralUnitsMap.containsKey(unit.getID()))
			{
				NeutralUnitManager newManager = new NeutralUnitManager(unit);
				neutralUnitsMap.put(newManager.unit.getID(), newManager);
			}
		}
		
		ControlTower.reset();
		
		buildingsExisting = new ArrayList<BuildingManager>();
		for (BuildingManager manager : buildings)
		{
			manager.update();
		}
		buildings = buildingsExisting;
		
		strategy.mineralsPerMinute = 0;
		strategy.gasPerMinute = 0;
		
		unitsExisting = new ArrayList<UnitManager>();

		for (UnitManager manager : units)
		{
			manager.update();
		}
		units = unitsExisting;
		
		strategy.ratioGas();
		
		neutralUnitsMapExists = new HashMap<Integer, NeutralUnitManager>();
		for (NeutralUnitManager manager : neutralUnitsMap.values())
		{
			manager.update();
		}
		neutralUnitsMap = neutralUnitsMapExists;
		
		for (BaseManager manager : bases)
		{
			manager.update();
		}
		
		TileGraph.update();
		
		for (EnemyBuildingManager manager : enemyBuildingsManager)
		{
			manager.fillGraph();
		}
		
		enemyUnitsGroundManager.clear();
		enemyUnitsAirManager.clear();
		enemyUnitsAirTargetManager.clear();
		enemyUnitsGroundTargetManager.clear();
		enemyUnitsDetectorManager.clear();
		enemyUnitsBroodlingManager.clear();
		
		enemyUnitsManagerExisting = new ArrayList<EnemyUnitManager>();
		for (EnemyUnitManager manager : enemyUnitsManager)
		{
			manager.update();
		}
		enemyUnitsManager = enemyUnitsManagerExisting;
		
		if (newAdapt)
			armyComposition.update();
		
		requestsManager.update();
		
		strategy.update(knowledge);
		
		ControlTower.update();
		
		BwapiCallsManager.processFrame();
		
		if (!RELEASE)
			totalTime = System.nanoTime() - totalTime;
		
		drawDebugInfo();
		
		BwapiCallsManager.resetFrame();
		}
		catch (Exception e) {
		}
	}

	private void firstFrame() {
		int first = -1;
		int second = -1;
		int third = -1;
		
		for (Unit u : bwapi.getMyUnits()) {
			if (u.getTypeID() == UnitTypes.Zerg_Drone.ordinal()) {
				
				int minDist = Integer.MAX_VALUE;
				Unit bestMineral = null;
				
				for (Unit neu : bwapi.getNeutralUnits()) {
					if (neu.getTypeID() == UnitTypes.Resource_Mineral_Field.ordinal()) {
						int id = neu.getID();
						if (id != first && id != second && id != third) {
							int distTest = Operators.distance(u, neu);
							if (distTest < minDist) {
								minDist = distTest;
								bestMineral = neu;
							}
						}
					}
				}
				if (bestMineral != null) {
					bwapi.rightClick(u.getID(), bestMineral.getID());
					if (first == -1)
						first = bestMineral.getID();
					else if (second == -1)
						second = bestMineral.getID();
					else if (third == -1)
						third = bestMineral.getID();
				}
			}
			else if (u.getTypeID() == UnitTypes.Zerg_Larva.ordinal()) {
				bwapi.morph(u.getID(), UnitTypes.Zerg_Drone.ordinal());
			}
		}
	}
	public void gameEnded() {}
	public void matchEnded(boolean winner) {}
	public void nukeDetect(int x, int y) {}
	public void nukeDetect() {}
	public void playerLeft(int id) {}
	public void unitCreate(int unitID) {
	}
	
	public void unitDestroy(int unitID) {
	}
	public void unitDiscover(int unitID) {}
	public void unitEvade(int unitID) {}
	public void unitHide(int unitID) {}
	
	public void unitMorph(int unitID) {
	}
	
	public void unitShow(int unitID) {}
	public void keyPressed(int keyCode) {}
	

    public int getNearestUnit(int unitTypeID, int x, int y) {
    	int nearestID = -1;
	    double nearestDist = 9999999;
	    for (Unit unit : bwapi.getMyUnits()) {
	    	if ((unit.getTypeID() != unitTypeID) || (!unit.isCompleted())) continue;
	    	double dist = Math.sqrt(Math.pow(unit.getX() - x, 2) + Math.pow(unit.getY() - y, 2));
	    	if (nearestID == -1 || dist < nearestDist) {
	    		nearestID = unit.getID();
	    		nearestDist = dist;
	    	}
	    }
	    return nearestID;
    }
	
	public static Point getBuildTile(int builderID, int buildingTypeID, int x, int y) {
		Point ret = new Point(-1, -1);
		int maxDist = 4;
		int stopDist = 40;
		int tileX = x/32; int tileY = y/32;

		if (buildingTypeID == UnitTypes.Zerg_Extractor.ordinal()) {
			double dist = -1;
			for (Unit n : bwapi.getNeutralUnits()) {
				if (n.getTypeID() == UnitTypes.Resource_Vespene_Geyser.ordinal()) {
					
					double distTest = Operators.distance(n, x, y);
					
					if (distTest < dist || dist == -1) {
						dist = distTest;
						ret = new Point(n.getTileX(),n.getTileY());
					}
				}
			}
			return ret;
		}
		
		while ((maxDist < stopDist) && (ret.x == -1)) {
			for (int i=tileX-maxDist; i<=tileX+maxDist; i++) {
				for (int j=tileY-maxDist; j<=tileY+maxDist; j++) {
					if (bwapi.canBuildHere(builderID, i, j, buildingTypeID, false)) {
						boolean unitsInWay = false;
						for (Unit u : bwapi.getAllUnits()) {
							if (u.getID() == builderID) continue;
							if ((Math.abs(u.getTileX()-i) < 4) && (Math.abs(u.getTileY()-j) < 4)) unitsInWay = true;
						}
						if (!unitsInWay) {
							ret.x = i; ret.y = j;
							return ret;
						}
					}
				}
			}
			maxDist += 2;
		}
		
		return ret;
	}
	
	public boolean weAreBuilding(int buildingTypeID) {
		for (Unit unit : bwapi.getMyUnits()) {
			if ((unit.getTypeID() == buildingTypeID) && (!unit.isCompleted())) return true;
			if (bwapi.getUnitType(unit.getTypeID()).isWorker() && unit.getConstructingTypeID() == buildingTypeID) return true;
		}
		return false;
	}
	
	public void drawDebugInfo() {

		bwapi.drawText(new Point(5, 0), "HomePosition: "+String.valueOf(homePositionX)+","+String.valueOf(homePositionY), true);

		bwapi.drawText(new Point(5, 9), "Units: " + units.size(), true);
		
		bwapi.drawText(new Point(5, 19), "Buildings: " + buildings.size(), true);
		
		bwapi.drawText(new Point(5, 29), "Enemy Buildings: " + enemyBuildingsManager.size() + "  ----  Enemy Units: " + enemyUnitsManager.size(), true);
		
		if (!RELEASE)
			bwapi.drawText(new Point(165, 39), "" + enemyUnitsManagerMap.size(), true);
		
		if (!RELEASE)
			bwapi.drawText(new Point(5, 39), "HasMap units: " + UnitsMap.size() + " buildings: " + BuildingsMap.size(), true);
		
		if (!RELEASE)
			for (UnitManager unit : units)  {
				unit.drawDebug();
			}
		
		if (!RELEASE)
			for (BuildingManager building : buildings)  {
				building.drawDebug();
			}
		
		if (!RELEASE)
			for (EnemyBuildingManager enemyBuilding : enemyBuildingsManager)  {
				enemyBuilding.drawDebug();
			}
		
		if (!RELEASE)
			for (EnemyCommandCenterManager enemyCommandCenter : enemyCommandCenters)  {
				enemyCommandCenter.drawDebug();
			}
		
		if (!RELEASE)
			for (EnemyUnitManager enemy : enemyUnitsManager)  {
				enemy.drawDebug();
			}
		
		for (BaseManager manager : bases)  {
			manager.drawDebug();
		}

		if (!RELEASE)
			knowledge.debug();
		
		if (!RELEASE)
			strategy.debug();
		
		if (!RELEASE)
			ControlTower.debug();
		
		if (!RELEASE)
			requestsManager.debug();
		
		if (!RELEASE)
			BwapiCallsManager.debug();
		
		if (!RELEASE)
			TileGraph.debug();
		
		if (!RELEASE)
			repartition.debug();
	}
}
