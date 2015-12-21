package javabot.managers;

import javabot.JNIBWAPI;
import javabot.JavaBot;

public class BwapiCallsManager {
	
	static JNIBWAPI bwapi;
	public static int BWAPIcalls = 0;
	public static int BWAPIFramescalls = 0;
	public static int maxCallsPerFrame = 15;
	
	public static void init() {
		bwapi = JavaBot.bwapi;
		BWAPIcalls = 0;
		BWAPIFramescalls = 0;
	}
	
	public static boolean callBack() {
		if (BWAPIFramescalls < maxCallsPerFrame) {
			BWAPIcalls++;
			BWAPIFramescalls++;
			return true;
		}
		return false;
	}
	
	//-----------------------------

	public static boolean attack(int id, int posXToGo, int posYToGo) {
		boolean success = callBack();
		if (success)
			bwapi.attack(id, posXToGo, posYToGo);
		return success;
	}
	
	public static boolean move(int id, int posXToGo, int posYToGo) {
		boolean success = callBack();
		if (success)
			bwapi.move(id, posXToGo, posYToGo);
		return success;
	}
	
	public static boolean upgrade(int id, int techId) {
		boolean success = callBack();
		if (success)
			bwapi.upgrade(id, techId);
		return success;
	}
	
	public static boolean research(int id, int techId) {
		boolean success = callBack();
		if (success)
			bwapi.research(id, techId);
		return success;
	}
	
	public static boolean morph(int id, int unitTypeId) {
		boolean success = callBack();
		if (success)
			bwapi.morph(id, unitTypeId);
		return success;
	}
	
	public static boolean rightClick(int id, int targetId) {
		boolean success = callBack();
		if (success)
			bwapi.rightClick(id, targetId);
		return success;
	}
	
	public static boolean build(int id, int x, int y, int typeId) {
		boolean success = callBack();
		if (success)
			bwapi.build(id, x, y, typeId);
		return success;
	}
	
	public static boolean stop(int id) {
		boolean success = callBack();
		if (success)
			bwapi.stop(id);
		return success;
	}
	
	public static boolean useTech(int id, int techId, int targetId) {
		boolean success = callBack();
		if (success)
			bwapi.useTech(id, techId, targetId);
		return success;
	}
	
	public static boolean useTech(int id, int techId) {
		boolean success = callBack();
		if (success)
			bwapi.useTech(id, techId);
		return success;
	}
	
	//-----------------------------

	public static void processFrame() {
		BWAPIcalls -= maxCallsPerFrame;
		if (BWAPIcalls < 0) BWAPIcalls = 0;
	}
	
	public static void resetFrame() {
		BWAPIFramescalls = 0;
	}

	public static void debug() {
		bwapi.drawText(10, 280, "Current calls = " +  BWAPIFramescalls + ", in list = " + BWAPIcalls, true);
		bwapi.drawText(10, 290, "Frame : " +  bwapi.getFrameCount(), true);
	}
}
