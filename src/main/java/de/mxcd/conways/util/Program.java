package de.mxcd.conways.util;

import de.mxcd.conways.game.Board;
import de.mxcd.conways.game.EvolutionThread;
import de.mxcd.conways.game.ForecastThread;
import de.mxcd.conways.game.Rules;
import de.mxcd.conways.ui.EvolutionWindow;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Max Partenfelder on 17.10.2015.
 */
public enum Program
{
    INSTANCE;

    private Stage mainStage;
    private Scene currentScene;
    private Board currentBoard;
    private Rules currentRules;
    private ForecastListener.ForecastResult currentResult;
    private EvolutionThread evolutionThread;
    private ForecastThread forecastThread;
    private Lock lock = new ReentrantLock(true);
    private List<StepListener> stepListeners= new ArrayList<>();
    private EvolutionWindow evolutionWindow;

    public Stage getMainStage()
    {
        return mainStage;
    }

    public void setMainStage(Stage mainStage)
    {
        this.mainStage = mainStage;
    }

    public Scene getCurrentScene()
    {
        return currentScene;
    }

    public void setScene(SceneDef def)
    {
        FXMLLoader loader = new FXMLLoader(def.getFxmlFile());
        try
        {
            Parent parent = loader.load();
            Scene scene = new Scene(parent, def.getWidth(), def.getHeight());
            this.currentScene = scene;
            this.getMainStage().setScene(scene);
            this.getMainStage().sizeToScene();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Board getCurrentBoard()
    {
        return currentBoard;
    }

    public void setCurrentBoard(Board currentBoard)
    {
        this.currentBoard = currentBoard;
    }

    public Rules getCurrentRules()
    {
        return currentRules;
    }

    public void setCurrentRules(Rules currentRules)
    {
        this.currentRules = currentRules;
    }

    public void setEvolutionThread(EvolutionThread evolutionThread)
    {
        this.evolutionThread = evolutionThread;
    }

    public EvolutionThread getEvolutionThread()
    {
        return evolutionThread;
    }

    public Lock getLock()
    {
        return lock;
    }

    public void addStepListener(StepListener stepListener)
    {
        this.stepListeners.add(stepListener);
    }

    public void stepComplete()
    {
        this.stepListeners.forEach(stepListener ->
        {
            stepListener.stepComplete();
        });
    }

    public ForecastThread getForecastThread()
    {
        return this.forecastThread;
    }

    public void setForecastThread(ForecastThread forecastThread)
    {
        this.forecastThread = forecastThread;
    }

    public ForecastListener.ForecastResult getCurrentResult()
    {
        return currentResult;
    }

    public void setCurrentResult(ForecastListener.ForecastResult currentResult)
    {
        this.currentResult = currentResult;
    }

    public void setEvolutionWindow(EvolutionWindow evolutionWindow)
    {
        this.evolutionWindow = evolutionWindow;
    }

    public EvolutionWindow getEvolutionWindow()
    {
        return evolutionWindow;
    }

    public void stopEvolutionThread()
    {
        if(Program.INSTANCE.getEvolutionThread() != null)
        {
            Program.INSTANCE.getEvolutionThread().setRunning(false);
            Program.INSTANCE.setEvolutionThread(null);
        }
    }

    public void stopForecastThread()
    {
        if(Program.INSTANCE.getForecastThread().isRunning())
        {
            Program.INSTANCE.getForecastThread().setRunning(false);
        }
    }
}