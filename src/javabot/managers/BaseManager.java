package javabot.managers;

import java.util.ArrayList;
import java.util.HashMap;

import javabot.JNIBWAPI;
import javabot.JavaBot;
import javabot.knowledge.BasePlanner;
import javabot.knowledge.Strategy.RaceType;
import javabot.knowledge.Tech;
import javabot.model.BaseLocation;
import javabot.model.Map;
import javabot.model.Unit;
import javabot.operators.Operators;
import javabot.types.UnitType.UnitTypes;
import javabot.util.BWColor;

public class BaseManager {
	
	public enum BaseState
	{
		FRIENDLY,
		ENNEMY,
		CONSTRUCTING,
		REQUESTED,
		UNKNOWN
	}
	
	int size = 350;
	
	BaseLocation baseLocation;
	public int posX;
	public int posY;
	int tileX;
	int tileY;
	boolean isIsland;
	JNIBWAPI bwapi;
	public BaseState state;
	boolean beingBuilt;
	
	public boolean possibleEnemy;
	public boolean inaccessible;
	public boolean hasGas;
	
	int dronesOnMinerals;
	int dronesOnGas;
	int timerGoOnGas;
	int timerGoOnMinerals;
	
	int nbDronesWantedOnGas;
	int safeExpandScore;
	
	public int timerTryExtractor;
	public int timerTryHydraDen;
	public int timerTryCreep;
	public int timerTryQueenNest;
	public int timerTrySpire;
	public int timerTryMacro;
	public int timerChangeRatio;
	
	public int timerRebuild;
	
	public boolean requestExtractor;
	public boolean requestHydraDen;
	public boolean requestCreep;
	public boolean requestQueenNest;
	public boolean requestLair;
	public boolean requestSpire;
	public boolean requestHive;
	
	int nbHatcheries;
	int nbExtractor;
	int nbHydraDen;
	int nbCreepColony;
	int nbSunkenColony;
	int nbSporeColony;
	int nbQueenNest;
	int nbSpire;
	int nbGreatSpire;
	
	public int nbMinerals;
	public int nbDronesIdeal;
	public Unit extractor;
	
	ArrayList<Unit> buildings;
	public HashMap<Integer, Unit> dronesMap;
	int nbDrones;
	
	public BasePlanner basePlanner;

	public int blockLair;
	
	public BaseManager(BaseLocation base) {
		
		baseLocation = base;
		posX = base.getX();
		posY = base.getY();
		
		tileX = base.getTx();
		tileY = base.getTy();
		
		basePlanner = new BasePlanner(posX, posY);
		
		isIsland = base.isIsland();
		
		bwapi = JavaBot.bwapi;
		buildings = new ArrayList<Unit>();

		dronesMap = new HashMap<Integer, Unit>();
		nbDrones = 0;
		state = BaseState.UNKNOWN;
		possibleEnemy = base.isStartLocation();
		inaccessible = false;
		hasGas = false;

		timerGoOnGas = 0;
		timerGoOnMinerals = 0;
		
		timerTryExtractor = 0;
		timerTryHydraDen = 0;
		timerTryCreep = 0;
		timerTryQueenNest = 0;
		timerTrySpire = 0;
		timerTryMacro = 0;
		timerChangeRatio = 0;
		
		timerRebuild = 3;
		blockLair = 1;
		
		dronesOnMinerals = 0;
		dronesOnGas = 0;
		nbDronesWantedOnGas = 0;
		
		safeExpandScore = 0;

		nbHatcheries = 0;
		nbExtractor = 0;
		nbHydraDen = 0;
		nbCreepColony = 0;
		nbSunkenColony = 0;
		nbSporeColony = 0;
		
		requestExtractor = false;
		requestHydraDen = false;
		requestCreep = false;
		requestQueenNest = false;
		requestLair = false;
		requestSpire = false;
		requestHive = false;
		
		extractor = null;
		nbMinerals = 0;
		nbDronesIdeal = 0;
	}
	
