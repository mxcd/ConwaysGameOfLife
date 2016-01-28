package de.mxcd.conways.ui;

import de.mxcd.conways.game.Board;
import de.mxcd.conways.util.Program;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Created by Max Partenfelder on 12.01.2016.
 */

/**
 * The variable-sized grid of tiles representing the cells of the game
 */
public class GridCanvas extends Canvas implements Grid
{
    private Board board;
    // Adjustment values for padding (grid to borders) and spacing (between the tiles)
    // Values just for adjustment and not yet pixel values; real value is calculated based on grid size
    private static final int PADDING = 20;
    private static final int SPACING = 100;
    // The calculated spacing
    private double spacing;

    // Time since last repaint (used for 60FPS adjustment)
    private long lastRepaintTime = 0;

    // Rectangles (tiles) of the grid mapped to their respective point
    private Rect[][] rectArray;

    public static final int FRAMETIME = 1000 / 60;


    // Stuff for the drag detection
    private boolean[][] selectedTiles;
    private boolean markingDirection = true; // true: from dead to live // false: from alive to dead
    private boolean firstTileToggled = true;
    private boolean dragging = false;

    public GridCanvas(Board board)
    {
        this.board = board;
        // Calculation of spacing
        this.spacing = SPACING * 1.0d / this.board.getWidth();

        rectArray = new Rect[board.getWidth()][board.getHeight()];

        // Creating of Rects
        for(int x = 0; x < board.getWidth(); ++x)
        {
            for(int y = 0; y < board.getHeight(); ++y)
            {
                Rect rect = new Rect(x,y);
                adjustRectBounds(rect);
                rectArray[x][y] = rect;
                board.getCell(x,y).addListener(((observable, oldValue, newValue) ->
                {
                    rect.setAlive(newValue);
                }));
            }
        }

        Program.INSTANCE.addStepListener(this);

        // 3 Mouse events for changing the board's cells
        // Cells can be changed by clicking on a singe one or by dragging over multiple ones
        // Note that cells are only changing one and only in one direction within a drag
        // So if you first cross a dead cell, the drag will only change dead cells into living ones,
        // leaving the living cells untouched
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, event ->
        {
            dragStartOrGo(event);
        });
        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, event ->
        {
            dragStartOrGo(event);
        });
        this.addEventHandler(MouseEvent.MOUSE_RELEASED, event ->
        {
            dragRelease(event);
        });

        this.selectedTiles = new boolean[this.board.getWidth()][this.board.getHeight()];
    }

    /**
     * Starts a drag sequence
     * Can be called by MOUSE_DRAGGED for a drag move or by MOUSE_PRESSED for a single click action
     * @param event
     */
    public void dragStartOrGo(MouseEvent event)
    {
        if(!this.dragging)
        {
            this.dragging = true;
            Program.INSTANCE.stopForecastThread();
            Program.INSTANCE.stopEvolutionThread();
        }
        this.clickOn(event.getX(), event.getY());
    }

    /**
     * Marks the end of a drag move or click action, resets all required variables and restarts the forecast thread
     * @param event
     */
    public void dragRelease(MouseEvent event)
    {
        this.dragging = false;
        this.selectedTiles = new boolean[this.board.getWidth()][this.board.getHeight()];
        this.firstTileToggled = true;
        Program.INSTANCE.getForecastThread().restart();
    }

    public synchronized void stepComplete()
    {
        repaint();
    }

    @Override
    public void setContainerWidth(double d)
    {
        this.setWidth(d);
    }

    @Override
    public void setContainerHeight(double d)
    {
        this.setHeight(d);
    }

    /**
     * There are in fact two ways of doing this: the brute force way, where all rects are being checked for
     * coverage with the click event and the intelligent waz where the x and y coordinates are being used to
     * calculate weather or not the click was within a rect. Of course we do the second one ;)
     *
     * @param xPos The pixel value of the click in x direction
     * @param yPos The pixel value of the click in y direction
     */
    private void clickOn(double xPos, double yPos)
    {
        // Basically the same stuff as below at the bounds calculation
        double offsetX = 0;
        double offsetY = 0;

        if(this.getWidth() > this.getHeight())
            offsetX = (this.getWidth() - this.getHeight())/2;
        else
            offsetY = (this.getHeight() - this.getWidth())/2;


        double width = (this.getWidth() - PADDING*2)/this.board.getWidth();
        double height = (this.getHeight() - PADDING*2)/this.board.getHeight();

        width = Math.min(width, height);
        height = Math.min(width, height);

        // Just this is inverted to get the proper x and y index instead of a x and y pixel value
        int x = (int)((xPos - PADDING - offsetX)/width);
        int y = (int)((yPos - PADDING - offsetY)/height);

        if(xPos > offsetX && yPos > offsetY)
        {
            if(!this.selectedTiles[x][y])
            {
                this.selectedTiles[x][y] = true;
                if(this.firstTileToggled)
                {
                    this.firstTileToggled = false;
                    this.markingDirection = !this.board.getCell(x, y).get();
                }

                if(this.markingDirection == !this.board.getCell(x, y).get())
                    this.board.getCell(x, y).set(!this.board.getCell(x, y).get());
            }
        }
        repaint();
    }

    /**
     * Repaint of the canvas, that will be triggered every time there is a change in color or size
     */
    public synchronized void repaint(boolean onlyColorChanged)
    {
        Platform.runLater(()->
        {
            // Check for last repaint time to avoid unnecessary high update rates
            if(System.currentTimeMillis() - this.lastRepaintTime > FRAMETIME)
            {
                this.lastRepaintTime = System.currentTimeMillis();

                // Clearing the canvas to avoid errors while resizing
                this.getGraphicsContext2D().clearRect(0, 0, this.getWidth(), this.getHeight());
                // Setting a white background
                this.getGraphicsContext2D().setFill(Color.WHITE);
                this.getGraphicsContext2D().fillRect(0,0,this.getWidth(), this.getHeight());
                // Repaint every rect in the rect map
                for (int x = 0; x < rectArray.length; x++)
                {
                    for (int y = 0; y < rectArray[x].length; y++)
                    {
                        if(rectArray[x][y] != null)
                            if(rectArray[x][y].isColorChanged() || !onlyColorChanged)
                                rectArray[x][y].paint(this.getGraphicsContext2D());
                    }
                }
            }
        });
    }

    public void repaint()
    {
        this.repaint(false);
    }

    /**
     * Calculation of the bounds (size and position) of a given rect (tile) on the canvas board based on board size and
     * spacing / padding
     * @param rect The rect whose bounds should be adjusted
     */
    public synchronized void adjustRectBounds(Rect rect)
    {
        // General X and Y offset from the borders of the canvas
        double offsetX = 0;
        double offsetY = 0;

        if(this.getWidth() > this.getHeight())
            offsetX = (this.getWidth() - this.getHeight())/2;
        else
            offsetY = (this.getHeight() - this.getWidth())/2;

        // Width and height of the rect
        double width = (this.getWidth() - PADDING*2)/this.board.getWidth();
        double height = (this.getHeight() - PADDING*2)/this.board.getHeight();

        // Keeping the rects square
        // Width is here with spacing
        width = Math.min(width, height);
        height = Math.min(width, height);

        // X and Y position of the rects
        double xPos = PADDING + offsetX + rect.getX()*width;
        double yPos = PADDING + offsetY + rect.getY()*height;

        // Now the spacing is subtracted to assign width and height to rect
        width -= this.spacing;
        height -= this.spacing;

        rect.setWidth(width);
        rect.setHeight(height);
        rect.setxPos(xPos);
        rect.setyPos(yPos);
    }

    /**
     * Recalculates the bounds of a rect when necessary instead of doing it on every single repaint
     */
    public void sizeChanged()
    {
        this.getGraphicsContext2D().clearRect(0, 0, this.getWidth(), this.getHeight());
        for (int x = 0; x < rectArray.length; x++)
        {
            for (int y = 0; y < rectArray[x].length; y++)
            {
                Rect rect = rectArray[x][y];
                adjustRectBounds(rect);
                rect.paint(this.getGraphicsContext2D());
            }
        }
    }
}