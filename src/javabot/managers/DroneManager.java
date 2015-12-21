package javabot.managers;

import java.awt.Point;
import javabot.JavaBot;
import javabot.knowledge.Tech;
import javabot.knowledge.Tile;
import javabot.knowledge.TileGraph;
import javabot.managers.BaseManager.BaseState;
import javabot.model.Unit;
import javabot.operators.Operators;
import javabot.types.TechType.TechTypes;
import javabot.types.UnitType.UnitTypes;
import javabot.util.BWColor;

public class DroneManager extends UnitManager {

	public enum DroneState {
		NORMAL,
		ATTACKING,
		IN_BURROW,
		UN_BURROW,
		BURROWED
	}
	
	int chosenForExpand;
	int chosenForRebuild;
	int chosenForPool;
	int chosenForMacro;
	int chosenForExtractor;
	int chosenForHydraDen;
	int chosenForCreep;
	int chosenForQueenNest;
	int chosenForSpire;
	int chosenForEvo;
	
	BaseManager oldBase;
	BaseManager friendlyBase;
	BaseManager bestExpand;
	
	int timeUntilInaccessible = 60;
	int radiusDetectBurrow = 210;
	int radiusDetectUnburrow = 220;
	
	int timerInaccessible;
	int timerAttack;
	int minDistance;
	int oldHp;
	int actFrameBurrow;
	
	Point buildPlace;
	
	DroneState state;
	int transitionStates;
	
	public boolean isConstructing;
	
	public DroneManager(Unit u) {
		super(u);
		chosenForExpand = 0;
		chosenForMacro = 0;
		chosenForExtractor= 0;
		chosenForPool = 0;
		chosenForHydraDen = 0;
		chosenForCreep = 0;
		chosenForQueenNest = 0;
		chosenForSpire = 0;
		chosenForEvo = 0;
		
		actFrameBurrow = 0;
		
		oldBase = null;
		bestExpand = null;
		isConstructing = true;
		buildPlace = new Point(-1,0);
		
		state = DroneState.NORMAL;
		transitionStates = 0;
	}
	
