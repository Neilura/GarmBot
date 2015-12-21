package javabot.managers;

import java.util.ArrayList;

import javabot.JNIBWAPI;
import javabot.operators.Operators;
import javabot.util.BWColor;

public class ControlTower {
	
	public static int posX = 1000;
	public static int posY = 1000;
	public static JNIBWAPI bwapi;
	public static int radius = 300;
	
	public static ArrayList<GuardianManager> guardians = new ArrayList<GuardianManager>();
	public static ArrayList<MutaliskManager> mutalisks = new ArrayList<MutaliskManager>();
	public static ArrayList<DevourerManager> devourers = new ArrayList<DevourerManager>();
	
	public static void reset() {
		guardians.clear();
		mutalisks.clear();
		devourers.clear();
	}
	
	public static void update() {
		
		if (guardians.size() == 0) {
			posX = -1;
			posY = -1;
		}
		
		if (guardians.size() > 0) {
			if (posX == -1) {
				GuardianManager firstGuardian = guardians.get(0);
				posX = firstGuardian.posX;
				posY = firstGuardian.posY;
			}
			else {
				int newposX = -1;
				int newposY = -1;
				int divider = 0;
				
				for (GuardianManager g : guardians) {
					if (Operators.distance(posX, posY, g.posX, g.posY) < radius) {
						newposX += g.posX;
						newposY += g.posY;
						divider++;
					}
				}
				if (divider == 0) divider = 1;
				posX = newposX / divider;
				posY = newposY / divider;
			}
		}
	}
	
	public static void debug() {
		bwapi.drawText(posX, posY, "Control tower", false);
		bwapi.drawText(posX, posY + 10, "Mutas : " + mutalisks.size(), false);
		bwapi.drawText(posX, posY + 20, "Guardians : " + guardians.size(), false);
		bwapi.drawText(posX, posY + 30, "Devourers : " + devourers.size(), false);
		
		bwapi.drawCircle(posX, posY, radius, BWColor.PURPLE, false, false);
	}
}
