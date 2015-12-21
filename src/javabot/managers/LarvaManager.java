package javabot.managers;

import javabot.JavaBot;
import javabot.model.Unit;
import javabot.operators.Operators;
import javabot.types.UnitType.UnitTypes;

public class LarvaManager extends UnitManager {

	boolean morphing;
	public static int blockMorph = 0;
	
	public LarvaManager(Unit u) {
		super(u);
		morphing = false;
	}
	
	public void update()
	{
		super.update();
		
		boolean askedCombat = (JavaBot.knowledge.requestedUnits[Operators.iterZerglings] > 0
				|| JavaBot.knowledge.requestedUnits[Operators.iterHydralisks] > 0
				|| JavaBot.knowledge.requestedUnits[Operators.iterMutalisks] > 0
				|| JavaBot.knowledge.requestedUnits[Operators.iterQueens] > 0)
				&& (base == null || base.nbDrones >= 7 || JavaBot.knowledge.currentUnits[Operators.iterDrones] >= 60);
				
		boolean askedDrone = base != null && base.nbDrones < base.nbDronesIdeal &&
				(JavaBot.repartition.dronesMinerals >= 50 || (base.nbDrones < base.nbDronesIdeal / 3
						&& JavaBot.repartition.dronesMinerals + JavaBot.repartition.unitsMinerals + JavaBot.repartition.techMinerals >= 50)
				|| (JavaBot.knowledge.warpingUnits[Operators.iterDrones] == 0 && JavaBot.knowledge.realMinerals >= 50))
				&& JavaBot.knowledge.requestedUnits[Operators.iterDrones] > 0;
		
		if (!morphing && JavaBot.knowledge.requestedUnits[Operators.iterOverlords] > 0 && JavaBot.knowledge.enoughMineralsforOverlords() >= 100/* JavaBot.knowledge.realMinerals >= 100*/) {
			JavaBot.knowledge.requestedUnits[Operators.iterOverlords]--;
			morph(UnitTypes.Zerg_Overlord.ordinal());
		}
		else if (!morphing) {
			if (askedCombat && askedDrone) {
				double test = Math.random();
				double probaCombat = base.nbDrones < base.nbDronesIdeal / 3 ? 0.25f : base.nbDrones < 2 * base.nbDronesIdeal / 3 ? 0.5f : 0.75f;
				if (test < probaCombat) {
					morphCombatUnit();
				}
				else {
					JavaBot.knowledge.requestedUnits[Operators.iterDrones]--;
					morph(UnitTypes.Zerg_Drone.ordinal());
				}
			}
			else if (askedCombat) {
				morphCombatUnit();
			}
			else if (askedDrone) {
				JavaBot.knowledge.requestedUnits[Operators.iterDrones]--;
				morph(UnitTypes.Zerg_Drone.ordinal());
			}
		}
	}
	
