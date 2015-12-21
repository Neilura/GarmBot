package javabot.managers;

import javabot.JavaBot;
import javabot.model.Unit;
import javabot.operators.Operators;
import javabot.types.UnitType.UnitTypes;
import javabot.util.BWColor;

public class MutaliskManager extends UnitManager{

	public boolean occupied = false;
	boolean dodging = false;
	
	public MutaliskManager(Unit u) {
		super(u);
		explore = true;
	}
	
	public void update()
	{
		super.update();
		
		safeMarge = (120 - hp) * 3;
		
		if (unit.isIdle() && occupied) occupied = false;
		
		ControlTower.mutalisks.add(this);
		
		if (JavaBot.knowledge.requestedUnits[Operators.iterGuardians] > 0
				&& (JavaBot.knowledge.requestedUnits[Operators.iterDevourers] == 0 || JavaBot.strategy.nbGuardiansTotal < JavaBot.strategy.nbDevourersTotal * 3)
				&& JavaBot.knowledge.timerMorphMutalisk == 0
				&& JavaBot.strategy.nbGuardiansTotal < JavaBot.strategy.idealNbGuardians) {
			if (JavaBot.knowledge.realMinerals >= 50
				&& JavaBot.knowledge.realGas >= 100) {
				if (BwapiCallsManager.morph(unit.getID(), UnitTypes.Zerg_Guardian.ordinal())) {
					JavaBot.knowledge.timerMorphMutalisk = 5;
					countDontAct = 5;
					JavaBot.repartition.reduceCombatUnits(50, 100);
				}
			}
			else {
				JavaBot.requestsManager.requestRessources(50, 100);
			}
		}
		
		if (countDontAct == 0 && JavaBot.knowledge.requestedUnits[Operators.iterDevourers] > 0
				&& JavaBot.knowledge.timerMorphMutalisk == 0
				&& JavaBot.strategy.nbDevourersTotal < JavaBot.strategy.idealNbDevourers){
			
			if (JavaBot.knowledge.realMinerals >= 150
				&& JavaBot.knowledge.realGas >= 50) {
				if (BwapiCallsManager.morph(unit.getID(), UnitTypes.Zerg_Devourer.ordinal())) {
					JavaBot.knowledge.timerMorphMutalisk = 5;
					countDontAct = 5;
					JavaBot.repartition.reduceCombatUnits(150, 50);
				}
			}
			else {
				JavaBot.requestsManager.requestRessources(150, 50);
			}
		}
		if (unit.isIdle() && countDontAct == 0)
			attackRandomMutalisk();
		
		if (countDontAct <= 1)
			dodgeEnemies();
	}
	
	private void attackRandomMutalisk() {
		if (Math.random() < 0.6 && JavaBot.enemyBuildingsManager.size() > 0) {
			int iterUnit = (int) (Math.random() * JavaBot.enemyBuildingsManager.size());
			if (iterUnit >= JavaBot.enemyBuildingsManager.size()) iterUnit--;
			if (iterUnit < 0) iterUnit = 0;
			
			EnemyBuildingManager unitToAttack = JavaBot.enemyBuildingsManager.get(iterUnit);
			
			if (BwapiCallsManager.attack(unit.getID(), unitToAttack.posX, unitToAttack.posY))
				countDontAct = 1;
		}
		else if (Math.random() < 0.6 && JavaBot.enemyUnitsManager.size() > 0) {
			int iterUnit = (int) (Math.random() * JavaBot.enemyUnitsManager.size());
			if (iterUnit >= JavaBot.enemyUnitsManager.size()) iterUnit--;
			if (iterUnit < 0) iterUnit = 0;
			
			EnemyUnitManager unitToAttack = JavaBot.enemyUnitsManager.get(iterUnit);
			
			if (BwapiCallsManager.attack(unit.getID(), unitToAttack.posX, unitToAttack.posY))
				countDontAct = 1;
		}
		else if (Math.random() < 0.6 && ControlTower.posX != -1 && Operators.distance(posX, posY, ControlTower.posX, ControlTower.posY) > ControlTower.radius + 100) {
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

	public void dodgeEnemies() {
		
		for (EnemyUnitManager enemy : JavaBot.enemyUnitsAirManager) {
			if (Operators.distance(posX,  posY, enemy.posX, enemy.posY) < enemy.range + 2*safeMarge - 300) {
				int dx = posX - enemy.posX;
				int dy = posY - enemy.posY;
				
				if (dx > 110) dx = 110;
				if (dx < -110) dx = -110;
				if (dy > 110) dy = 110;
				if (dy < -110) dy = -110;
				
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
				
				if (dx > 70) dx = 70;
				if (dx < -70) dx = -70;
				if (dy > 70) dy = 70;
				if (dy < -70) dy = -70;
				
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
		if (!JavaBot.RELEASE)
			bwapi.drawText(unit.getX() - 10, unit.getY() - 8, "Mutalisk:"+countDontAct, false);
			if (unit.isMoving())
				bwapi.drawLine(posX, posY, unit.getTargetX(), unit.getTargetY(), BWColor.GREEN, false);
	}
}
