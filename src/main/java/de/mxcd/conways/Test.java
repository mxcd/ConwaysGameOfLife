package de.mxcd.conways;

import de.mxcd.conways.game.Board;
import de.mxcd.conways.game.EvolutionMachine;
import de.mxcd.conways.game.Rules;

/**
 * Created by Max Partenfelder on 09.01.2016.
 */
public class Test
{
    public static void main(String[] args)
    {
        Board b = new Board(11);

//        setUpBlink(b);
//        setUpGlider(b);
        setUpCross(b);

        Rules r = new Rules();
        r.moduloRules();

        System.out.println(b);

        for(int i = 0; i < 100; ++i)
        {
            EvolutionMachine.doEvolutionStep(b, r);
            System.out.println(b);

            try
            {
                Thread.sleep(200);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void setUpBlink(Board b)
    {
        b.setCell(1,2,true);
        b.setCell(2,2,true);
        b.setCell(3,2,true);
    }

    public static void setUpGlider(Board b)
    {
        b.setCell(1,0,true);
        b.setCell(2,1,true);
        b.setCell(2,2,true);
        b.setCell(1,2,true);
        b.setCell(0,2,true);
    }

    public static void setUpCross(Board b)
    {
        b.setCell(5,5,true);
        b.setCell(4,4,true);
        b.setCell(6,6,true);
        b.setCell(6,4,true);
        b.setCell(4,6,true);
    }
}
