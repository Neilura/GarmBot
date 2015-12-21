package javabot.operators;

import java.awt.Point;

import javabot.managers.BaseManager;
import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;

public class Operators {
	
	public static int iterHatchery;
	public static int iterPool;
	public static int iterExtractor;
	public static int iterHydra;
	public static int iterLair;
	public static int iterQueenNest;
	public static int iterSpire;
	public static int iterHive;
	public static int iterGreatSpire;
	public static int iterEvolutionChamber;
	public static int iterInfestedCC;
	
	public static int iterDrones;
	public static int iterOverlords;
	public static int iterZerglings;
	public static int iterHydralisks;
	public static int iterQueens;
	public static int iterMutalisks;
	public static int iterGuardians;
	public static int iterDevourers;
	
	public static String stringFromType(int type)
	{
		if (type == UnitTypes.Zerg_Larva.ordinal())
			return "Larva";
		if (type == UnitTypes.Zerg_Drone.ordinal())
			return "Drone";
		if (type == UnitTypes.Zerg_Overlord.ordinal())
			return "Overlord";
		if (type == UnitTypes.Zerg_Egg.ordinal())
			return "Egg";
		if (type == UnitTypes.Zerg_Zergling.ordinal())
			return "Zergling";
		if (type == UnitTypes.Zerg_Hydralisk.ordinal())
			return "Hydra";
		if (type == UnitTypes.Zerg_Queen.ordinal())
			return "Queen";
		if (type == UnitTypes.Zerg_Broodling.ordinal())
			return "Broodlg";
		if (type == UnitTypes.Zerg_Mutalisk.ordinal())
			return "Mutalisk";
		if (type == UnitTypes.Zerg_Guardian.ordinal())
			return "Guardian";
		if (type == UnitTypes.Zerg_Devourer.ordinal())
			return "Devourer";
		
		if (type == UnitTypes.Zerg_Hatchery.ordinal())
			return "Hatchery";
		if (type == UnitTypes.Zerg_Spawning_Pool.ordinal())
			return "Pool";
		if (type == UnitTypes.Zerg_Evolution_Chamber.ordinal())
			return "Evo Chamber";
		if (type == UnitTypes.Zerg_Extractor.ordinal())
			return "Extractor";
		if (type == UnitTypes.Zerg_Hydralisk_Den.ordinal())
			return "Hydra Den";
		if (type == UnitTypes.Zerg_Creep_Colony.ordinal())
			return "Crp Colony";
		if (type == UnitTypes.Zerg_Sunken_Colony.ordinal())
			return "Snk Colony";
		if (type == UnitTypes.Zerg_Spore_Colony.ordinal())
			return "Spo Colony";
		if (type == UnitTypes.Zerg_Lair.ordinal())
			return "Lair";
		if (type == UnitTypes.Zerg_Queens_Nest.ordinal())
			return "Q Nest";
		if (type == UnitTypes.Zerg_Spire.ordinal())
			return "Spire";
		if (type == UnitTypes.Zerg_Hive.ordinal())
			return "Hive";
		if (type == UnitTypes.Zerg_Greater_Spire.ordinal())
			return "G Spire";
		if (type == UnitTypes.Zerg_Infested_Command_Center.ordinal())
			return "Infested CC";
		
		return "";
	}

	public static int unitTypeFromIter(int i) {
		if (i == 0)
			return UnitTypes.Zerg_Larva.ordinal();
		if (i == 1)
			return UnitTypes.Zerg_Drone.ordinal();
		if (i == 2)
			return UnitTypes.Zerg_Overlord.ordinal();
		if (i == 3)
			return UnitTypes.Zerg_Zergling.ordinal();
		if (i == 4)
			return UnitTypes.Zerg_Hydralisk.ordinal();
		if (i == 5)
			return UnitTypes.Zerg_Queen.ordinal();
		if (i == 6)
			return UnitTypes.Zerg_Broodling.ordinal();
		if (i == 7)
			return UnitTypes.Zerg_Mutalisk.ordinal();
		if (i == 8)
			return UnitTypes.Zerg_Guardian.ordinal();
		if (i == 9)
			return UnitTypes.Zerg_Devourer.ordinal();
		
		return UnitTypes.Zerg_Drone.ordinal();
	}
	
