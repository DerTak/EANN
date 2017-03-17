package org.eann.sim.simulation;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by martin on 17.03.17.
 */
public class WorldFactory {
    private final Random randomGenerator;
    private final org.eann.sim.configuration.World worldConfiguration;

    public WorldFactory(org.eann.sim.configuration.World worldConfiguration) {
        this.randomGenerator = new Random();
        this.worldConfiguration = worldConfiguration;
    }
    public World buildWorld() throws AddTitleException {
        World world = new World(this.worldConfiguration.getWidth(), this.worldConfiguration.getLength());

        int height  = this.worldConfiguration.getHeight();
        int xmin = 0;
        int ymin = 0;
        int xmax = this.worldConfiguration.getWidth() - 1;
        int ymax = this.worldConfiguration.getHeight() - 1;

        world.addTitle(this.buildTile(xmin, ymin));
        world.addTitle(this.buildTile(xmin, ymax));
        world.addTitle(this.buildTile(xmax, ymin));
        world.addTitle(this.buildTile(xmax, ymax));

        this.calcSquareDiamond(world, xmin, xmax, ymin, ymax);

        return world;
    }

    private void calcSquareDiamond(World world, int xmin, int xmax, int ymin, int ymax) throws AddTitleException {
        float newHeight = this.getRandomHeight();

        int newX = (xmin + xmax) / 2;
        int newY = (ymin + ymax) / 2;

        if (world.getTileAt(newX, newY) != null) {
            throw new AddTitleException(newX, newY);
        }

        int[] xarray = {xmin, xmax};
        int[] yarray = {ymin, ymax};

        // Diamond
        for(int x: xarray) {
            for(int y: yarray) {
                newHeight += world.getTileAt(x, y).getHeight();
            }
        }
        Tile newTile = this.buildTile(newHeight / 5, newX, newY);
        world.addTitle(newTile);

        // Square
        ArrayList<Tile> history = new ArrayList<Tile>();
        for(int x: xarray) {
            for(int y: yarray) {
                Tile tile = world.getTileAt(x, y);
                history.add(0, tile);
                if ( history.size() >= 2 ) {
                    newHeight = this.getRandomHeight() + history.get(0).getHeight() + history.get(1).getHeight() + newTile.getHeight();
                    int addx = (history.get(0).getX() + history.get(1).getX()) / 2;
                    int addy = (history.get(0).getY() + history.get(1).getY()) / 2;

                    history.remove(1);
                    world.addTitle(this.buildTile(newHeight, addx, addy));
                }
            }
        }
    }

    private float getRandomHeight() {
        int height = this.worldConfiguration.getHeight();
        return this.randomGenerator.nextFloat()*2*height - height;
    }

    private Tile buildTile(float height, int x, int y) {
        return new Tile(height, x, y);
    }
    private Tile buildTile(int x, int y) {
        return new Tile(this.getRandomHeight(), x, y);
    }
}
