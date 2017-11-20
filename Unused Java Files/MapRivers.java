package com.kedraney.ddgame.mapgeneration;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by USER on 7/2/2017.
 * used to create the river map/generate the rivers for the map
 */
public class MapRivers//rivers and roads, rivers and roOoads, rivers 'till I find you
{
	private final int[][] map;
	private final int[][] altitude;
	private final int size;
	private int[][] riversNotRoads;

	private ArrayList<RiverPath> rivers = new ArrayList<>();

	public MapRivers(int[][] daMap, int[][] alts, double riverFactor)
	{
		this(daMap, alts, riverFactor, .75, .02);
	}

	public MapRivers(int[][] daMap, int[][] alts, double riverFactor, double branchingFactor, double endFactor)
	{
		map = daMap;
		altitude = alts;
		size = map.length;

		riversNotRoads = new int[size][size];

		for(int y = 1; y < size-1; y++)
		{
			for(int x = 1; x < size-1; x++)
			{
				if(Math.random() < riverFactor * (getFactor(altitude[y][x])))
				{
					rivers.add(new RiverPath(x, y, altitude, branchingFactor, 0, endFactor, -1, -1, size));
				}
			}
		}

		for(int y = 0; y < size; y++)
		{
			for(int x = 0; x < size; x++)
			{
				riversNotRoads[y][x] = 0;
			}
		}

		for(RiverPath c : rivers)
		{
			ArrayList<MapNode> nodes = c.getAllPaths();
			for(MapNode n : nodes)
			{
				riversNotRoads[n.getY()][n.getX()] = 1;
//				System.out.println(n.getID());
			}
		}
	}

	public int[][] getRivers()
	{
		return riversNotRoads;
	}

	private double getFactor(int alt)
	{
		switch (alt)
		{
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
			case 18:
			case 19:
			case 20:
				return .2 + .75*((10-Math.abs(alt-10))/10.0) + alt*.01*.25;//.05 max -- slight boost to higher altitudes

			default:
				if(alt < 40)
					return .2 * (40-alt)/40.0;
				else
					return 0;
		}
	}
}

class RiverPath
{

	private final int startID;
	private final int endID;
	private final double branchChance;
	private final double endFactor;

	private int[][] altitudes;
	private int count = 0;
	private int min;
	private boolean isBranch;//Just in case
	private ArrayList<MapNode> mainPath = new ArrayList<>();//Holds the main path for the river
	private ArrayList<RiverPath> branches = new ArrayList<>();//Holes every branch
	private ArrayList<ArrayList<MapNode>> branchNodes = new ArrayList<>();//holds every node in every branch
	private ArrayList<MapNode> allPaths = new ArrayList<>();//holds every node in both the main path and the branch
	private boolean pondEnd = false;

