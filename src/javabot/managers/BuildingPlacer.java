package javabot.managers;

import java.awt.Point;

import javabot.JNIBWAPI;
import javabot.operators.Operators;
import javabot.util.BWColor;

public class BuildingPlacer {

	public int typeID;
	public Point size;
	public Point bestPlace;
	public JNIBWAPI bwapi;
	public String name;
	
	public int homePosTileX;
	public int homePosTileY;
	
	public BuildingPlacer(int typeid, JNIBWAPI bw, String string, int homeX, int homeY) {
		
		typeID = typeid;
		bwapi = bw;
		size = Operators.getSize(typeID);
		bestPlace = null;
		name = string;
		
		homePosTileX = homeX;
		homePosTileY = homeY;
	}

	public void debug() {
		if (bestPlace != null)
		{
			bwapi.drawBox(bestPlace.x * 32 - 1,
					bestPlace.y * 32 - 1,
					bestPlace.x * 32 + 32 * size.x + 1,
					bestPlace.y * 32 + 32 * size.y + 1,
					BWColor.PURPLE, false, false);
			bwapi.drawText(bestPlace.x * 32 + 16, bestPlace.y * 32 + 16, name, false);
		}
		else {
			bwapi.drawText(homePosTileX * 32 + 16, homePosTileY * 32 + 16, "No place found here for " + name + " !", false);
		}
	}

}
