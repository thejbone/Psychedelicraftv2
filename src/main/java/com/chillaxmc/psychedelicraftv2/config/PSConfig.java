/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.psychedelicraftv2.config;

import com.chillaxmc.psychedelicraftv2.Psychedelicraftv2;
import com.chillaxmc.psychedelicraftv2.entities.PSEntityList;
import com.chillaxmc.psychedelicraftv2.fluids.FluidAlcohol;
import net.minecraftforge.common.config.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.chillaxmc.psychedelicraftv2.Psychedelicraftv2.config;

/**
 * Created by lukas on 31.07.14.
 */
public class PSConfig
{
    public static final String CATEGORY_BALANCING = "balancing";
    public static final String CATEGORY_VISUAL = "visual";
    public static final String CATEGORY_AUDIO = "audio";

    private static final int MINUTE = 20 * 60;

    public static int randomTicksUntilRiftSpawn;
    public static boolean enableHarmonium;
    public static boolean enableRiftJars;

    public static boolean genJuniper;
    public static boolean genCannabis;
    public static boolean genHop;
    public static boolean genTobacco;
    public static boolean genCoffea;
    public static boolean genCoca;
    public static boolean genPeyote;

    public static boolean dungeonChests;
    public static boolean villageChests;

    public static boolean farmerDrugDeals;

    public static int dryingTableTickDuration;
    public static int ironDryingTableTickDuration;

    public static final Map<String, Boolean> drugBGM = new HashMap<>();

    public static final FluidAlcohol.TickInfo alcInfoWheatHop = new FluidAlcohol.TickInfo();
    public static final FluidAlcohol.TickInfo alcInfoWheat = new FluidAlcohol.TickInfo();
    public static final FluidAlcohol.TickInfo alcInfoCorn = new FluidAlcohol.TickInfo();
    public static final FluidAlcohol.TickInfo alcInfoPotato = new FluidAlcohol.TickInfo();
    public static final FluidAlcohol.TickInfo alcInfoRedGrapes = new FluidAlcohol.TickInfo();
    public static final FluidAlcohol.TickInfo alcInfoRice = new FluidAlcohol.TickInfo();
    public static final FluidAlcohol.TickInfo alcInfoJuniper = new FluidAlcohol.TickInfo();
    public static final FluidAlcohol.TickInfo alcInfoSugarCane = new FluidAlcohol.TickInfo();
    public static final FluidAlcohol.TickInfo alcInfoHoney = new FluidAlcohol.TickInfo();
    public static final FluidAlcohol.TickInfo alcInfoApple = new FluidAlcohol.TickInfo();
    public static final FluidAlcohol.TickInfo alcInfoPineapple = new FluidAlcohol.TickInfo();
    public static final FluidAlcohol.TickInfo alcInfoBanana = new FluidAlcohol.TickInfo();
    public static final FluidAlcohol.TickInfo alcInfoMilk = new FluidAlcohol.TickInfo();

    public static int slurryHardeningTime;

    public static boolean distortIncomingMessages;
    public static boolean distortOutgoingMessages;

