package javabot.knowledge;

import javabot.JavaBot;

public class Tech {

	public static int broodlings = 0;
	
	public static int metabolicBoost = 0;
	public static int adrenalGlands = 0;
	
	public static int groove = 0;
	public static int muscular = 0;
	public static int burrow = 0;
	
	public static int groundDefenseLvl = 0;
	public static int groundAttackLvl = 0;
	public static int groundMissileLvl = 0;
	
	public static int blockGroundDefenseUpgrade = 0;
	public static int blockGroundAttackUpgrade = 0;
	public static int blockGroundMissileUpgrade = 0;
	
	public static int airDefenseLvl = 0;
	public static int airAttackLvl = 0;
	
	public static boolean blockAirAttackUpgrade = false;
	public static boolean blockAirDefenseUpgrade = false;
	
	public static void update() {
		if (blockGroundDefenseUpgrade > 0)
			blockGroundDefenseUpgrade--;
		if (blockGroundAttackUpgrade > 0)
			blockGroundAttackUpgrade--;
		if (blockGroundMissileUpgrade > 0)
			blockGroundMissileUpgrade--;
	}

	public static void debug(int i) {
		JavaBot.bwapi.drawText(300, 200, "AirattackLvl:" + airAttackLvl + " block:" + blockAirAttackUpgrade, true);
		JavaBot.bwapi.drawText(300, 210, "AirdefenseLvl:" + airDefenseLvl + " block:" + blockAirDefenseUpgrade, true);
		
		JavaBot.bwapi.drawText(300, 220, "groundDefenseLvl:" + groundDefenseLvl + " block:" + blockGroundDefenseUpgrade, true);
		JavaBot.bwapi.drawText(300, 230, "groundAttackLvl:" + groundAttackLvl + " block:" + blockGroundAttackUpgrade, true);
		JavaBot.bwapi.drawText(300, 240, "groundMissileLvl:" + groundMissileLvl + " block:" + blockGroundMissileUpgrade, true);
	}
}