	public static int buildingTypeFromIter(int i) {
		if (i == 0)
			return UnitTypes.Zerg_Hatchery.ordinal();
		if (i == 1)
			return UnitTypes.Zerg_Spawning_Pool.ordinal();
		if (i == 2)
			return UnitTypes.Zerg_Extractor.ordinal();
		if (i == 3)
			return UnitTypes.Zerg_Hydralisk_Den.ordinal();
		if (i == 4)
			return UnitTypes.Zerg_Creep_Colony.ordinal();
		if (i == 5)
			return UnitTypes.Zerg_Sunken_Colony.ordinal();
		if (i == 6)
			return UnitTypes.Zerg_Lair.ordinal();
		if (i == 7)
			return UnitTypes.Zerg_Queens_Nest.ordinal();
		if (i == 8)
			return UnitTypes.Zerg_Spire.ordinal();
		if (i == 9)
			return UnitTypes.Zerg_Hive.ordinal();
		if (i == 10)
			return UnitTypes.Zerg_Greater_Spire.ordinal();
		if (i == 11)
			return UnitTypes.Zerg_Evolution_Chamber.ordinal();
		if (i == 12)
			return UnitTypes.Zerg_Spore_Colony.ordinal();
		if (i == 13)
			return UnitTypes.Zerg_Infested_Command_Center.ordinal();

		return UnitTypes.Zerg_Hatchery.ordinal();
	}

	public static int unitIterFromType(int typeID) {
		if (typeID == UnitTypes.Zerg_Larva.ordinal())
			return 0;
		if (typeID == UnitTypes.Zerg_Drone.ordinal())
			return 1;
		if (typeID == UnitTypes.Zerg_Overlord.ordinal())
			return 2;
		if (typeID == UnitTypes.Zerg_Zergling.ordinal())
			return 3;
		if (typeID == UnitTypes.Zerg_Hydralisk.ordinal())
			return 4;
		if (typeID == UnitTypes.Zerg_Queen.ordinal())
			return 5;
		if (typeID == UnitTypes.Zerg_Broodling.ordinal())
			return 6;
		if (typeID == UnitTypes.Zerg_Mutalisk.ordinal())
			return 7;
		if (typeID == UnitTypes.Zerg_Guardian.ordinal())
			return 8;
		if (typeID == UnitTypes.Zerg_Devourer.ordinal())
			return 9;

		return 0;
	}
	
	public static int buildingIterFromType(int typeID) {
		
		if (typeID == UnitTypes.Zerg_Hatchery.ordinal())
			return 0;
		if (typeID == UnitTypes.Zerg_Spawning_Pool.ordinal())
			return 1;
		if (typeID == UnitTypes.Zerg_Extractor.ordinal())
			return 2;
		if (typeID == UnitTypes.Zerg_Hydralisk_Den.ordinal())
			return 3;
		if (typeID == UnitTypes.Zerg_Creep_Colony.ordinal())
			return 4;
		if (typeID == UnitTypes.Zerg_Sunken_Colony.ordinal())
			return 5;
		if (typeID == UnitTypes.Zerg_Lair.ordinal())
			return 6;
		if (typeID == UnitTypes.Zerg_Queens_Nest.ordinal())
			return 7;
		if (typeID == UnitTypes.Zerg_Spire.ordinal())
			return 8;
		if (typeID == UnitTypes.Zerg_Hive.ordinal())
			return 9;
		if (typeID == UnitTypes.Zerg_Greater_Spire.ordinal())
			return 10;
		if (typeID == UnitTypes.Zerg_Evolution_Chamber.ordinal())
			return 11;
		if (typeID == UnitTypes.Zerg_Spore_Colony.ordinal())
			return 12;
		if (typeID == UnitTypes.Zerg_Infested_Command_Center.ordinal())
			return 13;
		
		return -1;
	}
	
