/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.psychedelicraftv2.worldgen;

import com.chillaxmc.psychedelicraftv2.blocks.PSBlocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenPeyote extends WorldGenerator
{
    public WorldGenPeyote(boolean notify)
    {
        super(notify);
    }

    @Override
    public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
    {
        if (par1World.isAirBlock(par3, par4, par5))
        {
            int meta = par2Random.nextInt(4);

            if (PSBlocks.peyote.canBlockStay(par1World, par3, par4, par5))
            {
                setBlockAndNotifyAdequately(par1World, par3, par4, par5, PSBlocks.peyote, meta);
            }
        }

        return true;
    }
}
