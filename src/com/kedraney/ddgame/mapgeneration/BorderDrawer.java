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


    public BorderDrawer(int facts, List<RiverPath> rivers, int[][][] map)
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

        for(int i = 0; i < factions.length; i++)
        {
            factions[i] = new BorderMaker();
        }

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
    }
}

class BorderMaker
{
    private List<Integer> possibleLands = new ArrayList<>();
    private List<Integer> lands = new ArrayList<>();

    public BorderMaker()
    {

    }

    //takes a random land in possibleLands, adds it to lands, and then returns that land as part of the function
    //returns -1 if there are no more possible lands
    public int pop()
    {
        int index = (int)(Math.random() * possibleLands.size());

        try
        {
            lands.add(possibleLands.get(index));
            possibleLands.remove(index);

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