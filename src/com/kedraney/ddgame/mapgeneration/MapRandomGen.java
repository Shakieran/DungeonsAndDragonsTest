package com.kedraney.ddgame.mapgeneration;

import java.math.*;
import java.util.*;

public class MapRandomGen
{
	//maps is just how many maps CAN be generated
	private final int maps = 3;
	private final int mapType;
	
	private final int size = 20;//For testing purposes so we don't need to deal with a 100 x 100 grid
	private int sum =  4;
	private int river = 2;
	private double frequency = Math.PI/size;
	
	private int[][] map = new int[size][size];
	
	public MapRandomGen()
	{
//		int whichMap = (int)(Math.random() * maps)+1;
		int whichMap = 1;
		mapType = whichMap;
		
		switch(whichMap)
		{
			case 1:
				islandGen();
				break;
			case 2:
				divideGen();
				break;
			case 3:
				flatlandsGen();
				break;
		}
	}
	
	//FLATLANDS METHODS BEGIN

	public int getMapType()
	{
		return mapType;
	}

	private void flatlandsGen()
	{
		for(int y = 0; y < size; y++)
		{
			for(int x = 0; x < size; x++)
			{
//				map[y][x] = (int)(Math.random() + .75);//Little water
				map[y][x] = 1;
			}
		}
		
		int sum;
		
		for(int c = 0; c < (int)(size/4.0); c++)
		{
			for(int y = 1; y < size-1; y++)
			{
				for(int x = 1; x < size -1; x++)
				{
					sum = 0;
					for(int i = -1; i < 2; i++)
					{
						for(int z  = -1; z < 2; z++)
						{
							if(map[y+i][x+z] == 1)
								sum++;
						}
					}
					
					sum -= map[y][x];
					
					if(sum > 4)
						map[y][x] = 1;
					else
						map[y][x] = 0;
				}
			}
		}
	}
	
	//FLATLANDS METHODS END
	
	//DIVIDE METHODS BEGIN
	
	private void divideGen()
	{
		for(int y = 0; y < size; y++)
		{
			for(int x = 0; x < size; x++)
			{
//				if(x==0 || y==0 || x==size-1 || y==size-1)
//					map[y][x] = 1;
//				else
				map[y][x] = (int)(Math.random() + .25 + .5*Math.abs(Math.cos(frequency * x)) + .3 * Math.abs(Math.sin(frequency * y)));
			}
		}
		
		for(int x = 0; x < (int)(size/25.0); x++)
			smoothenDivide();
	}
	
	private void smoothenDivide()
	{
		int[][] temp = new int[size][size];
		
		for(int y = 1; y < size-1; y++)
		{
			for(int x = 1; x < size-1; x++)
			{
				if(getSumDivide(x, y) > sum)
					temp[y][x] = 1;
				else
					temp[y][x] = 0;
			}
		}
		
		int s = 0;
		
		for(int y = 0; y < size; y++)
		{
			for(int x = 0; x < size; x++)
			{
				if(y==0 || x==0 || y==size-1 || x==size-1)
				{
					for(int i = -1; i < 2; i++)
					{
						for(int z = -1; z < 2; z++)
						{
							try
							{
								s += map[y+i][x+z];
							}
							catch(ArrayIndexOutOfBoundsException e)
							{
								
							}
						}
					}
					
					if(s>2)
						temp[y][x] = 1;
					else
						temp[y][x] = 0;
				}
			}
		}
		
		map = temp;
	}
	
	private int getSumDivide(int x, int y)
	{
		//same as island sum . . . for now
		int sum = 0;
		
		for(int yPos = y-1; yPos < y+2; yPos++)
		{
			for(int xPos = x-1; xPos < x+2; xPos++)
			{
				sum += map[yPos][xPos];
//				System.out.println("sum: " + map[y][x] + "at [" + y + "][" + x + "]");
			}
		}
		
		return sum - map[y][x];
	}
	
	//DIVIDE METHODS END
	
	//ISLAND METHODS BEGIN
	private void islandGen()
	{
		//In case the variables are changed, I will set them at the start of each gen method
		
		for(int y = 0; y < size; y++)
		{
			for(int x = 0; x < size; x++)
			{
//				if(x==0 || y==0 || x==size-1 || y==size-1)
//					map[y][x] = 0;
//				else
				map[y][x] = (int)(Math.random() + .25 + .5 * (((Math.sin(frequency * y)*.5 + Math.sin(frequency * x) * .5) + Math.sin(frequency * y) * Math.sin(frequency * x))/2 + .05 * Math.cos(frequency * x)) + bottomAdjIsland(y));//at x and y = size/2, it's at.75, .25 elsewhere
			}
		}
		
		for(int x = 0; x < (int)(size/10.0); x++)
		{
//			System.out.println(getSum(1+x,1+x));
			
			if(x%2==0)
				smoothenIsland(true);
			else
			{
				smoothenIsland(true);
			}
		}
		
		finalEditsIsland();
	}
	
	private double bottomAdjIsland(int y)
	{
		if(y > 9*size/10)
			return -.3*(Math.abs(y-size))/(size/10.0);
		else if(y < 1*size/10)
			return -.15*(Math.abs(y-size/10.0)/(size/10.0));
			
		return 0;
	}
	
