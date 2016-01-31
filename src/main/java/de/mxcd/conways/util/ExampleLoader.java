package de.mxcd.conways.util;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.io.File;
import java.net.URL;

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
            // Load the directory as a resource
            URL url = ClassLoader.getSystemResource("de/mxcd/conways/Examples");
            // Turn the resource into a File object
            exampleRoot = new File(url.toURI());
        }
        catch (Exception e)
        {
//            System.out.println("Examples root load exception");
            e.printStackTrace();
        }
        Menu menu = loadDirectory(exampleRoot);
        return menu;
    }

    public static Menu loadDirectory(File directory)
    {
//        System.out.println(directory);
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
//                System.out.println(files[i]);
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
