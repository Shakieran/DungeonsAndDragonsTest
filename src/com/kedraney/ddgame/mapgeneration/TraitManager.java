package com.kedraney.ddgame.mapgeneration;

import java.math.*;
import java.sql.*;
import java.util.*;

//This badboy will hold a TraitCellHolder for each race that exists as a fraction
public class TraitManager
{
    private TraitCellHolder[] tMaps;
    private final int stepSize;
    private final int size;
    //posRaces is an arraylist of all races that COULD exist on this map, totalRaces is the total number of races that will
    //own territory -- map is just the map
    public TraitManager(ArrayList<Integer> posRaces, int totalRaces, int map[][][], double[][][] traitMaps)
    {
        int[] races = new int[totalRaces];//the races that are on the map
        size = map[0].length;
        tMaps = new TraitCellHolder[totalRaces];
        ArrayList<ArrayList<Integer>> traits = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> trait = new ArrayList<>();
        Connection gameData;
        stepSize = getVal(map[0].length);

 //       System.out.println("The races though");

        try
        {
            String url = "jdbc:sqlite:C:\\Users\\Kieran Edraney\\Desktop\\DungeonsAndDragons\\src\\GameData\\database\\GameData.db";

            gameData = DriverManager.getConnection(url);
        }
        catch (SQLException e)
        {
            gameData = null;
        }

        for(int i = 0; i < races.length; i++)
        {
            races[i] = posRaces.get((int)(Math.random() * posRaces.size()));//randomly selects a race for each "slot"
            trait = new ArrayList<>();
 //           System.out.println(races[i]);

            try
            {
                String query = "select Trait from RaceTraits where RaceID == " + races[i];
                Statement stmt = gameData.createStatement();
                ResultSet allTraits = stmt.executeQuery(query);

                while(allTraits.next())//get every trait of which this race has
                {
                    trait.add(allTraits.getInt(1));
                }

                traits.add(trait);

//                System.out.println("tMaps[" + i + "]: " + trait.size());

                tMaps[i] = new TraitCellHolder(traitMaps, trait, map[0]);
            }
            catch (SQLException e)
            {
                System.out.println("failed");
            }

            //this allows things to get spicy
        }

        try
        {
            gameData.close();
        }
        catch (SQLException e)
        {

        }

        setUpLand(2);

        printAllMaps();
    }

    private void printAllMaps()
    {
        ArrayList<Integer> lands;
        for(TraitCellHolder map: tMaps)
        {
            lands = map.getLands();
//            System.out.println("This many lands bitch: " + lands.size());
            boolean tester;

            for(int y = 0; y < size; y++)
            {
                for(int x = 0; x < size; x++)
                {
                    tester = false;
                    for(int i = 0; i < lands.size(); i++)
                    {
                        if(lands.get(i)==MapNode.createID(x, y))
                            tester = true;
                    }
                    if(tester)
                        System.out.print("1 ");
                    else
                        System.out.print("0 ");
                }
                System.out.println();
            }

            System.out.println("--------------------------");
        }
    }

    private void setUpLand()
    {
        ArrayList<Integer> hold = new ArrayList<>();
        hold.add(1);

        while(hold.get(0) != -1)
        {
            for(int c = 0; c < tMaps.length; c++)
            {
                hold = tMaps[c].pop(stepSize);

                if(hold.get(0) == -1)
                {
                    c = tMaps.length;
                }
                else
                {
                    for(int o = 0; o < tMaps.length; o++)
                    {
                        if(o != c)
                        {
                            tMaps[o].setForeignLands(hold);
                        }
                    }
                }
            }
        }
    }

    private void setUpLand(int x)
    {
        int hold = 1;

        while(hold != -1)
        {
            for(int c = 0; c < tMaps.length; c++)
            {
                hold = tMaps[c].pop();

                if(hold == -1)
                {
                    c = tMaps.length;
                }
                else
                {
                    for(int o = 0; o < tMaps.length; o++)
                    {
                        if(o != c)
                        {
                            tMaps[o].setForeignLands(hold);
                        }
                    }
                }
            }
        }
    }

    private int getVal(int length)
    {
        int answer = 20;
        if(length * length < 400)
        {
            answer = (int)(Math.sqrt(length));

            if(answer < 0)
                answer = 1;
        }

        return answer;
    }
}