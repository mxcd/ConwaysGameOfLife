package de.mxcd.conways.ui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by Max Partenfelder on 12.01.2016.
 */
public class Tile extends Rectangle
{
    private int xPos;
    private int yPos;
    private boolean alive;

    private Color deadColor = Color.web("e7c4b1");
    private Color aliveColor = Color.web("31e322");

    public Tile(int xPos, int yPos)
    {
        this.xPos = xPos;
        this.yPos = yPos;

        this.setFill(this.deadColor);

        this.minWidth(1);
        this.minHeight(1);
    }

    public void setAlive()
    {
        this.alive = true;
    }

    public void setDead()
    {
        this.alive = false;
    }

    public void repaint()
    {
        if(this.alive)
        {
            this.setFill(this.aliveColor);
        }
        else
        {
            this.setFill(this.deadColor);
        }
    }
}