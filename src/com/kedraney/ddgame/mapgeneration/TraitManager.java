package com.kedraney.ddgame.mapgeneration;

import java.math.*;
import java.util.*;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//This badboy will hold a TraitCellHolder for each race that exists as a fraction
public class TraitManager
{
    //posRaces is an arraylist of all races that COULD exist on this map, totalRaces is the total number of races that will
    //own territory -- map is just the map
    public TraitManager(ArrayList<String> posRaces, int totalRaces, int map[][][], Connection gameData)
    {
        String[] races = new String[totalRaces];//the races that are on the map
        TraitCellHolder[] tMaps = new TraitCellHolder[totalRaces];
        ArrayList<ArrayList<Integer>> traits = new ArrayList<ArrayList<Integer>>();

        for(int i = 0; i < races.length; i++)
        {
            races[i] = posRaces.get((int)(Math.random() * posRaces.size()));//randomly selects a race for each "slot"

            //this allows things to get spicy
        }



        //-------------------------------------------------------
        //SQL DATABASE WILL BE INTEGRATED HERE SOON
        //-------------------------------------------------------


    }
}