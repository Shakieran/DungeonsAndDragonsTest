package com.kedraney.ddgame.mapgeneration;

import java.math.*;
import java.util.*;

public class LandMass
{
	private ArrayList<MapNode> boundaries = new ArrayList<>();
	//CURRENTLY INTERIOR WILL NEVER BE FILLED
	private ArrayList<MapNode> interior = new ArrayList<>();
	private boolean[] landIDS;//if you add land, set this to true, remove and set it to false
	private int size;
	private int[][] map;
	private int[][] altitudes;
//	private enum Direction {NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST};
	private int cur;
	private int xStart;
	private int yStart;//The x and y coordinates of the first node in the boundaries arraylist
	//send the altitude map
	private int maxHeight;
	private int heightID;
	private int maxDepth;
	private int maxLeft;
	private int leftID;
	private int maxRight;
	private int COUNTER = 1;
	
	public LandMass(int x, int y, int[][] map, int[][] alts)
	{
//		System.out.println("IN A LANDMASS!");
//		System.out.println("Landmass" + COUNTER + "  -  (" + x + ", " + y + ")");
		COUNTER++;
		this.map = map;
		altitudes = alts;
		size = map.length;
		
		boolean useCompound = false;
		int t;
		ArrayList<Integer> nearnodes = nearNodes(x,y, false);
		boolean[] nearnodesb;
//		Direction direction;
		MapNode currentNode;
		
		maxLeft = x;
		maxRight = x;
		maxHeight = y;
		maxDepth = y;
		
		landIDS = new boolean[size*size];
		
		boundaries.add(new MapNode(x, y, alts[y][x]));
		
		landIDS[boundaries.get(0).getID()] = true;
		if(nearnodes.size() > 0)
		{
			t = nearnodes.get(0);
			
	//		System.out.println("Before switch");
			
			switch(t)//try only going north, south, east and west
			{
				case 1:
	//				direction = Direction.NORTH;
					currentNode = new MapNode(x, y+1, alts[y][x]);
					cur = 1;
					break;
					
				case 2:
	//				direction = Direction.NORTHEAST;
					currentNode = new MapNode(x+1, y+1, alts[y][x]);
					cur = 2;
					break;
					
				case 3:
	//				direction = Direction.EAST;
					currentNode = new MapNode(x+1, y, alts[y][x]);
					cur = 3;
					break;
					
				case 4:
	//				direction = Direction.SOUTHEAST;
					currentNode = new MapNode(x+1, y-1, alts[y][x]);
					cur = 4;
					break;
					
				case 5:
	//				direction = Direction.SOUTH;
					currentNode = new MapNode(x, y-1, alts[y][x]);
					cur = 5;
					break;
					
				case 6:
	//				direction = Direction.SOUTHWEST;
					currentNode = new MapNode(x-1, y-1, alts[y][x]);
					cur = 6;
					break;
					
				case 7:
	//				direction = Direction.WEST;
					currentNode = new MapNode(x-1, y, alts[y][x]);
					cur = 7;
					break;
					
				case 8:
	//				direction = Direction.NORTHWEST;
					currentNode = new MapNode(x-1, y+1, alts[y][x]);
					cur = 8;
					break;
								
				default:
					currentNode = new MapNode(x, y, alts[y][x]);
					break;
			}
	//		System.out.println("Landmass" + COUNTER + "  -  (" + currentNode.getX() + ", " + currentNode.getY() + ")");//2
			COUNTER++;
	//		System.out.println("After switch before while");
					
			while(!(currentNode.getX() == x && currentNode.getY() == y))
			{//while throwaway boolean is true which is set to false, or current node is once again the first node
	//			nearnodes = currentNode.nearNodes(currentNode.getX(), currentNode.getY());
				boundaries.add(currentNode);
				landIDS[currentNode.getID()] = true;
	//			System.out.println("In while");
				try
				{
					nearnodesb = nearNodesB(currentNode.getX(), currentNode.getY(), false);//nodes that are nearby
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					nearnodesb = new boolean[8];
					System.out.println("(" + currentNode.getX() + ", " + currentNode.getY() + ")");
					System.exit(0);
				}
				
	//			if(COUNTER == 3)
	//			{
	//				System.out.println(nearnodesb[7] + " - " + nearnodesb[0] + " - " + nearnodesb[1]);
	//				System.out.println(nearnodesb[6] + " -      " + " - " + nearnodesb[2]);
	//				System.out.println(nearnodesb[5] + " - " + nearnodesb[4] + " - " + nearnodesb[3]);
	//			}
				
				t = cur - 2;//Instead of starting east, start north and go down to east in case it isn't actually east yet per se
				
				if(t < 1)//Just makes sure that t stays within the bounds 0 < t < 9
					t = 8 + t;//if t=0, t= 8,  if t=-1, t = 7, ect.
	
	
				for(int i = 0; i < 8; i++)
				{
					if(nearnodesb[t-1])
					{			
		//				if(COUNTER % 100000 == 0)
		//					System.out.println("Huh. . . - cur=" + t + "  x=" + x);
								
						cur = t;
						currentNode = new MapNode(getCoordinateX(currentNode.getX(), cur), getCoordinateY(currentNode.getY(), cur));//x and y aren't updated - hence currentNode.getX()
						i = 99;
		//				useCompound = true;
		//				if(COUNTER % 100000 == 0)
		//					System.out.println("Huh. . . - currentNode = " + "(" + currentNode.getX() + ", " + currentNode.getY() + ")");
					}
					else
					{
						t++;
						if(t > 8)
							t = 1;
							
	//					if(i==7 && !useCompound)
	//					{
	//						nearnodesb = nearNodesB(currentNode.getX(), currentNode.getY(), true);
	//					}
					}
				}
				
	//			useCompound = false;
				
				if(maxHeight > currentNode.getY())
				{
					maxHeight = currentNode.getY();
					heightID = currentNode.getID();
				}
					
				if(maxDepth < currentNode.getY())
				{
					maxDepth = currentNode.getY();
				}
					
				if(maxLeft > currentNode.getX())
				{
					maxLeft = currentNode.getX();
					leftID = currentNode.getID();
				}
					
				if(maxRight < currentNode.getX())
				{
					maxRight = currentNode.getX();
				}
					
				COUNTER++;
			}
			// Code will work up to here;
	//		System.out.println("We made it past boundaries!");
			COUNTER = 1;
			int c2 = 0;
			int teeemp = -1;
			
			ArrayList<MapNode> nodes;
			
			for(int i = 0; i < boundaries.size(); i++)
			{
				nodes = nonBoundNodes(boundaries.get(i));
				
				for(MapNode e : nodes)
				{
					interior.add(e);
					landIDS[e.getID()] = true;
				}
			}
//			Above gets all the nodes next to the boundary marked so that we can loop through the interior
//arraylist over and over again until no more are added
			ArrayList<MapNode> additions;
			
			while(COUNTER != 0)//While new land is being added
			{
				COUNTER = 0;
				additions = new ArrayList<>();
				
				
				for(int i = 0; i < interior.size(); i++)
				{
					nodes = nonBoundNodes(interior.get(i));//add each piece of land next to a piece of land already in the landmass
					
					for(MapNode e : nodes)
					{
						if(teeemp == e.getID())
							c2++;
							
						additions.add(e);
						landIDS[e.getID()] = true;
						COUNTER++;
						teeemp = e.getID();
					}
				}
				
				if(c2 > 2)
					System.exit(0);
				
				for(MapNode c : additions)
					interior.add(c);
			}
		}
		else
		{
			//FILL THIS - what happens if it's a 1 cell strip of land
		}
//		printIDS();
	}
	
