package javabot.knowledge;

import java.awt.Point;
import java.util.ArrayList;

import javabot.JNIBWAPI;
import javabot.JavaBot;
import javabot.managers.BuildingPlacer;
import javabot.model.ChokePoint;
import javabot.model.Map;
import javabot.model.Unit;
import javabot.operators.Operators;
import javabot.types.UnitType.UnitTypes;

public class BasePlanner {

	public JNIBWAPI bwapi;
	public Map map;
	public int sizeX;
	public int sizeY;
	
	public int posX;
	public int posY;
	
	public int posTileX;
	public int posTileY;
	
	public ArrayList<ChokePoint> chokePoints;
	
	public Point potentialHatchery;
	
	public ArrayList<BuildingPlacer> potentialBuildings;
	
	public int timerReset;
	public int timerResetMax = 50;
	
	public int allowedRangePylon;
	public Point bestChokePoint;
	
	public BasePlanner(int homePositionX, int homePositionY)
	{
		posX = homePositionX;
		posY = homePositionY;
		
		posTileX = homePositionX / 32;
		posTileY = homePositionY / 32;
		
		bwapi = JavaBot.bwapi;
		map = bwapi.getMap();

		sizeX = TileGraph.sizeX;
		sizeY = TileGraph.sizeY;
		timerReset = timerResetMax;

		potentialBuildings = new ArrayList<BuildingPlacer>();
		
		potentialBuildings.add(new BuildingPlacer(UnitTypes.Zerg_Hatchery.ordinal(), bwapi, "HATCH", posTileX, posTileY));

		potentialHatchery = null;
		redRessourceTiles();
	}
	
	public void redRessourceTiles() {
		
		for (Unit u : bwapi.getNeutralUnits())
		{
			if (u.getTypeID() == UnitTypes.Resource_Mineral_Field.ordinal() && Operators.distance(u, posX, posY) < 600)
			{
				int i = u.getTileY();
				int j = u.getTileX();
				for (int bn = -3; bn <= 3; bn++)
				{
					for (int jk = -3; jk <= 4; jk++)
					{
						if (TileGraph.OkPos(i + bn, j + jk))
							TileGraph.tiles[i + bn][j + jk].buildingValue = 4;
					}
				}
				TileGraph.tiles[i][j].buildingValue = 3;
				TileGraph.tiles[i][j + 1].buildingValue = 3;
			}
			if (u.getTypeID() == UnitTypes.Resource_Vespene_Geyser.ordinal() && Operators.distance(u, posX, posY) < 600)
			{
				int i = u.getTileY();
				int j = u.getTileX();
				
				for (int bn = -3; bn <= 4; bn++)
				{
					for (int jk = -3; jk <= 6; jk++)
					{
						if (TileGraph.OkPos(i + bn, j + jk))
							TileGraph.tiles[i + bn][j + jk].buildingValue = 4;
					}
				}
				
				for (int hj = 0; hj < 4; hj++) {
					for (int jk = 0; jk < 2; jk++) {
						TileGraph.tiles[i + jk][j + hj].buildingValue = 3;
					}
				}
			}
		}
	}
	
	public void update()
	{
		timerReset--;
		
		for (BuildingPlacer bPlacer : potentialBuildings)
		{
			if (timerReset == 0 || bPlacer.bestPlace == null || !OKposition(bPlacer.bestPlace, bPlacer.size, bPlacer.typeID == UnitTypes.Zerg_Hatchery.ordinal()))
				tryPlaceBuilding(bPlacer);
			if (bPlacer.typeID == UnitTypes.Zerg_Hatchery.ordinal()) {
				potentialHatchery = bPlacer.bestPlace;
			}
		}
		
		if (timerReset <= 0)
		{
			timerReset = timerResetMax;
		}
	}
	
	private boolean OKposition(Point potentialBuilding, Point size, boolean hatch) {
		
		for (int Y = potentialBuilding.y; Y < potentialBuilding.y + size.y; Y++)
		{
			for (int X = potentialBuilding.x; X < potentialBuilding.x + size.x; X++)
			{
				if (X < 0 || Y < 0 || X >= TileGraph.tiles[0].length || Y >= TileGraph.tiles.length)
					return false;
				int tileValue = TileGraph.tiles[Y][X].buildingValue;
				if ((tileValue != 1 && tileValue != 4) || (hatch && tileValue == 4))
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	private void tryPlaceBuilding(BuildingPlacer bPlacer)
	{
		int startPosX = bPlacer.homePosTileX;
		int startPosY = bPlacer.homePosTileY;
		boolean hatch = false;
		int allowedRange = 40;

		if (bPlacer.typeID == UnitTypes.Zerg_Hatchery.ordinal())
		{
			allowedRange = 1;
			hatch = true;
			startPosX -= 2;
			startPosY -= 1;
		}
		
		bPlacer.bestPlace = null;
		
		Point size = bPlacer.size;
		
		int i = 0;
		
		while (i < 5)
		{
			Point posTest = new Point((int)(startPosX + Math.random() * (hatch ? i : 1) * allowedRange * 2 - (hatch ? i : 1) * allowedRange),
									  (int)(startPosY + Math.random() * (hatch ? i : 1) * allowedRange * 2 - (hatch ? i : 1) * allowedRange));
			
			if (OKposition(posTest, size, hatch))
			{
				bPlacer.bestPlace = posTest;
				return;
			}
			i++;
		}
	}

	public void debug()
	{
		for (BuildingPlacer bPlacer : potentialBuildings)
		{
			bPlacer.debug();
		}
	}
}
