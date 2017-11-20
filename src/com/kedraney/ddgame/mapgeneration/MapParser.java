package com.kedraney.ddgame.mapgeneration;

import java.util.*;
import java.math.*;

public class MapParser
{
	ArrayList<LandMass> islands = new ArrayList<>();//actually all land masses, not just islands
	int[][] map;//NOT THE ALTITUDE MAP - just the land ones
	int size;
	int[][] altitudes;
	
	public MapParser(int[][] theMap, int[][] alts)//I'm the map I'm the map I'm the map I'm the map!
	{
		boolean stillLand = false;
		boolean tester;
		map = theMap;
		altitudes = alts;
		size = map.length;
		
		for(int y = 1; y < theMap.length - 1; y++)//Goes across the whole map
		{
			for(int x = 1; x < theMap.length - 1; x++)
			{
//				System.out.println("(" + x + ", " + y + ")");
				if(theMap[y][x] == 1 && !stillLand && (x!= 0 && y!= 0 && x != size-1 && y != size-1))
				{
					tester = false;
					for(int c = 0; c < islands.size(); c++)
					{//makes sure that the land IS NOT in another land mass
						if(islands.get(c).hasLand(x, y))
						{
							tester = true;
							
						}
					}
//					System.out.println("Before if after TESTER Loop");
					if(!tester)
					{
//						System.out.println("IN if: " + islands.size() + "landMasses in islands");
/*						if(islands.size() > 0)
						{
//							System.out.println("size = " + islands.get(0).approxSize());
							ArrayList<MapNode> b = islands.get(0).getBoundaries();
							for(MapNode c : b)
								System.out.println(c.getID());
								
							b = islands.get(0).getInteriors();
							System.out.println("----------------");
							
							for(MapNode c : b)
								System.out.println(c.getID());
								
							System.out.println("----------------");
						}*/
						
						if(islands.size() == 0)
						{
							islands.add(new LandMass(x, y, map, alts));//no land masses present that this could be a part of
						}
						else
						{
							for(int t = 0; t < islands.size(); t++)
							{
								if(islands.get(t).hasLand(x, y))
								{
									stillLand = true;//i.e. the land IS present in this land mass
									t = islands.size() + 1;//end the loop
								}
							}
							
							if(!stillLand)
							{
								islands.add(new LandMass(x, y, map, alts));//not in any land masses so it's a new land mass
							}
						}
					}
				}
				else if(stillLand && theMap[y][x] == 0)
				{
					stillLand = false;//It's now on water, so set still land to false
				}
			}
		}
//		System.out.println("WEAREHERE!");
		checkLandOwnerships();
//		System.out.println("We were here.");
	}
	
	public ArrayList<LandMass> getIslands()
	{
		return islands;
	}
	//makes sure that every single plot of land has only one island it's a part of
	private void checkLandOwnerships()
	{
		LandMass current;
		LandMass tester;
		ArrayList<MapNode> inter;
		ArrayList<MapNode> bound;
		boolean hold = false;
		
		for(int c = 0; c < islands.size(); c++)
		{
			current = islands.get(c);
			inter = current.getInteriors();
			bound = current.getBoundaries();
			hold = false;
			//Goes throw islands and, if it's bigger than the other, and if they share a piece of land, it removes it from the bigger island
			//and then it replaces the current landmass with the corrected one
			for(int x = 0; x < islands.size(); x++)
			{
				if(x!= c && current.approxSize() > islands.get(x).approxSize())
				{
					tester = islands.get(x);
					
					for(int v = 0; v < inter.size(); v++)
					{
						if(tester.hasLand(inter.get(v).getID()))
						{
							hold = true;
							current.removeLand(inter.get(v).getID());
							v = inter.size();
						}
					}
					
					if(!hold)
					{
						for(int v = 0; v < bound.size(); v++)
						{
							if(tester.hasLand(bound.get(v).getID()))
							{
								hold = true;
								current.removeLand(bound.get(v).getID());
								v = bound.size();
							}
						}
					}
				}
			}
			
			if(hold)
			{
				islands.set(c, current);
			}
		}
	}
}