	//DEPRECATED - NO DUPLICATE VALUES OCCURED! GO TEAM!!!!!
	//removes duplicate values - FUCK THIS, make it work the first time
	private void cleanUp()
	{
		int[] bounds = new int[boundaries.size()];
		ArrayList<MapNode> bound = new ArrayList<MapNode>();
		landIDS = new boolean[landIDS.length];
		
		for(int c = 0; c < boundaries.size(); c++)
		{
			bounds[c] = boundaries.get(c).getID();
		}

		Arrays.sort(bounds);
		bound.add(new MapNode(bounds[0]));
		landIDS[bounds[0]] = true;
		
		for(int c = 1; c < bounds.length; c++)
		{
			if(bounds[c] != bounds[c-1])
			{
				bound.add(new MapNode(bounds[c]));//not a duplicate so add it
				landIDS[bounds[c]] = true;
			}
		}
		
		//-------------------------
		//BOUNDARIES DONE - INTERIOR BEGINS
		//-------------------------
		
		if(interior.size() > 0)
		{
			ArrayList<MapNode> inter = new ArrayList<MapNode>();
			int[] inters = new int[interior.size()];
			
			for(int c = 0; c < interior.size(); c++)
			{
				inters[c] = interior.get(c).getID();
			}
			
			Arrays.sort(inters);
			if(!landIDS[inters[0]])
				inter.add(new MapNode(inters[0]));
				
			for(int c = 1; c < bounds.length; c++)
			{
				if(bounds[c] != bounds[c-1] && !landIDS[inters[c]])
				{
					inter.add(new MapNode(inters[c]));
					landIDS[inters[c]] = true;
				}
			}
			
			interior = inter;
		}
		boundaries = bound;	
	}
	
	public void printIDS()
	{
		for(int c = 0; c < boundaries.size(); c++)
		{
			System.out.println(boundaries.get(c).getID());
		}
		
		for(int c = 0; c < interior.size(); c++)
		{
			System.out.println(interior.get(c).getID());
		}
	}
	
