/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.psychedelicraftv2.blocks;

import com.chillaxmc.psychedelicraftv2.Psychedelicraftv2;
import com.chillaxmc.psychedelicraftv2.gui.PSGuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

/**
 * Created by lukas on 25.10.14.
 */
public class BlockDistillery extends Block
{
    public BlockDistillery()
    {
        super(Material.glass);
        setBlockBounds(0.25f, 0.0f, 0.25f, 0.75f, 0.9f, 0.75f);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityDistillery)
        {
            if (!world.isRemote)
                player.openGui(Psychedelicraftv2.instance, PSGuiHandler.distilleryContainerID, world, x, y, z);

            return true;
        }

        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack stack)
    {
        int direction = (MathHelper.floor_double((entityLivingBase.rotationYaw * 4F) / 360F + 0.5D) + 1) & 3;

        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityDistillery)
        {
            TileEntityDistillery tileEntityDistillery = (TileEntityDistillery) tileEntity;

            tileEntityDistillery.direction = direction;

            FluidStack fluidStack = stack.getItem() instanceof IFluidContainerItem ? ((IFluidContainerItem) stack.getItem()).getFluid(stack) : null;
            if (fluidStack != null)
                tileEntityDistillery.fill(ForgeDirection.UP, fluidStack, true);
        }
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
    {
        if (willHarvest)
        {
            TileEntity tileEntity = world.getTileEntity(x, y, z);
            if (tileEntity instanceof TileEntityDistillery)
            {
                TileEntityDistillery tileEntityDistillery = (TileEntityDistillery) tileEntity;
                FluidStack fluidStack = tileEntityDistillery.drain(ForgeDirection.DOWN, TileEntityDistillery.DISTILLERY_CAPACITY, true);
                ItemStack stack = new ItemStack(this);

                if (fluidStack != null && fluidStack.amount > 0)
                    ((IFluidContainerItem) stack.getItem()).fill(stack, fluidStack, true);

                dropBlockAsItem(world, x, y, z, stack);
            }
        }

        return super.removedByPlayer(world, player, x, y, z, willHarvest);
    }

    @Override
    public void dropBlockAsItemWithChance(World p_149690_1_, int p_149690_2_, int p_149690_3_, int p_149690_4_, int p_149690_5_, float p_149690_6_, int p_149690_7_)
    {

    }

    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {

    }

    @Override
    public IIcon getIcon(int side, int meta)
    {
        return Blocks.glass.getIcon(side, 0);
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public boolean hasTileEntity(int metadata)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int meta)
    {
        return new TileEntityDistillery();
    }
}