	public static boolean buildingIterFromZergType(int typeID) {
		return (typeID == UnitTypes.Zerg_Hatchery.ordinal()
		 || typeID == UnitTypes.Zerg_Extractor.ordinal()
		 || typeID == UnitTypes.Zerg_Spawning_Pool.ordinal()
		 || typeID == UnitTypes.Zerg_Creep_Colony.ordinal()
		 || typeID == UnitTypes.Zerg_Defiler_Mound.ordinal()
		 || typeID == UnitTypes.Zerg_Evolution_Chamber.ordinal()
		 || typeID == UnitTypes.Zerg_Greater_Spire.ordinal()
		 || typeID == UnitTypes.Zerg_Hive.ordinal()
		 || typeID == UnitTypes.Zerg_Hydralisk_Den.ordinal()
		 || typeID == UnitTypes.Zerg_Infested_Command_Center.ordinal()
		 || typeID == UnitTypes.Zerg_Lair.ordinal()
		 || typeID == UnitTypes.Zerg_Nydus_Canal.ordinal()
		 || typeID == UnitTypes.Zerg_Queens_Nest.ordinal()
		 || typeID == UnitTypes.Zerg_Spire.ordinal()
		 || typeID == UnitTypes.Zerg_Spore_Colony.ordinal()
		 || typeID == UnitTypes.Zerg_Sunken_Colony.ordinal()
		 || typeID == UnitTypes.Zerg_Ultralisk_Cavern.ordinal());
	}
	
	public static boolean buildingIterFromProtossType(int typeID) {
		return (typeID == UnitTypes.Protoss_Arbiter_Tribunal.ordinal()
		 || typeID == UnitTypes.Protoss_Assimilator.ordinal()
		 || typeID == UnitTypes.Protoss_Citadel_of_Adun.ordinal()
		 || typeID == UnitTypes.Protoss_Cybernetics_Core.ordinal()
		 || typeID == UnitTypes.Protoss_Fleet_Beacon.ordinal()
		 || typeID == UnitTypes.Protoss_Forge.ordinal()
		 || typeID == UnitTypes.Protoss_Gateway.ordinal()
		 || typeID == UnitTypes.Protoss_Nexus.ordinal()
		 || typeID == UnitTypes.Protoss_Observatory.ordinal()
		 || typeID == UnitTypes.Protoss_Photon_Cannon.ordinal()
		 || typeID == UnitTypes.Protoss_Pylon.ordinal()
		 || typeID == UnitTypes.Protoss_Robotics_Facility.ordinal()
		 || typeID == UnitTypes.Protoss_Robotics_Support_Bay.ordinal()
		 || typeID == UnitTypes.Protoss_Shield_Battery.ordinal()
		 || typeID == UnitTypes.Protoss_Stargate.ordinal()
		 || typeID == UnitTypes.Protoss_Templar_Archives.ordinal());
	}
	
