package javabot.managers;

import javabot.JavaBot;
import javabot.knowledge.Tech;
import javabot.model.Unit;
import javabot.operators.Operators;
import javabot.types.UpgradeType.UpgradeTypes;

public class EvoChamberManager extends BuildingManager {
	
	public enum UPGRADE_STATE {
		NONE,
		EVOLVING_ATTACK,
		EVOLVING_CRAPACE,
		EVOLVING_MISSILE,
		TRY_ATTACK,
		TRY_CRAPACE,
		TRY_MISSILE,
		FINISHED
	}
	
	public int nbLair;
	public int nbHive;
	public int upgradeTime;
	
	public int carapaceLvl;
	public int attackLvl;
	public int missileLvl;
	
	public UPGRADE_STATE state;
	
	public EvoChamberManager(Unit u) {
		super(u);
		state = UPGRADE_STATE.NONE;
		
		carapaceLvl = 0;
		attackLvl = 0;
		missileLvl = 0;
		
		nbLair = 0;
		nbHive = 0;
		upgradeTime = 0;
	}
	
	public void update()
	{
		super.update();
		
		if (state == UPGRADE_STATE.FINISHED || unit.getRemainingBuildTimer() > 0) return;
		
		upgradeTime = unit.getRemainingUpgradeTime();
		nbLair = JavaBot.knowledge.currentBuildings[Operators.iterLair];
		nbHive = JavaBot.knowledge.currentBuildings[Operators.iterHive];
		
		switch (state) {
			case NONE:
				updateNone();
			break;
			
			case TRY_CRAPACE:
				updateTryCarapace();
				break;
				
			case EVOLVING_CRAPACE:
				updateEvolveCarapace();
				break;
				
			case TRY_MISSILE:
				updateTryMissile();
				break;
				
			case EVOLVING_MISSILE:
				updateEvolveMissile();
				break;
				
			case TRY_ATTACK:
				updateTryAttack();
				break;
				
			case EVOLVING_ATTACK:
				updateEvolveAttack();
				break;

		default:
			break;
		}
	}

	private void updateEvolveCarapace() {
		Tech.blockGroundDefenseUpgrade = 5;
		if (upgradeTime == 0) {
			state = UPGRADE_STATE.NONE;
			Tech.groundDefenseLvl++;
			Tech.blockGroundDefenseUpgrade = 0;
		}
	}
	
	private void updateTryCarapace() {
		int requiredMinerals = 0;
		int requiredGas = 0;
		
		if (upgradeTime > 0) {
			state = UPGRADE_STATE.EVOLVING_CRAPACE;
			return;
		}
		
		switch (Tech.groundDefenseLvl) {
			case 0:
				requiredMinerals = 150;
				requiredGas = 150;
				break;
			case 1:
				requiredMinerals = 225;
				requiredGas = 225;
				break;
			case 2:
				requiredMinerals = 300;
				requiredGas = 300;
				break;
			case 3:
				state = UPGRADE_STATE.TRY_MISSILE;
				return;
		}
		
		if (JavaBot.knowledge.realMinerals >= requiredMinerals
				&& JavaBot.knowledge.realGas >= requiredGas) {
			BwapiCallsManager.upgrade(unit.getID(), UpgradeTypes.Zerg_Carapace.ordinal());
		}
	}
	
	private void updateEvolveMissile() {
		Tech.blockGroundMissileUpgrade = 5;
		if (upgradeTime == 0) {
			state = UPGRADE_STATE.NONE;
			Tech.groundMissileLvl++;
			Tech.blockGroundMissileUpgrade = 0;
		}
	}
	
