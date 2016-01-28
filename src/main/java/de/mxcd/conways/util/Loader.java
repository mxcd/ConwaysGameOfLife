package de.mxcd.conways.util;

import de.mxcd.conways.game.Board;
import de.mxcd.conways.game.GameState;
import de.mxcd.conways.game.SerializableBoard;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by Max Partenfelder on 26.01.2016.
 */
public class Loader
{
    public static void loadFileDialog()
    {
        // Stopping the evolution thread to make sure no changes are being made on the board during the loading process
        if (Program.INSTANCE.getEvolutionThread() != null)
        {
            Program.INSTANCE.getEvolutionThread().setRunning(false);
            Program.INSTANCE.setEvolutionThread(null);
        }

        // Select file to load from
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open game");
        File file = fileChooser.showOpenDialog(Program.INSTANCE.getMainStage());

        loadFile(file);
    }

    public static void loadFile(File file)
    {
        if (file != null && file.exists())
        {
            try
            {
                // Load object out of file
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
                Object o = inputStream.readObject();
                if (o instanceof GameState)
                {
                    GameState gameState = (GameState) o;
                    SerializableBoard savedBoard = gameState.getBoard();

                    // Convert board in the one with BooleanProperties
                    Board board = new Board(savedBoard.getWidth(), savedBoard.getHeight());
                    for(int x = 0; x < savedBoard.getWidth(); ++x)
                    {
                        for(int y = 0; y < savedBoard.getHeight(); ++y)
                        {
                            board.setCell(x,y, savedBoard.getCell(x,y));
                        }
                    }

                    Program.INSTANCE.setCurrentBoard(board);
                    Program.INSTANCE.setCurrentRules(gameState.getRules());
                    Program.INSTANCE.setCurrentResult(gameState.getResult());
                    Program.INSTANCE.setScene(SceneDef.EVOLUTION);
                    Program.INSTANCE.getCurrentBoard().triggerProperties();
                    Program.INSTANCE.getForecastThread().restart();

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("Loading done");
                    alert.showAndWait();
                }
            }
            catch (IOException|ClassNotFoundException e)
            {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Loading failed");
                alert.setHeaderText("Sorry!");
                alert.setContentText("The loading didn't work!");
                alert.showAndWait();
            }
        }
    }
}