	public static boolean buildingIterFromTerranType(int typeID) {
		return (typeID == UnitTypes.Terran_Academy.ordinal()
		 || typeID == UnitTypes.Terran_Armory.ordinal()
		 || typeID == UnitTypes.Terran_Barracks.ordinal()
		 || typeID == UnitTypes.Terran_Bunker.ordinal()
		 || typeID == UnitTypes.Terran_Command_Center.ordinal()
		 || typeID == UnitTypes.Terran_Comsat_Station.ordinal()
		 || typeID == UnitTypes.Terran_Control_Tower.ordinal()
		 || typeID == UnitTypes.Terran_Covert_Ops.ordinal()
		 || typeID == UnitTypes.Terran_Engineering_Bay.ordinal()
		 || typeID == UnitTypes.Terran_Factory.ordinal()
		 || typeID == UnitTypes.Terran_Machine_Shop.ordinal()
		 || typeID == UnitTypes.Terran_Missile_Turret.ordinal()
		 || typeID == UnitTypes.Terran_Nuclear_Silo.ordinal()
		 || typeID == UnitTypes.Terran_Physics_Lab.ordinal()
		 || typeID == UnitTypes.Terran_Refinery.ordinal()
		 || typeID == UnitTypes.Terran_Science_Facility.ordinal()
		 || typeID == UnitTypes.Terran_Starport.ordinal()
		 || typeID == UnitTypes.Terran_Supply_Depot.ordinal());
	}
	
	public static int requiredMinerals(int buildType) {
		
		if (buildType == UnitTypes.Zerg_Overlord.ordinal() || buildType == UnitTypes.Zerg_Hydralisk_Den.ordinal())
			return 100;
		
		if (buildType == UnitTypes.Zerg_Hydralisk.ordinal() || buildType == UnitTypes.Zerg_Spore_Colony.ordinal())
			return 75;
		
		if (buildType == UnitTypes.Zerg_Hatchery.ordinal())
			return 300;
		if (buildType == UnitTypes.Zerg_Spawning_Pool.ordinal())
			return 200;
		
		return 50;
	}
	
	public static int requiredGas(int buildType) {
		if (buildType == UnitTypes.Zerg_Hydralisk.ordinal())
			return 25;
		if (buildType == UnitTypes.Zerg_Hydralisk_Den.ordinal())
			return 50;
		return 0;
	}
	
	public static boolean isBroodlingable(int typeID) {
		if (typeID == UnitTypes.Terran_Marine.ordinal()
		|| typeID == UnitTypes.Terran_Firebat.ordinal())
			return true;
		return false;
	}
	
	public static int broodlingLevel(int typeID) {
		
		if (typeID == UnitTypes.Zerg_Zergling.ordinal()
			|| typeID == UnitTypes.Protoss_Zealot.ordinal())
			return 1;
		
		if (typeID == UnitTypes.Terran_Marine.ordinal()
			|| typeID == UnitTypes.Zerg_Hydralisk.ordinal()
			|| typeID == UnitTypes.Protoss_Dragoon.ordinal())
			return 2;
		
		if (typeID == UnitTypes.Terran_Firebat.ordinal()
			|| typeID == UnitTypes.Terran_Vulture.ordinal()
			|| typeID == UnitTypes.Zerg_Lurker.ordinal()
			|| typeID == UnitTypes.Zerg_Lurker_Egg.ordinal()
			|| typeID == UnitTypes.Zerg_Defiler.ordinal()
			|| typeID == UnitTypes.Protoss_Dark_Templar.ordinal()
			|| typeID == UnitTypes.Protoss_High_Templar.ordinal())
			return 3;
		
		if (typeID == UnitTypes.Terran_Medic.ordinal()
			|| typeID == UnitTypes.Terran_Ghost.ordinal()
			|| typeID == UnitTypes.Terran_Goliath.ordinal()
			|| typeID == UnitTypes.Zerg_Ultralisk.ordinal())
			return 4;
		
		if (typeID == UnitTypes.Terran_Siege_Tank_Siege_Mode.ordinal()
			|| typeID == UnitTypes.Terran_Siege_Tank_Tank_Mode.ordinal())
			return 5;
		
		return 0;
	}
	
