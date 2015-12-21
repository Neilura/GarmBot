package javabot.knowledge;

import javabot.JNIBWAPI;
import javabot.JavaBot;
import javabot.model.Player;

public class Repartition {
	
	public JNIBWAPI bwapi;
	public Player self;
	public Strategy strategy;
	
	public int dronesMinerals;
	public int expandMinerals;
	public int unitsMinerals;
	public int techMinerals;
	
	public int unitsGas;
	public int techGas;
	
	public Repartition() {
		dronesMinerals = 0;
		expandMinerals = 300;
		unitsMinerals = 0;
		techMinerals = 0;
		
		unitsGas = 0;
		techGas = 0;
		
		bwapi = JavaBot.bwapi;
		strategy = JavaBot.strategy;
		self = bwapi.getSelf();
	}
	
	public void update() {
		
		int playerminerals = self.getMinerals();
		
		int sumStocks = (int) (dronesMinerals + expandMinerals + unitsMinerals + techMinerals);
		
		if (playerminerals > sumStocks) {
			repartition(playerminerals - sumStocks);
		}
		
		if (playerminerals < sumStocks) {
			if (Math.random() < 0.3 && techMinerals > 0) techMinerals--;
			else if (Math.random() < 0.3 && unitsMinerals > 0) unitsMinerals--;
			else if (Math.random() < 0.1 && dronesMinerals > 0) dronesMinerals--;
			else if (Math.random() < 0.1 && expandMinerals > 0) expandMinerals--;
		}
		
		int playerGas = self.getGas();
		
		int sumStocksGas = (int) (unitsGas + techGas);
		
		if (playerGas > sumStocksGas) {
			repartitionGas(playerGas - sumStocksGas);
		}
		
		if (playerGas < sumStocksGas) {
			if (Math.random() < 0.5 && unitsGas > 0) unitsGas--;
			else if (techGas > 0) techGas--;
		}
	}
	
	private void repartitionGas(int gas) {
		
		float unitsCoef = 0;
		float techCoef = 0;
		
		if (techGas >= 300)
			techCoef = 0;
		else
			techCoef = 4;
		
		if (strategy.nothingToArmy() || (strategy.nbHydraTotal == 0 && strategy.nbQueenNestTotal == 0 && strategy.nbGreatSpireTotal == 0))
			unitsCoef = 0;
		else
			unitsCoef = 4;
		
		float totalCoef = unitsCoef + techCoef;
		
		int additionnalUnits = (int)((unitsCoef / totalCoef) * (float)gas);
		int additionnalTech = (int)((techCoef / totalCoef) * (float)gas);
		
		int totalAdd = additionnalUnits + additionnalTech;
		if (totalAdd < gas) {
			additionnalTech += gas - totalAdd;
		}
		
		unitsGas += additionnalUnits;
		techGas += additionnalTech;
	}

	private void repartition(int minerals) {
		
		float dronesCoef = 0;
		float expandCoef = 0;
		float unitsCoef = 0;
		float techCoef = 0;
		
		if (dronesMinerals >= 100 || strategy.nbDronesTotal >= strategy.idealNbDrones) {
			dronesCoef = 0;
		}
		else if (strategy.nbDronesTotal < 9)
			dronesCoef = 9;
		else if (strategy.nbDronesTotal < strategy.idealNbDrones * 0.25)
			dronesCoef = 8;
		else if (strategy.nbDronesTotal < strategy.idealNbDrones * 0.5)
			dronesCoef = 7;
		else if (strategy.nbDronesTotal < strategy.idealNbDrones * 0.75)
			dronesCoef = 6;
		
		if (strategy.nbDronesTotal < 9 || expandMinerals >= 300) {
			expandCoef = 0;
		}
		else if (strategy.nbHatcheryTotal <= 2)
			expandCoef = 8;
		else if (strategy.nbHatcheryTotal <= 6)
			expandCoef = 7;
		else if (strategy.nbHatcheryTotal <= 10)
			expandCoef = 5;
		else
			expandCoef = 4;
		
		if (strategy.nbDronesTotal < 9 || techMinerals >= 200 || strategy.nothingToBuild()) {
			techCoef = 0;
		}
		else
			techCoef = 4;
		
		if (strategy.nothingToArmy() || (strategy.nbPoolTotal == 0 && strategy.nbHydraTotal == 0 && strategy.nbQueenNestTotal == 0 && strategy.nbGreatSpireTotal == 0)) {
			unitsCoef = 0;
		}
		else if (strategy.nbHydralisksTotal + strategy.nbZerglingsTotal < (strategy.idealNbZerglings + strategy.idealNbHydras) * 0.25)
			unitsCoef = 4;
		else
			unitsCoef = 3;
		
		float totalCoef = dronesCoef + expandCoef + unitsCoef + techCoef;
		
		int additionnalDrones = (int)((dronesCoef / totalCoef) * (float)minerals);
		int additionnalExpands = (int)((expandCoef / totalCoef) * (float)minerals);
		int additionnalUnits = (int)((unitsCoef / totalCoef) * (float)minerals);
		int additionnalTech = (int)((techCoef / totalCoef) * (float)minerals);
		
		int totalAdd = additionnalDrones + additionnalExpands + additionnalUnits + additionnalTech;
		if (totalAdd < minerals) {
			if (JavaBot.strategy.nbPoolTotal > 0)
				additionnalUnits += minerals - totalAdd;
			else
				additionnalTech += minerals - totalAdd;
		}
		
		dronesMinerals += additionnalDrones;
		expandMinerals += additionnalExpands;
		unitsMinerals += additionnalUnits;
		techMinerals += additionnalTech;
	}
	
