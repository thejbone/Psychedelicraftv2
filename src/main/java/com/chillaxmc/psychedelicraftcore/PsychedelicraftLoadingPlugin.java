/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.psychedelicraftcore;

import com.google.common.collect.Sets;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.MissingModsException;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;
import java.util.Set;

/**
 * Created by lukas on 21.02.14.
 */
@IFMLLoadingPlugin.Name(PsychedelicraftLoadingPlugin.NAME)
@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.TransformerExclusions({"com.chillaxmc.psychedelicraftv2core.", "ivorius.ivtoolkit.asm."})
@IFMLLoadingPlugin.SortingIndex(2050)
@IFMLLoadingPlugin.DependsOn("ivtoolkit")
public class PsychedelicraftLoadingPlugin implements IFMLLoadingPlugin
{
    public static final String NAME = "Psychedelicraft Core";
    public static final String MODID = "psychedelicraftcore";

    public static boolean debugGlErrorTraceDumps = false;

    @Override
    public String[] getASMTransformerClass()
    {
        requireClass("ivorius.ivtoolkit.asm.IvClassTransformerManager", new DefaultArtifactVersion("ivtoolkit", true), MODID, NAME);
        return new String[]{PsychedelicraftClassTransformer.class.getName()};
    }

    @Override
    public String getModContainerClass()
    {
        return "com.chillaxmc.psychedelicraftv2core.PsychedelicraftCoreContainer";
    }

    @Override
    public String getSetupClass()
    {
        return "com.chillaxmc.psychedelicraftv2core.PsychedelicraftCoreSetup";
    }

    @Override
    public void injectData(Map<String, Object> data)
    {

    }

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }

    public static void requireClass(String classname, ArtifactVersion artifactVersion, String modID, String modName)
    {
        try
        {
            Class.forName(classname);
        }
        catch (ClassNotFoundException e)
        {
            FMLLog.bigWarning("The mod %s (%s) requires mods %s to be available", modID, modName, artifactVersion.getLabel());
            throw new MissingModsException(modID, modName);
        }
    }
}
