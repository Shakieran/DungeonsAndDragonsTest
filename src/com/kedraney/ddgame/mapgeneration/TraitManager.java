package com.kedraney.ddgame.mapgeneration;

import java.math.*;
import java.util.*;

enum Trait
{
    FORESTDWELLER(0),
    MOUNTAINDWELLER(1);

    private final int pos;

    Trait(int pos)
    {
        this.pos = pos;
    }

    int getPos()
    {
        return pos;
    }
}

public class TraitManager
{

}