	public void morphCombatUnit() {
		
		int tries = 3;
				
		while (tries > 0) {
			int result = (int) (Math.random() * 4);
			tries--;
			
			switch (result) {
			case 0:
				if (JavaBot.knowledge.currentBuildings[Operators.iterPool] > 0
						&& JavaBot.knowledge.enoughMineralsforZerglings() >= 50
						&& JavaBot.strategy.nbZerglingsTotal < JavaBot.strategy.idealNbZerglings) {
					JavaBot.knowledge.requestedUnits[Operators.iterZerglings]--;
					morph(UnitTypes.Zerg_Zergling.ordinal());
					tries = 0;
				}
				break;
			case 1:
				if (JavaBot.knowledge.currentBuildings[Operators.iterHydra] > 0
						&& JavaBot.knowledge.enoughMineralsforHydralisks() >= 75
						&& JavaBot.knowledge.enoughGasforHydralisks() >= 25
						&& JavaBot.strategy.nbHydralisksTotal < JavaBot.strategy.idealNbHydras) {
					JavaBot.knowledge.requestedUnits[Operators.iterHydralisks]--;
					morph(UnitTypes.Zerg_Hydralisk.ordinal());
					tries = 0;
				}
				break;
				
			case 2:
				if ((JavaBot.knowledge.currentBuildings[Operators.iterSpire] > 0 || JavaBot.knowledge.currentBuildings[Operators.iterGreatSpire] > 0)
						&& JavaBot.strategy.nbMutalisksTotal + JavaBot.strategy.nbGuardiansTotal + JavaBot.strategy.nbDevourersTotal < JavaBot.strategy.idealNbMutalisks + JavaBot.strategy.idealNbGuardians + JavaBot.strategy.idealNbDevourers
						&& JavaBot.knowledge.enoughMineralsforMutalisks() >= 100
						&& JavaBot.knowledge.enoughGasforMutalisks() >= 100) {
					JavaBot.knowledge.requestedUnits[Operators.iterMutalisks]--;
					morph(UnitTypes.Zerg_Mutalisk.ordinal());
					tries = 0;
				}
				break;
				
			case 3:
				if (JavaBot.knowledge.currentBuildings[Operators.iterQueenNest] > 0
						&& JavaBot.strategy.nbQueensTotal < JavaBot.strategy.idealNbQueens
						&& JavaBot.knowledge.enoughMineralsforQueens() >= 100
						&& JavaBot.knowledge.enoughGasforQueens() >= 100) {
					JavaBot.knowledge.requestedUnits[Operators.iterQueens]--;
					morph(UnitTypes.Zerg_Queen.ordinal());
					tries = 0;
				}
				break;

			default:
				break;
			}
		}
	}
	
	public void morph(int unitType) {
		if (JavaBot.knowledge.supplyUsed < 399 && blockMorph == 0) {
			if (BwapiCallsManager.morph(unit.getID(), unitType)) {
				blockMorph = 2;
				if (unitType == UnitTypes.Zerg_Drone.ordinal()) {
					JavaBot.repartition.dronesMinerals -= 50;
					if (JavaBot.repartition.dronesMinerals < 0) {
						JavaBot.repartition.techMinerals += JavaBot.repartition.dronesMinerals;
						JavaBot.repartition.dronesMinerals = 0;
						if (JavaBot.repartition.techMinerals < 0)
							JavaBot.repartition.techMinerals = 0;
					}
				}
				else if (unitType == UnitTypes.Zerg_Overlord.ordinal()) {
					JavaBot.repartition.unitsMinerals -= 100;
					if (JavaBot.repartition.unitsMinerals < 0)
						JavaBot.repartition.unitsMinerals = 0;
				}
				else if (unitType == UnitTypes.Zerg_Zergling.ordinal())
					JavaBot.repartition.reduceCombatUnits(50, 0);
				else if (unitType == UnitTypes.Zerg_Hydralisk.ordinal())
					JavaBot.repartition.reduceCombatUnits(75, 25);
				else if (unitType == UnitTypes.Zerg_Queen.ordinal())
					JavaBot.repartition.reduceCombatUnits(100, 100);
				else if (unitType == UnitTypes.Zerg_Mutalisk.ordinal())
					JavaBot.repartition.reduceCombatUnits(100, 100);
			}
		}
	}
	
	public void updateIsInBase()
	{
		int distMax = Integer.MAX_VALUE;
		BaseManager bestBase = null;
		
		for (BaseManager manager : JavaBot.bases)
		{
			int dist = (manager.posX - posX) * (manager.posX - posX) + (manager.posY - posY) * (manager.posY - posY);
			if (dist < distMax)
			{
				distMax = dist;
				bestBase = manager;
			}
		}
		if (bestBase != null)
		{
			base = bestBase;
		}
	}
	
	public void drawDebug()
	{
		if (!JavaBot.RELEASE)
			bwapi.drawText(unit.getX() - 10, unit.getY() - 15, Operators.stringFromType(typeID), false);
	}
}
