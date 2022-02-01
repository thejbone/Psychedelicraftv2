package com.chillaxmc.psychedelicraftv2.achievements;

import com.chillaxmc.psychedelicraftv2.Psychedelicraftv2;
import com.chillaxmc.psychedelicraftv2.Psychedelicraftv2;
import com.chillaxmc.psychedelicraftv2.blocks.PSBlocks;
import com.chillaxmc.psychedelicraftv2.fluids.PSFluids;
import com.chillaxmc.psychedelicraftv2.items.PSItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;

/**
 * Created by lukas on 18.05.15.
 */
public class PSAchievementList
{

    public static void grantAdvancement(EntityPlayer player, String advancementName) {
        if (!(player instanceof EntityPlayerMP))
            return;

        AdvancementManager manager = player.world.getMinecraftServer().getAdvancementManager();
        Advancement advancement = manager.getAdvancement(new ResourceLocation(Psychedelicraftv2.MODID, advancementName));
        if (advancement == null)
            return;

        ((EntityPlayerMP)player).getAdvancements().grantCriterion(advancement, "done");
    }
}