	public BaseManager(int x, int y) {
		baseLocation = null;
		posX = x;
		posY = y;
		
		tileX = x / 32;
		tileY = y / 32;
		
		basePlanner = new BasePlanner(posX, posY);
		
		isIsland = true;
		
		bwapi = JavaBot.bwapi;
		buildings = new ArrayList<Unit>();

		dronesMap = new HashMap<Integer, Unit>();
		nbDrones = 0;
		state = BaseState.UNKNOWN;
		possibleEnemy = false;
		inaccessible = false;
		hasGas = false;

		timerGoOnGas = 0;
		timerGoOnMinerals = 0;
		
		timerTryExtractor = 0;
		timerTryHydraDen = 0;
		timerTryCreep = 0;
		timerTryQueenNest = 0;
		timerTrySpire = 0;
		timerTryMacro = 0;
		
		dronesOnMinerals = 0;
		dronesOnGas = 0;
		nbDronesWantedOnGas = 0;
		
		safeExpandScore = 0;

		nbHatcheries = 0;
		nbExtractor = 0;
		nbHydraDen = 0;
		nbCreepColony = 0;
		nbSunkenColony = 0;
		nbSporeColony = 0;
		
		requestExtractor = false;
		requestHydraDen = false;
		requestCreep = false;
		requestQueenNest = false;
		requestLair = false;
		requestSpire = false;
		requestHive = false;
		
		extractor = null;
		nbMinerals = 0;
		nbDronesIdeal = 0;
	}

