/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.psychedelicraftv2.client.rendering;

import net.minecraft.item.ItemStack;

/**
 * Created by lukas on 23.10.14.
 */
public interface RenderPassesCustom
{
    boolean hasAlphaCustom(ItemStack stack, int pass);

    int getRenderPassesCustom(ItemStack stack);
}
