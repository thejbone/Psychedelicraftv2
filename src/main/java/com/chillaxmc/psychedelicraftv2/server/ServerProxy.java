/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.psychedelicraftv2.server;

import com.chillaxmc.psychedelicraftv2.PSProxy;
import com.chillaxmc.psychedelicraftv2.Psychedelicraftv2;
import com.chillaxmc.psychedelicraftv2.entities.drugs.DrugProperties;
import com.chillaxmc.psychedelicraftv2.events.PSCoreHandlerServer;
import net.minecraft.entity.Entity;

public class ServerProxy implements PSProxy
{
    @Override
    public void preInit()
    {
        Psychedelicraftv2.coreHandlerServer = new PSCoreHandlerServer();
        Psychedelicraftv2.coreHandlerServer.register();
    }

    @Override
    public void registerRenderers()
    {

    }

    @Override
    public void spawnColoredParticle(Entity entity, float[] color, Vec3 direction, float speed, float size)
    {

    }

    @Override
    public void createDrugRenderer(DrugProperties drugProperties)
    {

    }

    @Override
    public void loadConfig(String configID)
    {

    }
}
