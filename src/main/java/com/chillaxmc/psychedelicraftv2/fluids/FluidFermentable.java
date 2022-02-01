/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.psychedelicraftv2.fluids;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * A fluid that can ferment in the correct container, e.g. fermentation barrels.
 */
public interface FluidFermentable
{
    /**
     * Tick value indicating that the fluid is currently unfermentable.
     */
    public static final int UNFERMENTABLE = -1;

    /**
     * The creative subtype for FluidWithTypes.
     */
    public static final String SUBTYPE_OPEN = "openFermentation";

    /**
     * The creative subtype for FluidWithTypes.
     */
    public static final String SUBTYPE_CLOSED = "closedContainer";

    /**
     * Returns the ticks needed for the fluid to ferment. Return {@link #UNFERMENTABLE} if the fluid is curently unfermentable.
     *
     * @param stack         The fluid currently fermenting.
     * @param openContainer True if the fluid is exposed to oxygen.
     * @return The time it needs to ferment, in ticks.
     */
    int fermentationTime(FluidStack stack, boolean openContainer);

    /**
     * Notifies the fluid that the stack has fermented, and is expected to apply this change to the stack.
     *
     * @param stack         The fluid currently fermenting.
     * @param openContainer True if the fluid is exposed to oxygen.
     * @return A stack if the fluid should be replaced with a solid.
     */
    ItemStack fermentStep(FluidStack stack, boolean openContainer);
}