	//if any of the nearby nodes are already in the land mass, then this one is good to be included
	private boolean nextToLandmass(int x, int y)
	{
		for(int i = -1; i < 2; i++)
		{
			for(int z = -1; z < 2; z++)
			{
				if(landIDS[MapNode.createID(x + z, y + i)])
					return true;
			}
		}
		
		return false;
	}
	
	private ArrayList<MapNode> nonBoundNodes(MapNode center)
	{
		int x = center.getX();
		int y = center.getY();
		ArrayList<MapNode> nodes = new ArrayList<>();
		
		for(int i = -1; i < 2; i++)
		{
			for(int z = -1; z < 2; z++)
			{
				if(map[y + i][x + z] == 1 && !landIDS[MapNode.createID(x + z, y + i)])
					nodes.add(new MapNode(x+z, y+i));
			}
		}
		
		return nodes;
	}
	
	//checks if the land is present in the land mass
	public boolean hasLand(int x, int y)
	{//if the cell is a part of this land mass
		return landIDS[MapNode.createID(x,y)];
	}
	
	public boolean hasLand(int id)
	{//if the cell is a part of this land mass
/*		for(int c = 0; c < boundaries.size(); c++)
		{
			if(boundaries.get(c).getID() == id)
				return false;
		}
		
		for(int c = 0; c < interior.size(); c++)
		{
			if(interior.get(c).getID() == id)
				return false;
		}
		
		return true;*/
		
		return landIDS[id];
	}
	
	//removeLands simply remove the specified land from the land mass
	public void removeLand(int x, int y)
	{
		int id = MapNode.createID(x, y);
		removeLand(id);
	}
	
	public void removeLand(int id)
	{
		int count = 0;
		
		for(int c = 0; c < boundaries.size(); c++)
		{
			if(boundaries.get(c).getID() == id)
			{
				boundaries.remove(c);
				landIDS[id] = false;
				count++;
				c = boundaries.size();
			}
		}
		
		if(count < 1)
		{
			for(int c = 0; c < interior.size(); c++)
			{
				if(interior.get(c).getID() == id)
				{
					interior.remove(c);
					landIDS[id] = false;
					c = interior.size();
				}
			}
		}
	}
	//checks if there is sea next to the piece of land
	private boolean hasSea(int x, int y)
	{
		for(int i = -1; i < 2; i++)
		{
			for(int z = -1; z < 2; z++)
			{
				if(map[y+i][x+z] == 0)
					return true;
			}
		}
		
		return false;
	}
	//gets the coordinates of the next "cell"
	private int getCoordinateX(int x, int cur)
	{
		if(cur < 5 && cur > 1)
			return x + 1;
		else if(cur == 5 || cur == 1)
			return x;
		else
			return x - 1;
	}
	
	private int getCoordinateY(int y, int cur)
	{
		if(cur == 7 || cur == 3)
			return y;
		else if(cur > 3 && cur < 7)// 3 < cur < 7 --> cur = (4, 5, 6)
			return y + 1;
		//!(3, 4, 5, 6, 7) so (1, 2, 8)
			
		return y - 1;
	}
	//returns the list of "cells" or "nodes" next to the current one
	private ArrayList<Integer> nearNodes(int x, int y, boolean ttr)
	{
		ArrayList<Integer> nNodes = new ArrayList<>();
		boolean[] temp = nearNodesB(x, y, ttr);
		
		for(int i = 0; i < 8; i++)
		{
			if(temp[i])
				nNodes.add(i+1);
		}
		
		return nNodes;
	}
	
	private boolean[] nearNodesB(int x, int y, boolean compoundDirections)
	{
		boolean[] temp = new boolean[8];
		
		for(int i = -1; i < 2; i++)
		{
			for(int z = -1; z < 2; z++)
			{
				if(!(z == 0 && i == 0) && altitudes[y+i][x+z] > -1)
				{
					if(z==1)
						temp[i + z + 1] = true;//1, 2, or 3
					else if(z==0)
						temp[2 + 2*i] = true;//either 0 or 4
					else
						temp[6 + -1*i] = true;//5, 6, or 7
				}
			}
		}
		
		if(!compoundDirections)
		{
			for(int i = 1; i < 8; i+=2)
			{
				temp[i] = false;//NO compound directions(northeast, southwest, ect.)
			}
		}
		
		return temp;
	}
	
	//gets the approximate size of land to determine the likely hood of a plot of land being in another
	public int approxSize()
	{
		return Math.abs((maxDepth - maxHeight) * (maxRight - maxLeft));
	}
	
	public ArrayList<MapNode> getBoundaries()
	{
		return boundaries;
	}
	
	public ArrayList<MapNode> getInteriors()
	{
		return interior;
	}	
}