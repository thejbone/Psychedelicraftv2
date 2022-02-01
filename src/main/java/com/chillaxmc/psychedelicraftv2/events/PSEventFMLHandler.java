/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.psychedelicraftv2.events;

import com.chillaxmc.psychedelicraftv2.Psychedelicraftv2;
import com.chillaxmc.psychedelicraftv2.achievements.PSAchievementList;
import com.chillaxmc.psychedelicraftv2.blocks.PSBlocks;
import com.chillaxmc.psychedelicraftv2.client.rendering.DrugEffectInterpreter;
import com.chillaxmc.psychedelicraftv2.client.rendering.SmoothCameraHelper;
import com.chillaxmc.psychedelicraftv2.client.rendering.shaders.PSRenderStates;
import com.chillaxmc.psychedelicraftv2.config.PSConfig;
import com.chillaxmc.psychedelicraftv2.crafting.RecipeActionRegistry;
import com.chillaxmc.psychedelicraftv2.entities.EntityRealityRift;
import com.chillaxmc.psychedelicraftv2.entities.drugs.DrugProperties;
import com.chillaxmc.psychedelicraftv2.fluids.PSFluids;
import com.chillaxmc.psychedelicraftv2.gui.UpdatableContainer;
import com.chillaxmc.psychedelicraftv2.items.PSItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by lukas on 18.02.14.
 */
public class PSEventFMLHandler
{
    public static void spawnRiftAtPlayer(EntityPlayer player)
    {
        EntityRealityRift rift = new EntityRealityRift(player.getEntityWorld());

        double xP = (player.getRNG().nextDouble() - 0.5) * 100.0;
        double yP = (player.getRNG().nextDouble() - 0.5) * 100.0;
        double zP = (player.getRNG().nextDouble() - 0.5) * 100.0;

        rift.setPosition(player.posX + xP, player.posY + yP, player.posZ + zP);
        player.getEntityWorld().spawnEntityInWorld(rift);
    }

    public static boolean isFluidStack(ItemStack stack, ItemStack container, Fluid fluid)
    {
        return stack.getItem()== container.getItem()
                && (container.getItemDamage() == OreDictionary.WILDCARD_VALUE || container.getItemDamage() == stack.getItemDamage())
                && PSFluids.containsFluid(stack, fluid);
    }

    public void register()
    {
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent event)
    {
        if ((event.type == TickEvent.Type.CLIENT || event.type == TickEvent.Type.SERVER) && event.phase == TickEvent.Phase.END)
        {
            for (FMLInterModComms.IMCMessage message : FMLInterModComms.fetchRuntimeMessages(Psychedelicraftv2.instance))
            {
                Psychedelicraftv2.communicationHandler.onIMCMessage(message, event.type == TickEvent.Type.SERVER, true);
            }
        }

        if (event.type == TickEvent.Type.CLIENT && event.phase == TickEvent.Phase.START)
        {
            PSRenderStates.update();
        }

        if (event.type == TickEvent.Type.RENDER && event.phase == TickEvent.Phase.START)
        {
            PSBlocks.psycheLeaves.setGraphicsLevel(Minecraft.getMinecraft().gameSettings.fancyGraphics);
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            DrugProperties drugProperties = DrugProperties.getDrugProperties(event.player);

            if (drugProperties != null)
            {
                drugProperties.updateDrugEffects(event.player);

                if (!event.player.getEntityWorld().isRemote && PSConfig.randomTicksUntilRiftSpawn > 0)
                {
                    if (event.player.getRNG().nextInt(PSConfig.randomTicksUntilRiftSpawn) == 0)
                    {
                        spawnRiftAtPlayer(event.player);
                    }
                }

                Container container = event.player.openContainer;
                if (container instanceof UpdatableContainer)
                    ((UpdatableContainer) container).updateAsCustomContainer();
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            Minecraft mc = Minecraft.getMinecraft();

            if (mc != null && !mc.isGamePaused())
            {
                DrugProperties drugProperties = DrugProperties.getDrugProperties(mc.getRenderViewEntity());

                if (drugProperties != null)
                {
                    SmoothCameraHelper.instance.update(mc.gameSettings.mouseSensitivity, DrugEffectInterpreter.getSmoothVision(drugProperties));
                }
            }
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent event)
    {
        if (event instanceof ConfigChangedEvent.OnConfigChangedEvent && event.getModID().equals(Psychedelicraftv2.MODID))
        {
            PSConfig.loadConfig(event.getConfigID());

            if (Psychedelicraftv2.config.hasChanged())
                Psychedelicraftv2.config.save();
        }
    }

    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event)
    {
        if (event.craftMatrix instanceof InventoryCrafting)
            RecipeActionRegistry.finalizeCrafting(event.crafting, (InventoryCrafting) event.craftMatrix, event.player);

        if (event.crafting.isItemEqual(new ItemStack(PSBlocks.mashTub)))
        {
            PSAchievementList.grantAdvancement(event.player, "mashTub");
        }
        if (event.crafting.isItemEqual(new ItemStack(PSBlocks.distillery)))
        {
            PSAchievementList.grantAdvancement(event.player, "madeDistillery");
        }
        if (isFluidStack(event.crafting, new ItemStack(PSItems.itemMashTub, 1, OreDictionary.WILDCARD_VALUE), PSFluids.alcWheatHop))
        {
            PSAchievementList.grantAdvancement(event.player, "beerWash");
        }
        if (isFluidStack(event.crafting, new ItemStack(PSItems.itemMashTub, 1, OreDictionary.WILDCARD_VALUE), PSFluids.alcRedGrapes))
        {
            PSAchievementList.grantAdvancement(event.player, "grapeWash");
        }
        if (isFluidStack(event.crafting, new ItemStack(PSItems.itemMashTub, 1, OreDictionary.WILDCARD_VALUE), PSFluids.alcSugarCane))
        {
            PSAchievementList.grantAdvancement(event.player, "sugarcaneWash");
        }

        if (event.crafting.isItemEqual(new ItemStack(PSBlocks.dryingTable)) || event.crafting.isItemEqual(new ItemStack(PSBlocks.dryingTableIron)))
        {
            PSAchievementList.grantAdvancement(event.player, "madeDryingTable");
        }
        if (event.crafting.isItemEqual(new ItemStack(PSItems.joint)))
        {
            PSAchievementList.grantAdvancement(event.player, "madeJoint");
        }
        if (event.crafting.isItemEqual(new ItemStack(PSItems.hashMuffin)))
        {
            PSAchievementList.grantAdvancement(event.player, "madeHashMuffin");
        }
    }

    @SubscribeEvent
    public void onItemPickup(PlayerEvent.ItemPickupEvent event)
    {
        if (event.getStack().isItemEqual(new ItemStack(PSItems.hopCones)))
        {
            PSAchievementList.grantAdvancement(event.player, "hopCones");
        }
        else if (event.getStack().isItemEqual(new ItemStack(PSItems.wineGrapes)))
        {
            PSAchievementList.grantAdvancement(event.player, "grapes");
        }
        else if (event.getStack().isItemEqual(new ItemStack(PSItems.cannabisBuds)))
        {
            PSAchievementList.grantAdvancement(event.player, "cannabisBuds");
        }
    }
}