	public void update()
	{
		super.update();
		
		if (unit.isGatheringGas())
			JavaBot.strategy.gasPerMinute++;
		if (unit.isGatheringMinerals())
			JavaBot.strategy.mineralsPerMinute++;
		
		safeMarge = (40 - hp) * 2;
		
		if (dead == true) {
			if (chosenForExpand != 0 && JavaBot.knowledge.timerTryExpand > 0) {
				JavaBot.knowledge.timerTryExpand = 300;
				if (bestExpand != null) {
						if (bestExpand.safeExpandScore > 10)
					bestExpand.safeExpandScore *= 0.8f;
				}
			}
			if (base != null)
				base.dronesMap.remove(unit.getID());
		}
		
		if (free())
			buildPlace.x = -1;
		
		if (free() && JavaBot.knowledge.requestedBuildings[Operators.iterHatchery] > 0 && bestExpand != null)
		{
			if (JavaBot.knowledge.timerTryExpand == 0)
			{
				chosenForExpand = 1;
				JavaBot.knowledge.timerTryExpand = 2100;
			}
		}
		
		if (free() && !unit.isMoving() && base.state == BaseState.UNKNOWN && base.timerRebuild == 0)
		{
			chosenForRebuild = 1;
			base.timerRebuild = 10;
			bestExpand = base;
		}
		
		if (free() && base.state == BaseState.FRIENDLY && base.requestExtractor && base.timerTryExtractor == 0)
		{
			chosenForExtractor = 1;
			base.timerTryExtractor = 900;
		}
		
		if (free() && base.state == BaseState.FRIENDLY && base.requestHydraDen && base.timerTryHydraDen == 0 && JavaBot.knowledge.realGas >= 32 && JavaBot.knowledge.realMinerals >= 70
				&& JavaBot.knowledge.timerTryHydra == 0)
		{
			chosenForHydraDen = 1;
			base.timerTryHydraDen = 900;
			JavaBot.knowledge.timerTryHydra = 900;
		}
		
		if (free() && base.state == BaseState.FRIENDLY && base.requestQueenNest && base.timerTryQueenNest == 0
				 && JavaBot.knowledge.timerTryQueenNest == 0)
		{
			chosenForQueenNest = 1;
			base.timerTryQueenNest = 900;
			JavaBot.knowledge.timerTryQueenNest = 900;
		}
		
		if (free() && base.state == BaseState.FRIENDLY && base.requestSpire && base.timerTrySpire == 0
				 && JavaBot.knowledge.timerTrySpire == 0)
		{
			chosenForSpire = 1;
			base.timerTrySpire = 900;
			JavaBot.knowledge.timerTrySpire = 900;
		}
		
		if (free() && base.state == BaseState.FRIENDLY && base.requestCreep && base.timerTryCreep == 0 && JavaBot.repartition.getExpandMinerals() >= 65)
		{
			chosenForCreep = 1;
			base.timerTryCreep = 650;
		}
		
		/*if (free() && JavaBot.knowledge.requestMacro && base.timerTryMacro == 0 && base.nbDrones > 6 && base.nbHatcheries <= 1)
		{
			chosenForMacro = 1;
			base.timerTryMacro = 800;
		}*/
		
		if (free() && base.state == BaseState.FRIENDLY && JavaBot.knowledge.requestedBuildings[Operators.iterPool] > 0
				&& JavaBot.knowledge.timerTryPool == 0
				&& JavaBot.repartition.getPoolMinerals() >= 140)
		{
			JavaBot.knowledge.timerTryPool = 400;
			chosenForPool = 1;
		}
		
		if (free() && base.state == BaseState.FRIENDLY && JavaBot.knowledge.requestedBuildings[Operators.iterEvolutionChamber] > 0
				&& JavaBot.knowledge.timerTryEvo == 0
				&& JavaBot.repartition.getEvoMinerals() >= 60)
		{
			JavaBot.knowledge.timerTryEvo = 400;
			chosenForEvo = 1;
		}
		
		if (free() && base.extractor != null && base.dronesOnGas < base.nbDronesWantedOnGas && base.timerGoOnGas == 0)
		{
			bwapi.rightClick(unit.getID(), base.extractor.getID());
			base.timerGoOnGas = 300;
		}
		
		if (unit.isCarryingGas() && base.dronesOnGas > base.nbDronesWantedOnGas && base.timerGoOnMinerals == 0)
		{
			mineClosestmineral();
			base.timerGoOnMinerals = 300;
		}

		updateIsInBase();
		
		if (chosenForExpand != 0)
		{
			updateExpand();
		}
		else if (chosenForRebuild != 0)
		{
			updateRebuild();
		}
		/*else if (chosenForMacro != 0)
		{
			if (JavaBot.knowledge.minerals < 200 || base.nbHatcheries >= 2)
				chosenForMacro = 0;
			else {
				if (buildPlace.x == -1)
					buildPlace = base.basePlanner.potentialHatchery != null ? base.basePlanner.potentialHatchery : JavaBot.getBuildTile(unit.getID(), UnitTypes.Zerg_Hatchery.ordinal(), base.posX, base.posY);
				else
				{
					if (Operators.distance(posX, posY, buildPlace.x*32, buildPlace.y*32) < 50 || unit.isConstructing() || unit.isMoving()) {
						JavaBot.requestsManager.requestRessources(300, 0);
					}
					
					if (JavaBot.knowledge.realMinerals >= 300)
						BwapiCallsManager.build(unit.getID(), buildPlace.x, buildPlace.y, UnitTypes.Zerg_Hatchery.ordinal());
					else {
						BwapiCallsManager.move(unit.getID(), buildPlace.x*32, buildPlace.y*32);
					}
				}
			}
		}*/
		else if (chosenForPool != 0)
		{
			if (JavaBot.knowledge.currentBuildings[Operators.iterPool] + JavaBot.knowledge.warpingBuildings[Operators.iterPool] > 0)
				chosenForPool = 0;
			else
			{
				if (buildPlace.x == -1)
					buildPlace = JavaBot.getBuildTile(unit.getID(), UnitTypes.Zerg_Spawning_Pool.ordinal(), friendlyBase.posX, friendlyBase.posY);
				else
				{
					if (Operators.distance(posX, posY, buildPlace.x*32, buildPlace.y*32) < 50 || unit.isConstructing()) {
						JavaBot.requestsManager.requestRessources(200, 0);
						JavaBot.knowledge.timerTryPool = 200;
					}
					
					if (JavaBot.repartition.getPoolMinerals() >= 200)
						BwapiCallsManager.build(unit.getID(), buildPlace.x, buildPlace.y, UnitTypes.Zerg_Spawning_Pool.ordinal());
					else {
						BwapiCallsManager.move(unit.getID(), buildPlace.x*32, buildPlace.y*32);
					}
				}
			}
		}
		else if (chosenForExtractor != 0)
		{
			if (base.nbExtractor > 0 || base.timerTryExtractor == 0)
				chosenForExtractor = 0;
			else
			{
				if (buildPlace.x == -1)
					buildPlace = JavaBot.getBuildTile(unit.getID(), UnitTypes.Zerg_Extractor.ordinal(), base.posX, base.posY);
				else
				{
					if (Operators.distance(posX, posY, buildPlace.x*32, buildPlace.y*32) < 100 || unit.isConstructing() || unit.isMoving()) {
						JavaBot.requestsManager.requestRessources(50, 0);
					}
					
					if (JavaBot.repartition.getExtractMinerals() >= 50)
						BwapiCallsManager.build(unit.getID(), buildPlace.x, buildPlace.y, UnitTypes.Zerg_Extractor.ordinal());
					else {
						BwapiCallsManager.move(unit.getID(), buildPlace.x*32, buildPlace.y*32);
					}
				}
			}
		}
		else if (chosenForHydraDen != 0)
		{
			if (base.nbHydraDen > 0 || base.timerTryHydraDen == 0)
				chosenForHydraDen = 0;
			else
			{
				if (buildPlace.x == -1)
					buildPlace = JavaBot.getBuildTile(unit.getID(), UnitTypes.Zerg_Hydralisk_Den.ordinal(), base.posX, base.posY);
				else
				{
					if (Operators.distance(posX, posY, buildPlace.x*32, buildPlace.y*32) < 50 || unit.isConstructing()) {
						base.timerTryHydraDen = 200;
						JavaBot.requestsManager.requestRessources(100, 50);
					}
					
					if (JavaBot.repartition.getHydraDenMinerals() >= 100 && JavaBot.knowledge.realGas >= 50)
						BwapiCallsManager.build(unit.getID(), buildPlace.x, buildPlace.y, UnitTypes.Zerg_Hydralisk_Den.ordinal());
					else {
						BwapiCallsManager.move(unit.getID(), buildPlace.x*32, buildPlace.y*32);
					}
				}
			}
		}
		else if (chosenForQueenNest != 0)
		{
			if (base.nbQueenNest > 0 || base.timerTryQueenNest == 0 || !base.requestQueenNest || JavaBot.knowledge.timerTryQueenNest == 0)
				chosenForQueenNest = 0;
			else
			{
				if (buildPlace.x == -1)
					buildPlace = JavaBot.getBuildTile(unit.getID(), UnitTypes.Zerg_Queens_Nest.ordinal(), base.posX, base.posY);
				else
				{
					if (Operators.distance(posX, posY, buildPlace.x*32, buildPlace.y*32) < 100 || unit.isConstructing()) {
						base.timerTryQueenNest = 300;
						JavaBot.knowledge.timerTryQueenNest = 300;
						JavaBot.requestsManager.requestRessources(150, 100);
					}
					
					if (JavaBot.repartition.getQueenNestMinerals() >= 150 && JavaBot.knowledge.realGas >= 100)
						BwapiCallsManager.build(unit.getID(), buildPlace.x, buildPlace.y, UnitTypes.Zerg_Queens_Nest.ordinal());
					else {
						BwapiCallsManager.move(unit.getID(), buildPlace.x*32, buildPlace.y*32);
					}
				}
			}
		}
		else if (chosenForSpire != 0)
		{
			if (base.nbSpire > 0 || base.nbGreatSpire > 0 || base.timerTrySpire == 0 || !base.requestSpire)
				chosenForSpire = 0;
			else
			{
				if (buildPlace.x == -1)
					buildPlace = JavaBot.getBuildTile(unit.getID(), UnitTypes.Zerg_Spire.ordinal(), base.posX, base.posY);
				else
				{
					if (Operators.distance(posX, posY, buildPlace.x*32, buildPlace.y*32) < 50 || unit.isConstructing()) {
						base.timerTrySpire = 200;
						JavaBot.knowledge.timerTrySpire = 200;
						JavaBot.requestsManager.requestRessources(200, 150);
					}
					
					if (JavaBot.repartition.getSpireMinerals() >= 200 && JavaBot.knowledge.realGas >= 150) {
						BwapiCallsManager.build(unit.getID(), buildPlace.x, buildPlace.y, UnitTypes.Zerg_Spire.ordinal());
					}
					else {
						BwapiCallsManager.move(unit.getID(), buildPlace.x*32, buildPlace.y*32);
					}
				}
			}
		}
		else if (chosenForCreep != 0)
		{
			if (base.nbCreepColony + base.nbSporeColony + base.nbSunkenColony >= 3 || base.timerTryCreep == 0) {
				chosenForCreep = 0;
				BwapiCallsManager.stop(unit.getID());
			}
			else
			{
				if (buildPlace.x == -1)
					buildPlace = JavaBot.getBuildTile(unit.getID(), UnitTypes.Zerg_Creep_Colony.ordinal(), base.posX, base.posY);
				else
				{
					if (Operators.distance(posX, posY, buildPlace.x*32, buildPlace.y*32) < 50 || unit.isConstructing()) {
						base.timerTryCreep = 200;
						JavaBot.requestsManager.requestRessources(75, 0);
					}
					
					if (JavaBot.repartition.getCreepMinerals() >= 75)
						BwapiCallsManager.build(unit.getID(), buildPlace.x, buildPlace.y, UnitTypes.Zerg_Creep_Colony.ordinal());
					else {
						BwapiCallsManager.move(unit.getID(), buildPlace.x*32, buildPlace.y*32);
					}
				}
			}
		}
		else if (chosenForEvo != 0)
		{
			if (JavaBot.strategy.nbEvoChamberTotal > 0 || JavaBot.knowledge.timerTryEvo == 0) {
				chosenForEvo = 0;
				BwapiCallsManager.stop(unit.getID());
			}
			else
			{
				if (buildPlace.x == -1)
					buildPlace = JavaBot.getBuildTile(unit.getID(), UnitTypes.Zerg_Evolution_Chamber.ordinal(), base.posX, base.posY);
				else
				{
					if (Operators.distance(posX, posY, buildPlace.x*32, buildPlace.y*32) < 50 || unit.isConstructing()) {
						JavaBot.knowledge.timerTryEvo = 50;
					}
					
					if (JavaBot.repartition.getEvoMinerals() >= 75)
						BwapiCallsManager.build(unit.getID(), buildPlace.x, buildPlace.y, UnitTypes.Zerg_Evolution_Chamber.ordinal());
					else {
						BwapiCallsManager.move(unit.getID(), buildPlace.x*32, buildPlace.y*32);
					}
				}
			}
		}
		else if (unit.isIdle() && !unit.isBurrowed())
			updateIdle();
		
		switch (state) {
		
			case IN_BURROW:
				updateInBurrow();
				break;
				
			case UN_BURROW:
				updateUnBurrow();
				break;
				
			case BURROWED:
				updateBurrowed();
				break;
			
			case NORMAL:
				updateNormal();
				break;
				
			case ATTACKING:
				updateAttacking();
				break;
				
			default:
				break;
		}
		
		oldHp = unit.getHitPoints();
		
		if (!unit.isBurrowed()) {
			if (timerInaccessible > 0)
				timerInaccessible--;
		}
		if (timerAttack > 0)
			timerAttack--;
	}
	
