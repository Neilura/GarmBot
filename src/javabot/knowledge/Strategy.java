package javabot.knowledge;

import java.awt.Point;

import javabot.JNIBWAPI;
import javabot.JavaBot;
import javabot.operators.Operators;

public class Strategy {

	public enum RaceType {
		Terran,
		Zerg,
		Protoss;
	}
	
	public RaceType enemyRace = RaceType.Terran;
	
	public int idealNbDrones = 70;
	
	public int idealNbHydras = 33;
	public int idealNbQueens = 32;
	public int idealNbZerglings = 30;
	public int idealNbMutalisks = 0;
	public int idealNbGuardians = 5;
	public int idealNbDevourers = 4;
	
	public int idealPopHydras = 0;
	public int idealPopQueens = 0;
	public int idealPopZerglings = 0;
	public int idealPopMutalisks = 0;
	public int idealPopGuardians = 0;
	public int idealPopDevourers = 0;
	
	public int nbDronesTotal;
	public int nbOverlordsTotal;
	public int nbZerglingsTotal;
	public int nbHydralisksTotal;
	public int nbQueensTotal;
	public int nbMutalisksTotal;
	public int nbGuardiansTotal;
	public int nbDevourersTotal;
	
	public int mineralsPerMinute = 0;
	public int gasPerMinute = 0;
	public boolean needMoreGas = false;
	public boolean needMoreMinerals = false;
	
	public int nbHatcheryTotal;
	public int nbPoolTotal;
	public int nbExtractorTotal;
	public int nbHydraTotal;
	public int nbQueenNestTotal;
	public int nbSpireTotal;
	public int nbHiveTotal;
	public int nbLairTotal;
	public int nbGreatSpireTotal;
	public int nbEvoChamberTotal;
	public int nbInfestedCCTotal;
	
	int oldHatcheriesProduction;
	
	int supplyUsed;
	int supplyTotal;
	int supplyFictive;

	JNIBWAPI bwapi;
	
	int iterDrones;
	int iterOverlords;
	int iterZerglings;
	int iterHydralisks;
	int iterQueens;
	int iterMutalisks;
	int iterGuardians;
	int iterDevourers;
	
	int iterHatchery;
	int iterPool;
	int iterExtractor;
	int iterHydra;
	int iterLair;
	int iterQueenNest;
	int iterSpire;
	int iterHive;
	int iterGreatSpire;
	int iterEvoChamber;
	int iterInfestedCC;
	
	public Strategy()
	{
		nbDronesTotal = 0;
		nbOverlordsTotal = 0;
		nbZerglingsTotal = 0;
		nbHydralisksTotal = 0;
		nbQueensTotal = 0;
		nbMutalisksTotal = 0;
		nbGuardiansTotal = 0;
		nbDevourersTotal = 0;
		
		nbHatcheryTotal = 0;
		nbPoolTotal = 0;
		nbExtractorTotal = 0;
		nbHydraTotal = 0;
		nbQueenNestTotal = 0;
		nbSpireTotal = 0;
		nbHiveTotal = 0;
		nbLairTotal = 0;
		nbGreatSpireTotal = 0;
		nbEvoChamberTotal = 0;
		nbInfestedCCTotal = 0;
		
		oldHatcheriesProduction = 0;
		
		supplyUsed = 0;
		supplyTotal = 0;
		supplyFictive = 0;
		
		bwapi = JavaBot.bwapi;
		iterDrones = Operators.iterDrones;
		iterOverlords = Operators.iterOverlords;
		iterZerglings = Operators.iterZerglings;
		iterHydralisks = Operators.iterHydralisks;
		iterQueens = Operators.iterQueens;
		iterMutalisks = Operators.iterMutalisks;
		iterGuardians = Operators.iterGuardians;
		iterDevourers = Operators.iterDevourers;
		
		iterHatchery = Operators.iterHatchery;
		iterPool = Operators.iterPool;
		iterExtractor = Operators.iterExtractor;
		iterHydra = Operators.iterHydra;
		iterLair = Operators.iterLair;
		iterQueenNest = Operators.iterQueenNest;
		iterSpire = Operators.iterSpire;
		iterHive = Operators.iterHive;
		iterGreatSpire = Operators.iterGreatSpire;
		iterEvoChamber = Operators.iterEvolutionChamber;
		iterInfestedCC = Operators.iterInfestedCC;
	}
	
