package de.mxcd.conways.game;

import javafx.beans.property.SimpleBooleanProperty;

import java.io.Serializable;

/**
 * Created by Max Partenfelder on 08.01.2016.
 */
public class Board implements Serializable
{
    // Size of the Game Board (allowing non-quadratic shapes)
    private int width;
    private int height;

    // Array of cells
    // false = dead / true = alive
    // Coordinate origin ([0][0]) is by definition in top left corner of the board
    private SimpleBooleanProperty[][] cells;

    /**
     * Constructor for non-quadratic board
     * @param width
     * @param height
     */
    public Board(int width, int height)
    {
        this.width = width;
        this.height = height;

        init();
    }

    /**
     * Constructor for quadratic board
     * @param size
     */
    public Board(int size)
    {
        this.width = size;
        this.height = size;

        init();
    }

    /**
     * Initialization of game board
     * TODO: Do action in separate thread
     */
    public void init()
    {
        this.cells = new SimpleBooleanProperty[width][height];
        for (int i = 0; i < this.cells.length; i++)
        {
            for (int j = 0; j < this.cells[i].length; j++)
            {
                this.cells[i][j] = new SimpleBooleanProperty(false);
            }
        }
    }

    /**
     * Set the cell array
     * @param cells
     * @throws IllegalArgumentException
     */
    public void setCells(SimpleBooleanProperty[][] cells)
    {
        if(cells.length != width || cells[0].length != height)
        {
            throw new IllegalArgumentException("Data doesn't match board dimensions!");
        }
        else
        {
            this.cells = cells;
        }
    }

    /**
     * Get the cell array
     * @return
     */
    public SimpleBooleanProperty[][] getCells()
    {
        return cells;
    }

    /**
     * Set a specific cell
     * @param x X-Coordinate of the cell
     * @param y Y-Coordinate of the cell
     * @param flag The state to be assigned (false = dead / true = alive)
     */
    public void setCell(int x, int y, boolean flag)
    {
        try
        {
            this.cells[getInBound(x, this.width)][getInBound(y, this.height)].set(flag);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            throw new IllegalArgumentException("Position out of bounds!");
        }
    }

    public SimpleBooleanProperty getCell(int x, int y)
    {
        try
        {
            return this.cells[getInBound(x, this.width)][getInBound(y, this.height)];
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            throw new IllegalArgumentException("Position out of bounds!");
        }
    }

    /**
     * Resets all cells of the field to dead
     */
    public void clearField()
    {
        this.cells = new SimpleBooleanProperty[width][height];
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    /**
     * Returns a value within the bounds of the given size
     * @param x
     * @param size
     * @return
     */
    private int getInBound(int x, int size)
    {
        if(x < 0)
        {
            x = size + x;
        }
        if(x >= size)
        {
            x -= size;
        }

        return x;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        for(int y = 0; y <= this.height + 1; ++y)
        {
            for(int x = 0; x <= this.height + 1; x++)
            {
                if(x == 0 || x == this.width + 1)
                {
                    sb.append("\tX");
                }
                else if(y == 0 || y == this.height + 1)
                {
                    sb.append("\tX");
                }
                else
                {
                    if (this.cells[x - 1][y - 1].get())
                    {
                        sb.append("\to");
                    }
                    else
                    {
                        sb.append("\t");
                    }
                }
            }
            sb.append("\n");
        }

        sb.append("\n\n");

        return sb.toString();
    }

    /**
     * Creates a deep copy of this board and returns it
     * WARNING: Property bindings are not being copied!
     * @return
     */
    public Board deepCopy()
    {
        Board b = new Board(this.width, this.height);
        for (int x = 0; x < cells.length; x++)
        {
            for (int y = 0; y < cells[x].length; y++)
            {
                b.setCell(x,y, this.cells[x][y].get());
            }
        }
        return b;
    }

    public boolean isDead()
    {
        boolean alive = false;
        for (int i = 0; i < this.cells.length; i++)
        {
            for (int j = 0; j < this.cells[i].length; j++)
            {
                if(this.cells[i][j].get())
                {
                    alive = true;
                }
            }
        }
        return !alive;
    }

    @Override
    public boolean equals(Object o)
    {
        if(o instanceof Board)
        {
            Board b = (Board)o;
            if(b.getWidth() != this.getWidth() || b.getHeight() != this.getHeight())
                return false;

            for (int i = 0; i < this.cells.length; i++)
            {
                for (int j = 0; j < this.cells[i].length; j++)
                {
                    if(this.cells[i][j].get() != b.getCell(i,j).get())
                        return false;
                }
            }
            return true;
        }

        return false;
    }

    public void triggerProperties()
    {
        for (int i = 0; i < this.cells.length; i++)
        {
            for (int j = 0; j < this.cells[i].length; j++)
            {
                this.cells[i][j].set(!this.cells[i][j].get());
                this.cells[i][j].set(!this.cells[i][j].get());
            }
        }
    }
}