	public static boolean isDangerousGround(int typeID) {
		return (typeID == UnitTypes.Terran_Battlecruiser.ordinal()
		 || typeID == UnitTypes.Terran_Bunker.ordinal()
		 || typeID == UnitTypes.Terran_Ghost.ordinal()
		 || typeID == UnitTypes.Terran_Goliath.ordinal()
		 || typeID == UnitTypes.Terran_Marine.ordinal()
		 || typeID == UnitTypes.Terran_Wraith.ordinal()
		 || typeID == UnitTypes.Terran_Firebat.ordinal()
		 || typeID == UnitTypes.Terran_Siege_Tank_Siege_Mode.ordinal()
		 || typeID == UnitTypes.Terran_Siege_Tank_Tank_Mode.ordinal()
		 || typeID == UnitTypes.Terran_Vulture.ordinal()
						 
		 || typeID == UnitTypes.Zerg_Ultralisk.ordinal()
		 || typeID == UnitTypes.Zerg_Hydralisk.ordinal()
		 || typeID == UnitTypes.Zerg_Mutalisk.ordinal()
		 || typeID == UnitTypes.Zerg_Zergling.ordinal()
		 || typeID == UnitTypes.Zerg_Guardian.ordinal()
		 || typeID == UnitTypes.Zerg_Sunken_Colony.ordinal()
				 
		 || typeID == UnitTypes.Protoss_Arbiter.ordinal()
		 || typeID == UnitTypes.Protoss_Archon.ordinal()
		 || typeID == UnitTypes.Protoss_Carrier.ordinal()
		 || typeID == UnitTypes.Protoss_Zealot.ordinal()
		 || typeID == UnitTypes.Protoss_Dark_Templar.ordinal()
		 || typeID == UnitTypes.Protoss_Dragoon.ordinal()
		 || typeID == UnitTypes.Protoss_Photon_Cannon.ordinal()
		 || typeID == UnitTypes.Protoss_Reaver.ordinal()
		 || typeID == UnitTypes.Protoss_Scout.ordinal());
	}
	
	public static boolean isDangerousAir(int typeID) {
		return (typeID == UnitTypes.Terran_Battlecruiser.ordinal()
		 || typeID == UnitTypes.Terran_Bunker.ordinal()
		 || typeID == UnitTypes.Terran_Ghost.ordinal()
		 || typeID == UnitTypes.Terran_Goliath.ordinal()
		 || typeID == UnitTypes.Terran_Marine.ordinal()
		 || typeID == UnitTypes.Terran_Missile_Turret.ordinal()
		 || typeID == UnitTypes.Terran_Valkyrie.ordinal()
		 || typeID == UnitTypes.Terran_Wraith.ordinal()
		 
		 || typeID == UnitTypes.Zerg_Devourer.ordinal()
		 || typeID == UnitTypes.Zerg_Hydralisk.ordinal()
		 || typeID == UnitTypes.Zerg_Mutalisk.ordinal()
		 || typeID == UnitTypes.Zerg_Spore_Colony.ordinal()
		 || typeID == UnitTypes.Zerg_Scourge.ordinal()
		 
		 || typeID == UnitTypes.Protoss_Arbiter.ordinal()
		 || typeID == UnitTypes.Protoss_Archon.ordinal()
		 || typeID == UnitTypes.Protoss_Carrier.ordinal()
		 || typeID == UnitTypes.Protoss_Corsair.ordinal()
		 || typeID == UnitTypes.Protoss_Dragoon.ordinal()
		 || typeID == UnitTypes.Protoss_Photon_Cannon.ordinal()
		 || typeID == UnitTypes.Protoss_Scout.ordinal());
	}
	
