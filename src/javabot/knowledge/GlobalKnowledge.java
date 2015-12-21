package javabot.knowledge;

import java.awt.Point;

import javabot.JNIBWAPI;
import javabot.JavaBot;
import javabot.model.Unit;
import javabot.operators.Operators;
import javabot.types.UnitType.UnitTypes;

public class GlobalKnowledge {
	
	String[] unitNames;
	public int[] currentUnits;
	
	public int[] warpingUnits;
	public int[] requestedUnits;
	
	String[] buildingNames;
	public int[] currentBuildings;
	
	public int[] warpingBuildings;
	public int[] requestedBuildings;
	
	public int realMinerals;
	public int realGas;
	public int minerals;
	public int gas;
	public int supplyUsed;
	public int supplyMax;
	
	public int timerTryExpand;
	public int timerTryMacro;
	public int timerTryPool;
	public int timerTryExtractor;
	public int timerTryHydra;
	public int timerTryQueenNest;
	public int timerTrySpire;
	public int timerMorphMutalisk;
	public int timerTryEvo;
	
	//public boolean requestMacro;
	public boolean okExtractors;
	
	int nbUnits = 10;
	int nbBuildings = 14;
	
	JNIBWAPI bwapi;
	
	public GlobalKnowledge()
	{
		bwapi = JavaBot.bwapi;
		
		unitNames = new String[nbUnits];
		currentUnits = new int[nbUnits];
		
		warpingUnits = new int[nbUnits];
		requestedUnits = new int[nbUnits];
		
		buildingNames = new String[nbBuildings];
		currentBuildings = new int[nbBuildings];
		
		warpingBuildings = new int[nbBuildings];
		requestedBuildings = new int[nbBuildings];
		
		
		for (int i = 0; i < nbUnits; i++)
		{
			unitNames[i] = Operators.stringFromType(Operators.unitTypeFromIter(i));
			currentUnits[i] = 0;
			warpingUnits[i] = 0;
			requestedUnits[i] = 0;
		}
		
		for (int i = 0; i < nbBuildings; i++)
		{
			buildingNames[i] = Operators.stringFromType(Operators.buildingTypeFromIter(i));
			currentBuildings[i] = 0;
			warpingBuildings[i] = 0;
			requestedBuildings[i] = 0;
		}
		
		realMinerals = 0;
		realGas = 0;
		minerals = 0;
		gas = 0;
		supplyUsed = 0;
		supplyMax = 0;
		
		timerTryExpand = 10;
		timerTryMacro = 10;
		timerTryPool = 10;
		timerTryExtractor = 10;
		timerTryHydra = 10;
		timerTryQueenNest = 10;
		timerTrySpire = 10;
		timerTryEvo = 10;
		timerMorphMutalisk = 1;
		
		//requestMacro = false;
		okExtractors = false;
	}
	
	public void update()
	{
		realMinerals = bwapi.getSelf().getMinerals();
		realGas = bwapi.getSelf().getGas();
		
		minerals = realMinerals;
		gas = realGas;
		
		supplyUsed = bwapi.getSelf().getSupplyUsed();
		supplyMax = bwapi.getSelf().getSupplyTotal();
		
		if (timerTryExpand > 0)
			timerTryExpand--;
		if (timerTryMacro > 0)
			timerTryMacro--;
		if (timerTryPool > 0)
			timerTryPool--;
		if (timerTryExtractor > 0)
			timerTryExtractor--;
		if (timerTryHydra > 0)
			timerTryHydra--;
		if (timerTryQueenNest > 0)
			timerTryQueenNest--;
		if (timerTrySpire > 0)
			timerTrySpire--;
		if (timerMorphMutalisk > 0)
			timerMorphMutalisk--;
		if (timerTryEvo > 0)
			timerTryEvo--;
		
		for (int i = 0; i < nbUnits; i++)
		{
			currentUnits[i] = 0;
			warpingUnits[i] = 0;
		}
		
		for (int i = 0; i < nbBuildings; i++)
		{
			currentBuildings[i] = 0;
			warpingBuildings[i] = 0;
		}
		
		if (!okExtractors && (JavaBot.currFrame >= 6500 || JavaBot.strategy.nbHatcheryTotal >= 5))
			okExtractors = true;
	}
	
