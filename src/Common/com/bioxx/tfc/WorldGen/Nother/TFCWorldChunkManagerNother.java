package com.bioxx.tfc.WorldGen.Nother;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.bioxx.tfc.WorldGen.TFCBiome;
import com.bioxx.tfc.WorldGen.TFCWorldType;
import com.bioxx.tfc.WorldGen.GenLayers.GenLayerTFC;

import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class TFCWorldChunkManagerNother extends WorldChunkManager
{

	/** The biome generator object. */
	private TFCBiomeGenNother biomeGenerator;

	/** The rainfall in the world */
	private float rainfall;
	
    private GenLayerTFC genBiomes;

	private GenLayerTFC biomeIndexLayer;

	public long seed;
	protected World worldObj;
	private BiomeCache biomeCache;
	
	public TFCWorldChunkManagerNother()
	{
		super();
		biomeCache = new BiomeCache(this);
	}

	public TFCWorldChunkManagerNother(World world)
	{
		this();
		worldObj = world;
		seed = world.getSeed();
		GenLayerTFC[] var4;
		var4 = GenLayerTFC.initialize(seed, TFCWorldType.defaultWorldType);
		this.genBiomes = var4[0];
		this.biomeIndexLayer = var4[1];
	}



	/**
	 * Returns the BiomeGenBase related to the x, z position on the world.
	 */
	@Override
	public TFCBiomeGenNother getBiomeGenAt(int par1, int par2)
	{
		return this.biomeGenerator;
	}

	/**
	 * Returns an array of biomes for the location input.
	 */
	@Override
	public TFCBiomeGenNother[] getBiomesForGeneration(BiomeGenBase[] par1, int par2, int par3, int par4, int par5)
	{
		IntCache.resetIntCache();

		TFCBiomeGenNother[] biome = (TFCBiomeGenNother[]) par1;
		if (biome == null || biome.length < par4 * par5)
			biome = new TFCBiomeGenNother[par4 * par5];

		int[] var6 = this.genBiomes.getInts(par2, par3, par4, par5);
		for (int var7 = 0; var7 < par4 * par5; ++var7)
		{
			int index = Math.max(var6[var7], 0);
			biome[var7] = TFCBiomeGenNother.getBiome(index);
		}

		return biome;
	}

	/**
	 * Returns a list of rainfall values for the specified blocks. Args: listToReuse, x, z, width, length.
	 */
	@Override
	public float[] getRainfall(float[] par1ArrayOfFloat, int par2, int par3, int par4, int par5)
	{
		if (par1ArrayOfFloat == null || par1ArrayOfFloat.length < par4 * par5)
			par1ArrayOfFloat = new float[par4 * par5];
		Arrays.fill(par1ArrayOfFloat, 0, par4 * par5, this.rainfall);
		return par1ArrayOfFloat;
	}

	/**
	 * Returns biomes to use for the blocks and loads the other data like temperature and humidity onto the
	 * WorldChunkManager Args: oldBiomeList, x, z, width, depth
	 */
	@Override
	public TFCBiomeGenNother[] loadBlockGeneratorData(BiomeGenBase[] par1, int par2, int par3, int par4, int par5)
	{
		if (par1 == null || par1.length < par4 * par5)
			par1 = new TFCBiome[par4 * par5];
		Arrays.fill(par1, 0, par4 * par5, this.biomeGenerator);
		return (TFCBiomeGenNother[]) par1;
	}

	/**
	 * Return a list of biomes for the specified blocks. Args: listToReuse, x, y, width, length, cacheFlag (if false,
	 * don't check biomeCache to avoid infinite loop in BiomeCacheBlock)
	 */
	@Override
	public TFCBiomeGenNother[] getBiomeGenAt(BiomeGenBase[] par1, int par2, int par3, int par4, int par5, boolean par6)
	{
		return this.loadBlockGeneratorData(par1, par2, par3, par4, par5);
	}

	/**
	 * Finds a valid position within a range, that is in one of the listed biomes. Searches {par1,par2} +-par3 blocks.
	 * Strongly favors positive y positions.
	 */
	@Override
	public ChunkPosition findBiomePosition(int par1, int par2, int par3, List par4List, Random par5Random)
	{
		return par4List.contains(this.biomeGenerator) ? new ChunkPosition(par1 - par3 + par5Random.nextInt(par3 * 2 + 1), 0, par2 - par3 + par5Random.nextInt(par3 * 2 + 1)) : null;
	}

	/**
	 * checks given Chunk's Biomes against List of allowed ones
	 */
	@Override
	public boolean areBiomesViable(int par1, int par2, int par3, List par4List)
	{
		return par4List.contains(this.biomeGenerator);
	}
}