	private void updateTryMissile() {
		int requiredMinerals = 0;
		int requiredGas = 0;
		
		if (upgradeTime > 0) {
			state = UPGRADE_STATE.EVOLVING_MISSILE;
			return;
		}
		
		switch (Tech.groundMissileLvl) {
			case 0:
				requiredMinerals = 100;
				requiredGas = 100;
				break;
			case 1:
				requiredMinerals = 150;
				requiredGas = 150;
				break;
			case 2:
				requiredMinerals = 200;
				requiredGas = 200;
				break;
			case 3:
				state = UPGRADE_STATE.TRY_ATTACK;
				return;
		}
		
		if (JavaBot.knowledge.realMinerals >= requiredMinerals
				&& JavaBot.knowledge.realGas >= requiredGas) {
			BwapiCallsManager.upgrade(unit.getID(), UpgradeTypes.Zerg_Missile_Attacks.ordinal());
		}
	}
	
	private void updateEvolveAttack() {
		Tech.blockGroundAttackUpgrade = 5;
		if (upgradeTime == 0) {
			state = UPGRADE_STATE.NONE;
			Tech.groundAttackLvl++;
			Tech.blockGroundAttackUpgrade = 0;
		}
	}
	
	private void updateTryAttack() {
		int requiredMinerals = 0;
		int requiredGas = 0;
		
		if (upgradeTime > 0) {
			state = UPGRADE_STATE.EVOLVING_ATTACK;
			return;
		}
		
		switch (Tech.groundAttackLvl) {
			case 0:
				requiredMinerals = 100;
				requiredGas = 100;
				break;
			case 1:
				requiredMinerals = 150;
				requiredGas = 150;
				break;
			case 2:
				requiredMinerals = 200;
				requiredGas = 200;
				break;
			case 3:
				state = UPGRADE_STATE.FINISHED;
				return;
		}
		
		if (JavaBot.knowledge.realMinerals >= requiredMinerals
				&& JavaBot.knowledge.realGas >= requiredGas) {
			BwapiCallsManager.upgrade(unit.getID(), UpgradeTypes.Zerg_Melee_Attacks.ordinal());
		}
	}
	
	private void updateNone() {
		carapaceLvl = Tech.groundDefenseLvl;
		attackLvl = Tech.groundAttackLvl;
		missileLvl = Tech.groundMissileLvl;
		
		if (carapaceLvl == 0 && Tech.blockGroundDefenseUpgrade == 0)
			state = UPGRADE_STATE.TRY_CRAPACE;
		else if (missileLvl == 0 && Tech.blockGroundMissileUpgrade == 0)
			state = UPGRADE_STATE.TRY_MISSILE;
		else if (attackLvl == 0 && Tech.blockGroundAttackUpgrade == 0)
			state = UPGRADE_STATE.TRY_ATTACK;
		else if (carapaceLvl == 1 && Tech.blockGroundDefenseUpgrade == 0 && nbLair + nbHive >= 1)
			state = UPGRADE_STATE.TRY_CRAPACE;
		else if (missileLvl == 1 && Tech.blockGroundMissileUpgrade == 0 && nbLair + nbHive >= 1)
			state = UPGRADE_STATE.TRY_MISSILE;
		else if (attackLvl == 1 && Tech.blockGroundAttackUpgrade == 0 && nbLair + nbHive >= 1)
			state = UPGRADE_STATE.TRY_ATTACK;
		else if (carapaceLvl == 2 && Tech.blockGroundDefenseUpgrade == 0 && nbHive >= 1)
			state = UPGRADE_STATE.TRY_CRAPACE;
		else if (missileLvl == 2 && Tech.blockGroundMissileUpgrade == 0 && nbHive >= 1)
			state = UPGRADE_STATE.TRY_MISSILE;
		else if (attackLvl == 2 && Tech.blockGroundAttackUpgrade == 0 && nbHive >= 1)
			state = UPGRADE_STATE.TRY_ATTACK;
	}

	public void drawDebug()
	{
		bwapi.drawText(unit.getX() - 10, unit.getY() - 8, "Evo Chamber " + state + " " + unit.getRemainingUpgradeTime() + " " + unit.getRemainingResearchTime(), false);
	}
}