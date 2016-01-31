package de.mxcd.conways.util;

import de.mxcd.conways.App;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Max Partenfelder on 27.01.2016.
 */
public class ExampleLoader
{
    public static Menu loadExamples()
    {
        Menu menu = new Menu("Examples");
        HashMap<String, Menu> menuMap = new HashMap<>();
        try
        {
            BufferedInputStream inputStream = new BufferedInputStream(ExampleLoader.class.getResourceAsStream("examples.txt"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            String line;
            do
            {
               line = reader.readLine();
                if(line != null)
                    addMenu(line, menu, menuMap);
            }
            while(line != null);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return menu;
    }

    private static void addMenu(String line, Menu menu, HashMap<String, Menu> menuMap)
    {
        String[] path = line.split("/");
        Menu submenu = menu;
        for(int i = 1; i < path.length -1; ++i)
        {
            if(menuMap.containsKey(path[i]))
            {
                submenu = menuMap.get(path[i]);
            }
            else
            {
                Menu m = new Menu(path[i]);
                submenu.getItems().add(m);
                menuMap.put(path[i], m);
                submenu = m;
            }
        }
        MenuItem item = new MenuItem(path[path.length-1].split(".cws")[0]);
        submenu.getItems().add(item);
        Program.INSTANCE.addExample(App.class.getResourceAsStream(line));
        item.setOnAction(event ->
        {
            Loader.loadFile(App.class.getResourceAsStream(line));
        });
    }

    /**
     * Load Example of the day
     */
    public static void loadEOTD()
    {
        Random generator = new Random();
        int exampleIndex = (int) (generator.nextDouble() * (Program.INSTANCE.getExamples().size()-1));
        Loader.loadFile(Program.INSTANCE.getExamples().get(exampleIndex));
    }
}
