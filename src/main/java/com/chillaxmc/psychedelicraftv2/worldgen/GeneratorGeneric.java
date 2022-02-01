/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package com.chillaxmc.psychedelicraftv2.worldgen;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.*;

/**
 * Created by lukas on 11.03.14.
 */
public class GeneratorGeneric implements IWorldGenerator
{
    public WorldGenerator generator;
    public List<Entry> biomeEntries;

    public GeneratorGeneric(WorldGenerator worldGen, Entry... biomeEntries)
    {
        this.generator = worldGen;

        this.biomeEntries = Arrays.asList(biomeEntries);
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
    {
        BiomeGenBase biomeGen = world.getBiomeGenForCoords(chunkX * 16 + 8, chunkZ * 16 + 8);

        int generate = -1;

        for (Entry biomeEntry : biomeEntries)
        {
            generate = biomeEntry.numberGenerated(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider, biomeGen);
            if (generate >= 0)
                break;
        }

        for (int i = 0; i < generate; i++)
        {
            int genX = chunkX * 16 + random.nextInt(16) + 8;
            int genZ = chunkZ * 16 + random.nextInt(16) + 8;
            int genY = world.getHeightValue(genX, genZ);

            generator.generate(world, random, genX, genY, genZ);
        }
    }

    public static Entry[] simpleEntryList(float chance, int number, BiomeGenBase... biomes)
    {
        Entry[] list = new Entry[biomes.length];

        for (int i = 0; i < list.length; i++)
        {
            list[i] = new EntryBiome(biomes[i], chance, number);
        }

        return list;
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

    }

    public static interface Entry
    {
        public int numberGenerated(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider, BiomeGenBase biomeGen);
    }

    public static class EntryBiome implements Entry
    {
        private BiomeGenBase biome;
        private float chance;
        public int number;

        public EntryBiome(BiomeGenBase biome, float chance)
        {
            this(biome, chance, 1);
        }

        public EntryBiome(BiomeGenBase biome, float chance, int number)
        {
            this.biome = biome;
            this.chance = chance;
            this.number = number;
        }

        public int numberGenerated(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider, BiomeGenBase biomeGen)
        {
            if (getBiome() != biomeGen)
                return -1;

            return (random.nextFloat() < getChance()) ? number : 0;
        }

        public float getChance()
        {
            return chance;
        }

        public BiomeGenBase getBiome()
        {
            return biome;
        }
    }

    public static class EntryBiomeTypes implements Entry
    {
        private final List<BiomeDictionary.Type> biomeTypes = new ArrayList<>();
        private float chance;
        public int number;

        public EntryBiomeTypes(float chance, int number, BiomeDictionary.Type... types)
        {
            this.chance = chance;
            this.number = number;
            Collections.addAll(biomeTypes, types);
        }

        public int numberGenerated(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider, BiomeGenBase biomeGen)
        {
            for (BiomeDictionary.Type type : biomeTypes)
            {
                if (!BiomeDictionary.isBiomeOfType(biomeGen, type))
                    return -1;
            }

            return random.nextFloat() < getChance() ? number : 0;
        }

        public float getChance()
        {
            return chance;
        }
    }
}