	private void goToState(DroneState s, int transitionTime) {
		state = s;
		transitionStates = transitionTime;
	}
	
	private boolean mustBurrow() {
		if (Tech.burrow != 2) return false;
		
		return (nearbyGroundEnemy(safeMarge) && unit.getHitPoints() < 40);
	}

	private void updateAttacking() {
		if (mustBurrow()) {
			goToState(DroneState.IN_BURROW, 0);
		}
		else if (timerAttack == 0 && !unit.isAttacking()) {
			mineClosestmineral();
			goToState(DroneState.NORMAL, 0);
		}
	}

	private void updateNormal() {
		if (mustBurrow()) {
			goToState(DroneState.IN_BURROW, 0);
		}
		else if (oldHp > unit.getHitPoints() && !unit.isAttacking() && timerAttack == 0) {
			if (BwapiCallsManager.attack(unit.getID(), (int)(base.posX + Math.random() * 150 - 75), (int) (base.posY + Math.random() * 150 - 75))) {
				timerAttack = 200;
				goToState(DroneState.ATTACKING, 0);
			}
		}
	}
	
	private void updateUnBurrow() {
		if (countDontAct == 0) {
			if (BwapiCallsManager.useTech(unit.getID(), TechTypes.Burrowing.ordinal()))
				countDontAct = 100;
		}
		
		if (!unit.isBurrowed()) {
			goToState(DroneState.NORMAL, 10);
			countDontAct = 10;
		}
	}

