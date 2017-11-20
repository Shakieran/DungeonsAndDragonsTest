package com.kedraney.ddgame.mapgeneration;

import java.math.*;
import java.util.*;

public class MapForestBuilder
{
	final int[][] map;
	final int[][] altitude;
	int[][] forest;
	final int size;
	final double density;//a double that ranges from 0 for no trees to X for an average and 1 for literally trees on every piece of land
	final int sumBounds = 4;
	
	public MapForestBuilder(double dense, int[][] daMap, int[][] alts)
	{
/*		int peak = 0;
		for(int y = 1; y < size; y++)
		{
			for(int x = 1; x < size; x++)
			{
				if(alts[y][x] > peak)
					peak = alts[y][x];
			}
		}*/
		
		size = daMap.length;
		altitude = alts;
		map = daMap;
		density = dense;
		forest = new int[size][size];
		
		for(int y = 1; y < size -1; y++)
		{
			for(int x = 1; x < size -1; x++)
			{
				if(map[y][x] == 1)// 5 means no road, and ride/path size can(but doesn't have to) grow as forest density decreases
				{
					forest[y][x] = (int) (Math.random() + (1 / 3.0) * density + (2 / 3.0) * density * (getForestValue(altitude[y][x])));//5 is max density, 1 is minimum density, and 0 just means no forest(trees can be there, but no forest so yeah
					if(forest[y][x] > 1)
						forest[y][x] = 1;
				}
				else
					forest[y][x] = -1;
			}
		}
		
		for(int i = 0; i < (size/100); i++)
		{
			smooth();
		}
		
		//Now that we have the final placement of forests, we start adding the densities
		
		for(int i = 2; i < 5; i++)//goes over
		{
			for(int y = 1; y < size - 1; y++)
			{
				for(int x = 1; x < size - 1; x++)
				{
					if(getForestValueSum(x, y) > sumBounds * (i*.75) && Math.random() < .8)//.75 means 75% chance for up in forest, ect.
					{
						forest[y][x]++;//Yep. . .
					}
				}
			}
		}
//This just sets the boundaries to -1
		for(int i = 0; i < size; i++)
		{
			for(int z = 0; z < size; z++)
			{
				if(i==0 || z==0 || i==size-1 || z==size-1)
					forest[i][z] = -1;
			}
		}
	}
	
	private void smooth()
	{
		for(int y = 1; y < size-1; y++)
		{
			for(int x = 1; x < size-1; x++)
			{
				if(getForestSum(x, y) > sumBounds && altitude[y][x] > -1)
				{
					forest[y][x] = 1;
				}
				else if(altitude[y][x] == 0)
				{
					forest[y][x] = 0;
				}
			}
		}
	}
	
	private int getForestSum(int x, int y)
	{
		int sum = 0;
		
		for(int i = -1; i < 2; i++)
		{
			for(int z = -1; z < 2; z++)
			{
				if(forest[y+i][x+z] > 0)
					sum++;
			}
		}

		if(forest[y][x] > 0)
			sum--;
		
		return sum;
	}

	private int getForestValueSum(int x, int y)
	{
		int sum = 0;

		for(int i = -1; i < 2; i++)
		{
			for(int z = -1; z < 2; z++)
			{
				sum += forest[y + i][x + z];
			}
		}
		sum -= forest[y][x];

		return sum;
	}
	
	private double getForestValue(int height)
	{//This works essentially by making heights below 11 return a set value and then a function for the value for values over 11
		switch(height)
		{
			case 3:
			case 2:
				return 1;
				
			case 1:
			case 4:
				return .8;
				
			case 5:
			case 6:
				return .6;
				
			case 7:
			case 8:
				return .4;
				
			case 0:
			case 9:
			case 10:
				return .2;
			
			default:
				return .2*(1-(height-10)/20.0);//at 20, no longer can trees survive
		}
	}

	public int[][] getForest()
	{
		return forest;
	}
}