	public RiverPath(int x, int y, int[][] alts, double bChance, int c, double eFactor, int pX, int pY, int size)
	{
		count = c;

		if(c > 0)
		{
			isBranch = true;
		}
		else
		{
//			System.out.println("River starting at: (" + x + ", " + y + ")\n");
			isBranch = false;//pX and pY only matter if is Branch
		}

		startID = MapNode.createID(x, y);
		branchChance = bChance;
		altitudes = alts;
		endFactor = eFactor;

		mainPath.add(new MapNode(x, y, altitudes));

		boolean pathing = true;
		MapNode currentNode = mainPath.get(0);
		int[][] heights = new int[3][3];//heights around river
		boolean[][] heightTight = new boolean[3][3];

		MapNode temp;

		int minCount;
		int tID;
		RiverPath branch;

		while(pathing)
		{//count > 4 makes sure the river is a decent length and random chance, OR if it's on the border of the map
			if((count < 5 || Math.random() < endFactor*count) || (currentNode.getX() == 0 || currentNode.getY() == 0 || currentNode.getX() == size-1 || currentNode.getY() == size-1))
			{
				pathing = false;//Stop making the river
			}
			else
			{
				count++;//Count++ means we're adding 1 more piece to the river
				min = 99;
				minCount = 0;
				temp;

				for(int i = -1; i < 2; i++)
				{//These loops fill heights with the nearby heights of mapnodes and set min as the min
					for(int z = -1; z < 2; z++)
					{
						heights[i+1][z+1] = altitudes[currentNode.getY() + i][currentNode.getX() + z];
						if(heights[i+1][z+1] < min)
						{
							min = heights[i + 1][z + 1];
						}
					}
				}//This for for loop just gets the minimum height in the area

				if(min == currentNode.getAltitude())
				{//This is what happens if the minimum height is the same height we're at so far
					min = 0;
					for (int i = -1; i < 2; i++)
					{
						for (int z = -1; z < 2; z++)
						{
							tID = MapNode.createID(currentNode.getX() + z, currentNode.getY() + i);
							if (heights[i + 1][z + 1] == currentNode.getAltitude() && (!isBranch || !(MapNode.createID(pX, pY) == tID)) && (currentNode.getID() != tID))
							{//if the altitude is the min, and it either isn't a branch or it isn't the node before this one
								heightTight[1 + i][1 + z] = true;//a possible path exists here
								min++;//min now keeps track of the total number of possible paths there are
								minCount++;//also keeps track of total, but for a different implementation later
							}
						}
					}
					while (min != 0)
					{
						if (min == -1)
							min = -2;//signals that we can test branches
						for (int i = -1; i < 2; i++)
						{
							for (int z = -1; z < 2; z++)
							{//makes sure that there are more possible paths, that this is a possible bath, and then random chance
								if (min > 0 && heightTight[1 + i][1 + z] && Math.random() < 1.0 / minCount)//minCount is just making it so more or less each object gets a fair shot
								{
									temp = new MapNode(currentNode.getX() + i, currentNode.getY() + z);
									min = -1;//sets the progra, to the next phase
									//The if statements below simply make the neighboring booleans false so that a river doesn't have to touching branches
									heightTight = disableBranch(heightTight, i, z, true);//makes sure no neighboring branches will form
								} else if (min == -2 && heightTight[1 + i][1 + z] && Math.random() < branchChance)//both random chance and it is a possible path
								{//also makes sure we are thinking about more branches
									branch = new RiverPath(currentNode.getX() + z, currentNode.getY() + i, altitudes, branchChance, count, endFactor, currentNode.getX(), currentNode.getY(), size);
									branches.add(branch);//adds the branch to this riverPath
									heightTight = disableBranch(heightTight, i, z, true);//makes sure no neighboring branches will form

								}
							}
						}

						if (min == -2)
							min = 0;
					}
				}
				else if(min < currentNode.getAltitude())
				{
					for(int i = -1; i < 2; i++)
					{
						for(int z = -1; z < 2; z++)
						{
							if (heights[1 + i][1 + z] == min)
							{
								minCount++;
							}
							//counts number of spaces that are at the min AND sets all spaces less than current altitude to true
							if(heights[i + 1][z + 1] < currentNode.getAltitude())
							{
								heightTight[i+1][z+1] = true;
							}
						}
					}
					if(minCount == 0)
					{
						temp = null;
					}
					//above section just gets total number of cells/nodes with the minimum height
					while(minCount != 0)
					{
						for(int i = -1; i < 2; i++)
						{
							for(int z = -1; z < 2; z++)
							{//if the height is a min, and the chance factor is taken care of, then this is the next part of the river
								if(minCount != 0 && heights[i + 1][1 + z] == min && Math.random() < 1.0/minCount)
								{
									temp = new MapNode(currentNode.getX(), currentNode.getY(), heights[i+1][z+1]);
									minCount = 0;
									disableBranch(heightTight, i, z, false);
								}
							}
						}
					}
					//now that main path is made, get the branches going

					for(int i = -1; i < 2; i++)
					{
						for(int z = -1; z < 2; z++)
						{
							if(heightTight[i+1][z+1] && Math.random() < branchChance)
							{
								branch = new RiverPath(currentNode.getX() + z, currentNode.getY() + i, altitudes, branchChance, count, endFactor, currentNode.getX(), currentNode.getY(), size);
								branches.add(branch);
								heightTight = disableBranch(heightTight, i, z, false);
							}
						}
					}
				}
				else//current height is the lowest point here
				{
					pondEnd = true;
					pathing = false;
				}

				if(pathing)
				{
					currentNode = temp;
				}
/*				if(pathing && temp != null)
				{
//					currentNode = temp;
				}
				else if(pathing)
				{
//					System.out.println(pathing + " " + count);
					System.out.println(currentNode.getID());
					System.out.println("temp is null. . ." );
					pathing = false;
//					System.exit(56);
				}*/
			}
		}
//change because must vreify water first
//		endID = currentNode.getID();//Not sure I want this - be advised of that
		checkWater();
		endID = currentNode.getID();

		addBranches();
	}