	private void updateBurrowed() {
		if (!unit.isBurrowed()) {
			goToState(DroneState.NORMAL, 0);
		}
		else if (!nearbyGroundEnemy(safeMarge)) {
			goToState(DroneState.UN_BURROW, 0);
			return;
		}
	}

	private void updateInBurrow() {
		if (countDontAct == 0) {
			if (BwapiCallsManager.useTech(unit.getID(), TechTypes.Burrowing.ordinal()))
				countDontAct = 100;
		}
		
		if (unit.isBurrowed()) {
			countDontAct = 10;
			goToState(DroneState.BURROWED, 10);
		}
	}
	
	private void updateRebuild() {
		base.timerRebuild = 10;
		
		if (bestExpand == null || bestExpand.state == BaseState.FRIENDLY || (bestExpand.nbDrones >= 2 && Operators.distance(bestExpand, unit) > 15)) {
			chosenForRebuild = 0;
			if (friendlyBase != null) {
				BwapiCallsManager.move(unit.getID(), friendlyBase.posX, friendlyBase.posY);
			}
		}
		
		buildPlace = bestExpand.basePlanner.potentialHatchery != null ? bestExpand.basePlanner.potentialHatchery : JavaBot.getBuildTile(unit.getID(), UnitTypes.Zerg_Hatchery.ordinal(), bestExpand.posX, bestExpand.posY);
		if (Operators.distance(posX, posY, buildPlace.x*32 + 32 + 32, buildPlace.y*32 + 32 + 16) >= 40)
			BwapiCallsManager.move(unit.getID(), buildPlace.x*32 + 32*2, buildPlace.y*32 + 32 + 16);
		else if (JavaBot.knowledge.timerTryExpand < 200)
			JavaBot.knowledge.timerTryExpand++;
			
		
		if (JavaBot.knowledge.realMinerals >= 300)
			BwapiCallsManager.build(unit.getID(), buildPlace.x, buildPlace.y, UnitTypes.Zerg_Hatchery.ordinal());
	}

