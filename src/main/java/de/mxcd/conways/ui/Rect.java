package de.mxcd.conways.ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by Max Partenfelder on 25.01.2016.
 */
 /**
 * Rectangular tile that displays a single cell in the game of life
 */
public class Rect
{
    // TODO "Alive color" in different green and dead cell as frame
    private Color deadColor = Color.web("e7c4b1");
    private Color aliveColor = Color.web("31e322");

    private double width;
    private double height;
    private double xPos;
    private double yPos;
    private int x;
    private int y;
    private boolean alive = false;

    public Rect(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Paints this rect on the given canvas
     * @param gc
     */
    public void paint(GraphicsContext gc)
    {
        if(this.alive)
            gc.setFill(aliveColor);
        else
            gc.setFill(deadColor);

        gc.fillRect(xPos, yPos, width, height);
    }

    // Just some getters and setters
    // Nothing to see down here

    public double getWidth()
    {
        return width;
    }

    public double getHeight()
    {
        return height;
    }

    public double getxPos()
    {
        return xPos;
    }

    // Hey! Scroll back up. Nothing to see here
    public double getyPos()
    {
        return yPos;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public boolean isAlive()
    {
        return alive;
    }

    // You are not trusting me uh?
    public void setWidth(double width)
    {
        this.width = width;
    }

    public void setHeight(double height)
    {
        this.height = height;
    }

    public void setxPos(double xPos)
    {
        this.xPos = xPos;
    }

    public void setyPos(double yPos)
    {
        this.yPos = yPos;
    }

    public void setAlive(boolean alive)
    {
        this.alive = alive;
    }

    // I told you so
}