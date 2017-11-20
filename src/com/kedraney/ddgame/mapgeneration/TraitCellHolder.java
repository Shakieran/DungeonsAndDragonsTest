package com.kedraney.ddgame.mapgeneration;

import java.math.*;
import java.util.*;

public class TraitCellHolder
{
    HashMap<Integer, Double> weights = new HashMap<>();
    HashMap<Integer, Integer> places = new HashMap<>();

    int[] traits;
    int[][] map;
    double[][][] traitMaps;

    int size;

    public TraitCellHolder(double[][][] map, ArrayList<Integer> traitz, int[][] original_map)
    {
        traits = new int[traitz.size()];
        this.map = original_map;
        size = this.map.length;
        traitMaps = new double[traits.length][size][size];

        for(int t = 0; t < traitz.size(); t++)
        {
            traits[t] = traitz.get(t);
            for(int y = 0; y < size; y++)
            {
                for(int x = 0; x < size; x++)
                {
                    traitMaps[t][y][x] = map[traits[t]][y][x];
                }
            }
        }
        initializeMaps();
        setUpMaps();
    }
//initializes the maps so that we can set up maps successfully
    private void initializeMaps()
    {
        for(int i = 1; 1 < size * size + 1; i++)
            places.put(i, -1);

        weights.put(-1, -1.0);//makes it so it will always map to an less value if no value is established
    }
//sets up the maps so that places maps to a cell ID, and weights takes the ID and returns the weight
    private void setUpMaps()
    {
        int place;
        double sum;

        for(int y = 0; y < size; y++)
        {
            for(int x = 0; x < size; x++)
            {
                place = 1;
                sum = 0;

                for(int i = 0; i < traits.length; i++)
                {
                    sum += traitMaps[i][y][x];
                }

                if(map[y][x] == 0)
                {
                    sum *= .2;//prioritizes land over water due to obvious benefits of land over water and ect.
                }

                while(weights.get(places.get(place)) > sum)
                {
                    place++;
                }

                places.put(place, MapNode.createID(x, y));
                weights.put(MapNode.createID(x, y), sum);
            }
        }
    }
}