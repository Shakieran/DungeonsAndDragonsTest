package com.kedraney.ddgame.mapgeneration;

public class MapRiverMaker
{
    private int[][][] map;
    private int[][] altitudeSquares;

    private final int size, increment;

    public MapRiverMaker(int[][][] daMap)
    {
        map = daMap;
        size = map[0].length;
        increment = (int)(Math.pow(size, .6667)/2.0);

        int sum, sum2;
        int squareSize = (int)(.5 + (double)size/increment);
        boolean remainder = squareSize == (int)((double)size/increment);//if there is a remainder, this is true
        altitudeSquares = new int[squareSize][squareSize];

        if(remainder)
        {
            squareSize -= 2;
        }


        for(int xMult = 0; xMult < squareSize; xMult++)
        {
            for(int yMult = 0; yMult < squareSize; yMult++)//these values cycle through the standard values for the shapes and all that
            {
                sum = 0;
                for(int y = 0; y < squareSize; y++)
                {
                    for(int x = 0; x < squareSize; x++)
                    {
                        sum += map[1][y + yMult * squareSize][x + xMult * squareSize];//altitude sums and all that jazz
                    }
                }

                altitudeSquares[yMult][xMult] = sum;
            }
        }

        if(remainder)
        {
//            squareSize++;
            for(int multRollouts = 0; multRollouts < squareSize + 1; multRollouts++)
            {
                sum=0;
                sum2=0;
                for(int coor1 = 0; coor1 < squareSize; coor1++)
                {
                    for(int coor2 = 0; coor2 < size % increment; coor2++)
                    {
                        if(!(multRollouts == squareSize && coor1 >= size % increment))
                        {
                            sum += map[1][coor2 + squareSize * squareSize][coor1 + multRollouts * squareSize];//gets the border that isn't a square
                            sum2 += map[1][coor1 + multRollouts * squareSize][coor2 + squareSize * squareSize];
                        }
                    }
                }

                altitudeSquares[squareSize + 1][multRollouts] = sum;
                altitudeSquares[multRollouts][squareSize + 1] = sum2;
            }
        }
    }
}