package de.mxcd.conways;

import de.mxcd.conways.game.Board;
import de.mxcd.conways.game.Rules;
import de.mxcd.conways.ui.SplashScreen;
import de.mxcd.conways.util.Program;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Created by Max Partenfelder on 08.01.2016.
 */
public class App extends Application
{
    /**
     * Main method for starting the UI application
     */
    public static void main(String[] args)
    {
        App.launch(args);
    }

    /**
     * start method of JavaFX app
     */
    @Override
    public void start(Stage stage) throws Exception
    {
        stage.getIcons().add(new Image(App.class.getResource("ui/images/icon.png").toString()));
        Program.INSTANCE.setMainStage(stage);
        stage.setX(200);
        stage.setY(50);

        Board board = new Board(20);
        Rules rules = new Rules();

        Program.INSTANCE.setCurrentBoard(board);
        Program.INSTANCE.setCurrentRules(rules);

        Stage splash = new Stage();
        Program.INSTANCE.setSplashScreen(splash);
        splash.getIcons().add(new Image(App.class.getResource("ui/images/icon.png").toString()));
        FXMLLoader loader = new FXMLLoader(SplashScreen.class.getResource("SplashScreen.fxml"));
        try
        {
            Parent parent = loader.load();
            Scene scene = new Scene(parent, Color.TRANSPARENT);
            splash.initStyle(StageStyle.TRANSPARENT);
            splash.setScene(scene);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        splash.show();
    }

    @Override
    public void stop() throws InterruptedException
    {
        if(Program.INSTANCE.getEvolutionThread() != null)
            Program.INSTANCE.getEvolutionThread().setRunning(false);
        if(Program.INSTANCE.getForecastThread() != null)
            Program.INSTANCE.getForecastThread().setRunning(false);
        Thread.sleep(1000);
        System.exit(0);
    }
}