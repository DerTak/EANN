package org.eann.sim.simulation;

/**
 * Created by martin on 16.03.17.
 */
public class World {
    private Tile[][] tiles;
    private int noOfTileWidth;
    private int noOfTileLength;
    private int tileSize;

    public World(double[][] heights, int tileSize) {
        this.tiles = new Tile[heights.length][heights[0].length];
        this.tileSize = tileSize;

        this.noOfTileWidth = heights.length;
        this.noOfTileLength = heights[0].length;

        for (int i = 0; i < noOfTileWidth; i++) {
            for (int j = 0; j < noOfTileLength; j++) {
                this.addTitle( new Tile(heights[i][j], i, j) );
            }
        }
    }

    public Tile getTileAt(int x, int y) {
        return this.tiles[x][y];
    }

    public void setTitleAt(int x, int y, Tile tile) {
        this.tiles[x][y] = tile;
    }

    public void addTitle(Tile newTile) {
        if (this.getTileAt(newTile.getX(), newTile.getY())  == null) {
            this.setTitleAt(newTile.getX(), newTile.getY(), newTile);
        }
    }

    public Tile[][] getTiles() {
        return this.tiles;
    }

    public int getTileWidth() {
        return noOfTileWidth;
    }
    public int getTileLength() {
        return noOfTileLength;
    }
    public int getTileSize() {
        return tileSize;
    }
    public int getWidth() { return noOfTileWidth * tileSize; }
    public int getLength() { return noOfTileLength * tileSize; }
}
