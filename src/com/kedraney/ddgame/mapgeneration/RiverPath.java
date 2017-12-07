package com.kedraney.ddgame.mapgeneration;

import java.util.ArrayList;

public class RiverPath
{
    private int length;
    private ArrayList<RiverNode> riverNodes = new ArrayList<>();

    public RiverPath(int[][][] map, int x, int y, int squareSize)
    {
        riverNodes.add(new RiverNode(x, y, 1));

        int xMajor = majorCalc(x, squareSize);
        int yMajor = majorCalc(y, squareSize);

        while(map[0][y][x] != 0)
        {
            
        }
    }

    public int getLength()
    {
        return length;
    }

    private int majorCalc(int coordinate, int squareSize)
    {
        int x = 0;
        while(coordinate > 0)
        {
            coordinate -= squareSize;
            x++;
        }

        return x;
    }
}


class RiverNode
{
    private final int X;
    private final int Y;
    private final int SPEED;//flow of river -- faster also means larger

    public RiverNode(int x, int y, int speed)
    {
        X = x;
        Y = y;

        SPEED = speed;
    }
}