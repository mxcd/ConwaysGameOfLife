package de.mxcd.conways.util;

import de.mxcd.conways.game.Board;
import de.mxcd.conways.game.GameState;
import de.mxcd.conways.game.SerializableBoard;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by Max Partenfelder on 26.01.2016.
 * Takes the current state of the game and saves it in a file
 */
public class Saver
{
    public static void saveGame()
    {
        GameState state = new GameState();

        // Stopping the evolution thread to make sure no changes are being made on the board during the saving process


        state.setBoard(copyBoard(Program.INSTANCE.getCurrentBoard()));
        state.setResult(Program.INSTANCE.getCurrentResult());
        state.setRules(Program.INSTANCE.getCurrentRules());

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save current game state");
        File file = fileChooser.showSaveDialog(Program.INSTANCE.getMainStage());

        if(file != null)
        {
            try
            {
                ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
                outputStream.writeObject(state);
                outputStream.close();
            }
            catch (IOException e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Saving failed");
                alert.setHeaderText("Sorry!");
                alert.setContentText("The saving failed due to an error.");
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    /**
     * Converts a Board into a serializable counterpart
     * SimpleBooleanProperties are not serializable. That's why!
     * @param board Game Board with SimpleBooleanProperties
     * @return Game Board with simple booleans (no properties)
     */
    public static SerializableBoard copyBoard(Board board)
    {
        SerializableBoard b = new SerializableBoard(board.getWidth(), board.getHeight());

        for(int x = 0; x < board.getWidth(); ++x)
        {
            for(int y = 0; y < board.getHeight(); ++y)
            {
                b.setCell(x,y, board.getCell(x,y).get());
            }
        }

        return b;
    }
}