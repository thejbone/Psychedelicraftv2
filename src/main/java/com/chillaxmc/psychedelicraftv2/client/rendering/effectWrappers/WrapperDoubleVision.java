/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.psychedelicraftv2.client.rendering.effectWrappers;

import ivorius.ivtoolkit.rendering.IvDepthBuffer;
import com.chillaxmc.psychedelicraftv2.Psychedelicraftv2;
import com.chillaxmc.psychedelicraftv2.client.rendering.shaders.ShaderDoubleVision;
import com.chillaxmc.psychedelicraftv2.entities.drugs.Drug;
import com.chillaxmc.psychedelicraftv2.entities.drugs.DrugProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

/**
 * Created by lukas on 26.04.14.
 */
public class WrapperDoubleVision extends ShaderWrapper<ShaderDoubleVision>
{
    public WrapperDoubleVision(String utils)
    {
        super(new ShaderDoubleVision(Psychedelicraftv2.logger), getRL("shaderBasic.vert"), getRL("shaderDoubleVision.frag"), utils);
    }

    @Override
    public void setShaderValues(float partialTicks, int ticks, IvDepthBuffer depthBuffer)
    {
        DrugProperties drugProperties = DrugProperties.getDrugProperties(Minecraft.getMinecraft().renderViewEntity);

        shaderInstance.doubleVision = 0.0f;

        if (drugProperties != null)
        {
            for (Drug drug : drugProperties.getAllDrugs())
                shaderInstance.doubleVision += (1.0f - shaderInstance.doubleVision) * drug.doubleVision();

            shaderInstance.doubleVisionDistance = MathHelper.sin((ticks + partialTicks) / 20.0f) * 0.05f * shaderInstance.doubleVision;
        }
    }

    @Override
    public void update()
    {

    }

    @Override
    public boolean wantsDepthBuffer(float partialTicks)
    {
        return false;
    }
}