	private void updateExpand() {
		
		if ((bestExpand.nbDrones >= 2 && Operators.distance(bestExpand, unit) > 50)
				|| bestExpand.isIsland
				|| bestExpand.state == BaseState.FRIENDLY
				|| bestExpand.state == BaseState.ENNEMY) {
			if (friendlyBase != null) {
				BwapiCallsManager.move(unit.getID(), friendlyBase.posX, friendlyBase.posY);
			}
			chosenForExpand = 0;
			if (JavaBot.knowledge.timerTryExpand > 500)
				JavaBot.knowledge.timerTryExpand = 500;
		}
		if (chosenForExpand == 1 || chosenForExpand == 2)
		{
			if (chosenForExpand == 1) {
				chosenForExpand = 2;
				timerInaccessible = timeUntilInaccessible;
				minDistance = Operators.distance(bestExpand, unit);
			}
			BwapiCallsManager.move(unit.getID(), bestExpand.posX, bestExpand.posY);
			if ((bestExpand.posX - posX) * (bestExpand.posX - posX) + (bestExpand.posY - posY) * (bestExpand.posY - posY) < 100)
				chosenForExpand = 3;
			if (timerInaccessible == 0) {
				if (friendlyBase != null) {
					BwapiCallsManager.move(unit.getID(), friendlyBase.posX, friendlyBase.posY);
				}
				chosenForExpand = 0;
				bestExpand.inaccessible = true;
				bestExpand.safeExpandScore *= 0.8;
				JavaBot.knowledge.timerTryExpand = 20;
			}
		}
		
		if (unit.isIdle() && chosenForExpand == 3)
		{
			timerInaccessible = timeUntilInaccessible;
			if (bestExpand.nbHatcheries > 0) {
				if (friendlyBase != null) {
					if (BwapiCallsManager.move(unit.getID(), friendlyBase.posX, friendlyBase.posY)) {
						chosenForExpand = 0;
					}
				}
			}
			else
			{
				if (buildPlace.x == -1)
					buildPlace = bestExpand.basePlanner.potentialHatchery != null ? bestExpand.basePlanner.potentialHatchery : JavaBot.getBuildTile(unit.getID(), UnitTypes.Zerg_Hatchery.ordinal(), bestExpand.posX, bestExpand.posY);
				else
				{
					if (Operators.distance(posX, posY, buildPlace.x*32, buildPlace.y*32) < 100) {
						JavaBot.requestsManager.requestRessources(300, 0);
						JavaBot.knowledge.timerTryExpand++;
					}
					else {
						BwapiCallsManager.move(unit.getID(), buildPlace.x*32 + 64, buildPlace.y*32 + 32);
					}
					
					if (JavaBot.knowledge.realMinerals >= 300)
						BwapiCallsManager.build(unit.getID(), buildPlace.x, buildPlace.y, UnitTypes.Zerg_Hatchery.ordinal());
				}
			}
		}
	}

