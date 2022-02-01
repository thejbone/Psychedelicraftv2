/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.psychedelicraftv2.mods;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

/**
 * Created by lukas on 02.11.14.
 */
public class ModRepresentation
{
    public static String id(Block block)
    {
        return String.valueOf((Block.getIdFromBlock(block)));
    }

    public static String id(Item item)
    {
        return String.valueOf(Item.getIdFromItem(item));
    }
}
