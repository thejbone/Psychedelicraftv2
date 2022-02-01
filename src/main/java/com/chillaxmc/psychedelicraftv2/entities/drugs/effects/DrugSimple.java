/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.psychedelicraftv2.entities.drugs.effects;

import ivorius.ivtoolkit.math.IvMathHelper;
import com.chillaxmc.psychedelicraftv2.entities.drugs.Drug;
import com.chillaxmc.psychedelicraftv2.entities.drugs.DrugProperties;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class DrugSimple implements Drug
{
    protected double effect;
    protected double effectActive;
    protected boolean locked = false;

    private final double decreaseSpeed;
    private final double decreaseSpeedPlus;
    private final boolean invisible;

    public DrugSimple(double decSpeed, double decSpeedPlus)
    {
        this(decSpeed, decSpeedPlus, false);
    }

    public DrugSimple(double decSpeed, double decSpeedPlus, boolean invisible)
    {
        decreaseSpeed = decSpeed;
        decreaseSpeedPlus = decSpeedPlus;

        this.invisible = invisible;
    }

    public void setActiveValue(double value)
    {
        effectActive = value;
    }

    @Override
    public double getActiveValue()
    {
        return effectActive;
    }

    public double getDesiredValue()
    {
        return effect;
    }

    @Override
    public void setDesiredValue(double value)
    {
        effect = value;
    }

    @Override
    public void addToDesiredValue(double value)
    {
        if (!locked)
        {
            effect += value;
        }
    }

    @Override
    public void setLocked(boolean locked)
    {
        this.locked = locked;
    }

    @Override
    public boolean isLocked()
    {
        return locked;
    }

    @Override
    public boolean isVisible()
    {
        return !invisible;
    }

    @Override
    public void update(EntityLivingBase entity, DrugProperties drugProperties)
    {
        if (!locked)
        {
            effect *= decreaseSpeed;
            effect -= decreaseSpeedPlus;
        }

        effect = IvMathHelper.clamp(0.0, effect, 1.0);

        effectActive = IvMathHelper.nearValue(effectActive, effect, 0.05, 0.005);
    }

    @Override
    public void reset(EntityLivingBase entity, DrugProperties drugProperties)
    {
        if (!locked)
            effect = 0.0;
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        setDesiredValue(par1NBTTagCompound.getDouble("effect"));
        setActiveValue(par1NBTTagCompound.getDouble("effectActive"));
        setLocked(par1NBTTagCompound.getBoolean("locked"));
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        compound.setDouble("effect", getDesiredValue());
        compound.setDouble("effectActive", getActiveValue());
        compound.setBoolean("locked", isLocked());
    }

    @Override
    public float heartbeatVolume()
    {
        return 0;
    }

    @Override
    public float heartbeatSpeed()
    {
        return 0;
    }

    @Override
    public float breathVolume()
    {
        return 0;
    }

    @Override
    public float breathSpeed()
    {
        return 0;
    }

    @Override
    public float randomJumpChance()
    {
        return 0;
    }

    @Override
    public float randomPunchChance()
    {
        return 0;
    }

    @Override
    public float digSpeedModifier()
    {
        return 1;
    }

    @Override
    public float speedModifier()
    {
        return 1;
    }

    @Override
    public float soundVolumeModifier()
    {
        return 1;
    }

    @Override
    public EntityPlayer.EnumStatus getSleepStatus()
    {
        return null;
    }

    @Override
    public void applyContrastColorization(float[] rgba)
    {

    }

    @Override
    public void applyColorBloom(float[] rgba)
    {

    }

    @Override
    public float desaturationHallucinationStrength()
    {
        return 0;
    }

    @Override
    public float superSaturationHallucinationStrength()
    {
        return 0;
    }

    @Override
    public float contextualHallucinationStrength()
    {
        return 0;
    }

    @Override
    public float colorHallucinationStrength()
    {
        return 0;
    }

    @Override
    public float movementHallucinationStrength()
    {
        return 0;
    }

    @Override
    public float handTrembleStrength()
    {
        return 0;
    }

    @Override
    public float viewTrembleStrength()
    {
        return 0;
    }

    @Override
    public float headMotionInertness()
    {
        return 0;
    }

    @Override
    public float bloomHallucinationStrength()
    {
        return 0;
    }

    @Override
    public float viewWobblyness()
    {
        return 0;
    }

    @Override
    public float doubleVision()
    {
        return 0;
    }

    @Override
    public void drawOverlays(float partialTicks, EntityLivingBase entity, int updateCounter, int width, int height, DrugProperties drugProperties)
    {

    }

    @Override
    public float motionBlur()
    {
        return 0;
    }
}
