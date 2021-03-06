package org.eann.sim.simulation.mapgeneration;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by martin on 18.03.17.
 */
public class DiamondSquareFactory extends HeightArrayFactory {
    private final Random randomGenerator;
    private final double randomImpactReduction;

    public DiamondSquareFactory(double randomImpactReduction) {
        this.randomGenerator = new Random();
        this.randomImpactReduction = randomImpactReduction;
        if (randomImpactReduction < 1) {
            throw new IllegalArgumentException("randomImpactReduction must be langer than 1");
        }
    }

    private double getRandomDouble() {
        return randomGenerator.nextDouble() - 0.5;
    }

    public double[][] buildHeightMap(int width, int length) {
        double[][] map = new double[width][length];

        // uninitialized value
        for (int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[i].length; j++) {
                map[i][j] = Double.NaN;
            }
        }

        double randomimpact = 1;
        int xmin = 0;
        int ymin = 0;
        int xmax = width - 1;
        int ymax = length - 1;

        setValue(map, xmin, ymin, getRandomDouble());
        setValue(map, xmin, ymax, getRandomDouble());
        setValue(map, xmax, ymin, getRandomDouble());
        setValue(map, xmax, ymax, getRandomDouble());

        calcSquareDiamond(map, xmin, xmax, ymin, ymax, randomimpact);
        normalize(map);
        // printGrid(map);
        return map;
    }


    private void calcSquareDiamond(double[][] map, int xmin, int xmax, int ymin, int ymax, double randomimpact) {
        double newHeight = 0;
        int newX = (xmin + xmax) / 2;
        int newY = (ymin + ymax) / 2;

        // is already propper initialized
        if (Double.isNaN( map[newX][newY] )) {

            int[][] coords = {
                    {xmin, ymin},
                    {xmax, ymin},
                    {xmax, ymax},
                    {xmin, ymax}
            };

            // Diamond
            for (int[] pos : coords) {
                int x = pos[0];
                int y = pos[1];
                newHeight += map[x][y];
            }
            setValue(map, newX, newY, newHeight / 4 + getRandomDouble() * randomimpact);

            // Square
            ArrayList<Cell> history = new ArrayList<>();
            for (int[] pos : coords) {
                int x = pos[0];
                int y = pos[1];
                history.add(0, new Cell(x, y, map[x][y]));

                if (history.size() >= 2) {
                    makeHeight(map, history, newHeight, randomimpact);
                }
            }
            history.add(0, history.get(history.size() - 1));
            makeHeight(map, history, newHeight, randomimpact);

            randomimpact = randomimpact / randomImpactReduction;

            // bottom left
            this.calcSquareDiamond(map, xmin, newX, ymin, newY, randomimpact);

            // bottom right
            this.calcSquareDiamond(map, newX, xmax, ymin, newY, randomimpact);

            // top left
            this.calcSquareDiamond(map, xmin, newX, newY, ymax, randomimpact);

            // top right
            this.calcSquareDiamond(map, newX, xmax, newY, ymax, randomimpact);
        }
    }

    private void makeHeight(double[][] map, ArrayList<Cell> history, double lastHeight, double randomimpact) {
        double newHeight = ( history.get(0).height + history.get(1).height + lastHeight ) / 3;
        int addx = (history.get(0).x + history.get(1).x) / 2;
        int addy = (history.get(0).y + history.get(1).y) / 2;
        setValue(map, addx, addy, newHeight + getRandomDouble() * randomimpact);
    }
    private void setValue(double[][] map, int x, int y, double val) {
        // System.out.println("Setting " + x + ", " + y + " to " + val);
        if (Double.isNaN(map[x][y])) {
            map[x][y] = val;
        } else {
            // System.out.println("setting " + x + ", " + y + " WAS NAN");
        }
    }


    private class Cell {
        public int x;
        public int y;
        public double height;
        public Cell(int x, int y, double height) {
            this.x = x;
            this.y = y;
            this.height = height;
        }
    }
}
