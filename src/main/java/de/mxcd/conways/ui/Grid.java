package de.mxcd.conways.ui;

import de.mxcd.conways.util.StepListener;

/**
 * Created by Max Partenfelder on 12.01.2016.
 */
public interface Grid extends StepListener
{
    public abstract void setContainerWidth(double d);
    public abstract void setContainerHeight(double d);
    public abstract void repaint();
    public abstract void sizeChanged();
}
