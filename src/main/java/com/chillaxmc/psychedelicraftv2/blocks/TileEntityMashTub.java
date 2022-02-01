/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.psychedelicraftv2.blocks;

import ivorius.ivtoolkit.blocks.IvTileEntityHelper;
import com.chillaxmc.psychedelicraftv2.client.rendering.blocks.TileEntityMultiblockFluidHandler;
import com.chillaxmc.psychedelicraftv2.fluids.FluidFermentable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import static com.chillaxmc.psychedelicraftv2.fluids.FluidHelper.MILLIBUCKETS_PER_LITER;

/**
 * Created by lukas on 27.10.14.
 */
public class TileEntityMashTub extends TileEntityMultiblockFluidHandler
{
    public static final int MASH_TUB_CAPACITY = MILLIBUCKETS_PER_LITER * 16;

    public int timeFermented;

    public ItemStack solidContents;

    public TileEntityMashTub()
    {
        tank = new FluidTank(MASH_TUB_CAPACITY);
    }

    @Override
    public void updateEntityParent()
    {
        FluidStack fluidStack = tank.getFluid();
        if (fluidStack != null && fluidStack.getFluid() instanceof FluidFermentable)
        {
            FluidFermentable fluidFermentable = (FluidFermentable) fluidStack.getFluid();
            int neededFermentationTime = fluidFermentable.fermentationTime(fluidStack, true);

            if (neededFermentationTime >= 0)
            {
                if (timeFermented >= neededFermentationTime)
                {
                    if (!worldObj.isRemote)
                    {
                        ItemStack solid = fluidFermentable.fermentStep(fluidStack, true);
                        timeFermented = 0;

                        if (solid != null)
                        {
                            tank.setFluid(null);
                            solidContents = solid;
                        }

                        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                        markDirty();
                    }
                }
                else
                    timeFermented++;
            }
        }
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        if (solidContents != null)
            return 0;

        int fill = super.fill(from, resource, doFill);

        if (isParent() && doFill)
        {
            double amountFilled = (double) fill / (double) tank.getFluidAmount();
            timeFermented = MathHelper.floor_double(timeFermented * (1.0 - amountFilled));

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            markDirty();
        }

        return fill;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        if (solidContents != null)
            return null;

        FluidStack drain = super.drain(from, resource, doDrain);

        if (isParent() && doDrain)
        {
            if (tank.getFluidAmount() == 0)
                timeFermented = 0;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            markDirty();
        }

        return drain;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        if (solidContents != null)
            return null;

        FluidStack drain = super.drain(from, maxDrain, doDrain);

        if (isParent() && doDrain)
        {
            if (tank.getFluidAmount() == 0)
                timeFermented = 0;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            markDirty();
        }

        return drain;
    }

    public int getNeededFermentationTime()
    {
        FluidStack fluidStack = tank.getFluid();
        if (fluidStack != null && fluidStack.getFluid() instanceof FluidFermentable)
        {
            FluidFermentable fluidFermentable = (FluidFermentable) fluidStack.getFluid();
            return fluidFermentable.fermentationTime(fluidStack, true);
        }

        return FluidFermentable.UNFERMENTABLE;
    }

    public int getRemainingFermentationTimeScaled(int scale)
    {
        int neededFermentationTime = getNeededFermentationTime();
        if (neededFermentationTime >= 0)
            return (neededFermentationTime - timeFermented) * scale / neededFermentationTime;

        return scale;
    }

    public boolean isFermenting()
    {
        return getNeededFermentationTime() >= 0;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);

        nbttagcompound.setInteger("timeFermented", timeFermented);

        if (solidContents != null)
        {
            NBTTagCompound itemTag = new NBTTagCompound();
            solidContents.writeToNBT(itemTag);
            nbttagcompound.setTag("solidContents", itemTag);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);

        timeFermented = nbttagcompound.getInteger("timeFermented");

        solidContents = nbttagcompound.hasKey("solidContents", Constants.NBT.TAG_COMPOUND)
                ? ItemStack.loadItemStackFromNBT(nbttagcompound.getCompoundTag("solidContents"))
                : null;
    }

    @Override
    public Packet getDescriptionPacket()
    {
        return IvTileEntityHelper.getStandardDescriptionPacket(this);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        readFromNBT(pkt.func_148857_g());
    }
}