	public void debug()
	{
		bwapi.drawText(new Point(5, 53), "Units      Current  Warping  Requested", true);
		for (int i = 0; i < nbUnits; i++)
		{
			bwapi.drawText(5, 61 + i*8, unitNames[i], true);
			
			bwapi.drawText(new Point(70, 61 + i*8), "" + currentUnits[i], true);
			bwapi.drawText(new Point(115, 61 + i*8), "" + warpingUnits[i], true);
			bwapi.drawText(new Point(170, 61 + i*8), "" + requestedUnits[i], true);
		}
		
		bwapi.drawText(new Point(5, 142), "Buildings  Current  Warping  Requested", true);
		for (int i = 0; i < nbBuildings; i++)
		{
			bwapi.drawText(5, 150 + i*8, buildingNames[i], true);
			
			bwapi.drawText(new Point(70, 150 + i*8), "" + currentBuildings[i], true);
			bwapi.drawText(new Point(115, 150 + i*8), "" + warpingBuildings[i], true);
			bwapi.drawText(new Point(170, 150 + i*8), "" + requestedBuildings[i], true);
		}
		
		bwapi.drawText(new Point(220, 5), "Minerals: " + minerals, true);
		bwapi.drawText(new Point(220, 15), "Gas: " + gas, true);
		bwapi.drawText(new Point(220, 25), "Supply: " + supplyUsed + "/" + supplyMax, true);
		
		bwapi.drawText(new Point(220, 80), "Try expand: " + timerTryExpand, true);
		bwapi.drawText(new Point(220, 90), "Try Evo: " + timerTryEvo, true);
		//bwapi.drawText(new Point(220, 100), "Macro asked: " + requestMacro, true);
		
		Tech.debug(70);
	}
	
	public void addUnit(Unit unit) {
		int TypeId = unit.getTypeID();
		if (Operators.isAbuilding(TypeId))
		{
			if (unit.getRemainingBuildTimer() > 0)
				warpingBuildings[Operators.buildingIterFromType(TypeId)]++;
			else
				currentBuildings[Operators.buildingIterFromType(TypeId)]++;
		}
		else
		{
			if (unit.getTypeID() == UnitTypes.Zerg_Egg.ordinal() || unit.getTypeID() == UnitTypes.Zerg_Cocoon.ordinal())
				warpingUnits[Operators.unitIterFromType(unit.getBuildTypeID())]++;
			else if (unit.getRemainingBuildTimer() > 0) {
				warpingUnits[Operators.unitIterFromType(TypeId)]++;
			}
			else
				currentUnits[Operators.unitIterFromType(TypeId)]++;
		}
	}
	
	public int enoughMineralsforZerglings() {
		if (JavaBot.strategy.nbZerglingsTotal < 1)
			return JavaBot.repartition.unitsMinerals + JavaBot.repartition.techMinerals;
		return JavaBot.repartition.unitsMinerals;
	}
	
	public int enoughMineralsforHydralisks() {
		if (JavaBot.strategy.nbHydralisksTotal < 1)
			return JavaBot.repartition.unitsMinerals + JavaBot.repartition.techMinerals;
		return JavaBot.repartition.unitsMinerals;
	}
	
	public int enoughGasforHydralisks() {
		if (JavaBot.strategy.nbHydralisksTotal < JavaBot.strategy.idealNbHydras / 4)
			return JavaBot.repartition.unitsGas + JavaBot.repartition.techGas;
		return JavaBot.repartition.unitsGas;
	}

	public int enoughMineralsforMutalisks() {
		
		if (JavaBot.strategy.nbMutalisksTotal + JavaBot.strategy.nbGuardiansTotal + JavaBot.strategy.nbDevourersTotal
				< (JavaBot.strategy.idealNbMutalisks + JavaBot.strategy.idealNbGuardians + JavaBot.strategy.idealNbDevourers) / 5 + 1)
			return JavaBot.repartition.unitsMinerals + JavaBot.repartition.techMinerals + JavaBot.repartition.expandMinerals;
		return JavaBot.repartition.unitsMinerals;
	}
	
	public int enoughGasforMutalisks() {
		if (JavaBot.strategy.nbMutalisksTotal + JavaBot.strategy.nbGuardiansTotal + JavaBot.strategy.nbDevourersTotal < 
				(JavaBot.strategy.idealNbMutalisks + JavaBot.strategy.idealNbGuardians + JavaBot.strategy.idealNbDevourers) / 4
				|| Tech.blockGroundAttackUpgrade > 0 || Tech.blockGroundDefenseUpgrade > 0 || Tech.blockGroundMissileUpgrade > 0)
			return JavaBot.repartition.unitsGas + JavaBot.repartition.techGas;
		return JavaBot.repartition.unitsGas;
	}
	
	public int enoughMineralsforQueens() {
		if (JavaBot.strategy.nbQueensTotal < JavaBot.strategy.idealNbQueens / 4 + 1)
			return JavaBot.repartition.unitsMinerals + JavaBot.repartition.techMinerals + JavaBot.repartition.expandMinerals;
		return JavaBot.repartition.unitsMinerals;
	}
	
	public int enoughGasforQueens() {
		if (JavaBot.strategy.nbQueensTotal < JavaBot.strategy.idealNbQueens / 4
				|| Tech.blockGroundAttackUpgrade > 0 || Tech.blockGroundDefenseUpgrade > 0 || Tech.blockGroundMissileUpgrade > 0)
			return JavaBot.repartition.unitsGas + JavaBot.repartition.techGas;
		return JavaBot.repartition.unitsGas;
	}

	public int enoughMineralsforOverlords() {
		return JavaBot.repartition.unitsMinerals + JavaBot.repartition.dronesMinerals + JavaBot.repartition.techMinerals;
	}
}
