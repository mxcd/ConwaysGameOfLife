package de.mxcd.conways.ui;

import de.mxcd.conways.game.Board;
import de.mxcd.conways.util.Program;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;

/**
 * @Depricated
 * UI representation of the evolution board
 * Created by Max Partenfelder on 09.01.2016.
 */
public class GridBox extends HBox implements Grid
{
    private Board board;

    private static final int PADDING = 20;
    private double hGap;
    private double vGap;
    private SimpleDoubleProperty tileWidth;
    private SimpleDoubleProperty tileHeight;

    private long lastRepaintTime = 0;

    private double width = 0;
    private double height = 0;

    private HashMap<Point2D, Tile> tiles = new HashMap<>();

    public GridBox(Board board)
    {
        this.board = board;
        this.setPrefWidth(0);

        this.setPadding(new Insets(PADDING));

        hGap = 100.0/board.getWidth();
        vGap = 100.0/board.getHeight();

        tileWidth = new SimpleDoubleProperty(20);
        tileHeight = new SimpleDoubleProperty(20);

        this.setSpacing(hGap);

        for(int x = 0; x < board.getWidth(); ++x)
        {
            VBox vBox = new VBox();
            vBox.setSpacing(vGap);
            vBox.setPrefWidth(0);

            for(int y = 0; y < board.getHeight(); ++y)
            {
                // Create a UI element for each cell
                Tile tile = new Tile(x,y);

                tile.widthProperty().bindBidirectional(tileWidth);
                tile.heightProperty().bindBidirectional(tileHeight);

                // Bind status of the cell (living / dead) to the color of the cell
                board.getCell(x,y).addListener((observable, oldValue, newValue) ->
                {
                    Platform.runLater(()->
                    {
                        if(newValue)
                        {
                            //TODO more fancy animation based on speed and density of board
                            tile.setAlive();
                        }
                        else
                        {
                            tile.setDead();
                        }
                    });

                });

                tile.setOnMouseClicked((event)->
                {
                    //TODO toggle cell and set board cell

                });

                vBox.getChildren().add(tile);

            }
            this.getChildren().add(vBox);
        }
    }

    public void setWidth(double d)
    {
        this.width = d;
        this.setPrefWidth(d);
        adjustTileSize();
    }

    public void setHeight(double d)
    {
        this.height = d;
        this.setPrefHeight(d);
        adjustTileSize();
    }

    private void adjustTileSize()
    {
        Program.INSTANCE.getLock().lock();
        double width = ((this.width - hGap - PADDING*2) / board.getWidth()) - hGap;
        double height = ((this.height - vGap - PADDING*2) / board.getHeight()) - vGap;
        double resultingSize = 0;
        System.out.println("ContainerWidth:" + this.width + " - TileWidth:" + width + " - TileHeight:" + height);
        if(width < height)
            resultingSize = width;
        else
            resultingSize = height;


        tileWidth.set(resultingSize);
        tileHeight.set(resultingSize);
        Program.INSTANCE.getLock().unlock();
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

    @Override
    public void repaint()
    {

    }

    @Override
    public void sizeChanged()
    {
        adjustTileSize();
    }

    @Override
    public void stepComplete()
    {
        Platform.runLater(()->
        {
            if(System.currentTimeMillis() - this.lastRepaintTime > 1000/60)
            {
                this.lastRepaintTime = System.currentTimeMillis();
                for (Point2D point : tiles.keySet())
                {
                    Tile tile = tiles.get(point);
                    tile.repaint();
                }
            }
        });
    }
}