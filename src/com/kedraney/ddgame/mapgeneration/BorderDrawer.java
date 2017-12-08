package com.kedraney.ddgame.mapgeneration;

import java.util.ArrayList;
import java.util.List;

public class BorderDrawer
{
//    private int factions;This is factions.length
    private BorderMaker[] factions;
    private int[][][] map;
    private List<RiverPath> rivers;
    private int size;
    private int[][] usableLand;//0 is not used, 1 is used, 2 is boundary


    public BorderDrawer(int facts, List<RiverPath> rivers, int[][][] map, int maxHeight)
    {
        factions = new BorderMaker[facts];
        this.rivers = rivers;
        this.map = map;
        size = map[0].length;//have a boolean array and an arraylist
        //randomly choose value in arraylist, and then add any true cells next to it
        //in the bool array to the arraylist since they are now valid and contiguous
        //--> allow multiple arraylists to add one cell to themselves, BUT make sure
        //to remove it from all arrays upon use --> boolean array is more just for
        //rivers and repeats and borders --> only set a cell to false when it is
        //set to a faction, NOT when it is added to an arraylist

        ArrayList<RiverNode> riverNodes;

        for(RiverPath river: rivers)
        {
            riverNodes = river.getRiverNodes();

            for(RiverNode riveNode : riverNodes)//sets every river node to boundary so it isn't used in the political boundaries
            {
                usableLand[riveNode.getY()][riveNode.getX()] = 2;//no go on using these bad boys
            }
        }

        //this is where we drop the mountain algorithm

        calculateMountains(maxHeight);
        int x = 0;
        int y = 0;

        for(int i = 0; i < factions.length; i++)//creates the factions all at unique, random spots
        {
            while(map[0][y][x] != 1 && usableLand[y][x] != 0)
            {
                x = (int)(Math.random() * size);
                y = (int)(Math.random() * size);
            }

            usableLand[y][x] = 1;
            factions[i] = new BorderMaker(x, y, usableLand);

            for(int temp = 0; temp < i; temp++)//just updates the other maps
            {
                factions[temp].remove(MapNode.createID(x, y));
            }
        }
    }

    //makes the boundaries from thte mountains
    private void calculateMountains(int maxHeight)
    {
        ArrayList<Integer> peaks = new ArrayList<>();
        for(int y = 1; y < size-1; y++)
        {
            for(int x = 1; x < size-1; x++)
            {
                if(map[1][y][x] > maxHeight - 3)
                {
                    peaks.add(MapNode.createID(x, y));
                }
            }
        }

        ArrayList<Integer> ranged, hold;//holds the nodes in the range of the line
        int x, y, pos;
        double distance;

        while(peaks.size() > 0)
        {
            y = MapNode.getIDY(peaks.get(0));

            if(y >= 14)//makes sure that the node COULD be
            {
                ranged = new ArrayList<>();//the nodes in the range
                pos = 0;
                x = MapNode.getIDX(peaks.get(0));
                for (int index = 1; index < peaks.size(); index++)
                {//Makes sure that the nodes we compare are all above the first node so we know that it is at the bottom
                    distance = MapNode.getDistance(peaks.get(0), peaks.get(index));
                    if (MapNode.getIDY(peaks.get(index)) < y && distance <= 14)//14 is diameter
                    {
                        ranged.add(peaks.get(index));//if the range between two points is 20 or less, then we add it to the thing -- we will remove all but the last in the end
                    }
                }

                distance = -1;

                for(int index = 0; index < ranged.size(); index++)
                {
                    if(ranged.get(index) > distance)
                    {
                        pos = index;
                        distance = ranged.get(index);
                    }
                }

                hold = getConnection(peaks.get(0), ranged.get(pos));

                //below removes any places within range from the map except the final range
                for(int id : ranged)
                {
                    if(peaks.contains(id) && ranged.size() != 0)
                    {
                        peaks.remove(id);
                        ranged.remove(id);
                    }
                }

                for(int id : hold)
                {
                    usableLand[MapNode.getIDY(id)][MapNode.getIDX(id)] = 2;//sets the boundaries
                }
            }
            else if(usableLand[MapNode.getIDY(peaks.get(0))][MapNode.getIDX(peaks.get(0))] != 3)
            {//if y is big but she ain't the bottom -- i.e. we do nothing but move it to the back
                peaks.add(peaks.get(0));
                peaks.remove(0);//moves the front to the back
                usableLand[MapNode.getIDY(peaks.get(peaks.size()-1))][MapNode.getIDX(peaks.get(peaks.size()-1))] = 3;
            }
            else//it == 3
            {
                peaks.remove(0);//why deal with it if it likely won't work
            }
        }
    }

