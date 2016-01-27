package de.mxcd.conways.game;

import de.mxcd.conways.util.ForecastListener;

import java.io.Serializable;

/**
 * Created by Max Partenfelder on 26.01.2016.
 */
public class GameState implements Serializable
{
    private SerializableBoard board;
    private Rules rules;
    private ForecastListener.ForecastResult result;

    public SerializableBoard getBoard()
    {
        return board;
    }

    public void setBoard(SerializableBoard board)
    {
        this.board = board;
    }

    public Rules getRules()
    {
        return rules;
    }

    public void setRules(Rules rules)
    {
        this.rules = rules;
    }

    public ForecastListener.ForecastResult getResult()
    {
        return result;
    }

    public void setResult(ForecastListener.ForecastResult result)
    {
        this.result = result;
    }
}