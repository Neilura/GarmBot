package javabot.managers;

import javabot.JavaBot;
import javabot.knowledge.Tech;
import javabot.managers.BaseManager.BaseState;
import javabot.model.Unit;
import javabot.types.TechType.TechTypes;

public class HydraliskManager extends UnitManager{

	public enum MICRO_STATES {
		IDLE,
		ATTACKING,
		IN_BURROW,
		BURROWED,
		UN_BURROW
	}
	
	int transitionStates = 0;
	MICRO_STATES microState;
	
	public HydraliskManager(Unit u) {
		super(u);
		explore = true;
		microState = MICRO_STATES.ATTACKING;
	}
	
	public void update()
	{
		super.update();
		
		safeMarge = (80 - hp) * 3;
		
		if (transitionStates > 0) {
			transitionStates--;
		}
		else {
			switch(microState) {
				case ATTACKING:
					updateAttacking();
					break;
				case IN_BURROW:
					updateInBurrow();
					break;
				case BURROWED:
					updateBurrowed();
					break;
				case UN_BURROW:
					updateUnBurrow();
					break;
				default:
					updateAttacking();
					break;
			}
		}
		nbNemesis = 0;
	}
	
	private void goToState(MICRO_STATES state, int transitionTime) {
		microState = state;
		transitionStates = transitionTime;
	}
	
	private boolean mustBurrow() {
		if (((JavaBot.currFrame < 20000 && hp >= 75) || hp >= 15) || Tech.burrow != 2 || nearbyDetector()) return false;
		
		boolean nearbyThreat = nearbyGroundEnemy(safeMarge);
		
		return nearbyThreat;
	}
	
	private void updateAttacking() {
		if (mustBurrow()) {
			goToState(MICRO_STATES.IN_BURROW, 0);
		}
		else if (unit.isBurrowed()) {
			goToState(MICRO_STATES.BURROWED, 0);
		}
		else {
			attackRandomHydralisk();
		}
	}

	private void updateUnBurrow() {
		if (countDontAct == 0) {
			if (BwapiCallsManager.useTech(unit.getID(), TechTypes.Burrowing.ordinal()))
				countDontAct = 20;
		}
		
		if (!unit.isBurrowed()) {
			goToState(MICRO_STATES.ATTACKING, 2);
			countDontAct = 0;
		}
	}

	private void updateBurrowed() {
		if (!unit.isBurrowed()) {
			goToState(MICRO_STATES.ATTACKING, 8);
		}
		else if (nearbyDetector() || !nearbyGroundEnemy(safeMarge) || unit.getHitPoints() >= 75) {
			goToState(MICRO_STATES.UN_BURROW, 0);
			return;
		}
	}

	private void updateInBurrow() {
		if (countDontAct == 0) {
			if (BwapiCallsManager.useTech(unit.getID(), TechTypes.Burrowing.ordinal()))
				countDontAct = 60;
		}
		
		if (unit.isBurrowed()) {
			countDontAct = 10;
			goToState(MICRO_STATES.BURROWED, 10);
		}
	}
	
	public void attackRandomHydralisk() {
		if (countDontAct > 0) return;
		countResetAction++;
		
		if (countResetAction > countMaxResetAction) {
			if (BwapiCallsManager.stop(unit.getID())) {
				countResetAction = 0;
				countDontAct = 3;
			}
		}
		else if (unit.isIdle()) {
			if (JavaBot.enemyBuildingsManager.size() > 0 && Math.random() < 0.6) {
				int iterBuilding = (int) (Math.random() * JavaBot.enemyBuildingsManager.size());
				if (iterBuilding >= JavaBot.enemyBuildingsManager.size()) iterBuilding--;
				if (iterBuilding < 0) iterBuilding = 0;
				
				EnemyBuildingManager buildingToAttack = JavaBot.enemyBuildingsManager.get(iterBuilding);
				
				if (BwapiCallsManager.attack(unit.getID(), buildingToAttack.posX, buildingToAttack.posY)) {
					countResetAction = 0;
					countDontAct = 3;
				}
			}
			else if (JavaBot.enemyUnitsAirManager.size() > 0 && Math.random() < 0.6) {
				int iterUnit = (int) (Math.random() * JavaBot.enemyUnitsAirManager.size());
				if (iterUnit >= JavaBot.enemyUnitsAirManager.size()) iterUnit--;
				if (iterUnit < 0) iterUnit = 0;
				
				EnemyUnitManager unitToAttack = JavaBot.enemyUnitsAirManager.get(iterUnit);
				
				if (BwapiCallsManager.attack(unit.getID(), unitToAttack.posX, unitToAttack.posY)) {
					countResetAction = 0;
					countDontAct = 3;
				}
			}
			else if (JavaBot.enemyUnitsManager.size() > 0 && Math.random() < 0.6) {
				int iterUnit = (int) (Math.random() * JavaBot.enemyUnitsManager.size());
				if (iterUnit >= JavaBot.enemyUnitsManager.size()) iterUnit--;
				if (iterUnit < 0) iterUnit = 0;
				
				EnemyUnitManager unitToAttack = JavaBot.enemyUnitsManager.get(iterUnit);
				
				if (BwapiCallsManager.attack(unit.getID(), unitToAttack.posX, unitToAttack.posY)) {
					countResetAction = 0;
					countDontAct = 3;
				}
			}
			else
			{
				if (Math.random() < 0.5f) {
					for (BaseManager baseM : JavaBot.bases) {
						if (baseM.state != BaseState.FRIENDLY && baseM.baseLocation.isStartLocation() && Math.random() < 0.3) {
							if (BwapiCallsManager.attack(unit.getID(), baseM.posX, baseM.posY)) {
								countResetAction = 0;
								countDontAct = 3;
							}
							break;
						}
					}
				}
				else {
					int randX = (int) (Math.random() * ((double)bwapi.getMap().getWidth() * 32));
					int randY = (int) (Math.random() * ((double)bwapi.getMap().getHeight() * 32));
					
					if (BwapiCallsManager.attack(unit.getID(), randX, randY)) {
						countResetAction = 0;
						countDontAct = 3;
					}
				}
			}
		}
	}

	public void drawDebug()
	{
		if (!JavaBot.RELEASE)
			bwapi.drawText(unit.getX() - 10, unit.getY() - 8, "Hydralisk "+ microState + ":" + countDontAct + ":" + transitionStates, false);
	}
}
