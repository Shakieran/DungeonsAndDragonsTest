package com.kedraney.ddgame.mapgeneration;

import java.math.*;
import java.util.*;

public class MapAltitudeGen
{
	private final int SMOOTH;//SMOOTH will be the amount of times that the smooth function is called
	private final double smoothOdds = 1;//determins the chance that a cell is smoothed in the "smooth()" method
	
	private double mountainFactor;//bigger number = higher mountains -- default = 1
	private int maxHeight = 99;//bigger number, bigger maz peak -- NOT INCLUDED YET!
	private int size;
	private int[][] map;
	private int[][] altitudes;
	private boolean land;
	private boolean[][] completed;
	private boolean[][] safe;//safe means we can edit nearby cells, completed just means we've set the final value
	
	private int[][] middleGround;
	public MapAltitudeGen(int[][] theMap, double mFact)
	{
		this(theMap, mFact, 99);
	}
	
	public MapAltitudeGen(int[][] theMap)
	{
		this(theMap, 1, 99);
	}
	
	public MapAltitudeGen(int[][] theMap, double mFact, int maxHeight)
	{
		SMOOTH = (int)Math.pow(mFact, 5) + 5;
		mountainFactor = mFact;
		map = theMap;
		size = map.length;
		altitudes = new int[size][size];
		middleGround = new int[size][size];
		completed = new boolean[size][size];
		safe = new boolean[size][size];
		ArrayList<Integer> coordinates = new ArrayList<>();//holds all map cells in the form of MapNode.ids --> allows us to randomly access them
		
		int count = 0;
		
		for(int y = 0; y < size; y++)
		{
			for(int x = 0; x < size; x++)
			{
				if(map[y][x] == 1)
					altitudes[y][x] = -2;//-2 if land, -1 if not
				else
				{
					altitudes[y][x] = -1;//-1 is where all water starts - below sea level, hence why sea is on it
					count++;
				}
			}
		}
		
		if(count == 0)
		{
			for(int y = 0; y < size-1; y++)
			{
				for(int x  = 0; x < size-1; x++)
				{
					if(x == 0 || y == 0 || x == size-1 || y == size-1)
					{
						map[y][x] = 0;
						completed[y][x] = true;
						safe[y][x] = true;
					}
				}
			}
		}
		
		for(int y = 1; y < size-1; y++)
		{
			for(int x = 1; x < size-1; x++)
			{
				if(map[y][x] == 1 && someWater(x, y))
				{//change back to = 0 if it messes program up
//					altitudes[y][x] = (int)(Math.random()*mountainFactor + 0);
					altitudes[y][x] = 0;
					completed[y][x] = true;
					safe[y][x] = true;
				}
			}
		}
		
/*		for(int y = 0; y < size; y++)
		{
			for(int x = 0; x < size; x++)
			{
				if(y == 0 || x == 0 || y == size-1 || x == size-1)
				{
//					altitudes[y][x] = -1;
//					completed[y][x] = true;
//					safe[y][x] = true;
				}
					
				middleGround[y][x] = altitudes[y][x];
			}
		}*/
		
		//THE CODE DOES WORK 100% UP TO THIS POINT, WITH 0's ALONG THE SHORELINE
		//The constructor now works well
		int sum;
		int avg = 0;
		double average;
		int n, y, x, r;//4 on 1 line, #rebel
		count = 1;

		while(count > 0)
		{
			count = 0;
			//fills coordinates with all the. . .coordinates, obviously
			for(int i = 1; i < size - 1; i++)
				for(int z = 1; z < size - 1; z++)
					coordinates.add(MapNode.createID(z, i));

			while(coordinates.size() > 0)
			{
				r = (int)(Math.random() * coordinates.size());
				y = MapNode.getIDY(coordinates.get(r));
				x = MapNode.getIDX(coordinates.get(r));
				coordinates.remove(r);

				sum = 0;
				avg = 0;
					
				if(!completed[y][x] && nextToSafe(x, y)) {//////////////////////////////////////////////////////////////////////
					count++;
					n = altitudes[y][x];

					if (n == -2) {
						for (int i = -1; i < 2; i++) {
							for (int z = -1; z < 2; z++) {
								if (altitudes[y + i][x + z] > -1) {
									sum += altitudes[y + i][x + z];
									avg++;
								}
							}
						}

						average = (double) (sum) / avg;

						altitudes[y][x] = (int) (Math.random() - .5 + average + (Math.random() * mountainFactor));
						completed[y][x] = true;

						if (altitudes[y][x] > maxHeight)
							altitudes[y][x] = maxHeight;
						else if (altitudes[y][x] < 1)
							altitudes[y][x] = 1;
					} else if (n == -1) {
						for (int i = -1; i < 2; i++) {
							for (int z = -1; z < 2; z++) {
								if (altitudes[y + i][x + z] < 0) {
									sum += altitudes[y + i][x + z];
									avg++;
								}
							}
						}

						average = (double) (sum) / avg;

						altitudes[y][x] = (int) (Math.random() - .5 + average + (Math.random() * mountainFactor * -1));
						completed[y][x] = true;

						if (altitudes[y][x] > -1)
							altitudes[y][x] = -1;
					}
				}
			}
			//accidentally dropped z then i, but z is for x and i is for y always -- happens to not matter in the next
			//two loops hence why it remains
			for(int z = 0; z < size-1; z++)
			{
				for(int i = 0; i < size-1; i++)
				{
					if(completed[i][z] && z!= 0)
						safe[i-1][z] = true;
				}
			}
		}
		
		int peak = -1;
		for(int z = 0; z < size; z++)
		{
			for(int i = 0; i < size; i++)
			{
				if(altitudes[i][z] > peak)
					peak = altitudes[i][z];
			}
		}
		
		maxHeight = peak;

		System.out.println("-----------------------------------------------------------");
		
		for(int smooo = 0; smooo < SMOOTH; smooo++)
			smooth();
	}

