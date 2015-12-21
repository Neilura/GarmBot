package javabot.managers;

import javabot.JavaBot;
import javabot.knowledge.Tech;
import javabot.managers.BaseManager.BaseState;
import javabot.model.Unit;
import javabot.types.TechType.TechTypes;

public class ZerglingManager extends UnitManager {

	public enum MICRO_STATES {
		IDLE,
		ATTACKING,
		IN_BURROW,
		BURROWED,
		UN_BURROW
	}
	
	int transitionStates = 0;
	MICRO_STATES microState;
	
	public ZerglingManager(Unit u) {
		super(u);
		explore = true;
		microState = MICRO_STATES.ATTACKING;
	}
	
	public void update()
	{
		super.update();
		
		safeMarge = (35 - hp) * 2;
		
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
		if (((JavaBot.currFrame < 20000 && hp >= 33) || hp >= 10) || Tech.burrow != 2 || nearbyDetector()) return false;
		
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
			attackRandomZergling();
		}
	}

	private void updateUnBurrow() {
		if (countDontAct == 0) {
			if (BwapiCallsManager.useTech(unit.getID(), TechTypes.Burrowing.ordinal()))
				countDontAct = 40;
		}
		
		if (!unit.isBurrowed()) {
			goToState(MICRO_STATES.ATTACKING, 10);
			countDontAct = 0;
		}
	}

	private void updateBurrowed() {
		if (!unit.isBurrowed()) {
			goToState(MICRO_STATES.ATTACKING, 0);
		}
		else if (nearbyDetector() || !nearbyGroundEnemy(safeMarge) || unit.getHitPoints() >= 33) {
			goToState(MICRO_STATES.UN_BURROW, 0);
			return;
		}
	}

	private void updateInBurrow() {
		if (countDontAct == 0) {
			if (BwapiCallsManager.useTech(unit.getID(), TechTypes.Burrowing.ordinal()))
				countDontAct = 80;
		}
		
		if (unit.isBurrowed()) {
			countDontAct = 30;
			goToState(MICRO_STATES.BURROWED, 30);
		}
	}
	
	public void attackRandomZergling() {
		if (countDontAct > 0) return;
		countResetAction++;
		
		if (countResetAction > countMaxResetAction) {
			if (BwapiCallsManager.stop(unit.getID())) {
				countResetAction = 0;
				countDontAct = 3;
			}
		}
		else if (unit.isIdle()) {
			if (JavaBot.enemyBuildingsManager.size() > 0 && Math.random() < 0.7) {
				int iterBuilding = (int) (Math.random() * JavaBot.enemyBuildingsManager.size());
				if (iterBuilding >= JavaBot.enemyBuildingsManager.size()) iterBuilding--;
				if (iterBuilding < 0) iterBuilding = 0;
				
				EnemyBuildingManager buildingToAttack = JavaBot.enemyBuildingsManager.get(iterBuilding);
				
				if (BwapiCallsManager.attack(unit.getID(), buildingToAttack.posX, buildingToAttack.posY)) {
					countResetAction = 0;
					countDontAct = 3;
				}
			}
			else if (Math.random() < 0.6 && JavaBot.enemyUnitsGroundManager.size() > 0) {
				int iterUnit = (int) (Math.random() * JavaBot.enemyUnitsGroundManager.size());
				if (iterUnit < 0) iterUnit = 0;
				if (iterUnit >= JavaBot.enemyUnitsGroundManager.size()) iterUnit--;
				EnemyUnitManager unitToAttack = JavaBot.enemyUnitsGroundManager.get(iterUnit);
				
				BwapiCallsManager.attack(unit.getID(), unitToAttack.posX, unitToAttack.posY);
			}
			else
			{
				if (Math.random() < 0.7f) {
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
			bwapi.drawText(unit.getX() - 10, unit.getY() - 8, "Zergling " + microState, false);
	}
}