	public RiverPath(int x, int y, int[][] alts, double bChance, int size)
	{
		this(x, y, alts, bChance, 0, .02, -1, -1, size);
	}
//verifies that the tiles aren't water
	private void checkWater()
	{
		int v = 0;
		ArrayList<Integer> removes = new ArrayList<>();

		//if a node is in the sea, end
		for(MapNode i : mainPath)
		{
			v++;

			if(altitudes[i.getY()][i.getX()] < 0)
			{
				removes.add(v);
				pondEnd = false;
			}
		}

		for(int i = 0; i > removes.size(); i++)
		{
			mainPath.remove(removes.get(i) - i);//removes the new spot in the arraylist, ect. ect. - location - # of removed nodes
		}
	}

	private void addBranches()
	{
		ArrayList<MapNode> all = mainPath;
//		ArrayList<MapNode> holder = new ArrayList<>();

		for(RiverPath branch : branches)
		{
//			holder = branch.getAllPaths();
			all.addAll(branch.getAllPaths());
		}

		int[] nodeIDS = new int[all.size()];

		for(int c = 0; c < all.size(); c++)
		{
			nodeIDS[c] = all.get(c).getID();
		}

		Arrays.sort(nodeIDS);
		all = new ArrayList<>();
		all.add(new MapNode(nodeIDS[0]));

		for(int c = 1; c < nodeIDS.length; c++)
		{
			if(nodeIDS[c] != nodeIDS[c-1])
			{
				all.add(new MapNode(nodeIDS[c]));
			}
		}

		allPaths = all;
	}

	private boolean[][] disableBranch(boolean[][] heightTight, int i, int z, boolean equalHeight)
	{
		int c = 0;
		if(z == 0)
		{
			if(heightTight[i+1][z+2])
			{
				heightTight[i+1][z+2] = false;
				c++;
			}
			if(heightTight[i+1][z])
			{
				heightTight[i+1][z] = false;
				c++;
			}
		}
		else if(i == 0)
		{
			if(heightTight[i][z+1])
			{
				heightTight[i][z+1] = false;
				c++;
			}
			if(heightTight[i+2][z+1])
			{
				heightTight[i+2][z+1] = false;
				c++;
			}
		}
		else
		{
			if(heightTight[i+1][1])
			{
				heightTight[i+1][1] = false;
				c++;
			}
			if(heightTight[1][z + 1])
			{
				heightTight[1][z + 1] = false;
				c++;
			}
		}

		if(equalHeight && min > 0)
		{
			min -= c;
			if(min < 0)
				min = 0;
			else
				min = -1;
		}
		else//means min is below currentNode height
		{
			//nothing, but here incase I need it
		}

		return heightTight;
	}

	//Getters
	public ArrayList<MapNode> getMainPath()
	{
		return mainPath;
	}

	public ArrayList<ArrayList<MapNode>> getBranchNodes()
	{
		return branchNodes;
	}

	public ArrayList<MapNode> getAllPaths()
	{
		return allPaths;
	}

	public ArrayList<ArrayList<MapNode>> getBranches()
	{
//		return branches;
		return new ArrayList<>();//currently does nothing useful - I will correct later once I decide exactly what it will need to do
	}

	private int getStartID()
	{
		return startID;
	}

	private int getEndID()
	{
		return endID;
	}
}