    public static void loadConfig(String configID)
    {
        if (configID == null || configID.equals(Configuration.CATEGORY_GENERAL))
        {
            PSEntityList.villagerDealerProfessionID = config.get("General", "villagerDealerProfessionID", 87, "Internal ID for the drug dealer villager. Enter a negative number to disable.").getInt();
        }

        if (configID == null || configID.equals(CATEGORY_BALANCING))
        {
            randomTicksUntilRiftSpawn = config.get(CATEGORY_BALANCING, "randomTicksUntilRiftSpawn", MINUTE * 180, "Approximate number of ticks until a rift spawns. Enter a negative number to disable.").getInt();

            enableHarmonium = config.get(CATEGORY_BALANCING, "enableHarmonium", false).getBoolean();
            enableRiftJars = config.get(CATEGORY_BALANCING, "enableRiftJars", false).getBoolean();

            genJuniper = config.get(CATEGORY_BALANCING, "generateJuniper", true).getBoolean();
            genCannabis = config.get(CATEGORY_BALANCING, "generateCannabis", true).getBoolean();
            genHop = config.get(CATEGORY_BALANCING, "genHop", true).getBoolean();
            genTobacco = config.get(CATEGORY_BALANCING, "generateTobacco", true).getBoolean();
            genCoffea = config.get(CATEGORY_BALANCING, "generateCoffea", true).getBoolean();
            genCoca = config.get(CATEGORY_BALANCING, "generateCoca", true).getBoolean();
            genPeyote = config.get(CATEGORY_BALANCING, "generatePeyote", true).getBoolean();

            dungeonChests = config.get(CATEGORY_BALANCING, "dungeonChests", true).getBoolean();
            villageChests = config.get(CATEGORY_BALANCING, "villageChests", true).getBoolean();

            farmerDrugDeals = config.get(CATEGORY_BALANCING, "farmerDrugDeals", true).getBoolean();

            dryingTableTickDuration = config.get(CATEGORY_BALANCING, "dryingTableTickDuration", MINUTE * 16, "Time until plants in the drying table finish the drying process.").getInt();
            ironDryingTableTickDuration = config.get(CATEGORY_BALANCING, "ironDryingTableTickDuration", MINUTE * 12, "Time until plants in the iron drying table finish the drying process.").getInt();

            readTickInfo(alcInfoWheatHop, "wheatHop", MINUTE * 30, MINUTE * 60, MINUTE * 100, MINUTE * 30, config);
            readTickInfo(alcInfoWheat, "wheat", MINUTE * 40, MINUTE * 40, MINUTE * 30, MINUTE * 30, config);
            readTickInfo(alcInfoCorn, "corn", MINUTE * 40, MINUTE * 40, MINUTE * 30, MINUTE * 30, config);
            readTickInfo(alcInfoPotato, "potato", MINUTE * 40, MINUTE * 40, MINUTE * 30, MINUTE * 30, config);
            readTickInfo(alcInfoRedGrapes, "redGrapes", MINUTE * 40, MINUTE * 40, MINUTE * 30, MINUTE * 30, config);
            readTickInfo(alcInfoRice, "rice", MINUTE * 40, MINUTE * 40, MINUTE * 30, MINUTE * 30, config);
            readTickInfo(alcInfoJuniper, "juniper", MINUTE * 40, MINUTE * 40, MINUTE * 30, MINUTE * 30, config);
            readTickInfo(alcInfoSugarCane, "sugarCane", MINUTE * 40, MINUTE * 40, MINUTE * 30, MINUTE * 30, config);
            readTickInfo(alcInfoHoney, "honey", MINUTE * 40, MINUTE * 40, MINUTE * 30, MINUTE * 30, config);
            readTickInfo(alcInfoApple, "apple", MINUTE * 40, MINUTE * 40, MINUTE * 30, MINUTE * 30, config);
            readTickInfo(alcInfoPineapple, "pineapple", MINUTE * 40, MINUTE * 40, MINUTE * 30, MINUTE * 30, config);
            readTickInfo(alcInfoBanana, "banana", MINUTE * 40, MINUTE * 40, MINUTE * 30, MINUTE * 30, config);
            readTickInfo(alcInfoMilk, "milk", MINUTE * 40, MINUTE * 40, MINUTE * 30, MINUTE * 30, config);

            slurryHardeningTime = config.get(CATEGORY_BALANCING, "slurryHardeningTime", MINUTE * 30, "The amount of ticks slurry needs to sit in a vat to harden to dirt.").getInt();

            distortOutgoingMessages = config.getBoolean("distortOutgoingMessages", CATEGORY_BALANCING, true, "Whether the mod should distort chat messages when drugs have been consumed ('slurred speech').");
        }

        Psychedelicraftv2.proxy.loadConfig(configID);
    }

    public static void readTickInfo(FluidAlcohol.TickInfo tickInfo, String fluidName, int defaultFermentation, int defaultDistillation, int defaultMaturation, int defaultAcetification, Configuration config)
    {
        tickInfo.ticksPerFermentation = config.get(CATEGORY_BALANCING, fluidName + "_ticksPerFermentation", defaultFermentation, String.format("Time until %s wort ferments to the next step.", fluidName)).getInt();
        tickInfo.ticksPerDistillation = config.get(CATEGORY_BALANCING, fluidName + "_ticksPerDistillation", defaultDistillation, String.format("Time until %s distills to the next step.", fluidName)).getInt();
        tickInfo.ticksPerMaturation = config.get(CATEGORY_BALANCING, fluidName + "_ticksPerMaturation", defaultMaturation, String.format("Time until %s matures to the next step.", fluidName)).getInt();
        tickInfo.ticksUntilAcetification = config.get(CATEGORY_BALANCING, fluidName + "_ticksUntilAcetification", defaultAcetification, String.format("Time until %s acetifies to form vinegar. Enter -1 to disable.", fluidName)).getInt();
    }

    public static void readHasBGM(String drugName, Configuration config)
    {
        drugBGM.put(drugName, config.get(CATEGORY_AUDIO, "bgm_" + drugName, false, "Indicates if the drug is supposed to have background music when active (refer to the wiki for instructions).").getBoolean());
    }

    public static boolean hasBGM(String drugName)
    {
        Boolean bool = drugBGM.get(drugName);
        return bool != null && bool;
    }
}
