package javabot.managers;

import javabot.JavaBot;
import javabot.model.Unit;
import javabot.operators.Operators;
import javabot.util.BWColor;

public class GuardianManager extends UnitManager{

	public enum Guardian_STATES {
		IDLE,
		ATTACK,
		STOP,
		RETREAT
	};
	
	public int timeRecharge = 0;
	public boolean occupied = false;
	public boolean explore = false;
	public int locationRadius = 423;
	public int dodgeRadius = 295;
	
	public int timerIdle = 0;
	
	Guardian_STATES state = Guardian_STATES.IDLE;
	
	public GuardianManager(Unit u) {
		super(u);
		explore = false;
	}
	
	public void update()
	{
		super.update();
		
		safeMarge = (150 - hp) / 2;
		
		ControlTower.guardians.add(this);
		
		timeRecharge = unit.getGroundWeaponCooldown();
		
		if (unit.isIdle())
			timerIdle++;
		else
			timerIdle = 0;
		
		if (timerIdle >= 8) {
			timerIdle = 0;
			
			if (Math.random() < 0.4 && ControlTower.posX != -1 && Operators.distance(posX, posY, ControlTower.posX, ControlTower.posY) > ControlTower.radius) {
				BwapiCallsManager.attack(unit.getID(), ControlTower.posX, ControlTower.posY);
			}
			else if (Math.random() < 0.6 && JavaBot.enemyUnitsGroundManager.size() > 0) {
				int iterUnit = (int) (Math.random() * JavaBot.enemyUnitsGroundManager.size());
				if (iterUnit < 0) iterUnit = 0;
				if (iterUnit >= JavaBot.enemyUnitsGroundManager.size()) iterUnit--;
				EnemyUnitManager unitToAttack = JavaBot.enemyUnitsGroundManager.get(iterUnit);
				
				BwapiCallsManager.attack(unit.getID(), unitToAttack.posX, unitToAttack.posY);
			}
			else if (Math.random() < 0.95 && JavaBot.enemyBuildingsManager.size() > 0) {
				int iterBuilding = (int) (Math.random() * JavaBot.enemyBuildingsManager.size());
				if (iterBuilding < 0) iterBuilding = 0;
				if (iterBuilding >= JavaBot.enemyBuildingsManager.size()) iterBuilding--;
				EnemyBuildingManager buildingToAttack = JavaBot.enemyBuildingsManager.get(iterBuilding);
				
				BwapiCallsManager.attack(unit.getID(), buildingToAttack.posX, buildingToAttack.posY);
			}
			else {
				int randX = (int) (Math.random() * ((double)bwapi.getMap().getWidth() * 32));
				int randY = (int) (Math.random() * ((double)bwapi.getMap().getHeight() * 32));
				
				BwapiCallsManager.attack(unit.getID(), randX, randY);
			}
		}
		
		if (countDontAct == 0)
			dodgeEnemies();
	}
	
	private void dodgeEnemies() {
		
		for (EnemyUnitManager enemy : JavaBot.enemyUnitsAirManager) {
			if ((enemy.isAir || timeRecharge != 0) && Operators.distance(posX,  posY, enemy.posX, enemy.posY) < enemy.range + safeMarge) {
				int dx = posX - enemy.posX;
				int dy = posY - enemy.posY;
				
				if (dx > 50) dx = 50;
				if (dx < -50) dx = -50;
				if (dy > 50) dy = 50;
				if (dy < -50) dy = -50;
				
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
			if (Operators.distance(posX,  posY, enemy.posX, enemy.posY) < enemy.range + safeMarge) {
				int dx = posX - enemy.posX;
				int dy = posY - enemy.posY;
				
				if (dx > 50) dx = 50;
				if (dx < -50) dx = -50;
				if (dy > 50) dy = 50;
				if (dy < -50) dy = -50;
				
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
		if (!JavaBot.RELEASE) {
			bwapi.drawText(posX - 10, posY - 8, "Guardian " + countDontAct + " " + timeRecharge, false);
			if (unit.isMoving())
				bwapi.drawLine(posX, posY, unit.getTargetX(), unit.getTargetY(), BWColor.GREEN, false);
		}
	}
}