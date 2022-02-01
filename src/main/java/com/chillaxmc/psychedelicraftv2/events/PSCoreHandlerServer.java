/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.psychedelicraftv2.events;

import com.chillaxmc.psychedelicraftv2core.PsycheCoreBusCommon;

/**
 * Created by lukas on 04.03.14.
 */
public class PSCoreHandlerServer
{
    public void register()
    {
        PsycheCoreBusCommon.EVENT_BUS.register(this);
    }
}
