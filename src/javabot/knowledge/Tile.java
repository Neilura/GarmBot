package javabot.knowledge;

import javabot.JNIBWAPI;
import javabot.JavaBot;

public class Tile {

	public int buildingValue;
	public int BordersMap;
	public int DroneVisited;
	
	public boolean hasEnemy;
	
	int x;
	int y;
	
	public Tile(int X, int Y) {
		buildingValue = 0;
		BordersMap = 0;
		DroneVisited = 0;
		
		hasEnemy = false;
		
		x = X;
		y = Y;
	}
	
	public void update() {
		if (buildingValue == 2)
			buildingValue = 1;
		if (DroneVisited > 0) DroneVisited--;
		
		hasEnemy = false;
	}

	public void debug() {
		JNIBWAPI bwapi = JavaBot.bwapi;
		if (DroneVisited != 0)
			bwapi.drawText(x*32, y*32, ""+DroneVisited, false);
	}
}
