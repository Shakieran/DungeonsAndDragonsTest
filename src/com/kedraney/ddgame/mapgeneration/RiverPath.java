package com.kedraney.ddgame.mapgeneration;

import java.util.ArrayList;

public class RiverPath
{
    public ArrayList<RiverNode> getRiverNodes()
    {
        return riverNodes;
    }

    private ArrayList<RiverNode> riverNodes = new ArrayList<>();//the list of riverNodes i.e. the list of cells with the river

    private int length;//the total number of riverNodes

/*    public RiverPath()
    {
        RiverNode a = new RiverNode(1, 1);
        RiverNode b = new RiverNode(1, 1);
        RiverNode c = new RiverNode(2, 1);
        RiverNode d = new RiverNode(1, 2);

        System.out.print("*(**)(*)(*)()*()(*)**)(*)(*)(*)(*(&^&((*(&*&(*&*(");
        System.out.print(a.equals(b));
        System.out.print(a.equals(c));
        System.out.print(d.equals(c));
    }*/

    public RiverPath(int[][][] map, int x, int y, int squareSize, double[][] altAvg)
    {
        riverNodes.add(new RiverNode(x, y, 1));

        int xMajor = majorCalc(x, squareSize);
        int yMajor = majorCalc(y, squareSize);
        int dir = getDirection(xMajor, yMajor, altAvg);
        double xDir = coorPercent(true, x);
        double yDir = coorPercent(false, y);

        RiverNode node;
        //   -1 0 1
        //-1-7  0 1
        // 0-6  x 2
        // 1-5  4 3

        while(map[0][y][x] != 0)
        {
            if(yMajor != y/squareSize || xMajor != x/squareSize)//recalculate the majors and all that jazz
            {
                dir = getDirection(x, y, altAvg);
                xDir = coorPercent(true, dir);
                yDir = coorPercent(false, dir);
            }

            if(Math.random() < xDir)
            {
                x++;
            }
            else if(Math.random() > xDir)
            {
                x--;
            }

            if(Math.random() < yDir)
            {
                y++;
            }
            else if(Math.random() > yDir)
            {
                y--;
            }
            //allows y to be unchanged for strait lines and shit

            node = new RiverNode(x,y, 1);
            if(!riverNodes.contains(node))//if the node is new, add it to the arraylist -- otherwise, set x and y to the previous values
            {
                riverNodes.add(node);
            }
            else
            {
                x = riverNodes.get(riverNodes.size()-1).getX();
                y = riverNodes.get(riverNodes.size()-1).getY();
            }
        }//at this point the arraylist has the complete collection of river nodes

        //makes sure we don't count water tiles
 /*       for(int index = 0; index < riverNodes.size(); index++)
        {
            if(map[1][riverNodes.get(index).getY()][riverNodes.get(index).getX()] == 0)
            {
                riverNodes.remove(index);
                index--;
            }
        }*/
        /*if(map[0][riverNodes.get(riverNodes.size()-1).getY()][riverNodes.get(riverNodes.size()-1).getX()] == 0)//just makes it so if it ends in water that cell
        {//                                                                                                    won't count
            riverNodes.remove(riverNodes.size()-1);
        }*/

        length = riverNodes.size();
    }

    //ugly ass if statement but yeah gets dir
    private int getDirection(int x, int y, double[][] altAvg)
    {
        double max = -1;
        int dir = -1;

        for(int i = -1; i < 2; i++)
        {
            for(int z = -1; z < 2; z++)
            {
                try
                {
                    if(altAvg[y + i][x + z] > max)
                    {
                        max = altAvg[y + i][x + z];
                        if(z == 0)
                        {
                            if(i == -1)
                            {
                                dir = 0;
                            }
                            else// i == 1
                            {
                                dir = 4;
                            }
                        }
                        else if(z == -1)
                        {
                            if(i == -1)
                            {
                                dir = 7;
                            }
                            else if(i == 0)
                            {
                                dir = 6;
                            }
                            else// i == 1
                            {
                                dir = 5;
                            }
                        }
                        else//z == 1
                        {
                            if(i == -1)
                            {
                                dir = 1;
                            }
                            else if(i == 0)
                            {
                                dir = 2;
                            }
                            else//i == 1
                            {
                                dir = 3;
                            }
                        }
                    }
                }
                catch (ArrayIndexOutOfBoundsException e)
                {

                }
            }
        }

        return dir;
    }

    private double coorPercent(boolean isX, int coor)
    {
        if(!isX)
        {
            coor =- 2;
        }

        return .35 * Math.sin(Math.PI/4.0 * coor) + .5;
    }

    public int getLength()
    {
        return length;
    }

    private int majorCalc(int coordinate, int squareSize)
    {
        return coordinate/squareSize;
    }
}


class RiverNode
{
    private final int X;
    private final int Y;
    private final int ID;
    private final int SPEED;//flow of river -- faster also means larger

 /*   public RiverNode(int x, int y)
    {
        SPEED = 1;
        X = x;
        Y = y;
        ID = MapNode.createID(X, Y);
    }*/

    public RiverNode(int x, int y, int speed)
    {
        X = x;
        Y = y;
        ID = MapNode.createID(X, Y);

        SPEED = speed;
    }

    public boolean equals(Object obj){
        if(obj instanceof RiverNode){
            return ((RiverNode)obj).getID() == (this.getID());
        }
        return false;
    }

    public int getID()
    {
        return ID;
    }
    public int getX()
    {
        return X;
    }
    public int getY()
    {
        return Y;
    }
    public int getSPEED()
    {
        return SPEED;
    }
}