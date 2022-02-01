/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.psychedelicraftv2.client.rendering.shaders;

import com.google.common.base.Charsets;
import ivorius.ivtoolkit.rendering.*;
import com.chillaxmc.psychedelicraftv2.Psychedelicraftv2;
import com.chillaxmc.psychedelicraftv2.client.rendering.EntityFakeSun;
import com.chillaxmc.psychedelicraftv2.client.rendering.GLStateProxy;
import com.chillaxmc.psychedelicraftv2.client.rendering.PSAccessHelperClient;
import com.chillaxmc.psychedelicraftv2.client.rendering.PsycheShadowHelper;
import com.chillaxmc.psychedelicraftv2.client.rendering.effectWrappers.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class PSRenderStates
{
    public static ShaderWorld currentShader;
    public static ShaderMain shaderInstance;
    public static ShaderMainDepth shaderInstanceDepth;
    public static ShaderShadows shaderInstanceShadows;

    public static List<EffectWrapper> effectWrappers = new ArrayList<>();

    public static IvOpenGLTexturePingPong realtimePingPong;

    public static IvDepthBuffer depthBuffer;
    public static boolean didDepthPass = false;

    public static boolean disableDepthBuffer = false;
    public static boolean bypassPingPongBuffer = false;
    public static boolean shader3DEnabled = true;
    public static boolean shader2DEnabled = true;
    public static boolean doShadows = false;
    public static boolean doHeatDistortion = false;
    public static boolean doWaterDistortion = false;
    public static boolean doMotionBlur = false;

    public static float sunFlareIntensity;
    public static int shadowPixelsPerChunk = 256;

    public static String currentRenderPass;
    public static float currentRenderPassTicks;

    public static boolean renderFakeSkybox = true;

    public static void preRender(float ticks)
    {
        didDepthPass = false;
    }

    public static void preRender3D(float ticks)
    {

    }

    public static List<String> getRenderPasses(float partialTicks)
    {
        List<String> passes = new ArrayList<>();

        passes.add("Default");

        if (!disableDepthBuffer && depthBuffer.isAllocated() && shaderInstanceDepth.getShaderID() > 0)
        {
            boolean addDepth = false;

            for (EffectWrapper wrapper : effectWrappers)
            {
                if (wrapper.wantsDepthBuffer(partialTicks))
                {
                    addDepth = true;
                    break;
                }
            }

            if (addDepth)
                passes.add("Depth");
        }

        if (shaderInstanceShadows.depthBuffer.isAllocated() && shaderInstanceShadows.getShaderID() > 0 && doShadows)
        {
            passes.add("Shadows");
        }

        return passes;
    }

    public static boolean startRenderPass(String pass, float partialTicks, float ticks)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (currentRenderPass != null)
            endRenderPass();

        currentRenderPass = pass;
        currentRenderPassTicks = ticks;

        switch (pass)
        {
            case "Default":
                shaderInstance.shouldDoShadows = doShadows;
                shaderInstance.shadowDepthTextureIndex = shaderInstanceShadows.depthBuffer.getDepthTextureIndex();

                return useShader(partialTicks, ticks, shaderInstance);
            case "Depth":
                depthBuffer.setParentFB(getMCFBO());
                depthBuffer.setSize(mc.displayWidth, mc.displayHeight);
                depthBuffer.bind();

                return useShader(partialTicks, ticks, shaderInstanceDepth);
            case "Shadows":
                Minecraft.getMinecraft().renderViewEntity = new EntityFakeSun(mc.renderViewEntity);
                return useShader(partialTicks, ticks, shaderInstanceShadows);
        }

        return true;
    }

    public static void endRenderPass()
    {
        switch (currentRenderPass)
        {
            case "Default":

                break;
            case "Depth":
                didDepthPass = shaderInstanceDepth.isShaderActive();
                depthBuffer.unbind();
                break;
            case "Shadows":
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.renderViewEntity instanceof EntityFakeSun)
                    mc.renderViewEntity = ((EntityFakeSun) mc.renderViewEntity).prevViewEntity;
                break;
        }

        if (currentShader != null)
        {
            currentShader.deactivate();
            currentShader = null;
        }

        IvOpenGLHelper.checkGLError(Psychedelicraftv2.logger, "Post render pass '" + currentRenderPass + "'");
        currentRenderPass = null;
    }

    public static boolean setupCameraTransform()
    {
        if ("Shadows".equals(currentRenderPass)/* || (Minecraft.getMinecraft().ingameGUI.getUpdateCounter() % 100 > 2)*/)
        {
            PsycheShadowHelper.setupSunGLTransform();

            return true;
        }

        return false;
    }

    public static void setShader3DEnabled(boolean enabled)
    {
        shader3DEnabled = enabled;
    }

    public static void setShader2DEnabled(boolean enabled)
    {
        shader2DEnabled = enabled;
    }

    public static void allocate()
    {
        IvOpenGLHelper.checkGLError(Psychedelicraftv2.logger, "Pre-Allocation");

        Minecraft mc = Minecraft.getMinecraft();
        deallocate();

        String utils = null;

        try
        {
            IResource utilsResource = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(Psychedelicraftv2.MODID, Psychedelicraftv2.filePathShaders + "shaderUtils.frag"));
            utils = IOUtils.toString(utilsResource.getInputStream(), Charsets.UTF_8);
        }
        catch (Exception ex)
        {
            Psychedelicraftv2.logger.error("Could not load shader utils!", ex);
        }

        shaderInstance = new ShaderMain(Psychedelicraftv2.logger);
        setUpShader(shaderInstance, "shader3D.vert", "shader3D.frag", utils);

        shaderInstanceDepth = new ShaderMainDepth(Psychedelicraftv2.logger);
        setUpShader(shaderInstanceDepth, "shader3D.vert", "shader3DDepth.frag", utils);

        shaderInstanceShadows = new ShaderShadows(Psychedelicraftv2.logger);
        setUpShader(shaderInstanceShadows, "shader3D.vert", "shader3DDepth.frag", utils);

        IvOpenGLHelper.checkGLError(Psychedelicraftv2.logger, "Allocation-Shaders");

        // Add order = Application order!
        effectWrappers.add(new WrapperHeatDistortion(utils));
        effectWrappers.add(new WrapperUnderwaterDistortion(utils));
        effectWrappers.add(new WrapperWaterOverlay(utils));
        effectWrappers.add(new WrapperSimpleEffects(utils));
        effectWrappers.add(new WrapperMotionBlur());
        effectWrappers.add(new WrapperBlur(utils));
        effectWrappers.add(new WrapperDoF(utils));
        effectWrappers.add(new WrapperRadialBlur(utils));
        effectWrappers.add(new WrapperBloom(utils));
        effectWrappers.add(new WrapperColorBloom(utils));
        effectWrappers.add(new WrapperDoubleVision(utils));
        effectWrappers.add(new WrapperBlurNoise(utils));
        effectWrappers.add(new WrapperDigital(utils));

        for (EffectWrapper effectWrapper : effectWrappers)
            effectWrapper.alloc();

        IvOpenGLHelper.checkGLError(Psychedelicraftv2.logger, "Allocation-Effects");

        setUpRealtimeCacheTexture();
        depthBuffer = new IvDepthBuffer(mc.displayWidth, mc.displayHeight, Psychedelicraftv2.logger);
        if (!disableDepthBuffer)
            depthBuffer.allocate();

        IvOpenGLHelper.checkGLError(Psychedelicraftv2.logger, "Allocation");
    }

    public static void setUpShader(IvShaderInstance shader, String vertexFile, String fragmentFile, String utils)
    {
        IvShaderInstanceMC.trySettingUpShader(shader, new ResourceLocation(Psychedelicraftv2.MODID, Psychedelicraftv2.filePathShaders + vertexFile), new ResourceLocation(Psychedelicraftv2.MODID, Psychedelicraftv2.filePathShaders + fragmentFile), utils);
    }

    public static void setUpRealtimeCacheTexture()
    {
        deleteRealtimeCacheTexture();

        realtimePingPong = new IvOpenGLTexturePingPong(Psychedelicraftv2.logger);
        realtimePingPong.setScreenSize(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        realtimePingPong.initialize(!bypassPingPongBuffer);
    }

    public static void update()
    {
        if (Minecraft.getMinecraft().theWorld != null)
        {
            for (EffectWrapper effectWrapper : effectWrappers)
                effectWrapper.update();
        }
    }

    public static boolean useShader(float partialTicks, float ticks, ShaderWorld shader)
    {
        currentShader = null;

        if (shader != null && shader3DEnabled)
        {
            if (shader.isShaderActive())
                return true;

            if (shader.activate(partialTicks, ticks))
            {
                currentShader = shader;
                return true;
            }
        }

        return false;
    }

    public static void preRenderSky(float partialTicks)
    {
        if (renderFakeSkybox)
        {
            setForceColorSafeMode(true);
            float boxSize = Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16 * 0.75f;
            float[] fogColor = PSAccessHelperClient.getFogColor();

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glColor3f(fogColor[0], fogColor[1], fogColor[2]);
            Tessellator.instance.startDrawingQuads();
            IvRenderHelper.renderCuboid(Tessellator.instance, -boxSize, -boxSize, -boxSize, 1.0f);
            Tessellator.instance.draw();
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            setForceColorSafeMode(false);
        }
    }

    public static void setEnabled(int cap, boolean enabled)
    {
        if (cap == GL11.GL_TEXTURE_2D)
            setTexture2DEnabled(GLStateProxy.getActiveTextureUnit(), enabled);
        else
        {
            GLStateProxy.setEnabled(cap, enabled);

            if (currentShader != null)
            {
                switch (cap)
                {
                    case GL11.GL_FOG:
                        currentShader.setFogEnabled(enabled);
                        break;
                    case GL11.GL_BLEND:
                        currentShader.setBlendModeEnabled(enabled);
                        break;
                }
            }
        }
    }

    public static void setTexture2DEnabled(int textureUnit, boolean enabled)
    {
        GLStateProxy.setTextureEnabled(textureUnit, enabled);

        if (textureUnit == OpenGlHelper.defaultTexUnit && currentShader != null)
            currentShader.setTexture2DEnabled(enabled);

        if (textureUnit == OpenGlHelper.lightmapTexUnit && currentShader != null)
            currentShader.setLightmapEnabled(enabled);
    }

    public static void setBlendFunc(int sFactor, int dFactor, int sFactorAlpha, int dFactorAlpha)
    {
        GLStateProxy.glBlendFunc(sFactor, dFactor, sFactorAlpha, dFactorAlpha);
        if (currentShader != null)
            currentShader.setBlendFunc(sFactor, dFactor, sFactorAlpha, dFactorAlpha);
    }

    public static void setOverrideColor(float... color)
    {
        if (color != null && color.length != 4)
            throw new IllegalArgumentException("Color must be a length-4 float array");

        if (currentShader != null)
            currentShader.setOverrideColor(color);
    }

    public static void setGLLightEnabled(boolean enabled)
    {
        if (currentShader != null)
            currentShader.setGLLightEnabled(enabled);
    }

    public static void setGLLight(int number, float x, float y, float z, float strength, float specular)
    {
        if (currentShader != null)
            currentShader.setGLLight(number, x, y, z, strength, specular);
    }

    public static void setGLLightAmbient(float strength)
    {
        if (currentShader != null)
            currentShader.setGLLightAmbient(strength);
    }

    public static void setFogMode(int mode)
    {
        if (currentShader != null)
            currentShader.setFogMode(mode);
    }

    public static void setDepthMultiplier(float depthMultiplier)
    {
        if (currentShader != null)
            currentShader.setDepthMultiplier(depthMultiplier);
    }

    public static void setUseScreenTexCoords(boolean enabled)
    {
        if (currentShader != null)
            currentShader.setUseScreenTexCoords(enabled);
    }

    public static void setScreenSizeDefault()
    {
        Minecraft mc = Minecraft.getMinecraft();
        setScreenSize(mc.displayWidth, mc.displayHeight);
    }

    public static void setScreenSize(float screenWidth, float screenHeight)
    {
        setPixelSize(1.0f / screenWidth, 1.0f / screenHeight);
    }

    public static void setPixelSize(float pixelWidth, float pixelHeight)
    {
        if (currentShader != null)
            currentShader.setPixelSize(pixelWidth, pixelHeight);
    }

    public static void setForceColorSafeMode(boolean enable)
    {
        if (currentShader != null)
            currentShader.setForceColorSafeMode(enable);
    }

    public static void setProjectShadows(boolean projectShadows)
    {
        if (currentShader != null)
            currentShader.setProjectShadows(projectShadows);
    }

    public static int getCurrentAllowedGLDataMask()
    {
        if ("Depth".equals(currentRenderPass))
            return GL11.GL_DEPTH_BUFFER_BIT;
        else if ("Shadows".equals(currentRenderPass))
            return GL11.GL_DEPTH_BUFFER_BIT;

        return ~0;
    }

    public static int getMCFBO()
    {
        Minecraft mc = Minecraft.getMinecraft();
        Framebuffer framebuffer = mc.getFramebuffer();

        return (OpenGlHelper.isFramebufferEnabled() && framebuffer != null && framebuffer.framebufferObject >= 0) ? framebuffer.framebufferObject : 0;
    }

    public static void postRender(float ticks, float partialTicks)
    {
        apply2DShaders(ticks, partialTicks);
    }

    public static void apply2DShaders(float ticks, float partialTicks)
    {
        Minecraft mc = Minecraft.getMinecraft();

        int screenWidth = mc.displayWidth;
        int screenHeight = mc.displayHeight;

        IvOpenGLHelper.setUpOpenGLStandard2D(screenWidth, screenHeight);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);

        realtimePingPong.setParentFrameBuffer(getMCFBO());
        realtimePingPong.preTick(screenWidth, screenHeight);

        for (EffectWrapper effectWrapper : effectWrappers)
            effectWrapper.apply(partialTicks, realtimePingPong, didDepthPass ? depthBuffer : null);

        realtimePingPong.postTick();

        IvOpenGLHelper.checkGLError(Psychedelicraftv2.logger, "2D Shaders");
    }

    public static int getTextureIndex(ResourceLocation loc)
    {
        TextureManager tm = Minecraft.getMinecraft().renderEngine;
        tm.bindTexture(loc); // Allocate texture. MOJANG!
        ITextureObject texture = tm.getTexture(loc);
        return texture.getGlTextureId();
    }

    public static void delete3DShaders()
    {
        if (shaderInstance != null)
            shaderInstance.deleteShader();
        shaderInstance = null;

        if (shaderInstanceDepth != null)
            shaderInstanceDepth.deleteShader();
        shaderInstanceDepth = null;

        if (shaderInstanceShadows != null)
            shaderInstanceShadows.deleteShader();
        shaderInstanceShadows = null;
    }

    public static void deleteRealtimeCacheTexture()
    {
        if (realtimePingPong != null)
            realtimePingPong.destroy();
        realtimePingPong = null;
    }

    public static void deallocate()
    {
        delete3DShaders();
        deleteRealtimeCacheTexture();

        for (EffectWrapper effectWrapper : effectWrappers)
            effectWrapper.dealloc();
        effectWrappers.clear();

        if (depthBuffer != null)
            depthBuffer.deallocate();
        depthBuffer = null;
    }

    public static void outputShaderInfo()
    {
        Psychedelicraftv2.logger.info("Graphics card info: ");
        IvShaderInstance.outputShaderInfo(Psychedelicraftv2.logger);
    }
}