	public static boolean isAir(int typeID) {
		return (typeID == UnitTypes.Terran_Battlecruiser.ordinal()
		 || typeID == UnitTypes.Terran_Dropship.ordinal()
		 || typeID == UnitTypes.Terran_Science_Vessel.ordinal()
		 || typeID == UnitTypes.Terran_Valkyrie.ordinal()
		 || typeID == UnitTypes.Terran_Wraith.ordinal()
		 
		 || typeID == UnitTypes.Zerg_Devourer.ordinal()
		 || typeID == UnitTypes.Zerg_Cocoon.ordinal()
		 || typeID == UnitTypes.Zerg_Mutalisk.ordinal()
		 || typeID == UnitTypes.Zerg_Guardian.ordinal()
		 || typeID == UnitTypes.Zerg_Scourge.ordinal()
		 || typeID == UnitTypes.Zerg_Overlord.ordinal()
		 || typeID == UnitTypes.Zerg_Queen.ordinal()
		 
		 || typeID == UnitTypes.Protoss_Arbiter.ordinal()
		 || typeID == UnitTypes.Protoss_Carrier.ordinal()
		 || typeID == UnitTypes.Protoss_Corsair.ordinal()
		 || typeID == UnitTypes.Protoss_Shuttle.ordinal()
		 || typeID == UnitTypes.Protoss_Observer.ordinal()
		 || typeID == UnitTypes.Protoss_Scout.ordinal());
	}
	
	public static boolean isDetector(int typeID) {
		return (typeID == UnitTypes.Terran_Missile_Turret.ordinal()
		 || typeID == UnitTypes.Terran_Science_Vessel.ordinal()
		 
		 || typeID == UnitTypes.Zerg_Overlord.ordinal()
		 || typeID == UnitTypes.Zerg_Spore_Colony.ordinal()
				
		 || typeID == UnitTypes.Protoss_Observer.ordinal()
		 || typeID == UnitTypes.Protoss_Photon_Cannon.ordinal());
	}
	
	public static boolean isCritter(int typeID) {
		if (typeID == UnitTypes.Critter_Bengalaas.ordinal()
		 || typeID == UnitTypes.Critter_Kakaru.ordinal()
		 || typeID == UnitTypes.Critter_Ragnasaur.ordinal()
		 || typeID == UnitTypes.Critter_Rhynadon.ordinal()
		 || typeID == UnitTypes.Critter_Scantid.ordinal()
		 || typeID == UnitTypes.Critter_Ursadon.ordinal())
			return true;
		return false;
	}
	
	public static int getRange(int typeID) {
		if (typeID == UnitTypes.Terran_Siege_Tank_Siege_Mode.ordinal())
			return 350;
		if (typeID == UnitTypes.Terran_Marine.ordinal())
			return 220;
		if (typeID == UnitTypes.Terran_Firebat.ordinal())
			return 140;
		if (typeID == UnitTypes.Terran_Missile_Turret.ordinal())
			return 310;
		if (typeID == UnitTypes.Terran_Goliath.ordinal())
			return 285;
		
		if (typeID == UnitTypes.Protoss_Zealot.ordinal())
			return 140;
		if (typeID == UnitTypes.Protoss_Dark_Templar.ordinal())
			return 130;
		if (typeID == UnitTypes.Protoss_Photon_Cannon.ordinal())
			return 310;
		
		if (typeID == UnitTypes.Zerg_Zergling.ordinal())
			return 140;
		if (typeID == UnitTypes.Zerg_Ultralisk.ordinal())
			return 140;
		if (typeID == UnitTypes.Zerg_Hydralisk.ordinal())
			return 240;
		if (typeID == UnitTypes.Zerg_Broodling.ordinal())
			return 120;
		if (typeID == UnitTypes.Zerg_Overlord.ordinal())
			return 300;
		if (typeID == UnitTypes.Zerg_Spore_Colony.ordinal())
			return 310;
		
		return 270;
	}

	public static boolean isAbuilding(int typeID) {
		return (buildingIterFromZergType(typeID));
	}
	
	public static boolean isAbuildingAll(int typeID) {
		return (buildingIterFromZergType(typeID) || buildingIterFromTerranType(typeID) || buildingIterFromProtossType(typeID));
	}

