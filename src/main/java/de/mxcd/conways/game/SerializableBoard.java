package de.mxcd.conways.game;

import java.io.Serializable;

/**
 * Created by Max Partenfelder on 26.01.2016.
 */
public class SerializableBoard implements Serializable
{
    // Size of the Game Board (allowing non-quadratic shapes)
    private int width;
    private int height;

    // Array of cells
    // false = dead / true = alive
    // Coordinate origin ([0][0]) is by definition in top left corner of the board
    private boolean[][] cells;

    public SerializableBoard(int width, int height)
    {
        this.width = width;
        this.height = height;

        this.cells = new boolean[width][height];
        for (int i = 0; i < this.cells.length; i++)
        {
            for (int j = 0; j < this.cells[i].length; j++)
            {
                this.cells[i][j] = false;
            }
        }
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public boolean[][] getCells()
    {
        return cells;
    }

    public void setCells(boolean[][] cells)
    {
        this.cells = cells;
    }

    public boolean getCell(int x, int y)
    {
        if(x < this.width && y < this.height)
            return cells[x][y];
        else
            return false;
    }

    public void setCell(int x, int y, boolean flag)
    {
        if(x < this.width && y < this.height)
        {
            cells[x][y] = flag;
        }
    }
}