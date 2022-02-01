/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.psychedelicraftv2.client.rendering.blocks;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import ivorius.ivtoolkit.rendering.IvRenderHelper;
import com.chillaxmc.psychedelicraftv2.Psychedelicraftv2;
import com.chillaxmc.psychedelicraftv2.blocks.BlockWineGrapeLattice;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

/**
 * Created by lukas on 17.02.14.
 */
public class RenderWineGrapeLattice implements ISimpleBlockRenderingHandler
{
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {
        IvRenderHelper.renderCubeInvBlock(renderer, block, metadata);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        int meta = world.getBlockMetadata(x, y, z);
        int rotation = meta & 1;
        int grapesGrownState = meta >> 1;

        BlockWineGrapeLattice latticeBlock = ((BlockWineGrapeLattice) block);

        latticeBlock.currentIcon = latticeBlock.getDefaultBlockIcon();

        float d = 0.1F;
        switch (rotation)
        {
            case 0:
                renderer.setRenderBounds(0.01f, 0.01f, 0.5f - d, 0.99f, 0.99f, 0.5f + d);
                break;
            case 1:
                renderer.setRenderBounds(0.5f - d, 0.01f, 0.01F, 0.5F + d, 0.99f, 0.99f);
                break;
        }
        renderer.renderStandardBlock(block, x, y, z);

        if (grapesGrownState > 0)
        {
            latticeBlock.currentIcon = latticeBlock.leavesIcons[grapesGrownState - 1];

            float f1 = 0.11f + (grapesGrownState - 1) * 0.06f;
            switch (rotation)
            {
                case 0:
                    renderer.setRenderBounds(0.0F, 0.0F, 0.5F - f1, 1.0F, 1.0F, 0.5F + f1);
                    break;
                case 1:
                    renderer.setRenderBounds(0.5F - f1, 0.0F, 0.0F, 0.5F + f1, 1.0F, 1.0F);
                    break;
            }

            renderer.renderStandardBlock(block, x, y, z);
        }

        block.setBlockBoundsBasedOnState(world, x, y, z);
        latticeBlock.currentIcon = latticeBlock.getDefaultBlockIcon();

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return true;
    }

    @Override
    public int getRenderId()
    {
        return Psychedelicraftv2.blockWineGrapeLatticeRenderType;
    }
}
