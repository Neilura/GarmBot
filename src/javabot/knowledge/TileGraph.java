package javabot.knowledge;

import java.awt.Point;

import javabot.JNIBWAPI;
import javabot.model.Map;
import javabot.model.Unit;
import javabot.operators.Operators;
import javabot.types.UnitType.UnitTypes;

public class TileGraph {

	public static int sizeX;
	public static int sizeY;
	public static JNIBWAPI bwapi;
	public static Map map;
	
	public static Tile tiles[][];
	
	public static void init(JNIBWAPI bw, int homePositionX, int homePositionY) {
		bwapi = bw;
		map = bwapi.getMap();
		
		sizeX = map.getWidth();
		sizeY = map.getHeight();
		
		tiles = new Tile[sizeY][sizeX];
		
		for (int i = 0; i < sizeY; i++)
		{
			for (int j = 0; j < sizeX; j++)
			{
				Tile t = new Tile(j, i);
				
				t.buildingValue = 0;
				if (map.isBuildable(j, i))
					t.buildingValue = 1;
				tiles[i][j] = t;
			}
		}
		
		for (int i = 0; i < sizeY; i++)
		{
			for (int j = 0; j < 10; j++)
			{
				Tile t = tiles[i][j];
				t.BordersMap = 100 - 10*j;
			}
		}
		
		for (int i = 0; i < sizeY; i++)
		{
			for (int j = sizeX - 11; j < sizeX; j++)
			{
				Tile t = tiles[i][j];
				t.BordersMap = 10*(j - sizeX + 11);
			}
		}
		
		for (int i = 0; i < 10; i++)
		{
			for (int j = 5; j < sizeX - 5; j++)
			{
				Tile t = tiles[i][j];
				t.BordersMap = 100 - 10*i;
			}
		}
		
		for (int i = sizeY - 11; i < sizeY; i++)
		{
			for (int j = 5; j < sizeX - 5; j++)
			{
				Tile t = tiles[i][j];
				t.BordersMap = 10*(i - sizeY + 11);
			}
		}
	}
	
	public static void update()
	{
		for (int i = 0; i < sizeY; i++)
		{
			for (int j = 0; j < sizeX; j++)
			{
				Tile t = tiles[i][j];
				t.update();
			}
		}
	}

	public static boolean OkPos(int i, int j) {
		return (i >= 0 && j >= 0 && i < sizeY && j < sizeX);
	}

	public static void debug() {
		for (int i = 0; i < sizeY; i++)
		{
			for (int j = 0; j < sizeX; j++)
			{
				Tile t = tiles[i][j];
				t.debug();
			}
		}
	}
	
	public static void fillBuilding(Unit unit) {
		
		if (unit.getTypeID() == UnitTypes.Zerg_Extractor.ordinal())
			return;
		
		int tileX = unit.getTileX();
		int tileY = unit.getTileY();
		
		Point size = Operators.getSize(unit.getTypeID());
		
		for (int Y = tileY; Y < tileY + size.y; Y++)
		{
			if (Y < 0 || Y >= tiles.length) continue;
			for (int X = tileX; X < tileX + size.x; X++)
			{
				if (X < 0 || X >= tiles[0].length) continue;
				tiles[Y][X].buildingValue = 2;
			}
		}
	}

	public static Tile getTile(int Y, int X) {
		if (Y < 0 || Y >= tiles.length || X < 0 || X >= tiles[0].length) return null;
		return tiles[Y][X];
	}

	
}