    //makes the path from one id to another
    private ArrayList<Integer> getConnection(int id1, int id2)
    {
        ArrayList<Integer> answer = new ArrayList<>();
        int x = MapNode.getIDX(id1);
        int y = MapNode.getIDY(id1);
        int gX = MapNode.getIDX(id2);
        int gY = MapNode.getIDY(id2);//goal X and Y vs current X and Y
        int max, pos;

        while(!(x == gX && y == gY) && (y < gY))
        {//while x and y aren't the end goal and the values aren't too high
            max = -1;
            pos = -1;
            for(int z = -1; z < 2; z++)
            {
                if(map[1][y][x+z] > max)
                {
                    pos = z;
                    max = map[1][y+1][x+z];
                }
            }

            y++;
            x += pos;

            answer.add(MapNode.createID(x, y));
        }

        //below is just a hasty catch in case we miss the goal node
        if(!(x==gX && y==gY))
        {
            while(x != gX)
            {
                if(x > gX)
                    x--;
                else
                    x++;

                if(!answer.contains(MapNode.createID(x, y)))
                    answer.add(MapNode.createID(x, y));
            }

            while(y != gY)
            {
                if(y > gY)
                    y--;
                else
                    y++;

                if(!answer.contains(MapNode.createID(x, y)))
                    answer.add(MapNode.createID(x, y));
            }
        }

        return answer;
    }
}

class BorderMaker
{
    private List<Integer> possibleLands = new ArrayList<>();
    private List<Integer> lands = new ArrayList<>();
    private final int X, Y;
    private int[][] useableLand;

    public BorderMaker(int x, int y, int[][] usableLand)
    {
        X = x;
        Y = y;

        this.useableLand = usableLand;

        for(int i = -1; i < 2; i+=2)
        {
            for(int z = -1; z < 2; z+=2)
            {
                if(usableLand[Y + i][X + z] == 0)
                    possibleLands.add(MapNode.createID(X + z, Y + i));
            }
        }
    }

    //takes a random land in possibleLands, adds it to lands, and then returns that land as part of the function
    //returns -1 if there are no more possible lands -- it also adds nearby lands to the
    public int pop()
    {
        int index = (int)(Math.random() * possibleLands.size());

        try
        {
            lands.add(possibleLands.get(index));
            useableLand[MapNode.getIDY(lands.get(lands.size()-1))][MapNode.getIDX(lands.get(lands.size()-1))] = 1;
            possibleLands.remove(index);

            try
            {
                for(int i = -1; i < 2; i++)
                {
                    for(int z = -1; z < 2; z++)
                    {
                        if(useableLand[MapNode.getIDY(lands.get(lands.size()-1)) + i][MapNode.getIDX(lands.get(lands.size()-1)) + z] == 0)
                            useableLand[MapNode.getIDY(lands.get(lands.size()-1)) + i][MapNode.getIDX(lands.get(lands.size()-1)) + z] = 1;
                    }
                }
            }
            catch (ArrayIndexOutOfBoundsException e)
            {

            }

            return lands.get(lands.size()-1);
        }
        catch (IndexOutOfBoundsException e)
        {
            return -1;
        }
    }

    //removes a land from the possibleLands arraylist
    public void remove(int land)
    {
        if(possibleLands.contains(land))//Will never be in lands if another selected it
        {                               //so we only check possibleLands
            possibleLands.remove(land);
        }
    }

    public void addLand(int land)
    {
        possibleLands.add(land);
    }
}