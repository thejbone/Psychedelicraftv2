/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.pscoreutils.events;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Created by lukas on 13.03.14.
 */
public class GetSoundVolumeEvent extends Event
{
    public final float originalVolume;
    public final ISound sound;
    public final SoundPoolEntry entry;
    public final SoundCategory category;
    public final SoundManager manager;

    public float volume;

    public GetSoundVolumeEvent(float volume, ISound sound, SoundPoolEntry entry, SoundCategory category, SoundManager manager)
    {
        this.originalVolume = volume;
        this.volume = volume;
        this.sound = sound;
        this.entry = entry;
        this.category = category;
        this.manager = manager;
    }
}
