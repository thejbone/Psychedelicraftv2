/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.psychedelicraftv2.entities.drugs.effects;

/**
 * Created by lukas on 01.11.14.
 */
public class DrugRedShrooms extends DrugSimple
{
    public DrugRedShrooms(double decSpeed, double decSpeedPlus)
    {
        super(decSpeed, decSpeedPlus);
    }

    @Override
    public float colorHallucinationStrength()
    {
        return (float) getActiveValue() * 1.3f;
    }

    @Override
    public float movementHallucinationStrength()
    {
        return (float) getActiveValue() * 0.7f;
    }

    @Override
    public float contextualHallucinationStrength()
    {
        return (float) getActiveValue() * 0.2f;
    }

    @Override
    public float viewWobblyness()
    {
        return (float) getActiveValue() * 0.03f;
    }
}
