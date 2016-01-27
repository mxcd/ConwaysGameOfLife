package de.mxcd.conways.game;

import de.mxcd.conways.util.ForecastListener;
import de.mxcd.conways.util.Program;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Max Partenfelder on 25.01.2016.
 * Calculating the board evolution in the background, trying to find the ultimate fate of all the little cells
 * Will they starve in loneliness? Will the find together and build a steady figure? Or will they circle around each other
 * in a repeating loop? This thread will find out! For sure! (Unless they keep on going forever. Then your RAM will be overloaded)
 */
public class ForecastThread extends Thread
{
    private List<Board> pastBoardList = new ArrayList<>();
    private Lock lock = new ReentrantLock(true);
    private boolean running = true;
    private List<ForecastListener> forecastListeners = new ArrayList<>();
    public static final int TIMEOUT = 10000;

    // Deep copy of the current board
    // So lucky that the deep copy I implemented in the Board class doesn't copy the property bindings.
    // Otherwise this would change the game board while doing the simulation
    private Board board;
    private Rules rules;

    @Override
    public void run()
    {
        this.setRunning(true);

        forecastListeners.forEach(l -> l.calculatingForecast());

        this.lock.lock();
        if(this.board == null)
            this.board = Program.INSTANCE.getCurrentBoard().deepCopy();
        if(this.rules == null)
            this.rules = Program.INSTANCE.getCurrentRules().deepCopy();
        this.lock.unlock();

        ForecastListener.ForecastResult result = new ForecastListener.ForecastResult();

        // Check if board is empty -> return result DEAD
        if(board.isDead())
        {
            result.setStepCount(0);
            result.setType(ForecastListener.ForecastResultType.DEAD);
            this.callListeners(result);
        }

        int stepCounter = 1;

        //System.out.println("Forecast thread started");

        while(this.isRunning())
        {
            if(stepCounter == TIMEOUT)
            {
                result.setStepCount(stepCounter);
                result.setType(ForecastListener.ForecastResultType.TIMEOUT);
                this.callListeners(result);
            }

            this.lock.lock();
            boolean allDead = EvolutionMachine.doEvolutionStep(this.board, this.rules);

            if(allDead)
            {
                result.setStepCount(stepCounter);
                result.setType(ForecastListener.ForecastResultType.DEAD);
                this.callListeners(result);
            }
            else
            {
                this.pastBoardList.add(this.board.deepCopy());

                if(this.pastBoardList.size() > 1)
                {
                    if (this.board.equals(this.pastBoardList.get(this.pastBoardList.size() - 2)))
                    {
                        result.setStepCount(stepCounter-1);
                        result.setType(ForecastListener.ForecastResultType.STEADY);
                        callListeners(result);
                    }
                    else
                    {
                        for (int i = 0; i < pastBoardList.size()-1; i++)
                        {
                            if (pastBoardList.get(i).equals(this.pastBoardList.get(this.pastBoardList.size() - 1)))
                            {
                                int duration = this.pastBoardList.size() - 1 - i;
                                result.setStepCount(stepCounter - duration);
                                result.setType(ForecastListener.ForecastResultType.LOOP);
                                result.setLoopDuration(duration);
                                callListeners(result);
                            }
                        }
                    }
                }
            }
            this.lock.unlock();
            ++stepCounter;
        }
    }

    public void callListeners(ForecastListener.ForecastResult result)
    {
        forecastListeners.forEach(l -> l.pushForecastResult(result));
        Program.INSTANCE.setCurrentResult(result);
        this.setRunning(false);
    }

    public void addForecastListener(ForecastListener l)
    {
        this.forecastListeners.add(l);
    }

    public boolean isRunning()
    {
        boolean result = false;
        lock.lock();
        result = running;
        lock.unlock();
        return result;
    }

    public void setRunning(boolean running)
    {
        lock.lock();
        this.running = running;
        lock.unlock();
    }

    public void restart()
    {
        this.setRunning(false);
        Program.INSTANCE.setForecastThread(new ForecastThread());
        forecastListeners.forEach(l -> Program.INSTANCE.getForecastThread().addForecastListener(l));
        Program.INSTANCE.getForecastThread().start();
    }
}