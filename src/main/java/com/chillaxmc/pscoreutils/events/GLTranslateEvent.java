/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.pscoreutils.events;

import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Created by lukas on 21.03.14.
 */
public class GLTranslateEvent extends Event
{
    public final float x;
    public final float y;
    public final float z;

    public GLTranslateEvent(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
