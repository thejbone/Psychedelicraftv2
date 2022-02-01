/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.psychedelicraftv2.client.rendering.blocks;

import com.chillaxmc.psychedelicraftv2.Psychedelicraftv2;
import com.chillaxmc.psychedelicraftv2.blocks.TileEntityBarrel;
import com.chillaxmc.psychedelicraftv2.fluids.FluidWithIconSymbol;
import com.chillaxmc.psychedelicraftv2.fluids.FluidWithIconSymbolRegistering;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class TileEntityRendererBarrel extends TileEntitySpecialRenderer
{
    private ModelBarrel barrelModel;

    private ResourceLocation barrelTexture;
    private ResourceLocation barrelTextureSpruce;
    private ResourceLocation barrelTextureBirch;
    private ResourceLocation barrelTextureJungle;
    private ResourceLocation barrelTextureAcacia;
    private ResourceLocation barrelTextureDarkOak;

    public TileEntityRendererBarrel()
    {
        super();

        this.barrelModel = new ModelBarrel();
        this.barrelTexture = new ResourceLocation(Psychedelicraftv2.MODID, Psychedelicraftv2.filePathTextures + "barrelTexture.png");
        this.barrelTextureSpruce = new ResourceLocation(Psychedelicraftv2.MODID, Psychedelicraftv2.filePathTextures + "barrelTextureSpruce.png");
        this.barrelTextureBirch = new ResourceLocation(Psychedelicraftv2.MODID, Psychedelicraftv2.filePathTextures + "barrelTextureBirch.png");
        this.barrelTextureJungle = new ResourceLocation(Psychedelicraftv2.MODID, Psychedelicraftv2.filePathTextures + "barrelTextureJungle.png");
        this.barrelTextureAcacia = new ResourceLocation(Psychedelicraftv2.MODID, Psychedelicraftv2.filePathTextures + "barrelTextureAcacia.png");
        this.barrelTextureDarkOak = new ResourceLocation(Psychedelicraftv2.MODID, Psychedelicraftv2.filePathTextures + "barrelTextureDarkOak.png");
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f)
    {
        renderTileEntityStatueAt((TileEntityBarrel) tileentity, d, d1, d2, f);
    }

    public void renderTileEntityStatueAt(TileEntityBarrel tileEntity, double d, double d1, double d2, float f)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) d + 0.5F, (float) d1 + 0.5f, (float) d2 + 0.5F);
        GL11.glRotatef(-90.0f * tileEntity.getBlockRotation() + 180.0f, 0.0f, 1.0f, 0.0f);

        GL11.glPushMatrix();
        GL11.glTranslatef(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);

        Entity emptyEntity = new EntityArrow(tileEntity.getWorldObj());
        emptyEntity.ticksExisted = (int) (tileEntity.getTapRotation() * 100.0f);

        this.bindTexture(getBarrelTexture(tileEntity));
        barrelModel.render(emptyEntity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

        GL11.glPopMatrix();

        FluidStack containedFluid = tileEntity.containedFluid();
        if (containedFluid != null && containedFluid.getFluid() instanceof FluidWithIconSymbol)
        {
            IIcon drinkIcon = ((FluidWithIconSymbol) containedFluid.getFluid()).getIconSymbol(containedFluid, FluidWithIconSymbolRegistering.TEXTURE_TYPE_ITEM);
            if (drinkIcon != null)
            {
                this.bindTexture(TextureMap.locationItemsTexture);
                Tessellator tessellator = Tessellator.instance;

                double barrelZ = -0.45;
                double iconSize = 1.0;
                double centerX = 0.0;
                double centerY = 0.0;

                GL11.glColor3f(1.0f, 1.0f, 1.0f);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, 0.0f, -1.0f);
                tessellator.addVertexWithUV(centerX - iconSize * 0.5, centerY - iconSize * 0.5, barrelZ, drinkIcon.getMaxU(), drinkIcon.getMaxV());
                tessellator.addVertexWithUV(centerX - iconSize * 0.5, centerY + iconSize * 0.5, barrelZ, drinkIcon.getMaxU(), drinkIcon.getMinV());
                tessellator.addVertexWithUV(centerX + iconSize * 0.5, centerY + iconSize * 0.5, barrelZ, drinkIcon.getMinU(), drinkIcon.getMinV());
                tessellator.addVertexWithUV(centerX + iconSize * 0.5, centerY - iconSize * 0.5, barrelZ, drinkIcon.getMinU(), drinkIcon.getMaxV());
                tessellator.draw();
            }
        }

        GL11.glPopMatrix();
    }

    public ResourceLocation getBarrelTexture(TileEntityBarrel barrel)
    {
        switch (barrel.barrelWoodType)
        {
            case 1:
                return barrelTextureSpruce;
            case 2:
                return barrelTextureBirch;
            case 3:
                return barrelTextureJungle;
            case 4:
                return barrelTextureAcacia;
            case 5:
                return barrelTextureDarkOak;
            default:
                return barrelTexture;
        }
    }
}
