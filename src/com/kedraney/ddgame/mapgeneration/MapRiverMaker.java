package com.kedraney.ddgame.mapgeneration;

import java.util.ArrayList;

public class MapRiverMaker
{
    private int[][][] map;
    private double[][] altitudeSquares;//holds the average height for the squares

    private final int size, increment;

   /* public MapRiverMaker()
    {
        size = 0;
        increment = 0;

        RiverPath x = new RiverPath();
    }*/

    public MapRiverMaker(int[][][] daMap)
    {
        map = daMap;
        size = map[0].length;
//        increment = (int)(Math.pow(size, .6667)/2.0);
        increment = 5;

        int sum, sum2, num;
        int squareSize = (int)(.5 + (double)size/increment);
//        boolean remainder = !(squareSize == (int)((double)size/increment));//if there is a remainder, this is true
        boolean remainder;

        if(squareSize == (double)size/increment)
            remainder = false;
        else
            remainder = true;


        altitudeSquares = new double[squareSize][squareSize];

        if(remainder)
        {
            squareSize -= 2;
        }

        int tempMultLimit = squareSize;

        if(increment < squareSize)
        {
            tempMultLimit = increment;//DEBUG LATER
        }

//        System.out.println(remainder);

        for(int xMult = 0; xMult < tempMultLimit; xMult++)//was squareSize instead of increment
        {
            for(int yMult = 0; yMult < tempMultLimit; yMult++)//these values cycle through the standard values for the shapes and all that
            {
                sum = 0;
                for(int y = 0; y < squareSize; y++)
                {
                    for(int x = 0; x < squareSize; x++)
                    {
//                        System.out.println(y + " + " + yMult + " * " + squareSize + " --- " + x + " + " + xMult + " * " + squareSize);
                        sum += map[1][y + yMult * squareSize][x + xMult * squareSize];//altitude sums and all that jazz
                    }
                }

                altitudeSquares[yMult][xMult] = (double)sum/(squareSize * squareSize);
            }
        }

        if(remainder)
        {
            squareSize--;
            for(int multRollouts = 0; multRollouts < squareSize + 1; multRollouts++)
            {
                sum = 0;
                sum2 = 0;
                num = 0;
                for(int coor1 = 0; coor1 < squareSize; coor1++)
                {
                    for(int coor2 = 0; coor2 < size % increment; coor2++)
                    {
                        if(!(multRollouts == squareSize && coor1 >= size % increment))
                        {
                            sum += map[1][coor2 + squareSize * squareSize][coor1 + multRollouts * squareSize];//gets the border that isn't a square
                            sum2 += map[1][coor1 + multRollouts * squareSize][coor2 + squareSize * squareSize];
                            num++;
                        }
                    }
                }

                altitudeSquares[squareSize + 1][multRollouts] = (double)sum/num;
                altitudeSquares[multRollouts][squareSize + 1] = (double)sum2/num;//adds the bounds in and all that jazz
            }
        }

        //Now altitudeSquares is filled with the averages of each boxed in area that we have
        //so we drop the method for actually creating the rivers

        createRivers(squareSize);
    }

    //creates all teh rivers
    private void createRivers(int squareSize)
    {
        ArrayList<RiverPath> rivers = new ArrayList<>();
        int x, y;
        int totalRivers = 0;
        boolean running = true;

        while(running)
        {
            x = (int)(Math.random()*(size-2) + 1);//-2 + 1 so that we avoid the boundaries of the map
            y = (int)(Math.random()*(size-2) + 1);

            if(Math.random() > getChance(map[1][y][x]))
            {
                rivers.add(new RiverPath(map, x, y, squareSize, altitudeSquares));
                if(rivers.get(rivers.size()-1).getLength() == 1)
                {
                    rivers.remove(rivers.size() - 1);
                }
                else
                {
                    totalRivers += rivers.get(rivers.size()-1).getLength();
                }
            }
            if(totalRivers > 100)/*Something with total rivers so we get x distance at least but what about with varying sizes #ect.*/
            {
                running = false;
            }
        }
        //below prints out each river's contents
        System.out.println("#$%^&*(#$&*$#$((");

        for (RiverPath river: rivers)
        {
            System.out.println("A New River Flows:");
            for(RiverNode node: river.getRiverNodes())
            {
                System.out.println(node.getID());
            }
        }

        System.out.println("&&&*&%(*$&$#^%$^^*@$^^%^%$#");
    }

    //random chance based on altitude that there will be a river there
    private double getChance(int altitude)
    {
        double answer;

        answer = .00886427 * (-1 * altitude * altitude + 19 * altitude);//this equation gets us 80% chance of success at height of 9.5 and goes down from there,
        //plus a random chance just to make it uncertain and all that jazz
        answer += Math.random() * .2 - .1;//+-10% to odds

        return answer;
    }
}