	public static void init() {
		iterHatchery = buildingIterFromType(UnitTypes.Zerg_Hatchery.ordinal());
		iterPool = buildingIterFromType(UnitTypes.Zerg_Spawning_Pool.ordinal());
		iterExtractor = buildingIterFromType(UnitTypes.Zerg_Extractor.ordinal());
		iterHydra = buildingIterFromType(UnitTypes.Zerg_Hydralisk_Den.ordinal());
		iterLair = buildingIterFromType(UnitTypes.Zerg_Lair.ordinal());
		iterQueenNest = buildingIterFromType(UnitTypes.Zerg_Queens_Nest.ordinal());
		iterSpire = buildingIterFromType(UnitTypes.Zerg_Spire.ordinal());
		iterHive = buildingIterFromType(UnitTypes.Zerg_Hive.ordinal());
		iterGreatSpire = buildingIterFromType(UnitTypes.Zerg_Greater_Spire.ordinal());
		iterEvolutionChamber = buildingIterFromType(UnitTypes.Zerg_Evolution_Chamber.ordinal());
		iterInfestedCC = buildingIterFromType(UnitTypes.Zerg_Infested_Command_Center.ordinal());
		
		iterDrones = unitIterFromType(UnitTypes.Zerg_Drone.ordinal());
		iterOverlords = unitIterFromType(UnitTypes.Zerg_Overlord.ordinal());
		iterZerglings = unitIterFromType(UnitTypes.Zerg_Zergling.ordinal());
		iterHydralisks = unitIterFromType(UnitTypes.Zerg_Hydralisk.ordinal());
		iterQueens = unitIterFromType(UnitTypes.Zerg_Queen.ordinal());
		iterMutalisks = unitIterFromType(UnitTypes.Zerg_Mutalisk.ordinal());
		iterGuardians = unitIterFromType(UnitTypes.Zerg_Guardian.ordinal());
		iterDevourers = unitIterFromType(UnitTypes.Zerg_Devourer.ordinal());
	}

	public static int distance(BaseManager baseManager, Unit u) {
		return (int)Math.sqrt((baseManager.posX - u.getX()) * (baseManager.posX - u.getX()) + (baseManager.posY - u.getY()) * (baseManager.posY - u.getY()));
	}

	public static int distance(Unit unit, Unit u) {
		return (int)Math.sqrt((unit.getX() - u.getX()) * (unit.getX() - u.getX()) + (unit.getY() - u.getY()) * (unit.getY() - u.getY()));
	}

	public static int distance(Unit unit, int x, int y) {
		return (int)Math.sqrt((unit.getX() - x) * (unit.getX() - x) + (unit.getY() - y) * (unit.getY() - y));
	}
	
	public static int distance(int x1, int y1, int x, int y) {
		return (int)Math.sqrt((x1 - x) * (x1 - x) + (y1 - y) * (y1 - y));
	}

	public static Point getSize(int typeID) {
		if (typeID == UnitTypes.Zerg_Hatchery.ordinal()
				|| typeID == UnitTypes.Zerg_Lair.ordinal()
				|| typeID == UnitTypes.Zerg_Hive.ordinal())
			return new Point(4, 3);
		if (typeID == UnitTypes.Zerg_Extractor.ordinal())
			return new Point(4, 2);
		if (typeID == UnitTypes.Zerg_Creep_Colony.ordinal()
				|| typeID == UnitTypes.Zerg_Sunken_Colony.ordinal()
				|| typeID == UnitTypes.Zerg_Spore_Colony.ordinal()
				|| typeID == UnitTypes.Zerg_Spire.ordinal()
				|| typeID == UnitTypes.Zerg_Greater_Spire.ordinal())
			return new Point(2, 2);
		
		return new Point(3, 2);
	}

	public static boolean requirePsi(int typeID) {
		if (typeID == UnitTypes.Protoss_Pylon.ordinal())
			return false;
		return true;
	}

	public static void normalize(Point p) {
		double norme = Math.sqrt(p.x*p.x + p.y*p.y);
		p.x/=norme;
		p.y/=norme;
	}
}
