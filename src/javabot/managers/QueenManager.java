package javabot.managers;

import javabot.JavaBot;
import javabot.model.Unit;
import javabot.operators.Operators;
import javabot.types.TechType.TechTypes;
import javabot.util.BWColor;

public class QueenManager extends UnitManager {

	int currentEnergy = 0;
	EnemyUnitManager bestTarget;
	NeutralUnitManager bestParasiteTarget;
	int locationRadius = 400;
	int oldEnergy = 0;
	
	boolean occupied = false;
	boolean dodging = false;
	
	public QueenManager(Unit u) {
		super(u);
		bestTarget = null;
		oldEnergy = 0;
	}
	
	public void update()
	{
		super.update();
		
		safeMarge = (120 - hp) * 2;
		
		currentEnergy = unit.getEnergy() - 1;
		bestTarget = null;
		bestParasiteTarget = null;
		int targetLevel = 0;
		
		if (countDontAct > 0 && currentEnergy < oldEnergy) {
			countDontAct = 1;
		}
		
		if (currentEnergy >= 150 && !occupied) {
			for (EnemyUnitManager enemy : JavaBot.enemyUnitsBroodlingManager) {
				if (Operators.distance(unit, enemy.unit) < locationRadius) {
					if (enemy.broodlingLevel > 0 && enemy.isBroodlinged == 0 && enemy.broodlingLevel > targetLevel) {
						bestTarget = enemy;
						targetLevel = enemy.broodlingLevel;
					}
				}
			}
		}
		if (bestTarget == null && currentEnergy >= 75 && !occupied) {
			for (NeutralUnitManager u : JavaBot.neutralUnitsMap.values()) {
				if (Operators.distance(unit, u.unit) < locationRadius) {
					if (!u.parasited && u.timeParasite == 0) {
						bestParasiteTarget = u;
						u.timeParasite = 25;
						break;
					}
				}
			}
		}
		
		oldEnergy = currentEnergy;
		
		if (bestTarget != null && countDontAct == 0) {
			if (BwapiCallsManager.useTech(unit.getID(), TechTypes.Spawn_Broodlings.ordinal(), bestTarget.unit.getID())) {
				bestTarget.isBroodlinged = 100;
				countDontAct = 30;
			}
		}
		
		if (bestParasiteTarget != null && countDontAct == 0) {
			if (BwapiCallsManager.useTech(unit.getID(), TechTypes.Parasite.ordinal(), bestParasiteTarget.unit.getID())) {
				countDontAct = 20;
				bestParasiteTarget.timeParasite = 35;
			}
		}
		
		if (countDontAct <= 1)
			dodgeEnemies();
		
		if (countDontAct == 0 && bestParasiteTarget == null && currentEnergy < 150) {
			for (EnemyCommandCenterManager commandCenter : JavaBot.enemyCommandCenters) {
				if (!commandCenter.targetable) continue;
				int distToCommandCenter = Operators.distance(unit, commandCenter.unit);
				if (distToCommandCenter < 1800) {
					if (distToCommandCenter < 300) {
						BwapiCallsManager.useTech(unit.getID(), TechTypes.Infestation.ordinal(), commandCenter.unit.getID());
					}
					else {
						BwapiCallsManager.move(unit.getID(), commandCenter.posX, commandCenter.posY);
					}
					countDontAct = 20;
					break;
				}
			}
		}
		
		if (countDontAct == 0 && unit.isIdle()) {
			if (Math.random() < 0.85f && JavaBot.enemyUnitsBroodlingManager.size() > 0) {
				int iterToGet = (int) (Math.random() * JavaBot.enemyUnitsBroodlingManager.size());
				if (iterToGet < 0) iterToGet = 0;
				if (iterToGet >= JavaBot.enemyUnitsBroodlingManager.size()) iterToGet = JavaBot.enemyUnitsBroodlingManager.size() - 1;
				
				EnemyUnitManager enemy = JavaBot.enemyUnitsBroodlingManager.get(iterToGet);
				if (BwapiCallsManager.move(unit.getID(), enemy.posX, enemy.posY))
					countDontAct = 1;
			}
			else if (Math.random() < 0.5f && JavaBot.enemyBuildingsManager.size() > 0) {
				
				int iterToGet = (int) (Math.random() * JavaBot.enemyBuildingsManager.size());
				if (iterToGet < 0) iterToGet = 0;
				if (iterToGet >= JavaBot.enemyBuildingsManager.size()) iterToGet = JavaBot.enemyBuildingsManager.size() - 1;
				
				EnemyBuildingManager enemy = JavaBot.enemyBuildingsManager.get(iterToGet);
				if (BwapiCallsManager.move(unit.getID(), enemy.posX, enemy.posY))
					countDontAct = 1;
			}
			else {
				int randX = (int) (Math.random() * ((double)bwapi.getMap().getWidth() * 32));
				int randY = (int) (Math.random() * ((double)bwapi.getMap().getHeight() * 32));
				
				if (BwapiCallsManager.move(unit.getID(), randX, randY))
					countDontAct = 1;
			}
		}
	}
	
	public void dodgeEnemies() {
		
		for (EnemyUnitManager enemy : JavaBot.enemyUnitsAirManager) {
			if ((enemy.broodlingLevel == 0 || currentEnergy < 150) && Operators.distance(posX,  posY, enemy.posX, enemy.posY) < enemy.range + safeMarge + 60) {
				int dx = posX - enemy.posX;
				int dy = posY - enemy.posY;
				
				if (dx > 100) dx = 100;
				if (dx < -100) dx = -100;
				if (dy > 100) dy = 100;
				if (dy < -100) dy = -100;
				
				int posXtoGo = posX + dx;
				int posYtoGo = posY + dy;
				
				if (BwapiCallsManager.move(unit.getID(), posXtoGo, posYtoGo)) {
					countDontAct = 8;
				}
				break;
			}
		}
		
		if (countDontAct == 0)
		for (EnemyBuildingManager enemy : JavaBot.enemyBuildingsAirManager) {
			if (Operators.distance(posX,  posY, enemy.posX, enemy.posY) < enemy.range + safeMarge + 50) {
				int dx = posX - enemy.posX;
				int dy = posY - enemy.posY;
				
				if (dx > 80) dx = 80;
				if (dx < -80) dx = -80;
				if (dy > 80) dy = 80;
				if (dy < -80) dy = -80;
				
				int posXtoGo = posX + dx;
				int posYtoGo = posY + dy;
				
				if (BwapiCallsManager.move(unit.getID(), posXtoGo, posYtoGo)) {
					countDontAct = 8;
				}
				break;
			}
		}
	}
	
	public void drawDebug()
	{
		bwapi.drawText(unit.getX() - 10, unit.getY() - 8, "Queen:"+currentEnergy+":"+""+countDontAct, false);
		if (bestParasiteTarget != null) {
			bwapi.drawLine(posX, posY, bestParasiteTarget.unit.getX(), bestParasiteTarget.unit.getY(), BWColor.YELLOW, false);
		}
		if (unit.isMoving())
			bwapi.drawLine(posX, posY, unit.getTargetX(), unit.getTargetY(), BWColor.GREEN, false);
	}
}
