package javabot.managers;

import javabot.JavaBot;
import javabot.model.Unit;
import javabot.operators.Operators;
import javabot.util.BWColor;

public class DevourerManager extends UnitManager{

	public boolean occupied = false;
	
	public DevourerManager(Unit u) {
		super(u);
		explore = true;
	}
	
	public void update()
	{
		super.update();
		
		safeMarge = (250 - hp) / 2;
		
		if (unit.isIdle() && occupied) occupied = false;
		
		ControlTower.devourers.add(this);
		
		if (unit.isIdle() && countDontAct == 0) {
			
			if (Math.random() < 0.7 && JavaBot.enemyBuildingsAirTargetManager.size() > 0) {
				int iterUnit = (int) (Math.random() * JavaBot.enemyBuildingsAirTargetManager.size());
				if (iterUnit >= JavaBot.enemyBuildingsAirTargetManager.size()) iterUnit--;
				if (iterUnit < 0) iterUnit = 0;
				
				EnemyBuildingManager unitToAttack = JavaBot.enemyBuildingsAirTargetManager.get(iterUnit);
				
				if (BwapiCallsManager.attack(unit.getID(), unitToAttack.posX, unitToAttack.posY))
					countDontAct = 1;
			}
			else if (Math.random() < 0.9 && JavaBot.enemyUnitsAirTargetManager.size() > 0) {
				int iterUnit = (int) (Math.random() * JavaBot.enemyUnitsAirTargetManager.size());
				if (iterUnit >= JavaBot.enemyUnitsAirTargetManager.size()) iterUnit--;
				if (iterUnit < 0) iterUnit = 0;
				
				EnemyUnitManager unitToAttack = JavaBot.enemyUnitsAirTargetManager.get(iterUnit);
				
				if (BwapiCallsManager.attack(unit.getID(), unitToAttack.posX, unitToAttack.posY))
					countDontAct = 1;
			}
			else if (Math.random() < 0.5 && ControlTower.posX != -1 && Operators.distance(posX, posY, ControlTower.posX, ControlTower.posY) > ControlTower.radius + 100) {
				if (BwapiCallsManager.attack(unit.getID(), ControlTower.posX + (int)(Math.random() * 100) - 50, ControlTower.posY + (int)(Math.random() * 100) - 50))
					countDontAct = 1;
			}
			else {
				int randX = (int) (Math.random() * ((double)bwapi.getMap().getWidth() * 32));
				int randY = (int) (Math.random() * ((double)bwapi.getMap().getHeight() * 32));
				
				if (BwapiCallsManager.attack(unit.getID(), randX, randY))
					countDontAct = 1;
			}
		}
		
		if (countDontAct == 0)
			dodgeEnemies();
	}
	
	private void dodgeEnemies() {
		
		for (EnemyUnitManager enemy : JavaBot.enemyUnitsAirManager) {
			if (!enemy.isAir && Operators.distance(posX,  posY, enemy.posX, enemy.posY) < enemy.range + safeMarge) {
				int dx = posX - enemy.posX;
				int dy = posY - enemy.posY;
				
				if (dx > 60) dx = 60;
				if (dx < -60) dx = -60;
				if (dy > 60) dy = 60;
				if (dy < -60) dy = -60;
				
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
				
				if (dx > 60) dx = 60;
				if (dx < -60) dx = -60;
				if (dy > 60) dy = 60;
				if (dy < -60) dy = -60;
				
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
			bwapi.drawText(unit.getX() - 10, unit.getY() - 8, "Devourer", false);
			if (unit.isMoving())
				bwapi.drawLine(posX, posY, unit.getTargetX(), unit.getTargetY(), BWColor.GREEN, false);
		}
	}
}