	public void update() {
		
		basePlanner.update();

		nbHatcheries = 0;
		nbExtractor = 0;
		nbHydraDen = 0;
		nbCreepColony = 0;
		nbSunkenColony = 0;
		nbSporeColony = 0;
		nbSpire = 0;
		nbGreatSpire = 0;
		dronesOnMinerals = 0;
		dronesOnGas = 0;
		
		nbDrones = dronesMap.size();

		extractor = null;
		for (Unit u : buildings)
		{
			if (u.getTypeID() == UnitTypes.Zerg_Hatchery.ordinal() || u.getTypeID() == UnitTypes.Zerg_Lair.ordinal() || u.getTypeID() == UnitTypes.Zerg_Hive.ordinal())
				nbHatcheries++;
			if (u.getTypeID() == UnitTypes.Zerg_Extractor.ordinal()) {
				nbExtractor++;
				if (u.getRemainingBuildTimer() == 0)
					extractor = u;
			}
			if (u.getTypeID() == UnitTypes.Zerg_Hydralisk_Den.ordinal())
				nbHydraDen++;
			if (u.getTypeID() == UnitTypes.Zerg_Creep_Colony.ordinal())
				nbCreepColony++;
			if (u.getTypeID() == UnitTypes.Zerg_Sunken_Colony.ordinal())
				nbSunkenColony++;
			if (u.getTypeID() == UnitTypes.Zerg_Spore_Colony.ordinal())
				nbSporeColony++;
			if (u.getTypeID() == UnitTypes.Zerg_Queens_Nest.ordinal())
				nbQueenNest++;
			if (u.getTypeID() == UnitTypes.Zerg_Spire.ordinal())
				nbSpire++;
			if (u.getTypeID() == UnitTypes.Zerg_Greater_Spire.ordinal())
				nbGreatSpire++;
		}
		
		if (state == BaseState.ENNEMY) state = BaseState.UNKNOWN;
		for (EnemyBuildingManager b : JavaBot.enemyBuildingsManager) {
			if (Operators.distance(this, b.unit) < size) {
				state = BaseState.ENNEMY;
				break;
			}
		}
		
		if (state != BaseState.FRIENDLY && nbHatcheries > 0) {
			state = BaseState.FRIENDLY;
			inaccessible = false;
		}
		
		nbMinerals = 0;
		if (state == BaseState.FRIENDLY) {
			for (Unit u : bwapi.getNeutralUnits()) {
				if (!hasGas && u.getTypeID() == UnitTypes.Resource_Vespene_Geyser.ordinal() && Operators.distance(this, u) < 350) {
					hasGas = true;
				}
				
				if (u.getTypeID() == UnitTypes.Resource_Mineral_Field.ordinal() && Operators.distance(this, u) < 350) {
					nbMinerals++;
				}
			}
		}
		
		nbDronesIdeal = (int) (1.8f*nbMinerals + (hasGas ? 3 : 0));
			
		if (state == BaseState.FRIENDLY && nbHatcheries == 0)
			state = BaseState.UNKNOWN;
		
		for (Unit u : dronesMap.values())
		{
			if (u.isGatheringMinerals())
				dronesOnMinerals++;
			if (u.isGatheringGas())
				dronesOnGas++;
		}
		
		if (hasGas && JavaBot.strategy.nbPoolTotal >= 1
				&& nbExtractor == 0
				&& JavaBot.strategy.needMoreGas
				&& nbHatcheries >= 1
				&& (nbDrones >= 8 || JavaBot.strategy.nbDronesTotal >= JavaBot.strategy.idealNbDrones * 0.85)
				&& JavaBot.knowledge.okExtractors) {
			requestExtractor = true;
		}
		else
			requestExtractor = false;
		
		if (JavaBot.strategy.nbHydraTotal == 0 && (JavaBot.strategy.nbHatcheryTotal >= 3 || JavaBot.currFrame >= 4000) && JavaBot.strategy.nbPoolTotal >= 1
				&& JavaBot.knowledge.realGas >= 1
				&& Tech.burrow != 0
				&& nbHydraDen == 0
				&& nbHatcheries >= 1) {
			requestHydraDen = true;
		}
		else
			requestHydraDen = false;
		
		if (JavaBot.strategy.nbQueenNestTotal == 0 && (JavaBot.knowledge.currentBuildings[Operators.iterLair] >= 1 || JavaBot.strategy.nbHiveTotal > 0)
				&& nbHatcheries >= 1
				&& (nbDrones >= 5 || JavaBot.strategy.nbDronesTotal >= JavaBot.strategy.idealNbDrones * 0.75)) {
			requestQueenNest = true;
		}
		else
			requestQueenNest = false;
		
		if (JavaBot.strategy.nbSpireTotal + JavaBot.strategy.nbGreatSpireTotal == 0
				&& JavaBot.strategy.nbQueenNestTotal > 0
				&& (JavaBot.knowledge.currentBuildings[Operators.iterLair] > 0 || JavaBot.strategy.nbHiveTotal > 0)
				&& nbSpire == 0
				&& nbHatcheries >= 1
				&& (nbDrones >= 5 || JavaBot.strategy.nbDronesTotal >= JavaBot.strategy.idealNbDrones * 0.75)
				&& JavaBot.strategy.nbMutalisksTotal + JavaBot.strategy.nbGuardiansTotal + JavaBot.strategy.nbDevourersTotal < 
				JavaBot.strategy.idealNbMutalisks + JavaBot.strategy.idealNbGuardians + JavaBot.strategy.idealNbDevourers) {
			requestSpire = true;
		}
		else
			requestSpire = false;
		
		if (timerTryCreep == 0 && ((JavaBot.strategy.nbPoolTotal >= 1 && nbCreepColony + nbSunkenColony + nbSporeColony <= 0
				&& nbHatcheries >= 1
				&& (nbDrones >= 7 || JavaBot.strategy.nbDronesTotal >= JavaBot.strategy.idealNbDrones * 0.75))
				&& ((JavaBot.strategy.nbHatcheryTotal >= 3 || JavaBot.strategy.nbOverlordsTotal >= 2) || JavaBot.strategy.enemyRace == RaceType.Zerg))
				||
				(JavaBot.strategy.nbPoolTotal >= 1 && nbCreepColony + nbSunkenColony + nbSporeColony <= 1
				&& JavaBot.strategy.nbHatcheryTotal >= 5
				&& (nbDrones >= 8 || JavaBot.strategy.nbDronesTotal >= JavaBot.strategy.idealNbDrones * 0.75))
				||
				(JavaBot.strategy.nbEvoChamberTotal >= 1 && nbCreepColony + nbSunkenColony + nbSporeColony <= 2
				&& JavaBot.strategy.nbHatcheryTotal >= 5
				&& (nbDrones >= 8 || JavaBot.strategy.nbDronesTotal >= JavaBot.strategy.idealNbDrones * 0.75))) {
			requestCreep = true;
		}
		else {
			requestCreep = false;
		}
		
		if (JavaBot.strategy.nbPoolTotal >= 1 && nbCreepColony + nbSunkenColony > 0
				&& nbHatcheries >= 1
				&& JavaBot.strategy.nbHydraTotal >= 1
				&& nbCreepColony + nbSunkenColony > 0
				&& (nbDrones >= 5 || JavaBot.strategy.nbDronesTotal >= JavaBot.strategy.idealNbDrones * 0.75)
				&& JavaBot.knowledge.currentBuildings[Operators.iterLair] == 0
				&& JavaBot.knowledge.warpingBuildings[Operators.iterLair] == 0
				&& JavaBot.knowledge.currentBuildings[Operators.iterHive] == 0
				&& JavaBot.knowledge.warpingBuildings[Operators.iterHive] == 0) {
			requestLair = true;
		}
		else
			requestLair = false;
		
		if (JavaBot.knowledge.currentBuildings[Operators.iterQueenNest] >= 1
				&& nbCreepColony + nbSunkenColony > 0
				&& nbHatcheries >= 1
				&& (JavaBot.strategy.nbQueensTotal >= 3 || Tech.broodlings == 1)
				&& (nbDrones >= 5 || JavaBot.strategy.nbDronesTotal >= JavaBot.strategy.idealNbDrones * 0.75)
				&& JavaBot.strategy.nbSpireTotal >= 1
				&& JavaBot.knowledge.currentBuildings[Operators.iterHive] == 0
				&& JavaBot.knowledge.warpingBuildings[Operators.iterHive] == 0) {
			requestHive = true;
		}
		else
			requestHive = false;
		
		if (nbExtractor > 0 && timerChangeRatio == 0) {
			/*
			if (nbDronesWantedOnGas == 0 && (nbDrones >= 6 || JavaBot.strategy.nbDronesTotal >= JavaBot.strategy.idealNbDrones * 0.75) && JavaBot.knowledge.realMinerals >= 50 && (JavaBot.knowledge.realGas <= 260 || JavaBot.knowledge.realMinerals >= 450))
				nbDronesWantedOnGas = 1;
			
			if (nbDronesWantedOnGas == 1 && JavaBot.knowledge.realMinerals >= 70 && (nbDrones >= 7 || JavaBot.strategy.nbDronesTotal >= JavaBot.strategy.idealNbDrones * 0.75) && (JavaBot.knowledge.realGas <= 100 || JavaBot.knowledge.realMinerals >= 450))
				nbDronesWantedOnGas = 2;
			if (nbDronesWantedOnGas == 1 && JavaBot.knowledge.realGas >= 260)
				nbDronesWantedOnGas = 0;
			
			if (nbDronesWantedOnGas == 2 && (JavaBot.knowledge.realGas >= 180 || nbDrones < 5))
				nbDronesWantedOnGas = 1;
			if (nbDronesWantedOnGas == 2 && JavaBot.knowledge.realMinerals >= 200 && (JavaBot.knowledge.realGas <= 120 || JavaBot.knowledge.realMinerals >= 450) && (nbDrones >= 9 || JavaBot.strategy.nbDronesTotal >= JavaBot.strategy.idealNbDrones * 0.75))
				nbDronesWantedOnGas = 3;
			
			if (nbDronesWantedOnGas == 3 && ((JavaBot.knowledge.realGas > 150 && JavaBot.knowledge.realMinerals < 400) || (nbDrones < 7 && JavaBot.strategy.nbDronesTotal < JavaBot.strategy.idealNbDrones * 0.6)))
				nbDronesWantedOnGas = 2;
				*/
			if (nbDronesWantedOnGas < 3 && JavaBot.strategy.needMoreGas) {
				nbDronesWantedOnGas++;
				timerChangeRatio = 100;
			}
			if (nbDronesWantedOnGas > 0 && JavaBot.strategy.needMoreMinerals) {
				nbDronesWantedOnGas--;
				timerChangeRatio = 100;
			}
		}
		
		if (timerTryExtractor > 0)
			timerTryExtractor--;
		if (timerTryHydraDen > 0)
			timerTryHydraDen--;
		if (timerTryCreep > 0)
			timerTryCreep--;
		if (timerTrySpire > 0)
			timerTrySpire--;
		if (timerTryMacro > 0)
			timerTryMacro--;
		if (timerTryQueenNest > 0)
			timerTryQueenNest--;
		if (timerChangeRatio > 0)
			timerChangeRatio--;
		
		if (timerRebuild > 0)
			timerRebuild--;
		if (blockLair > 0)
			blockLair--;
		
		if (timerGoOnGas > 0)
			timerGoOnGas--;
		if (timerGoOnMinerals > 0)
			timerGoOnMinerals--;
		
		if (state == BaseState.FRIENDLY)
			possibleEnemy = false;
		else {
			timerTryExtractor = 0;
			timerTryHydraDen = 0;
			timerTryCreep = 0;
			timerTrySpire = 0;
			timerTryMacro = 0;
		}
	}
	