	public void ratioGas() {
		needMoreGas = false;
		needMoreMinerals = false;
		if (JavaBot.knowledge.realMinerals < JavaBot.knowledge.realGas)
			needMoreMinerals = true;
		else if (nbHiveTotal == 0 && nbLairTotal == 0) {
			if (mineralsPerMinute * 0.14 > gasPerMinute)
				needMoreGas = true;
			if (mineralsPerMinute * 0.29 < gasPerMinute)
				needMoreMinerals = true;
		}
		else if (nbHiveTotal == 0) {
			if (mineralsPerMinute * 0.22 > gasPerMinute)
				needMoreGas = true;
			if (mineralsPerMinute * 0.35 < gasPerMinute)
				needMoreMinerals = true;
		}
		else {
			if (mineralsPerMinute * 0.28 > gasPerMinute)
				needMoreGas = true;
			if (mineralsPerMinute * 0.41 < gasPerMinute)
				needMoreMinerals = true;
		}
	}
	
	public void update(GlobalKnowledge knowledge) {
		
		nbDronesTotal = knowledge.currentUnits[iterDrones] + knowledge.warpingUnits[iterDrones];
		nbOverlordsTotal = knowledge.currentUnits[iterOverlords] + knowledge.warpingUnits[iterOverlords];
		nbZerglingsTotal = knowledge.currentUnits[iterZerglings] + 2*knowledge.warpingUnits[iterZerglings];
		nbHydralisksTotal = knowledge.currentUnits[iterHydralisks] + knowledge.warpingUnits[iterHydralisks];
		nbQueensTotal = knowledge.currentUnits[iterQueens] + knowledge.warpingUnits[iterQueens];
		nbMutalisksTotal = knowledge.currentUnits[iterMutalisks] + knowledge.warpingUnits[iterMutalisks];
		nbGuardiansTotal = knowledge.currentUnits[iterGuardians] + knowledge.warpingUnits[iterGuardians];
		nbDevourersTotal = knowledge.currentUnits[iterDevourers] + knowledge.warpingUnits[iterDevourers];
		
		nbHatcheryTotal = knowledge.currentBuildings[iterHatchery] + knowledge.warpingBuildings[iterHatchery];
		nbPoolTotal = knowledge.currentBuildings[iterPool] + knowledge.warpingBuildings[iterPool];
		nbExtractorTotal = knowledge.currentBuildings[iterExtractor] + knowledge.warpingBuildings[iterExtractor];
		nbHydraTotal = knowledge.currentBuildings[iterHydra] + knowledge.warpingBuildings[iterHydra];
		nbQueenNestTotal = knowledge.currentBuildings[iterQueenNest] + knowledge.warpingBuildings[iterQueenNest];
		nbSpireTotal = knowledge.currentBuildings[iterSpire] + knowledge.warpingBuildings[iterSpire];
		nbHiveTotal = knowledge.currentBuildings[iterHive] + knowledge.warpingBuildings[iterHive];
		nbLairTotal = knowledge.currentBuildings[iterLair] + knowledge.warpingBuildings[iterLair];
		nbGreatSpireTotal = knowledge.currentBuildings[iterGreatSpire] + knowledge.warpingBuildings[iterGreatSpire];
		nbEvoChamberTotal = knowledge.currentBuildings[iterEvoChamber] + knowledge.warpingBuildings[iterEvoChamber];
		nbInfestedCCTotal = knowledge.currentBuildings[iterInfestedCC];
		
		supplyUsed = knowledge.supplyUsed;
		supplyTotal = knowledge.supplyMax;
		supplyFictive = 1 * nbHatcheryTotal + 8 * nbOverlordsTotal;
		supplyFictive *= 2;
		
		createDronesAndOverlords(knowledge);
		techBuildings(knowledge);
		army(knowledge);
		expandAndMacro(knowledge);
	}

