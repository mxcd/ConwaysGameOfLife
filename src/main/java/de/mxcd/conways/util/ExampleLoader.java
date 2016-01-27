package de.mxcd.conways.util;

import de.mxcd.conways.App;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Created by Max Partenfelder on 27.01.2016.
 */
public class ExampleLoader
{
    public static Menu loadExamples()
    {
        File exampleRoot = null;
        try
        {
            exampleRoot = new File(App.class.getResource("Examples").toURI());
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
        Menu menu = loadDirectory(exampleRoot);
        return menu;
    }

    public static Menu loadDirectory(File directory)
    {
        Menu menu = new Menu(directory.getName());

        File[] files = directory.listFiles();
        for(int i = 0; i < files.length; ++i)
        {
            if(files[i].isDirectory())
            {
                menu.getItems().add(loadDirectory(files[i]));
            }
        }

        for(int i = 0; i < files.length; ++i)
        {
            if(files[i].isFile())
            {
                File file = files[i];
                MenuItem item = new MenuItem(files[i].getName());
                item.setOnAction(event ->
                {
                    Loader.loadFile(file);
                });

                menu.getItems().add(item);
            }
        }
        return menu;
    }
}
