package de.mxcd.conways.game;

import java.io.Serializable;

/**
 * Created by Max Partenfelder on 08.01.2016.
 */
public class Rules implements Serializable
{
    /**
     * The possible fates of a cell
     */
    public enum Fate
    {
        BIRTH,
        LIFE,
        DEATH;
    }

    private Fate[] fates = new Fate[9];

    /**
     * initializes the rule set with the default conway rules
     */
    public Rules()
    {
        fates[0] = Fate.DEATH;  // Fate of cell with 0 living neighbors
        fates[1] = Fate.DEATH;  // ... with 1 living neighbor
        fates[2] = Fate.LIFE;   // etc.
        fates[3] = Fate.BIRTH;
        fates[4] = Fate.DEATH;
        fates[5] = Fate.DEATH;
        fates[6] = Fate.DEATH;
        fates[7] = Fate.DEATH;
        fates[8] = Fate.DEATH;
    }

    /**
     * Inverts the rules, e.g. to create the Anti-Conway-Rules
     */
    public void invertRules()
    {
        for(int i = 0; i < 9; ++i)
        {
            if(fates[i] == Fate.BIRTH)
            {
                fates[i] = Fate.DEATH;
            }
            else if(fates[i] == Fate.DEATH)
            {
                fates[i] = Fate.BIRTH;
            }
        }
    }

    public void moduloRules()
    {
        for(int i = 0; i < 9; ++i)
        {
            if(i%2 == 0)
            {
                fates[i] = Fate.DEATH;
            }
            else if(i%2 == 1)
            {
                fates[i] = Fate.BIRTH;
            }
        }
    }

    public void setRule(int neighborsAlive, Fate fate)
    {
        if(neighborsAlive >= 0 && neighborsAlive <= 8)
        {
            fates[neighborsAlive] = fate;
        }
    }

    /**
     * Returns the Fate of a cell defined by this rule set based
     * on the number of living neighbors in the moore neighborhood
     * @param neighborsAlive
     * @return
     */
    public Fate getFate(int neighborsAlive)
    {
        return fates[neighborsAlive];
    }

    public Rules deepCopy()
    {
        Rules r = new Rules();
        for (int i = 0; i < fates.length; i++)
        {
            r.setRule(i, fates[i]);
        }
        return r;
    }
}