	public void createDronesAndOverlords(GlobalKnowledge knowledge)
	{
		if (nbPoolTotal > 0) {
			knowledge.requestedBuildings[iterPool] = 0;
			if (nbDronesTotal < idealNbDrones && supplyUsed < supplyTotal - 1)
			{
				knowledge.requestedUnits[iterDrones] = 1;
			}
			else
				knowledge.requestedUnits[iterDrones] = 0;
			if (supplyFictive - supplyUsed <= supplyTotal / 4)
			{
				knowledge.requestedUnits[iterOverlords] = 1;
			}
			else
				knowledge.requestedUnits[iterOverlords] = 0;
		}
		else
		{
			if (nbDronesTotal < 9)
			{
				if (supplyFictive <= supplyUsed) {
					knowledge.requestedUnits[iterDrones] = 0;
					knowledge.requestedUnits[iterOverlords] = 1;
				}
				else
					knowledge.requestedUnits[iterDrones] = 1;
			}
			else {
				knowledge.requestedUnits[iterDrones] = 0;
				if (nbPoolTotal == 0 && (nbHatcheryTotal >= 4 || JavaBot.currFrame >= 3800 || (enemyRace == RaceType.Zerg && nbHatcheryTotal >= 2)))
					knowledge.requestedBuildings[iterPool] = 1;
				
				if (JavaBot.knowledge.realMinerals >= 400 && nbOverlordsTotal <= 1)
					knowledge.requestedUnits[iterOverlords] = 1;
				else
					knowledge.requestedUnits[iterOverlords] = 0;
			}
		}
		
		if (nbPoolTotal > 0) {
			knowledge.requestedBuildings[iterPool] = 0;
			knowledge.timerTryPool = 0;
		}
	}
	
	public void army(GlobalKnowledge knowledge)
	{
		if (supplyUsed > 9 && JavaBot.knowledge.requestedUnits[iterOverlords] == 0 && supplyUsed < supplyTotal - 1)
		{
			if (JavaBot.knowledge.currentBuildings[iterPool] >= 1 && nbZerglingsTotal < idealNbZerglings)
				knowledge.requestedUnits[iterZerglings] = 1;
			else
				knowledge.requestedUnits[iterZerglings] = 0;
			
			if (JavaBot.knowledge.currentBuildings[iterHydra] >= 1 && nbHydralisksTotal < idealNbHydras) {
				knowledge.requestedUnits[iterHydralisks] = 1;
			}
			else {
				knowledge.requestedUnits[iterHydralisks] = 0;
			}
			
			if (JavaBot.knowledge.currentBuildings[iterQueenNest] >= 1 && nbQueensTotal < idealNbQueens)
				knowledge.requestedUnits[iterQueens] = 1;
			else
				knowledge.requestedUnits[iterQueens] = 0;
			
			
			if (JavaBot.strategy.nbGreatSpireTotal >= 1
					&& nbGuardiansTotal + nbMutalisksTotal + nbDevourersTotal < idealNbGuardians + idealNbMutalisks + idealNbDevourers)
				knowledge.requestedUnits[iterMutalisks] = 1;
			else
				knowledge.requestedUnits[iterMutalisks] = 0;
			
			if (JavaBot.knowledge.currentBuildings[iterGreatSpire] >= 1 && nbGuardiansTotal < idealNbGuardians)
				knowledge.requestedUnits[iterGuardians] = 1;
			else
				knowledge.requestedUnits[iterGuardians] = 0;
			
			if (JavaBot.knowledge.currentBuildings[iterGreatSpire] >= 1 && nbDevourersTotal < idealNbDevourers)
				knowledge.requestedUnits[iterDevourers] = 1;
			else
				knowledge.requestedUnits[iterDevourers] = 0;
		}
		else {
			knowledge.requestedUnits[iterZerglings] = 0;
			knowledge.requestedUnits[iterHydralisks] = 0;
			knowledge.requestedUnits[iterQueens] = 0;
			knowledge.requestedUnits[iterMutalisks] = 0;
		}
	}
	
	public void techBuildings(GlobalKnowledge knowledge) {
		if (knowledge.currentBuildings[iterSpire] > 0 && knowledge.currentBuildings[iterHive] > 0 && nbGreatSpireTotal == 0) {
			knowledge.requestedBuildings[iterGreatSpire] = 1;
		}
		else
			knowledge.requestedBuildings[iterGreatSpire] = 0;
		
		if (nbGreatSpireTotal > 0 && nbEvoChamberTotal == 0)
			knowledge.requestedBuildings[iterEvoChamber] = 1;
		else
			knowledge.requestedBuildings[iterEvoChamber] = 0;
	}
	
