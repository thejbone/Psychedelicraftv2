/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.psychedelicraftv2.client.rendering.shaders;

import ivorius.ivtoolkit.math.IvMathHelper;
import ivorius.ivtoolkit.rendering.IvDepthBuffer;
import ivorius.ivtoolkit.rendering.IvOpenGLTexturePingPong;
import net.minecraft.client.renderer.OpenGlHelper;
import org.apache.logging.log4j.Logger;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by lukas on 18.02.14.
 */
public class ShaderDigitalDepth extends ShaderDigital
{
    public int depthTextureIndex;

    public float zNear;
    public float zFar;

    public ShaderDigitalDepth(Logger logger)
    {
        super(logger);
    }

    @Override
    public boolean shouldApply(float ticks)
    {
        return digital > 0.0f && depthTextureIndex > 0 && digitalTextTexture > 0 && super.shouldApply(ticks);
    }

    @Override
    public void apply(int screenWidth, int screenHeight, float ticks, IvOpenGLTexturePingPong pingPong)
    {
        useShader();

        IvDepthBuffer.bindTextureForSource(OpenGlHelper.lightmapTexUnit + 2, depthTextureIndex);
        setUniformInts("depthTex", 3);

        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit + 1);
        glBindTexture(GL_TEXTURE_2D, digitalTextTexture);
        setUniformInts("asciiTex", 2);

        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        setUniformInts("tex", 0);

        setUniformFloats("totalAlpha", 1.0f);

        float downscale = (float) IvMathHelper.mixEaseInOut(0.0, 0.95, Math.min(digital * 3.0, 1.0f));
        downscale += digital * 0.05f; //Bigger pixels!
        float textProgress = (float) IvMathHelper.easeZeroToOne((digital - 0.2f) * 5.0f);
        float maxColors = digital > 0.4f ? (Math.max(256.0f / ((digital - 0.4f) * 640.0f + 1.0f), 2.0f)) : -1; //Step 3, 0.2 is enough for only 2 colors
        float saturation = 1.0f - (float) IvMathHelper.easeZeroToOne((digital - 0.6f) * 5.0f);
        float binaryProgress = (float) IvMathHelper.easeZeroToOne((digital - 0.8f) * 10.0f);
        textProgress += binaryProgress;
        float[] newResolution = new float[]{screenWidth * (1.0f + (maxDownscale[0] - 1.0f) * downscale), screenHeight * (1.0f + (maxDownscale[1] - 1.0f) * downscale)};

        setUniformFloats("newResolution", newResolution);
        setUniformFloats("textProgress", textProgress);
        setUniformFloats("maxColors", maxColors);
        setUniformFloats("saturation", saturation);

        setUniformFloats("depthRange", zNear, zFar);

        glColor3f(1.0f, 1.0f, 1.0f);
        drawFullScreen(screenWidth, screenHeight, pingPong);

        stopUsingShader();
    }
}
