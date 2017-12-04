package com.kedraney.ddgame.mapgeneration;

import java.math.*;
import java.util.*;

public class TraitCellHolder
{
    HashMap<Integer, Double> weights = new HashMap<>();//holds the sum of all trait maps at each cell location
    HashMap<Integer, Integer> valid = new HashMap<>();//0 or null is not good, 1 is valid to use, 2 is already in use
    ArrayList<Integer> places = new ArrayList<>();//at index 0 is the highest weighted cell, and ect.
    ArrayList<Integer> lands = new ArrayList<>();//contains all lands that are had by this faction
//    ArrayList<Integer> usable = new ArrayList<>();

    int[] traits;
    int[][] politicalTraits;
    int[][] map;
    int size;

    public TraitCellHolder(double[][][] map, ArrayList<Integer> traitz, int[][] original_map)
    {
        traits = new int[traitz.size()];
        this.map = original_map;
        size = this.map.length;

        for(int t = 0; t < traitz.size(); t++)
        {
            traits[t] = traitz.get(t);
        }

        //updateMaps(map);

        for(int y = 0; y < size; y++)
        {
            for(int x = 0; x < size; x++)
            {
                valid.put(MapNode.createID(x, y), 0);
            }
        }

        updateMaps(map);

        valid.put(places.get(0), 1);
    }

//sets up the maps so that places maps to a cell ID, and weights takes the ID and returns the weight
    //public so that other classes can send it updated trait maps as stuff happens ect. ect.
    private void updateMaps(double uppedMapsT[][][])
    {
        int place;
        double sum;

        places = new ArrayList<>();
        places.add(-1);//values are always above this, so all will be above this
        weights.put(-1, -1.0);

        for(int y = 0; y < size; y++)
        {
            for(int x = 0; x < size; x++)
            {//if we can use the value of the land
                if(valid.get(MapNode.createID(x, y)) < 2)
                {
                    place = 0;
                    sum = 0;

                    for(int i = 0; i < traits.length; i++)
                    {
                        sum += uppedMapsT[traits[i]][y][x];
                    }
//                    System.out.println(sum + ":(" + x + ", " + y + ")");
                    //WORKS UP TO HERE
                    weights.put(MapNode.createID(x, y), sum);

                    while(weights.get(places.get(place)) > sum)
                    {
                        place++;
                    }

                    places.add(place, MapNode.createID(x, y));
                }//this else is for when the land cell is already in use, i.e. == 2
                else//if we can't make sure it won't get picked/ect.
                {
//                    if(places.get(places.size()-1) == -1)
//                        places.remove(-1);

                    places.add(MapNode.createID(x, y));
                    weights.put(MapNode.createID(x, y), -1.0);
                }
            }
        }

        places.remove(places.size()-1);//the final object is of a value -1, i.e. not a land mass ect.
    }

    //returns the next top-most value, and add potential
    public int pop()
    {
        if(valid.get(places.get(0)) > 1)
        {
            return -1;//-1 means that there are no further values of which we can use that are valid
        }
        else
        {
            int index = 0;
            int y, x;

            while(valid.get(places.get(index)) < 2)
                index++;//essentially if we can't pop it we don't care about it

            y = MapNode.getIDY(places.get(index));
            x = MapNode.getIDX(places.get(index));

            //these below loops make sure that the nearby nodes are now able to be popped
            for(int i = -1; i < 2; i+= 2)
            {
                for(int z = -1; z < 2; z += 2)
                {
                    if(y + i < size && y + i > -1 && x + z < size && x + z > -1)
                        valid.put(MapNode.createID(x + z, y + i), 1);
                }
            }

            valid.put(places.get(index), 2);

            lands.add(places.get(index));

            return places.get(index);
        }
    }

    public ArrayList<Integer> pop(int num)
    {
        ArrayList<Integer> answer = new ArrayList<>();
        for (int c = 0; c < num; c++)
        {
            if (valid.get(places.get(0)) > 1)
            {
                c = num + 1;//-1 means that there are no further values of which we can use that are valid
            }
            else
            {
                int index = 0;
                int y, x;

                while (valid.get(places.get(index)) != 1)
                    index++;//essentially if we can't pop it we don't care about it

                y = MapNode.getIDY(places.get(index));
                x = MapNode.getIDX(places.get(index));

                //these below loops make sure that the nearby nodes are now able to be popped
                for (int i = -1; i < 2; i += 2)
                {
                    for (int z = -1; z < 2; z += 2)
                    {
                        if (y + i < size && y + i > -1 && x + z < size && x + z > -1 && valid.get(MapNode.createID(x + z, y + i)) != 2)
                            valid.put(MapNode.createID(x + z, y + i), 1);
                    }
                }

                valid.put(places.get(index), 2);

                answer.add(places.get(index));

                if(c == 0)
                    System.out.println("Test -- " + answer.get(answer.size()-1));
            }
        }

        if(answer.size() < 1)
            answer.add(-1);

        lands.addAll(answer);

        return answer;
    }

    //simply method that just sets all land other factions use as in use
    public void setForeignLands(ArrayList<Integer> others)
    {
        for(int i = 0; i < others.size(); i++)
            valid.put(others.get(i), 2);
    }

    public ArrayList<Integer> getLands()
    {
        return lands;
    }
}