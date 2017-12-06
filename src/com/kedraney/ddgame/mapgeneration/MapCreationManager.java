package com.kedraney.ddgame.mapgeneration;

import java.math.*;
import java.sql.*;
import java.util.*;

public class MapCreationManager
{
	private static MapRandomGen mapMaker;//mapMaker mapMaker make me a map, find me a man, catch me a catch
	private static MapAltitudeGen heightProfiler;//Creates an altitude map
	private static MapParser mapParse;//Creates arraylists for each contiguous land mass
	private static MapForestBuilder mapForestBuilder;//creates the forest distribution
	private static MapRiverMaker mapRiverMaker;
//	private static MapRivers mapRiverCrafter;//crafts the rivers of the map
//	private static RaceTraitCalculator raceTraitCalculator;//calculates trait maps
//	private static TraitManager traitManager;//essentially is the creator of political boundaries and what not
	private static int size;
	
	private static int[][][] mapAttributes;
	
	public static void main(String[] args)
	{
		mapMaker = new MapRandomGen();
		size = mapMaker.getSize();//              0       1         2       3
		mapAttributes = new int[4][size][size];//map, altitudes, forests, rivers
		mapAttributes[0] = mapMaker.getMap();
		heightProfiler = new MapAltitudeGen(mapAttributes[0], 1.5);//Use a slider with .1 increments ranged from 0.0-2.0
		mapAttributes[1] = heightProfiler.getMap();//               1.1 default
		int totalRaces = 3;

		for(int y = 0; y < size; y++)//This section just makes sure the two maps are the same
		{
			for(int x = 0; x < size; x++)
			{
				if(mapAttributes[1][y][x] > -1)
					mapAttributes[0][y][x] = 1;
				else
					mapAttributes[0][y][x] = 0;
			}
		}
		//Now that we know it's all the same, we begin with the more specific creation features
		mapForestBuilder = new MapForestBuilder(.75, mapAttributes[0], mapAttributes[1]);
		mapRiverMaker = new MapRiverMaker(mapAttributes);
		mapAttributes[2] = mapForestBuilder.getForest();

//		raceTraitCalculator = new RaceTraitCalculator(mapAttributes);

//		double[][][] traitTester = new double[2][size][size];

//		traitTester[0] = raceTraitCalculator.getMountainDwellers();
//		traitTester[1] = raceTraitCalculator.getForestDwellers();

//		traitManager = new TraitManager(applicableRaces, totalRaces, mapAttributes, traitTester);


/*		for(int i = 0; i < traitTester.length; i++)
		{
			for(int y = 0; y < size; y++)
			{
				for(int x = 0; x < size; x++)
				{
					System.out.print(traitTester[i][y][x] + " ");
				}
				System.out.println();
			}
			System.out.println("--------------------" + "Trait map " + i + "above" + "-------------------");
		}*/
		//                                     -1 for forets
		for(int i = 0; i < mapAttributes.length-2; i++)
		{
			for(int y = 0; y < size; y++)
			{
				for(int x = 0; x < size; x++)
				{
					System.out.print(mapAttributes[i][y][x] + " ");
				}
				System.out.println();
			}
			System.out.println("\n-----------------------MAP_UP--" + i + "--------------------------------\n");
		}
/*
		mapRiverCrafter = new MapRivers(mapAttributes[0], mapAttributes[1], .1, 0.0, .02);
		mapAttributes[3] = mapRiverCrafter.getRivers();

		for(int y = 0; y < size; y++)
		{
			for(int x = 0; x < size; x++)
			{
				System.out.print(mapAttributes[3][y][x] + " ");
			}
			System.out.println();
		}*/


//		mapParse = new MapParser(mapAttributes[0], mapAttributes[1]);
//		ArrayList<LandMass> islands = mapParse.getIslands();
		
/*		LandMass ttter;
		ArrayList<MapNode> inter;
		ArrayList<MapNode> bound;
		
		for(int x = 0; x < islands.size(); x++)
		{
			ttter = islands.get(x);
			inter = ttter.getInteriors();
			bound = ttter.getBoundaries();
			
			System.out.println();
		}*/
	}
}