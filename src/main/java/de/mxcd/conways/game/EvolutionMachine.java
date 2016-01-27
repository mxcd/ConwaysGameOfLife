package de.mxcd.conways.game;

/**
 * Created by Max Partenfelder on 08.01.2016.
 */
public class EvolutionMachine
{
    /**
     * Performs ONE evolution step towards the next generation on the given board based on the given rules
     * @param board The board on which the evolution is executed
     * @param rules The rules that are applied
     * @return
     */
    public static boolean doEvolutionStep(Board board, Rules rules)
    {
//        Program.INSTANCE.getLock().lock();
        Board newBoard = board.deepCopy();
        for(int x = 0; x < newBoard.getWidth(); ++x)
        {
            for(int y = 0; y < newBoard.getHeight(); ++y)
            {
                Rules.Fate fate = rules.getFate(getAliveNeighborsCount(board, x, y));
                if(fate == Rules.Fate.BIRTH)
                {
                    newBoard.setCell(x,y, true);
                }
                else if(fate == Rules.Fate.DEATH)
                {
                    newBoard.setCell(x,y, false);
                }
            }
        }


        for(int x = 0; x < newBoard.getWidth(); ++x)
        {
            for(int y = 0; y < newBoard.getHeight(); ++y)
            {
                board.setCell(x,y, newBoard.getCell(x,y).get());
            }
        }
//        Program.INSTANCE.getLock().unlock();

        return board.isDead();
    }


    /**
     * Checks all elements in the moore neighborhood and returns the number of cells alive (true)
     * @param board The board to check on
     * @param x the current X position
     * @param y the current Y position
     * @return Number of living cells
     */
    public static int getAliveNeighborsCount(Board board, int x, int y)
    {
        int count = 0;

        if(board.getCell((x+1),(y+0)).get())
            ++count;
        if(board.getCell((x+1),(y+1)).get())
            ++count;
        if(board.getCell((x+0),(y+1)).get())
            ++count;
        if(board.getCell((x-1),(y+1)).get())
            ++count;
        if(board.getCell((x-1),(y+0)).get())
            ++count;
        if(board.getCell((x-1),(y-1)).get())
            ++count;
        if(board.getCell((x+0),(y-1)).get())
            ++count;
        if(board.getCell((x+1),(y-1)).get())
            ++count;

        return count;
    }
}