	//fills coordiante arrays with the coordinates 0 to size-1 in order to allow them to be randomly accessed
	private ArrayList<Integer> fill()
	{
		ArrayList<Integer> answer = new ArrayList<>();

		for(int i = 0; i < size; i++)
		{
			answer.add(i);
		}

		return answer;
	}

	private boolean nextToSafe(int x, int y)
	{//adding count to make it "safer" so we have more valid results(hopefully)
		int count = 0;
		for(int i = -1; i < 2; i++)
		{
			for(int z = -1; z < 2; z++)
			{
				if(safe[y+i][x+z])
					count++;
			}
		}

		if(count > 1)
			return true;
		
		return false;
	}
	
	//helps make sure that when the sides of the map converge they aren't fucked up with a 10 next to a 3, or worse
	private void smooth()
	{//Ex = x and Why = y but phonetically because fuck you kieran
		ArrayList<Integer> Ex = new ArrayList<>();
		ArrayList<Integer> Why = new ArrayList<>();

		int y;
		int x;
		int peak;
		//adds any point in the top 33% of possible altitudes from 0-maxHeight
		for(int i = 1; i < size-1; i++)
		{
			for(int z = 1; z < size-1; z++)
			{
				if(altitudes[i][z] > 3*maxHeight/4)//WAS 80%, CHANGE BACK IF YOU NEED TO
				{
					Why.add(i);
					Ex.add(z);//They all share the same index
				}
			}
		}
		
		while(Why.size() > 0)
		{
			peak = (int)(Math.random() * Ex.size());
			x = Ex.get(peak);
			y = Why.get(peak);
			Ex.remove(peak);
			Why.remove(peak);
			peak = altitudes[y][x];


			for(int g = 1; g < 7; g++)//Change 4 to determine range that is smoothens
			{//g is the range of which
				for(int i = -g; i < g+1; i++)
				{
					for(int z = -g; z < g+1; z++)
					{
						if(Math.abs(i) == g || Math.abs(z) == g)
						{
							try
							{//drops a higher chance if it's way further from what it should be
								if(altitudes[y+i][x+z] < peak - g && Math.random() < smoothOdds)
								{
									altitudes[y+i][x+z]+= (int)(1 + (-altitudes[y+i][x+z] + peak - g) * Math.random());
								}
								else if(altitudes[y+i][x+z] > peak - g && Math.random() < smoothOdds)
								{
									altitudes[y+i][x+z] -= (int)(1 + (altitudes[y+i][x+z] - peak + g) * Math.random());
								}
							}
							catch(ArrayIndexOutOfBoundsException e)
							{
								
							}
						}
					}
				}
			}

			Ex.remove(0);
			Why.remove(0);
		}
		if(maxHeight > 17)
		{
			ArrayList<Integer> coordinates = new ArrayList<>();
			for (int i = 1; i < size - 1; i++) {
				for (int z = 1; z < size - 1; z++) {
					//only smooths land here
					if (map[i][z] == 1)
						coordinates.add(MapNode.createID(z, i));
				}
			}

			int r, total, average, max, min;
			final int RANGE, VARIANCE;
//this section smooths based on an average of a rangexrange square around each land point
			//this helps create a gradient of change versus a sudden change in elevation
			RANGE = 3;
			VARIANCE = 3;
			while (coordinates.size() > 0) {
				r = (int) (Math.random() * coordinates.size());
				x = MapNode.getIDX(coordinates.get(r));
				y = MapNode.getIDY(coordinates.get(r));
				coordinates.remove(r);
				average = 0;
				total = 0;
				max = -1;
				min = maxHeight;

				for (int i = -RANGE; i <= RANGE; i++) {
					for (int z = -RANGE; z <= RANGE; z++) {//makes sure points are valid too
						if ((y + i >= 0 && y + i < size && x + z >= 0 && x + z < size) && z != 0 && i != 0 && map[y + i][x + z] == 1)//ignores the water tiles and the tile we're adjusting
						{
							average += altitudes[y + i][x + z];
							total++;

							if (altitudes[y + i][x + z] > max)
								max = altitudes[y + i][x + z];
							else if (altitudes[y + i][x + z] < min)
								min = altitudes[y + i][x + z];
						}
					}
				}

				if (total > 0) {
					average /= total;
					//just a bit more of an adjustment to the variability and smoothng and all that jazz and ect.
					if (max - min < mountainFactor * 2.5) {
						if (altitudes[y][x] == min)
							altitudes[y][x] = min;
						else if (altitudes[y][x] == max)
							altitudes[y][x] = max;
					} else {
						if (altitudes[y][x] == min)
							altitudes[y][x] = average;
						else if (altitudes[y][x] == max)
							altitudes[y][x] = average;
					}

					if (altitudes[y][x] < average - VARIANCE)
						altitudes[y][x]++;
					else if (altitudes[y][x] > average + VARIANCE)
						altitudes[y][x]--;
				}
			}
		}
	}
	