	public void updateIsInBase()
	{
		int distMax = Integer.MAX_VALUE;
		BaseManager bestBase = null;
		
		int distMaxFriend = Integer.MAX_VALUE;
		BaseManager bestBaseFriend = null;
		
		BaseManager bestBaseExpand = null;
		
		int bestSafescore = 0;
		
		for (BaseManager manager : JavaBot.bases)
		{
			int dist = (manager.posX - posX) * (manager.posX - posX) + (manager.posY - posY) * (manager.posY - posY);
			if (dist < distMax)
			{
				distMax = dist;
				bestBase = manager;
			}
			
			if (manager.state == BaseState.FRIENDLY && dist < distMaxFriend)
			{
				distMaxFriend = dist;
				bestBaseFriend = manager;
			}
			
			if (chosenForExpand == 0)
			if (!manager.isIsland && manager.state == BaseState.UNKNOWN && manager.timerRebuild == 0 &&
				(bestSafescore + (manager.inaccessible ? 1200 : 0) < manager.safeExpandScore || Math.random() < 0.03f) && (bestExpand == null || Math.random() < 0.85f))
			{
				bestBaseExpand = manager;
				bestSafescore = manager.safeExpandScore;
			}
		}
		if (bestBase != null)
		{
			base = bestBase;
			if (base != oldBase) {
				base.dronesMap.put(unit.getID(), unit);
				if (oldBase != null) {
					oldBase.dronesMap.remove(unit.getID());
				}
			}
			oldBase = base;
		}
		if (bestBaseFriend != null) {
			friendlyBase = bestBaseFriend;
		}
		if (chosenForExpand == 0 && chosenForRebuild == 0) {
			bestExpand = bestBaseExpand;
		}
		
		if (bestExpand != null && chosenForExpand != 0) {
			
			Tile t = TileGraph.getTile(posY / 32, posX / 32);
			if (t != null) {
				if (t.DroneVisited == 0) {
					timerInaccessible = timeUntilInaccessible;
					t.DroneVisited = 600;
				}
			}
		}
	}
	
