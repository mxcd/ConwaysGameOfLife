package de.mxcd.conways.util;

import de.mxcd.conways.ui.EvolutionWindow;

import java.net.URL;

/**
 * Created by Max Partenfelder on 17.10.2015.
 */
public enum SceneDef
{
    EVOLUTION(1024, 768, EvolutionWindow.class.getResource("EvolutionWindow.fxml"));

    private int width;
    private int height;
    private URL fxmlFile;

    SceneDef(int width, int height, URL fxmlFile)
    {
        this.width = width;
        this.height = height;
        this.fxmlFile = fxmlFile;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public URL getFxmlFile()
    {
        return fxmlFile;
    }
}