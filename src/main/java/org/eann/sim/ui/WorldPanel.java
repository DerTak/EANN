package org.eann.sim.ui;

import org.eann.sim.simulation.World;

import javax.swing.*;
import java.awt.*;

/**
 * Created by martin on 17.03.17.
 */
public class WorldPanel extends JPanel {

    private World world;
    private int zoomLevel = 8;

    public WorldPanel() {

    }
    public WorldPanel(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();

        size.width = Math.max(size.width, 200);
        size.height = Math.max(size.height, 20);

        if (this.world != null) {
            size.width = Math.max(size.width, this.world.getWidth() * zoomLevel);
            size.height = Math.max(size.height, this.world.getLength() * zoomLevel + 10);
        }

        return size;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        this.paintWorld(graphics);
        this.paintLegend(graphics);
    }

    private void paintLegend(Graphics graphics) {
        if (this.world != null) {
            int widthOffset = this.world.getWidth();
            int lengthOffset = this.world.getLength();

            for (int i = 1; i < 10; i++) {
                for (int w = -100; w <= 100; w++) {
                    Color color = this.heightToColor(w);
                    this.fillRect(graphics, color, lengthOffset + i, widthOffset + 100 + w, 9, 1, this.zoomLevel, 2);
                }
            }
        }
    }

    private void paintWorld(Graphics graphics) {
        if (this.world != null) {
            int width = this.world.getWidth();
            int length = this.world.getLength();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < length; y++) {
                    float height = this.world.getTileAt(x, y).getHeight();
                    Color color = heightToColor(height);
                    this.fillRect(graphics, color, x, y, 1, 1, this.zoomLevel, this.zoomLevel);
                }
            }
        }
    }

    private void fillRect(Graphics graphics, Color color, int x, int y, int sizeX, int sizeY, int zoomLevelX, int zoomLevelY) {
        graphics.setColor(color);
        graphics.fillRect(x * zoomLevelX, y * zoomLevelY, sizeX * zoomLevelX, sizeY * zoomLevelY);
    }

    private Color heightToColor(float height) {
        float brigthness = 0.8f - Math.abs(height) / 200;
        // System.out.println(x + " " + y + " : höhe " + height + " zu " + brigthness);
        Color color = height < 0 ? Color.getHSBColor(.60f, 0.90f, brigthness) : Color.getHSBColor(.40f, 0.90f, brigthness);
        return color;
    }
}
