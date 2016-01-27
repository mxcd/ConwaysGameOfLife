package de.mxcd.conways;

import de.mxcd.conways.game.Board;
import de.mxcd.conways.game.Rules;
import de.mxcd.conways.util.Program;
import de.mxcd.conways.util.SceneDef;
import javafx.application.Application;
import javafx.stage.Stage;

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
        Program.INSTANCE.setMainStage(stage);
        stage.setX(200);
        stage.setY(50);

        // TODO Splash screen
        // TODO Welcome screen with Evolution screen selection


        Board board = new Board(20);
        Rules rules = new Rules();

        Program.INSTANCE.setCurrentBoard(board);
        Program.INSTANCE.setCurrentRules(rules);

        Program.INSTANCE.setScene(SceneDef.EVOLUTION);
        Test.setUpGlider(board);

        stage.show();
    }

    @Override
    public void stop()
    {
        if(Program.INSTANCE.getEvolutionThread() != null)
            Program.INSTANCE.getEvolutionThread().setRunning(false);
    }
}