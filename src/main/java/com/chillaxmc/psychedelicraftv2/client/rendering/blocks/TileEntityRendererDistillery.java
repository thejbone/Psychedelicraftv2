/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.psychedelicraftv2.client.rendering.blocks;

import ivorius.ivtoolkit.math.IvMathHelper;
import com.chillaxmc.psychedelicraftv2.Psychedelicraftv2;
import com.chillaxmc.psychedelicraftv2.blocks.TileEntityDistillery;
import com.chillaxmc.psychedelicraftv2.client.rendering.FluidBoxRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

/**
 * Created by lukas on 25.10.14.
 */
public class TileEntityRendererDistillery extends TileEntitySpecialRenderer
{
    public static IModelCustom modelDistillery = AdvancedModelLoader.loadModel(new ResourceLocation(Psychedelicraftv2.MODID, Psychedelicraftv2.filePathModels + "distillery.obj"));

    private IModelCustom model;
    private ResourceLocation texture;

    public TileEntityRendererDistillery()
    {
        model = modelDistillery;
        texture = new ResourceLocation(Psychedelicraftv2.MODID, Psychedelicraftv2.filePathTextures + "distillery.png");
    }

    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks)
    {
        TileEntityDistillery distillery = (TileEntityDistillery) tileEntity;

        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5f, y + 0.502f, z + 0.5f);
        GL11.glRotatef(-90.0f * distillery.direction + 180.0f, 0.0f, 1.0f, 0.0f);

        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glPushMatrix();
        GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
        GL11.glTranslatef(-.5f, -.5f, .5f);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        this.bindTexture(texture);
        model.renderAll();
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_CULL_FACE);

        FluidStack fluidStack = distillery.containedFluid();
        if (fluidStack != null)
        {
            float fluidHeight = 2.8f * IvMathHelper.clamp(0.0f, (float) fluidStack.amount / (float) distillery.tankCapacity(), 1.0f);

            FluidBoxRenderer fluidBoxRenderer = new FluidBoxRenderer(1.0f / 16.0f);
            fluidBoxRenderer.prepare(fluidStack);

            fluidBoxRenderer.renderFluid(-1.9f, -8.0f, -3.9f, 3.8f, fluidHeight, 0.9f, ForgeDirection.NORTH, ForgeDirection.UP);
            fluidBoxRenderer.renderFluid(-1.9f, -8.0f, 3.0f, 3.8f, fluidHeight, 0.9f, ForgeDirection.SOUTH, ForgeDirection.UP);
            fluidBoxRenderer.renderFluid(-3.9f, -8.0f, -1.9f, 0.9f, fluidHeight, 3.8f, ForgeDirection.WEST, ForgeDirection.UP);
            fluidBoxRenderer.renderFluid(3.0f, -8.0f, -1.9f, 0.9f, fluidHeight, 3.8f, ForgeDirection.EAST, ForgeDirection.UP);
            fluidBoxRenderer.renderFluid(-3.0f, -8.0f, -3.0f, 6.0f, fluidHeight, 6.0f, ForgeDirection.UP);

            fluidBoxRenderer.cleanUp();
        }

        GL11.glPopMatrix();
    }
}
