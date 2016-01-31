package de.mxcd.conways.game;

import de.mxcd.conways.util.Program;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Max Partenfelder on 09.01.2016.
 */
public class EvolutionThread extends Thread
{
    private Lock lock = new ReentrantLock();
    public static final int[] DELAYS = {2000, 1000, 500, 250, 125, 100, 50, 20, 10, 5, 4, 2, 1};
    private boolean running = true;



    @Override
    public void run()
    {
        while(this.isRunning())
        {
            // Do the evolution step on the current board. If all cells on the board are dead true is being returned
            Board oldBoard = Program.INSTANCE.getCurrentBoard().deepCopy();
            boolean dead = EvolutionMachine.doEvolutionStep(Program.INSTANCE.getCurrentBoard(), Program.INSTANCE.getCurrentRules());
            Program.INSTANCE.stepComplete();

            // If all cells are dead (dead == true) stop the thread (running = false aka running = !dead)
            // If the new board equals the old board (steady state) stop the thread
            this.setRunning(!dead && !oldBoard.equals(Program.INSTANCE.getCurrentBoard()));

            try
            {
                Thread.sleep(DELAYS[Program.INSTANCE.getCurrentEvolutionDelayIndex()]);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
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
}