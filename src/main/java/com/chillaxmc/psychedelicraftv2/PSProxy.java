/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.psychedelicraftv2;

import com.chillaxmc.psychedelicraftv2.entities.drugs.DrugProperties;
import net.minecraft.entity.Entity;

/**
 * Created by lukas on 24.05.14.
 */
public interface PSProxy
{
    void preInit();

    void registerRenderers();

    void spawnColoredParticle(Entity entity, float[] color, Vec3 direction, float speed, float size);

    void createDrugRenderer(DrugProperties drugProperties);

    void loadConfig(String configID);
}
