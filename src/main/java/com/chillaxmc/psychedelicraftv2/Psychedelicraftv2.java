/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.psychedelicraftv2;

import ivorius.ivtoolkit.network.PacketExtendedEntityPropertiesData;
import ivorius.ivtoolkit.network.PacketExtendedEntityPropertiesDataHandler;
import ivorius.ivtoolkit.network.PacketTileEntityData;
import ivorius.ivtoolkit.network.PacketTileEntityDataHandler;
import com.chillaxmc.psychedelicraftv2.achievements.PSAchievementList;
import com.chillaxmc.psychedelicraftv2.commands.CommandDrug;
import com.chillaxmc.psychedelicraftv2.commands.CommandPsyche;
import com.chillaxmc.psychedelicraftv2.config.PSConfig;
import com.chillaxmc.psychedelicraftv2.crafting.PSCrafting;
import com.chillaxmc.psychedelicraftv2.entities.drugs.DrugInfluence;
import com.chillaxmc.psychedelicraftv2.entities.drugs.DrugInfluenceHarmonium;
import com.chillaxmc.psychedelicraftv2.entities.drugs.DrugRegistry;
import com.chillaxmc.psychedelicraftv2.events.*;
import com.chillaxmc.psychedelicraftv2.gui.CreativeTabPsyche;
import com.chillaxmc.psychedelicraftv2.gui.PSGuiHandler;
import com.chillaxmc.psychedelicraftv2.items.PSItems;
import com.chillaxmc.psychedelicraftv2.worldgen.PSWorldGen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.Logger;

@Mod(modid = Psychedelicraftv2.MODID, version = Psychedelicraftv2.VERSION, name = Psychedelicraftv2.NAME,
        guiFactory = "com.chillaxmc.Psychedelicraftv2.gui.PSConfigGuiFactory")
public class Psychedelicraftv2
{
    public static final String MODID = "psychedelicraftv2";
    public static final String NAME = "Psychedelicraftv2";
    public static final String VERSION = "1.5.2";

    @Mod.Instance(value = "psychedelicraft")
    public static Psychedelicraftv2 instance;

    @SidedProxy(clientSide = "com.chillaxmc.psychedelicraftv2.client.ClientProxy", serverSide = "com.chillaxmc.psychedelicraftv2.server.ServerProxy")
    public static PSProxy proxy;

    public static Logger logger;
    public static Configuration config;

    public static PSGuiHandler guiHandler;
    public static PSEventForgeHandler eventForgeHandler;
    public static PSEventFMLHandler eventFMLHandler;
    public static PSCommunicationHandler communicationHandler;

    public static SimpleNetworkWrapper network;

    public static PSCoreHandlerClient coreHandlerClient;
    public static PSCoreHandlerCommon coreHandlerCommon;
    public static PSCoreHandlerServer coreHandlerServer;

    public static CreativeTabPsyche creativeTab;
    public static CreativeTabPsyche drinksTab;
    public static CreativeTabPsyche weaponsTab;

    public static String filePathTextures = "textures/mod/";
    public static String filePathModels = "models/";
    public static String filePathOther = "other/";
    public static String filePathShaders = "shaders/";
    public static String modBase = "psychedelicraft:";

    public static EntityPlayer.EnumStatus sleepStatusDrugs;
    public static DamageSource alcoholPoisoning;
    public static DamageSource respiratoryFailure;
    public static DamageSource stroke;
    public static DamageSource heartFailure;

    public static int blockWineGrapeLatticeRenderType;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();

        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        PSConfig.loadConfig(null);
        if (config.hasChanged())
            config.save();

        creativeTab = new CreativeTabPsyche("psychedelicraft");
        drinksTab = new CreativeTabPsyche("psycheDrinks");
        weaponsTab = new CreativeTabPsyche("psycheWeapons");

        guiHandler = new PSGuiHandler();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, guiHandler);

        eventForgeHandler = new PSEventForgeHandler();
        eventForgeHandler.register();
        eventFMLHandler = new PSEventFMLHandler();
        eventFMLHandler.register();

        communicationHandler = new PSCommunicationHandler(logger, MODID, this);

        coreHandlerCommon = new PSCoreHandlerCommon();
        coreHandlerCommon.register();

        sleepStatusDrugs = EnumHelper.addStatus("onDrugs");
        alcoholPoisoning = new DamageSource("alcoholPoisoning").setDamageBypassesArmor().setDamageIsAbsolute();
        respiratoryFailure = new DamageSource("respiratoryFailure").setDamageBypassesArmor().setDamageIsAbsolute();
        stroke = new DamageSource("stroke").setDamageBypassesArmor().setDamageIsAbsolute();
        heartFailure = new DamageSource("heartFailure").setDamageBypassesArmor().setDamageIsAbsolute();

        PSRegistryHandler.preInit(event, this);

        proxy.preInit();
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event)
    {
        network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        network.registerMessage(PacketExtendedEntityPropertiesDataHandler.class, PacketExtendedEntityPropertiesData.class, 0, Side.CLIENT);
        network.registerMessage(PacketTileEntityDataHandler.class, PacketTileEntityData.class, 1, Side.CLIENT);

        proxy.registerRenderers();

        creativeTab.tabIcon = PSItems.cannabisLeaf;
        drinksTab.tabIcon = PSItems.itemBarrel;
        weaponsTab.tabIcon = PSItems.molotovCocktail;

        DrugRegistry.registerInfluence(DrugInfluence.class, "default");
        DrugRegistry.registerInfluence(DrugInfluenceHarmonium.class, "harmonium");

        PSRegistryHandler.load(event, this);

        PSCrafting.initialize();
        PSWorldGen.initWorldGen();

        PSConfig.loadConfig(null); // Reload based on new config stuff, like DrugFactories
        if (config.hasChanged())
            config.save();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        PSOutboundCommunicationHandler.init();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent evt)
    {
        evt.registerServerCommand(new CommandDrug());
        evt.registerServerCommand(new CommandPsyche());
    }

    @Mod.EventHandler
    public void onIMCEvent(FMLInterModComms.IMCEvent event)
    {
        // Could be fatal if we don't know the side
//        for (FMLInterModComms.IMCMessage message : event.getMessages())
//        {
//            communicationHandler.onIMCMessage(message, false, false);
//        }
    }
}