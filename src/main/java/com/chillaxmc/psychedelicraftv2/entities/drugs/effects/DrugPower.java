/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.psychedelicraftv2.entities.drugs.effects;

import com.chillaxmc.psychedelicraftv2.Psychedelicraftv2;
import com.chillaxmc.psychedelicraftv2.client.rendering.DrugRenderer;
import com.chillaxmc.psychedelicraftv2.entities.drugs.DrugProperties;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Random;

/**
 * Created by lukas on 01.11.14.
 */
public class DrugPower extends DrugSimple
{
    public ResourceLocation powerParticle;
    public ResourceLocation[] lightningTextures;

    public DrugPower(double decSpeed, double decSpeedPlus)
    {
        super(decSpeed, decSpeedPlus, true);

        powerParticle = new ResourceLocation(Psychedelicraftv2.MODID, Psychedelicraftv2.filePathTextures + "powerParticle.png");

        lightningTextures = new ResourceLocation[4];
        for (int i = 0; i < lightningTextures.length; i++)
        {
            lightningTextures[i] = new ResourceLocation(Psychedelicraftv2.MODID, Psychedelicraftv2.filePathTextures + "lightning" + i + ".png");
        }
    }

    @Override
    public float soundVolumeModifier()
    {
        return 1.0f - (float) getActiveValue();
    }

    @Override
    public float desaturationHallucinationStrength()
    {
        return (float)getActiveValue() * 0.75f;
    }

    @Override
    public float motionBlur()
    {
        return (float) getActiveValue() * 0.3f;
    }

    @Override
    public void drawOverlays(float partialTicks, EntityLivingBase entity, int updateCounter, int width, int height, DrugProperties drugProperties)
    {
        Tessellator tessellator = Tessellator.instance;;

        float power = (float)getActiveValue();
        Random powerR = new Random(entity.ticksExisted); // 20 changes / sec is alright
        int powerParticles = MathHelper.floor_float(powerR.nextFloat() * 200.0f * power);
        if (powerParticles > 0)
        {
            DrugRenderer.bindTexture(powerParticle);
            renderRandomParticles(powerParticles, height / 10, MathHelper.ceiling_float_int(height / 10 * power), width, height, powerR);
        }
        Random powerLR = new Random((long) (entity.ticksExisted / 2) * 21124871824l); // Chaos principle doesn't apply ;_;
        float lightningChance = (power - 0.5f) * 0.1f;
        int powerLightnings = 0;
        while (powerLR.nextFloat() < lightningChance && powerLightnings < 3)
        {
            powerLightnings++;
        }
        if (powerLightnings > 0)
        {
            int lightningW = height;

            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE, GL11.GL_ZERO);
            for (int i = 0; i < powerLightnings; i++)
            {
                float lX = powerLR.nextInt(width + lightningW) - lightningW;
                lX += (powerLR.nextFloat() - 0.5f) * lightningW * partialTicks * 2.0f;
                int lIndex = powerLR.nextInt(lightningTextures.length);
                boolean upsideDown = powerLR.nextBoolean();
                float lightningTime = ((entity.ticksExisted % 2) + partialTicks) * 0.5f;

                GL11.glColor4f(1.0f, 1.0f, 1.0f, (0.05f + power * 0.1f) * (1.0f - lightningTime));
                DrugRenderer.bindTexture(lightningTextures[lIndex]);
                tessellator.startDrawingQuads();
                tessellator.addVertexWithUV(lX, height, -90.0, 0, upsideDown ? 0 : 1);
                tessellator.addVertexWithUV(lX + lightningW, height, -90.0, 1, upsideDown ? 0 : 1);
                tessellator.addVertexWithUV(lX + lightningW, 0, -90.0, 1, upsideDown ? 1 : 0);
                tessellator.addVertexWithUV(lX, 0, -90.0, 0, upsideDown ? 1 : 0);
                tessellator.draw();
            }
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        }
    }

    public void renderRandomParticles(int number, int width, int height, int screenWidth, int screenHeight, Random rand)
    {
        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawingQuads();
        for (int i = 0; i < number; i++)
        {
            int x = rand.nextInt(screenWidth + width) - width;
            int y = rand.nextInt(screenHeight + height) - height;

            tessellator.addVertexWithUV(x, y + height, -90.0, 0, 1);
            tessellator.addVertexWithUV(x + width, y + height, -90.0, 1, 1);
            tessellator.addVertexWithUV(x + width, y, -90.0, 1, 0);
            tessellator.addVertexWithUV(x, y, -90.0, 0, 0);
        }
        tessellator.draw();
    }
}