	public void updateIdle()
	{
		if (base != null && base.state != BaseState.FRIENDLY && friendlyBase != null) {
			BwapiCallsManager.move(unit.getID(), friendlyBase.posX, friendlyBase.posY);
			countDontAct = 20;
		}
		else {
			mineClosestmineral();
		}
	}
	
	public void mineClosestmineral()
	{
		int closestId = -1;
		double closestDist = 99999999;
		
		int mineralsID = UnitTypes.Resource_Mineral_Field.ordinal();
		for (Unit neu : bwapi.getNeutralUnits()) {
			if (neu.getTypeID() == mineralsID) {
				int a = neu.getX() - unit.getX();
				int b = neu.getY() - unit.getY();
				double distance = Math.sqrt(a*a + b*b);
				if ((closestId == -1) || (distance < closestDist)) {
					closestDist = distance;
					closestId = neu.getID();
				}
			}
		}
		
		if (closestId != -1) BwapiCallsManager.rightClick(unit.getID(), closestId);
	}
	
	public boolean free()
	{
		return (!unit.isCarryingMinerals() && !unit.isGatheringGas()
				&& chosenForExpand == 0
				&& chosenForRebuild == 0
				&& chosenForPool == 0
				&& chosenForMacro == 0
				&& chosenForExtractor == 0
				&& chosenForHydraDen == 0
				&& chosenForCreep == 0
				&& chosenForQueenNest == 0
				&& chosenForSpire == 0
				&& chosenForEvo == 0
				&& !unit.isConstructing()
				&& state == DroneState.NORMAL);
	}
	
	public void drawDebug()
	{
		if (!JavaBot.RELEASE)
			bwapi.drawText(unit.getX() - 10, unit.getY() - 8, Operators.stringFromType(typeID) + ":" + state, false);
		
		if (chosenForExpand != 0)
			bwapi.drawText(unit.getX() - 10, unit.getY() - 1, "EXPAND " + timerInaccessible, false);
		else if (chosenForRebuild != 0)
			bwapi.drawText(unit.getX() - 10, unit.getY() - 1, "REBUILD", false);
		else if (chosenForMacro != 0)
			bwapi.drawText(unit.getX() - 10, unit.getY() - 1, "MACRO " + timerInaccessible, false);
		else if (chosenForPool != 0)
			bwapi.drawText(unit.getX() - 10, unit.getY() - 1, "POOL", false);
		else if (chosenForExtractor != 0)
			bwapi.drawText(unit.getX() - 10, unit.getY() - 1, "EXTRACTOR", false);
		else if (chosenForHydraDen != 0)
			bwapi.drawText(unit.getX() - 10, unit.getY() - 1, "HYDRA", false);
		else if (chosenForQueenNest != 0)
			bwapi.drawText(unit.getX() - 10, unit.getY() - 1, "QUEENnest", false);
		else if (chosenForSpire != 0)
			bwapi.drawText(unit.getX() - 10, unit.getY() - 1, "SPIRE", false);
		else if (chosenForCreep != 0)
			bwapi.drawText(unit.getX() - 10, unit.getY() - 1, "CREEP", false);
		else if (chosenForEvo != 0)
			bwapi.drawText(unit.getX() - 10, unit.getY() - 1, "EVO", false);
		
		/*if (unit.isMoving()) {
			bwapi.drawLine(posX, posY, unit.getTargetX(), unit.getTargetY(), BWColor.WHITE, false);
		}*/
		
		if (bestExpand != null && (chosenForExpand != 0 || chosenForRebuild != 0)) {
			bwapi.drawLine(posX, posY, bestExpand.posX, bestExpand.posY, BWColor.GREY, false);
			if (Tech.burrow == 2)
				if (unit.isBurrowed())
					bwapi.drawCircle(posX, posY, radiusDetectUnburrow, BWColor.RED, false, false);
				else
					bwapi.drawCircle(posX, posY, radiusDetectBurrow, BWColor.YELLOW, false, false);
		}
	}
}
