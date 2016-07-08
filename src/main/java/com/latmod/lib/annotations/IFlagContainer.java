package com.latmod.lib.annotations;

/**
 * Created by LatvianModder on 26.03.2016.
 */
public interface IFlagContainer extends IAnnotationContainer
{
    int getFlags();

    void setFlags(int flags);
}