	public static void setScoresExpand(ArrayList<BaseManager> bases, ArrayList<BaseLocation> locations, int homePositionX, int homePositionY, JNIBWAPI bwapi2) {

		Map map = bwapi2.getMap();
		
		if (!JavaBot.RELEASE)
			System.out.println("Current map is " + map.getName());
		
		for (BaseManager base : bases) {
			base.safeExpandScore = 0;
			
			for (BaseLocation baseLoc : locations) {
				if (baseLoc.isStartLocation()) {
					
					if (baseLoc.getX() == homePositionX && baseLoc.getY() == homePositionY) {
						base.safeExpandScore += Operators.distance(base.posX, base.posY, baseLoc.getX(), baseLoc.getY()) * 0.7f;
					}
					else
						base.safeExpandScore += Operators.distance(base.posX, base.posY, baseLoc.getX(), baseLoc.getY());
				}
			}
			if (base.possibleEnemy)
				base.safeExpandScore *= 0.6f;
			if (Operators.distance(base.posX, base.posY, homePositionX, homePositionY) < 1500)
				base.safeExpandScore *= 0.9f;
		}
	}
	
	public void drawDebug() {

		if (state == BaseState.FRIENDLY)
			bwapi.drawCircle(posX, posY, 350, BWColor.GREEN, false, false);
		if (state == BaseState.ENNEMY)
			bwapi.drawCircle(posX, posY, 350, BWColor.RED, false, false);
		if (state == BaseState.UNKNOWN)
			bwapi.drawCircle(posX, posY, 350, BWColor.GREY, false, false);

		if (isIsland)
			bwapi.drawText(posX, posY - 15, "Island", false);
		if (inaccessible)
			bwapi.drawText(posX, posY - 20, "INACCESSIBLE", false);
		
		bwapi.drawText(posX, posY - 30, "Nb buildings: " + buildings.size(), false);
		bwapi.drawText(posX, posY - 40, "Nb drones: " + nbDrones, false);
		
		bwapi.drawText(posX, posY - 50, "NbHatcheries: " + nbHatcheries, false);
		bwapi.drawText(posX, posY - 60, "DronesMineral: " + dronesOnMinerals, false);
		bwapi.drawText(posX, posY - 70, "DronesGas: " + dronesOnGas, false);
		/*bwapi.drawText(posX, posY - 80, "Request creep: " + requestCreep, false);
		bwapi.drawText(posX, posY - 90, "Request Extractor: " + requestExtractor, false);
		bwapi.drawText(posX, posY - 100, "Request hydraden: " + requestHydraDen, false);
		bwapi.drawText(posX, posY - 110, "Request QueenNest: " + requestQueenNest, false);*/
		
		if (!JavaBot.RELEASE) {
			bwapi.drawText(posX, posY - 80, "NbCreep: " + nbCreepColony, false);
			bwapi.drawText(posX, posY - 90, "NbSunken: " + nbSunkenColony, false);
			bwapi.drawText(posX, posY - 100, "NbSpore: " + nbSporeColony, false);
			bwapi.drawText(posX, posY - 110, "RequestHydra: " + requestHydraDen, false);
			bwapi.drawText(posX, posY - 120, "RequestExtr: " + requestExtractor, false);
			bwapi.drawText(posX, posY - 130, "Timer creep: " + timerTryCreep, false);
			bwapi.drawText(posX, posY - 140, "Minerals patches: " + nbMinerals, false);
			bwapi.drawText(posX, posY - 150, "Drones needed: " + nbDronesIdeal, false);
			
			bwapi.drawText(posX, posY - 165, "Safe: " + safeExpandScore, false);
		}
		/*bwapi.drawText(posX, posY - 80, "timerExtract: " + timerTryExtractor, false);
		bwapi.drawText(posX, posY - 90, "timerHydra: " + timerTryHydraDen, false);
		bwapi.drawText(posX, posY - 100, "NbExtract: " + nbExtractor, false);
		bwapi.drawText(posX, posY - 110, "NbHydraDen: " + nbHydraDen, false);
		bwapi.drawText(posX, posY - 120, "RequestExtractor: " + requestExtractor, false);
		bwapi.drawText(posX, posY - 130, "RequestHydraDen: " + requestHydraDen, false);*/
		
		if (!JavaBot.RELEASE)
			basePlanner.debug();
	}
}
