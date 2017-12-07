package com.kedraney.ddgame.mapgeneration;

import java.util.ArrayList;
import java.util.List;

public class BorderDrawer
{
    private int factions;
    private int[][][] map;
    private List<RiverPath> rivers;
    private int size;
    private boolean[][] usableLand;


    public BorderDrawer(int facts, List<RiverPath> rivers, int[][][] map)
    {
        factions = facts;
        this.rivers = rivers;
        this.map = map;
        size = map[0].length;//have a boolean array and an arraylist
        //randomly choose value in arraylist, and then add any true cells next to it
        //in the bool array to the arraylist since they are now valid and contiguous
        //--> allow multiple arraylists to add one cell to themselves, BUT make sure
        //to remove it from all arrays upon use --> boolean array is more just for
        //rivers and repeats and borders --> only set a cell to false when it is
        //set to a faction, NOT when it is added to an arraylist


    }
}
