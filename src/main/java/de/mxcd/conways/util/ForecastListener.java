package de.mxcd.conways.util;

import java.io.Serializable;

/**
 * Created by Max Partenfelder on 25.01.2016.
 */
public interface ForecastListener
{
    // Possible outcomes of the calculation
    enum ForecastResultType
    {
        LOOP,
        DEAD,
        STEADY,
        TIMEOUT
    }

    // Wrapper for the result. Value (not banana) for scale.
    class ForecastResult implements Serializable
    {
        private ForecastResultType type;
        private int stepCount;
        private int loopDuration;

        public ForecastResultType getType()
        {
            return type;
        }

        public void setType(ForecastResultType type)
        {
            this.type = type;
        }

        public int getStepCount()
        {
            return stepCount;
        }

        public void setStepCount(int stepCount)
        {
            this.stepCount = stepCount;
        }

        public int getLoopDuration()
        {
            return loopDuration;
        }

        public void setLoopDuration(int loopDuration)
        {
            this.loopDuration = loopDuration;
        }

        @Override
        public String toString()
        {
            String s = "Forecast Result: ";
            switch (this.type)
            {
                case LOOP:
                    s += "Loop found after " + this.stepCount + " steps. Loop duration: " + loopDuration;
                    break;
                case DEAD:
                    s += "Field dead after " + this.stepCount + " steps.";
                    break;
                case STEADY:
                    s += "Field steady after " + this.stepCount + " steps.";
                    break;
                case TIMEOUT:
                    s += "No result after " + this.stepCount + " steps.";
            }
            return s;
        }
    }

    public void pushForecastResult(ForecastResult result);
    public void calculatingForecast();
}