	//readyToWater makes sure that the water is 1) not bordering land and 2) bordering water that has already been set
	private boolean readyToWater(int x, int y)
	{
		for(int i = -1; i < 2; i++)
		{
			for(int z = -1; z < 2; z++)
			{
				if(altitudes[y+i][x+z] < -1)//If the water on any side is below -1 then it means that it's been edited
					return true;
					
				try
				{
					if(i == 0 && altitudes[y+i][x+z*2] > -1)
					{
						land = true;
						return true;
					}
					else if(z == 0 && altitudes[y+i*2][x+z] > -1)
					{
						land = true;
						return true;
					}
					else if(altitudes[y+2*i][x+z] > -1 || altitudes[y+i][x+2*z] > -1 || altitudes[y+2*i][x+2*z] > -1)
					{
						land = true;
						return true;
					}
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					land = false;
				}
			}
		}
		
		return false;
	}
	
	//readyToAltitude makes sure that the land value is nezt to at least one real value to base this vallue on
	private boolean readyToAltitude(int x, int y)
	{
		for(int i = -1; i < 2; i++)
		{
			for(int z = -1; z < 2; z++)
			{
				if(altitudes[y+i][x+z] > -1)
					return true;
			}
		}
		
		return false;
	}
	
	//if the land borders water
	private boolean someWater(int x, int y)
	{
		for(int i = -1; i < 2; i++)
		{
			for(int z = -1; z < 2; z++)
			{
				if(altitudes[y+i][x+z] == -1)
					return true;
			}
		}
		
		return false;
	}
	//General  methods
	public int[][] getMap()
	{
		return altitudes;
	}
	
	public int[][] getMiddle()
	{
		return middleGround;
	}
	//gets the maxHeight, which is the highest peak
	public int getMaxHeight()
	{
		return maxHeight;
	}
}