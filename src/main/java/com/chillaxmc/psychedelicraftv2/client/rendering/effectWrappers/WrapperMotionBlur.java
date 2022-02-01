/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.psychedelicraftv2.client.rendering.effectWrappers;

import com.chillaxmc.psychedelicraftv2.client.rendering.EffectMotionBlur;
import com.chillaxmc.psychedelicraftv2.client.rendering.shaders.PSRenderStates;
import com.chillaxmc.psychedelicraftv2.entities.drugs.DrugProperties;
import net.minecraft.client.Minecraft;

/**
 * Created by lukas on 26.04.14.
 */
public class WrapperMotionBlur extends ScreenEffectWrapper<EffectMotionBlur>
{
    public WrapperMotionBlur()
    {
        super(new EffectMotionBlur());
    }

    @Override
    public void setScreenEffectValues(float partialTicks, int ticks)
    {
        DrugProperties drugProperties = DrugProperties.getDrugProperties(Minecraft.getMinecraft().renderViewEntity);

        if (PSRenderStates.doMotionBlur && drugProperties != null)
            screenEffect.motionBlur = drugProperties.hallucinationManager.getMotionBlur(drugProperties, partialTicks);
        else
            screenEffect.motionBlur = 0.0f;
    }

    @Override
    public void update()
    {

    }
}
