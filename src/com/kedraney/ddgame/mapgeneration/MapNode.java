package com.kedraney.ddgame.mapgeneration;

import java.util.*;
import java.math.*;

public class MapNode implements Comparable<MapNode>
{//note that mapnode DOES NOT work with maps where size > 100 due to IDs
	private int x;
	private int y;
	private int id;
	private int altitude;
	private boolean land;
	
	public MapNode()
	{
		//0,0 is part of the boundary
		this(1, 1, 0);
	}
	
	public MapNode(int ID)
	{
		this(ID % 100,( ID - (ID % 100))/100, 0);
	}
	
	public MapNode(int xPos, int yPos)
	{
		this(xPos, yPos, 0);
	}
	
	public MapNode(int xPos, int yPos, int height)
	{
		x = xPos;
		y = yPos;
		altitude = height;
		setID();
		
		if(height < 0)
			land = false;
		else
			land = true;
	}
	
	public MapNode(int xPos, int yPos, int[][] mappy)
	{
		this(xPos, yPos, mappy[yPos][xPos]);
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}

	public int getAltitude()
	{
		return altitude;
	}

	private void setID()
	{
		id = (100*y) + x;//id is basically x + 100*y
	}
	
	//in case you want to get an ID without making a map node
	public static int createID(int x, int y)
	{
		return (100*y) + x;
	}

	//these two return the x and y components of an ID
	public static int getIDX(int id)
	{
		return id % 100;
	}

	public static int getIDY(int id)
	{
		return (int)((id - (id % 100))/100);
	}
	
	public int getID()
	{
		return id;
	}
	
	public int compareTo(MapNode otherNode)
	{
		int t = id - otherNode.getID();
		
		if(id == 0)
			return 0;
		else if(id < 0)
			return -1;
		
		return 1;
	}
}