	public void expandAndMacro(GlobalKnowledge knowledge)
	{
		if (nbDronesTotal >= 8)
		{
			if (knowledge.timerTryExpand == 0)
			{
				knowledge.requestedBuildings[iterHatchery] = 1;
			}
			else
				knowledge.requestedBuildings[iterHatchery] = 0;
			
			/*if (knowledge.realMinerals >= 700 && knowledge.timerTryMacro == 0)
			{
				knowledge.requestMacro = true;
			}
			else
				knowledge.requestMacro = false;*/
			
			if (knowledge.warpingBuildings[iterHatchery] > oldHatcheriesProduction) {
				if (knowledge.timerTryExpand > 0) {
					JavaBot.knowledge.timerTryExpand = 10;
					/*if (bwapi.getFrameCount() < 15000)
						JavaBot.knowledge.timerTryExpand = 600;
					if (bwapi.getFrameCount() < 1000)
						JavaBot.knowledge.timerTryExpand = 1600;
					if (bwapi.getFrameCount() < 3000)
						JavaBot.knowledge.timerTryExpand = 2100;*/
				}
				knowledge.timerTryMacro = 0;
			}
			oldHatcheriesProduction = knowledge.warpingBuildings[iterHatchery];
		}
	}
	
	public void adaptArmyComposition() {
		
		idealNbHydras = 34; //34 + 62 + 16 + 10 + 12 + 6 = 140
		idealNbQueens = 31;
		idealNbZerglings = 32;
		idealNbMutalisks = 5;
		idealNbGuardians = 6;
		idealNbDevourers = 3;
		idealNbDrones = 60;
		
		switch(enemyRace) {
			case Terran :
				idealNbHydras = 26; //26 + 70 + 12 + 8 + 16 + 8 = 140
				idealNbQueens = 35;
				idealNbZerglings = 24;
				idealNbMutalisks = 4;
				idealNbGuardians = 8;
				idealNbDevourers = 4;
				idealNbDrones = 60;
				if (!JavaBot.RELEASE)
					System.out.println("Adapted to terran !");
				break;
			case Zerg : 
				idealNbHydras = 19; //19 + 16 + 21 + 48 + 6 + 30 = 140
				idealNbQueens = 8;
				idealNbZerglings = 42;
				idealNbMutalisks = 24;
				idealNbGuardians = 3;
				idealNbDevourers = 15;
				idealNbDrones = 60;
				if (!JavaBot.RELEASE)
					System.out.println("Adapted to zerg !");
				break;
			case Protoss : 
				idealNbHydras = 27; //27 + 70 + 9 + 24 + 6 + 4 = 140
				idealNbQueens = 35;
				idealNbZerglings = 18;
				idealNbMutalisks = 12;
				idealNbGuardians = 3;
				idealNbDevourers = 2;
				idealNbDrones = 60;
				if (!JavaBot.RELEASE)
					System.out.println("Adapted to protoss !");
				break;
		}
	}
	
	public void build(GlobalKnowledge knowledge)
	{
	}
	
	public void tech(GlobalKnowledge knowledge)
	{
	}
	
	public boolean nothingToBuild() {
		return nbGreatSpireTotal > 0 && nbPoolTotal > 0 && nbQueenNestTotal > 0 && nbHydraTotal > 0 && nbHiveTotal > 0 && nbEvoChamberTotal > 0;
	}
	
	public boolean nothingToArmy() {
		return nbZerglingsTotal >= idealNbZerglings &&
				nbHydralisksTotal >= idealNbHydras &&
				nbQueensTotal >= idealNbQueens &&
				nbGuardiansTotal >= idealNbGuardians &&
				nbDevourersTotal >= idealNbDevourers;
	}

	public void debug() {
		int x = 320;
		int y = 5;
		bwapi.drawText(new Point(x, y), "Z = " + idealPopZerglings, true);
		y += 10;
		bwapi.drawText(new Point(x, y), "H = " + idealPopHydras, true);
		y += 10;
		bwapi.drawText(new Point(x, y), "Q = " + idealPopQueens, true);
		y += 10;
		bwapi.drawText(new Point(x, y), "M = " + idealPopMutalisks, true);
		y += 10;
		bwapi.drawText(new Point(x, y), "G = " + idealPopGuardians, true);
		y += 10;
		bwapi.drawText(new Point(x, y), "D = " + idealPopDevourers, true);
		y += 12;
		
		bwapi.drawText(new Point(x, y), "Total = " + (idealPopZerglings + idealPopHydras + idealPopQueens + idealPopMutalisks + idealPopGuardians + idealPopDevourers), true);
		y += 13;
		
		bwapi.drawText(new Point(x, y), "Reset = " + JavaBot.armyComposition.coefficient, true);
		
		y += 11;
		bwapi.drawText(new Point(x, y), "M/m = " + mineralsPerMinute + " " + needMoreMinerals, true);
		y += 10;
		bwapi.drawText(new Point(x, y), "G/m = " + gasPerMinute + " " + needMoreGas, true);
	}
}
