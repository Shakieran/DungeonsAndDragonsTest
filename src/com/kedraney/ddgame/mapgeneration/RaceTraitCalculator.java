package com.kedraney.ddgame.mapgeneration;

/**
 * Created by USER on 7/2/2017.
 * creates double arrays that hold values ranging from 0 to 1, or -1 if no value is present
 * each array is one specific trait
 *
 * THIS CLASS DOES NOT COVER THE CHANGING TRAIT MAPS! For example, if a trait map is based on mountains then it is here since
 * mountains will NOT change. But the map describing relations with other races? Well, when the elvish border expands the map
 * changes, so the border creation class will handle that.
 */
public class RaceTraitCalculator
{
	/*MAP ATTRIBUTES BELOW*/
    private final int[][] map;
	private final int[][] altitude;
	private final int[][] forest;
	private final int size;

    //TRAIT MAPS BELOW
    private double[][] mountainDwellers;//they prefer the mountains
	private double[][] forestDwellers;//they prefer the forests
	private double[][] water;//based on water availability

	public double[][] getMountainDwellers()
	{
		return mountainDwellers;
	}

	public double[][] getForestDwellers()
	{
		return forestDwellers;
	}

	public double[][] getWater()
	{
		return water;
	}
//getTraitMap will return the double array based on the string associated with the trait map
	public double[][] getTraitMap(String trait)
	{
		switch(trait)
		{
			case "Water":
				return water;

			case "MountainDweller":
				return mountainDwellers;

			case "ForestDweller":
				return forestDwellers;

			default:
				return new double[size][size];
		}
	}

	public RaceTraitCalculator(int[][][] mapAttributes)//mapAttributes is a collections of maps as follows:
	{//0 = map, 1=altitudes, 2=forest
		map = mapAttributes[0];
		altitude = mapAttributes[1];
		forest = mapAttributes[2];
		size = map[0].length;//100x100, ect. ect.
		//above just sets the final map attributes to their respective values
		//below initializes the trait maps
		mountainDwellers = new double[size][size];
		forestDwellers = new double[size][size];
		water = new double[size][size];

		mapMountainDwellers();
		mapForestDwellers();
		mapWater();//HOLD OFF until rivers are put in
	}

	private void mapMountainDwellers()
	{
		double peak = -1;
		int sum;

		for(int y = 0; y < size; y++)
		{
			for(int x = 0; x < size; x++)
			{
				if(x == 0 || y == 0 || x == size-1 || y == size-1 || map[y][x] == 0)
				{
					mountainDwellers[y][x] = -1;//-1 means that it's not counted = i.e. no impact, ect. ect.
				}
				else//not the boundaries -- therefore we can sum surroundings, ect. ect.
				{
					sum = 0;

					for(int i = -1; i < 2; i++)
					{
						for(int z = -1; z < 2; z++)
						{
							sum += altitude[y + i][x + z];//sum of surrounding values, so a 9 next to 0's isn't
						}//higher than an 8 surrounded by 8's
					}

					mountainDwellers[y][x] = sum;//Just the number for starters
				}

				if(mountainDwellers[y][x] < 0)
					mountainDwellers[y][x] = 0;
			}
		}

		for(int y = 0; y < size; y++)
		{
			for(int x = 0; x < size; x++)
			{
				if(mountainDwellers[y][x] > peak)
				{
					peak = mountainDwellers[y][x];//sets the max to the max present
				}
			}
		}

		for(int y = 0; y < size; y++)
		{
			for(int x = 0; x < size; x++)
			{
				if(mountainDwellers[y][x] != -1)
				{
					mountainDwellers[y][x] = ((int) (100 * (mountainDwellers[y][x]) / peak)) / 100.0;//sets each "cell" to the current/max(present)

					if(mountainDwellers[y][x] < 0)
					{
						mountainDwellers[y][x] = 0;
					}
				}
			}//over present max instead of max possible because why be like "it could be higher" and not settle? Not going to happen.
		}//would just be relative, ect. ect.
	}

	private void mapForestDwellers()
	{
		double average;
		int total;
		int count;

		for(int y = 0; y < size; y++)
		{
			for(int x = 0; x < size; x++)
			{
				if(forest[y][x] == -1)
				{
					forestDwellers[y][x] = -1;
				}
				else
				{
					total = 0;
					count = 0;

					for(int i = -1; i < 2; i++)
					{
						for(int z  = -1; z < 2; z++)
						{
							if(forest[y + i][x + z] != -1)
							{
								total += forest[y + i][x + z];
								count++;
							}
						}
					}

					average = total/count;

					if(average < 0)
					{
						average = 0;
					}

					forestDwellers[y][x] = Math.pow(average, 3)/Math.pow(5, 3);
				}

				if(forestDwellers[y][x] < 0)
					forestDwellers[y][x] = 0;
			}
		}
	}

	private void mapWater()
	{
		//Empty UNTIL we get to a point where rivers and so forth are all entered in
	}
}
