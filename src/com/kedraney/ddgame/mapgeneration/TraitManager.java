package com.kedraney.ddgame.mapgeneration;

import java.math.*;
import java.util.*;

//This badboy will hold a TraitCellHolder for each race that exists as a fraction
public class TraitManager
{
    public TraitManager(ArrayList<Integer> applicableRaces, ArrayList<String> allRaces)
    {
        ArrayList<String> races = new ArrayList<>();

        for(int i = 0; i < applicableRaces.size(); i++)
        {
            races.add(allRaces.get(i));
        }

        //-------------------------------------------------------
        //SQL DATABASE WILL BE INTEGRATED HERE SOON
        //-------------------------------------------------------


    }
}