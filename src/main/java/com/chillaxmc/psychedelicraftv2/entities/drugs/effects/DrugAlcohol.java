/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.psychedelicraftv2.entities.drugs.effects;

import ivorius.ivtoolkit.math.IvMathHelper;
import com.chillaxmc.psychedelicraftv2.Psychedelicraftv2;
import com.chillaxmc.psychedelicraftv2.client.rendering.DrugRenderer;
import com.chillaxmc.psychedelicraftv2.entities.drugs.DrugProperties;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

import java.util.Random;

/**
 * Created by lukas on 01.11.14.
 */
public class DrugAlcohol extends DrugSimple
{
    public static void rotateEntityPitch(Entity entity, double amount)
    {
        entity.rotationPitch += amount;
        if (entity.rotationPitch < -90.0F)
            entity.rotationPitch = -90.0F;

        if (entity.rotationPitch > 90.0F)
            entity.rotationPitch = 90.0F;
    }

    public static void rotateEntityYaw(Entity entity, double amount)
    {
        entity.rotationYaw += amount;
    }

    public DrugAlcohol(double decSpeed, double decSpeedPlus)
    {
        super(decSpeed, decSpeedPlus);
    }

    @Override
    public void update(EntityLivingBase entity, DrugProperties drugProperties)
    {
        super.update(entity, drugProperties);

        if (getActiveValue() > 0.0)
        {
            int ticksExisted = drugProperties.ticksExisted;
            Random random = entity.getRNG();

            double activeValue = getActiveValue();

            if ((ticksExisted % 20) == 0)
            {
                double damageChance = (activeValue - 0.9f) * 2.0f;

                if (ticksExisted % 20 == 0 && random.nextFloat() < damageChance)
                {
                    DamageSource damageSource = Psychedelicraftv2.alcoholPoisoning;
                    entity.attackEntityFrom(damageSource, (int) ((activeValue - 0.9f) * 50.0f + 4.0f));
                }
            }

            double motionEffect = Math.min(activeValue, 0.8);

//            player.motionX += MathHelper.sin(ticksExisted / 10.0F * (float) Math.PI) / 40.0F * motionEffect * (random.nextFloat() + 0.5F);
//            player.motionZ += MathHelper.cos(ticksExisted / 10.0F * (float) Math.PI) / 40.0F * motionEffect * (random.nextFloat() + 0.5F);
//
//            player.motionX *= (random.nextFloat() - 0.5F) * 2 * motionEffect + 1.0F;
//            player.motionZ *= (random.nextFloat() - 0.5F) * 2 * motionEffect + 1.0F;

            rotateEntityPitch(entity, MathHelper.sin(ticksExisted / 600.0F * (float) Math.PI) / 2.0F * motionEffect * (random.nextFloat() + 0.5F));
            rotateEntityYaw(entity, MathHelper.cos(ticksExisted / 500.0F * (float) Math.PI) / 1.3F * motionEffect * (random.nextFloat() + 0.5F));

            rotateEntityPitch(entity, MathHelper.sin(ticksExisted / 180.0F * (float) Math.PI) / 3.0F * motionEffect * (random.nextFloat() + 0.5F));
            rotateEntityYaw(entity, MathHelper.cos(ticksExisted / 150.0F * (float) Math.PI) / 2.0F * motionEffect * (random.nextFloat() + 0.5F));
        }
    }

    @Override
    public float viewWobblyness()
    {
        return (float)getActiveValue() * 0.5f;
    }

    @Override
    public void drawOverlays(float partialTicks, EntityLivingBase entity, int updateCounter, int width, int height, DrugProperties drugProperties)
    {
        float alcohol = (float)getActiveValue();
        if (alcohol > 0)
        {
            float overlayAlpha = (MathHelper.sin(updateCounter / 80F) * alcohol * 0.5F + alcohol);
            if (overlayAlpha > 0.8F)
                overlayAlpha = 0.8F;

            IIcon portalIcon = Blocks.portal.getIcon(0, 0);
            DrugRenderer.renderOverlay(overlayAlpha * 0.25f, width, height, TextureMap.locationBlocksTexture, portalIcon.getMinU(), portalIcon.getMinV(), portalIcon.getMaxU(), portalIcon.getMaxV(), 0);
        }
    }

    @Override
    public float doubleVision()
    {
        return IvMathHelper.zeroToOne((float)getActiveValue(), 0.25f, 1.0f);
    }

    @Override
    public float motionBlur()
    {
        return IvMathHelper.zeroToOne((float)getActiveValue(), 0.5f, 1.0f) * 0.3f;
    }
}