	private void finalEditsIsland()
	{
		int count;
		int count2;
		int sum;
		
		for(int y = 1; y < size-2; y++)
		{
			for(int x = 1; x < size-2; x++)
			{
				sum = 0;
				//Making sure there aren't single lines of islands randomly in the ocean or branching off the main island
				if(y==1)
				{
					count = 2;
					sum = map[y+1][x] + map[y+2][x];
				}
				else if(y==2)
				{
					count = 3;
					sum = map[y-1][x] + map[y+1][x] + map[y+2][x];
				}
				else if(y==size-2)
				{
					count = -2;
					sum = map[y-1][x] + map[y-2][x];
				}
				else if(y==size-3)
				{
					sum = map[y+1][x] + map[y-1][x] + map[y-2][x];
					count = -3;
				}
				else
				{
					count = 4;
					sum = map[y+1][x] + map[y+2][x] + map[y-1][x] + map[y-2][x];
				}
				
				if((sum >= 2 && count != 4) || sum >= 3)
				{
					if(Math.abs(count) > 2)
					{
						for(int i = -1; i < 2; i++)
						{
							for(int z = -1; z < 2; z++)
							{
								if(y+i != 0 && y+i != size-1 && x+z != 0 && x+z != size-1 && Math.random() < .75)
									map[y+i][x+z] = 1;
							}
						}
					}
				}
				//This part deals with straight horizontal strips who are part of the main island
				if(x==1)
				{
					count = 2;
					sum = map[y][x+1] + map[y][x+2];
				}
				else if(x==2)
				{
					count = 3;
					sum = map[y][x-1] + map[y][x+1] + map[y][x+2];
				}
				else if(x==size-2)
				{
					count = -2;
					sum = map[y][x-1] + map[y][x-2];
				}
				else if(x==size-3)
				{
					sum = map[y][x+1] + map[y][x-1] + map[y][x-2];
					count = -3;
				}
				else
				{
					count = 4;
					sum = map[y][x+1] + map[y][x+2] + map[y][x-1] + map[y][x-2];
				}
				
				if(((sum >= 2 && count != 4) || sum >= 3) && (map[y+1][x] == 0 || map[y-1][x]==0) && (map[y+1][x+1] == 0 || map[y-1][x+1]==0) && (map[y+1][x-1] == 0 || map[y-1][x-1] == 0))
				{
					if(Math.abs(count) > 2)//This is semi hard to understand but it just randomly sets some land to water to make sure there aren't straight lines
					{
						for(int i = -2; i < 3; i++)
						{
							if(i!=0 && Math.random() > .5)
								map[y][x+i]=0;
						}
					}
				}
				
				//This part deals with straight vertical strips who are part of the main island
/*				if(y==1)
				{
					count = 2;
					sum = map[y+1][x] + map[y+2][x];
				}
				else if(y==2)
				{
					count = 3;
					sum = map[y-1][x] + map[y+1][x] + map[y+2][x];
				}
				else if(y==size-2)
				{
					count = -2;
					sum = map[y-1][x] + map[y-2][x];
				}
				else if(y==size-3)
				{
					sum = map[y+1][x] + map[y-1][x] + map[y-2][x];
					count = -3;
				}
				else
				{
					count = 4;
					sum = map[y+1][x] + map[y+2][x] + map[y-1][x] + map[y-2][x];
				}
				
				if(((sum >= 2 && count != 4) || sum >= 3) && (map[y+1][x+1] == 0 || map[y+1][x-1]==0) && (map[y][x+1] == 0 || map[y][x-1]==0) && (map[y-1][x+1] == 0 || map[y-1][x-1] == 0))
				{
					if(Math.abs(count) > 2)
					{
						for(int i = -2; i < 3; i++)
						{
							if(i!=0 && Math.random() > .5)
								map[y+i][x]=0;
						}
					}
				} */

			}
		}
	}
	
	private void smoothenIsland(boolean rio)
	{
		int[][] temp = new int[size][size];
		
		for(int y = 1; y < size-1; y++)
		{
			for(int x = 1; x < size-1; x++)
			{
				if(!rio)
				{
					if(getSumIsland(x, y) > sum)
						temp[y][x] = 1;
					else
						temp[y][x] = 0;
				}
				else
				{
					if(getSumIsland(x, y) > sum || getRiverIsland(x, y) < river)
						temp[y][x] = 1;
					else
						temp[y][x] = 0;
				}
			}
		}
		
		map = temp;
	}
	
	private int getSumIsland(int x, int y)
	{
		int sum = 0;
		
		for(int yPos = y-1; yPos < y+2; yPos++)
		{
			for(int xPos = x-1; xPos < x+2; xPos++)
			{
				sum += map[yPos][xPos];
//				System.out.println("sum: " + map[y][x] + "at [" + y + "][" + x + "]");
			}
		}
		
		return sum - map[y][x];
	}
	
	private int getRiverIsland(int x, int y)
	{
		if(y == 1)
			return 3 - (map[y+1][x] + map[y+2][x] + map[y-1][x]);
		else if(y == size - 2)
			return 3 - (map[y+1][x] + map[y-1][x] + map[y-2][x]);
		else
			return 4- (map[y+1][x] + map[y+2][x] + map[y-1][x] + map[y-2][x]);
	}
	
	//ISLAND METHODS END
	
	//GENERAL METHODS BEGIN
	
	public int[][] getMap()
	{
		return map;
	}
	
	public int getSize()
	{
		return size;
	}
	
/*	public static void main(String[] args)
	{
		MapRandomGen test = new MapRandomGen();
		
		int[][] Mappy = test.getMap();
		
		for(int y = 0; y < Mappy.length; y++)
		{
			for(int x : Mappy[y])
				if(x==1)
					System.out.print("= ");
				else if(x==0)
					System.out.print("1 ");
				else
					System.out.println("@ ");
				
			System.out.println();
			
		}
	}*/
}