	public void reduceExpandMinerals(int amount) {
		expandMinerals -= amount;
		if (expandMinerals < 0) expandMinerals = 0;
	}
	
	public void reduceTechMinerals(int amount) {
		techMinerals -= amount;
		if (techMinerals < 0) techMinerals = 0;
	}
	
	public void reduceCreepMinerals(int amount) {
		unitsMinerals -= amount / 2;
		dronesMinerals -= amount / 2;
		if (unitsMinerals < 0) unitsMinerals = 0;
		if (dronesMinerals < 0) dronesMinerals = 0;
	}
	
	public void reduceSunkenMinerals(int amount) {
		unitsMinerals -= amount / 2;
		dronesMinerals -= amount / 2;
		if (unitsMinerals < 0) unitsMinerals = 0;
		if (dronesMinerals < 0) dronesMinerals = 0;
	}
	
	public int getTechMinerals() {
		return techMinerals;
	}
	
	public int getExpandMinerals() {
		return expandMinerals;
	}
	
	public int getUnitsMinerals() {
		return unitsMinerals;
	}
	
	public int getPoolMinerals() {
		return JavaBot.knowledge.realMinerals;
	}
	
	public int getExtractMinerals() {
		return JavaBot.knowledge.realMinerals;
	}
	
	public int getHydraDenMinerals() {
		return JavaBot.knowledge.realMinerals;
	}
	
	public int getQueenNestMinerals() {
		return JavaBot.knowledge.realMinerals;
	}
	
	public int getSpireMinerals() {
		return JavaBot.knowledge.realMinerals;
	}
	
	public int getEvoMinerals() {
		return JavaBot.knowledge.realMinerals;
	}
	
	public int getCreepMinerals() {
		return expandMinerals + techMinerals;
	}

	public void debug() {
		bwapi.drawText(220, 35, "dr", true);
		bwapi.drawText(220, 45, "ex", true);
		bwapi.drawText(220, 55, "un", true);
		bwapi.drawText(220, 65, "tc", true);
		bwapi.drawText(220, 75, "total", true);
		
		bwapi.drawText(239, 35, "" + dronesMinerals, true);
		bwapi.drawText(239, 45, "" + expandMinerals, true);
		bwapi.drawText(239, 55, "" + unitsMinerals, true);
		bwapi.drawText(239, 65, "" + techMinerals, true);
		bwapi.drawText(249, 75, "" + (dronesMinerals + expandMinerals + unitsMinerals + techMinerals), true);
		
		bwapi.drawText(269, 55, "" + unitsGas, true);
		bwapi.drawText(269, 65, "" + techGas, true);
	}

	public void reduceCombatUnits(int amount, int gas) {
		unitsMinerals -= amount;
		if (unitsMinerals < 0) {
			unitsMinerals = 0;
		}
		
		unitsGas -= gas;
		if (unitsGas < 0) unitsGas = 0;
	}
}
