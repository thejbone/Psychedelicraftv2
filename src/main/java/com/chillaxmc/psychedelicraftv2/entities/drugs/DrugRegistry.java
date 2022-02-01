/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.psychedelicraftv2.entities.drugs;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.entity.EntityLivingBase;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by lukas on 22.10.14.
 */
public class DrugRegistry
{
    private static List<DrugFactory> drugFactories = new ArrayList<>();
    private static BiMap<String, Class<? extends DrugInfluence>> drugMap = HashBiMap.create();

    public static void registerFactory(DrugFactory factory)
    {
        drugFactories.add(factory);
    }

    public static Collection<DrugFactory> allFactories()
    {
        return Collections.unmodifiableCollection(drugFactories);
    }

    public static List<Pair<String, Drug>> createDrugs(EntityLivingBase entity)
    {
        List<Pair<String, Drug>> list = new ArrayList<>();
        for (DrugFactory drugFactory : drugFactories)
            drugFactory.createDrugs(entity, list);
        return list;
    }

    public static void registerInfluence(Class<? extends DrugInfluence> clazz, String key)
    {
        drugMap.put(key, clazz);
    }

    public static Class<? extends DrugInfluence> getClass(String drugID)
    {
        return drugMap.get(drugID);
    }

    public static String getID(Class<? extends DrugInfluence> clazz)
    {
        return drugMap.inverse().get(clazz);
    }
}
