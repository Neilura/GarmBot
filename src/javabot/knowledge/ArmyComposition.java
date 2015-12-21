package javabot.knowledge;

import java.util.HashMap;

import javabot.JavaBot;
import javabot.managers.EnemyUnitManager;
import javabot.types.UnitType.UnitTypes;

public class ArmyComposition {

	private HashMap<Integer, int[]> compositionReaction;
	double Z;
	int Z2; //zerglings
	double H;
	int H2; //hydralisks
	double Q;
	int Q2; //queens
	double M;
	int M2; //mutalisks
	double G;
	int G2; //guardians
	double D;
	int D2; //devourers
	
	//Unused for now
	int F,F2; //defilers
	int U,U2; //ultralisks
	int S,S2; //scourge
	int L,L2; //lurkers
	
	double nbZsupp;
	double nbHsupp;
	double nbQsupp;
	double nbMsupp;
	double nbGsupp;
	double nbDsupp;
	double nbFsupp;
	double nbUsupp;
	double nbSsupp;
	double nbLsupp;
	
	public int coefficient, coefficient2;
	
	public ArmyComposition() {
		compositionReaction = new HashMap<Integer, int[]>();
																			 				  // Z   H   Q   M   G   D
		compositionReaction.put(UnitTypes.Terran_Marine.ordinal(), 					new int[]{  10, 15,  0,  5, 70,  0,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Terran_Firebat.ordinal(), 				new int[]{   0, 40,  0, 40, 20,  0,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Terran_Medic.ordinal(), 					new int[]{   0, 10, 40,  0, 50,  0,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Terran_Ghost.ordinal(), 					new int[]{   0, 50, 40,  0, 10,  0,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Terran_Vulture.ordinal(), 				new int[]{   0, 40,  5, 40, 15,  0,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Terran_Goliath.ordinal(), 				new int[]{   5, 20, 70,  0,  5,  0,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Terran_Siege_Tank_Siege_Mode.ordinal(), 	new int[]{   0,  0, 80, 10, 10,  0,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Terran_Siege_Tank_Tank_Mode.ordinal(), 	new int[]{   0,  0, 80, 10, 10,  0,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Terran_Wraith.ordinal(), 					new int[]{   0, 50,  0, 10,  0, 40,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Terran_Valkyrie.ordinal(), 				new int[]{   0, 80,  0,  0,  0, 20,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Terran_Science_Vessel.ordinal(), 			new int[]{   0, 50,  0,  0,  0, 50,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Terran_Battlecruiser.ordinal(),	 		new int[]{   0, 60,  0,  0,  0, 40,  0,  0,  0,  0});
		
		  																	  // Z   H   Q   M   G   D
		compositionReaction.put(UnitTypes.Zerg_Zergling.ordinal(), 	new int[]{   0,  0,  0, 90, 10,  0,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Zerg_Hydralisk.ordinal(), new int[]{  40, 10,  5, 30, 15,  0,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Zerg_Mutalisk.ordinal(), 	new int[]{   0, 20,  0, 40,  0, 40,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Zerg_Scourge.ordinal(), 	new int[]{   0,100,  0,  0,  0,  0,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Zerg_Queen.ordinal(), 	new int[]{   0, 10,  0, 70,  0, 20,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Zerg_Ultralisk.ordinal(), new int[]{   0,  0, 90,  5,  5,  0,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Zerg_Defiler.ordinal(), 	new int[]{  60,  0, 40,  0,  0,  0,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Zerg_Guardian.ordinal(), 	new int[]{   0, 10,  0, 80,  0, 10,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Zerg_Devourer.ordinal(), 	new int[]{   0, 90,  0,  0,  0, 10,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Zerg_Lurker.ordinal(), 	new int[]{   0,  0, 70, 15, 15,  0,  0,  0,  0,  0});
		
																			  		  // Z   H   Q   M   G   D
		compositionReaction.put(UnitTypes.Protoss_Zealot.ordinal(), 		new int[]{   0,  0,  0, 80, 20,  0,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Protoss_Dragoon.ordinal(), 		new int[]{  15, 15, 60,  5,  5,  0,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Protoss_High_Templar.ordinal(), 	new int[]{   0,  0, 80,  0, 20,  0,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Protoss_Dark_Templar.ordinal(), 	new int[]{   0,  0, 50, 30, 20,  0,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Protoss_Archon.ordinal(), 		new int[]{   0, 80,  0,  0, 20,  0,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Protoss_Dark_Archon.ordinal(), 	new int[]{  45, 45,  0,  0, 10,  0,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Protoss_Reaver.ordinal(), 		new int[]{   0,  0,  0, 80, 20,  0,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Protoss_Scout.ordinal(), 			new int[]{   0, 80,  0, 10,  0, 10,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Protoss_Corsair.ordinal(), 		new int[]{   0, 70,  0,  0,  0, 30,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Protoss_Arbiter.ordinal(), 		new int[]{   0, 60,  0, 10,  0, 30,  0,  0,  0,  0});
		compositionReaction.put(UnitTypes.Protoss_Carrier.ordinal(), 		new int[]{   0, 90,  0,  0,  0, 10,  0,  0,  0,  0});
	}
	
	public void update() {
		if (coefficient > 0 && JavaBot.currFrame % 100 == 0) coefficient--;
		computeTotal();
	}
	
	public void add(int z, int h, int q, int m, int g, int d, int f, int u, int s, int l) {
		Z2 += z;
		H2 += h;
		Q2 += q;
		M2 += m;
		G2 += g;
		D2 += d;
		F2 += f;
		U2 += u;
		S2 += s;
		L2 += l;
		
		coefficient2++;
	}
	
	public void computeTotal() {
		if (coefficient == 0 && coefficient2 == 0) {
			coefficient = 1;
			nbZsupp = 40;
			nbHsupp = 40;
			nbQsupp = 10;
			nbMsupp = 18;
			nbGsupp = 12;
			nbDsupp = 4;
			nbFsupp = 0;
			nbUsupp = 0;
			nbSsupp = 0;
			nbLsupp = 0;
		}
		else {
			if (coefficient2 != 0) {
				
				Z = (Z*coefficient + (double)Z2) / (coefficient + coefficient2);
				H = (H*coefficient + (double)H2) / (coefficient + coefficient2);
				Q = (Q*coefficient + (double)Q2) / (coefficient + coefficient2);
				M = (M*coefficient + (double)M2) / (coefficient + coefficient2);
				G = (G*coefficient + (double)G2) / (coefficient + coefficient2);
				D = (D*coefficient + (double)D2) / (coefficient + coefficient2);
				
				
				nbZsupp = 1.24 * (Z);
				nbHsupp = 1.24 * (H);
				nbQsupp = 1.24 * (Q);
				nbMsupp = 1.24 * (M);
				nbGsupp = 1.24 * (G);
				nbDsupp = 1.24 * (D);
				nbFsupp = 1.24 * (F);
				nbUsupp = 1.24 * (U);
				nbSsupp = 1.24 * (S);
				nbLsupp = 1.24 * (L);
				
				if (coefficient2 > coefficient) {
					coefficient = coefficient2;
				}
			}
		}
		newComposition();
	}

	public void newComposition() {
		
		double totalPop = nbZsupp + nbHsupp + nbQsupp + nbMsupp + nbGsupp + nbDsupp;
		
		int nbZsuppInt;
		int nbHsuppInt;
		int nbQsuppInt;
		int nbMsuppInt;
		int nbGsuppInt;
		int nbDsuppInt;
		/*int nbFsuppInt;
		int nbUsuppInt;
		int nbSsuppInt;
		int nbLsuppInt;*/
		
		if (totalPop > 124) {
			nbZsuppInt = (int) Math.floor(nbZsupp);
			nbHsuppInt = (int) Math.floor(nbHsupp);
			nbQsuppInt = (int) Math.floor(nbQsupp);
			nbMsuppInt = (int) Math.floor(nbMsupp);
			nbGsuppInt = (int) Math.floor(nbGsupp);
			nbDsuppInt = (int) Math.floor(nbDsupp);
			/*nbFsuppInt = (int) Math.floor(nbFsupp);
			nbUsuppInt = (int) Math.floor(nbUsupp);
			nbSsuppInt = (int) Math.floor(nbSsupp);
			nbLsuppInt = (int) Math.floor(nbLsupp);*/
		}
		else {
			nbZsuppInt = (int) Math.ceil(nbZsupp);
			nbHsuppInt = (int) Math.ceil(nbHsupp);
			nbQsuppInt = (int) Math.ceil(nbQsupp);
			nbMsuppInt = (int) Math.ceil(nbMsupp);
			nbGsuppInt = (int) Math.ceil(nbGsupp);
			nbDsuppInt = (int) Math.ceil(nbDsupp);
			/*nbFsuppInt = (int) Math.ceil(nbFsupp);
			nbUsuppInt = (int) Math.ceil(nbUsupp);
			nbSsuppInt = (int) Math.ceil(nbSsupp);
			nbLsuppInt = (int) Math.ceil(nbLsupp);*/
		}
		
		int totalPopInt = nbZsuppInt + nbHsuppInt + nbQsuppInt + nbMsuppInt + nbGsuppInt + nbDsuppInt;
		boolean goUp = totalPopInt < 124;
		
		if (nbQsuppInt % 2 == 1) {
			if (goUp)
				nbQsuppInt++;
			else
				nbQsuppInt--;
		}
		totalPopInt = nbZsuppInt + nbHsuppInt + nbQsuppInt + nbMsuppInt + nbGsuppInt + nbDsuppInt;
		goUp = totalPopInt < 124;
		if (nbMsuppInt % 2 == 1) {
			if (goUp)
				nbMsuppInt++;
			else
				nbMsuppInt--;
		}
		totalPopInt = nbZsuppInt + nbHsuppInt + nbQsuppInt + nbMsuppInt + nbGsuppInt + nbDsuppInt;
		goUp = totalPopInt < 124;
		if (nbGsuppInt % 2 == 1) {
			if (goUp)
				nbGsuppInt++;
			else
				nbGsuppInt--;
		}
		totalPopInt = nbZsuppInt + nbHsuppInt + nbQsuppInt + nbMsuppInt + nbGsuppInt + nbDsuppInt;
		goUp = totalPopInt < 124;
		if (nbDsuppInt % 2 == 1) {
			if (goUp)
				nbDsuppInt++;
			else
				nbDsuppInt--;
		}
		totalPopInt = nbZsuppInt + nbHsuppInt + nbQsuppInt + nbMsuppInt + nbGsuppInt + nbDsuppInt;
		
		nbHsuppInt -= totalPopInt - 124;
		
		JavaBot.strategy.idealPopZerglings = 5 + nbZsuppInt;
		JavaBot.strategy.idealPopHydras = 5 + nbHsuppInt;
		JavaBot.strategy.idealPopQueens = 2 + nbQsuppInt;
		JavaBot.strategy.idealPopMutalisks = 2 + nbMsuppInt;
		JavaBot.strategy.idealPopGuardians = 2 + nbGsuppInt;
		JavaBot.strategy.idealPopDevourers = 0 + nbDsuppInt;
		/*JavaBot.strategy.idealPopDefiler = 0 + nbFsuppInt;
		JavaBot.strategy.idealPopUltralisk = 0 + nbUsuppInt;
		JavaBot.strategy.idealPopScourge = 0 + nbSsuppInt;
		JavaBot.strategy.idealPopLurker = 0 + nbLsuppInt;*/
		
		JavaBot.strategy.idealNbZerglings = JavaBot.strategy.idealPopZerglings * 2;
		JavaBot.strategy.idealNbHydras = JavaBot.strategy.idealPopHydras;
		JavaBot.strategy.idealNbQueens = JavaBot.strategy.idealPopQueens / 2;
		JavaBot.strategy.idealNbMutalisks = JavaBot.strategy.idealPopMutalisks / 2;
		JavaBot.strategy.idealNbGuardians = JavaBot.strategy.idealPopGuardians / 2;
		JavaBot.strategy.idealNbDevourers = JavaBot.strategy.idealPopDevourers / 2;
	}

	public void setAdaptComposition(EnemyUnitManager enemyUnitManager) {
		int[] data = compositionReaction.get(enemyUnitManager.unit.getTypeID());
		if (data != null) {
			enemyUnitManager.valZ = data[0];
			enemyUnitManager.valH = data[1];
			enemyUnitManager.valQ = data[2];
			enemyUnitManager.valM = data[3];
			enemyUnitManager.valG = data[4];
			enemyUnitManager.valD = data[5];
		}
	}

	public void reset() {
		coefficient2 = 0;
		Z2 = 0;
		H2 = 0;
		Q2 = 0;
		M2 = 0;
		G2 = 0;
		D2 = 0;
		F2 = 0;
		U2 = 0;
		S2 = 0;
		L2 = 0;
	}
}
