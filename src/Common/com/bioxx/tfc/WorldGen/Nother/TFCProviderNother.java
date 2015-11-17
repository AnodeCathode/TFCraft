package com.bioxx.tfc.WorldGen.Nother;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderHell;
import net.minecraft.world.storage.WorldInfo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import com.bioxx.tfc.Core.TFC_Climate;
import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.WorldGen.TFCProvider;
import com.bioxx.tfc.WorldGen.WorldCacheManager;
import com.bioxx.tfc.api.TFCBlocks;

public class TFCProviderNother extends TFCProvider
{
	@Override
	protected void registerWorldChunkManager()
	{
		/**
		 * ChunkEventHandler.onLoadWorld gets called after the NEW World gen stuff.
		 * Trying to make a NEW World will produce a crash because the cache is empty.
		 * ..maybe this is not the best place for this, but it works :)
		 */
		TFC_Climate.worldPair.put(worldObj, new WorldCacheManager(worldObj));
		TFC_Core.addCDM(worldObj);
		this.worldChunkMgr = new TFCWorldChunkManagerNother(worldObj);
        this.dimensionId = -1;
        this.hasNoSky = true;
		
	}

	@Override
	protected void generateLightBrightnessTable()
	{
		float var1 = 0.1F;
		for (int var2 = 0; var2 <= 15; ++var2)
		{
			float var3 = 1.0F - var2 / 15.0F;
			this.lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
		}
	}

	@Override
	public IChunkProvider createChunkGenerator()
	{
		return new TFCChunkProviderNother(this.worldObj, (long) (this.worldObj.getSeed() * Math.PI), true);
	}

	@Override
	public ChunkCoordinates getSpawnPoint()
	{
		WorldInfo info = worldObj.getWorldInfo();
		return new ChunkCoordinates(info.getSpawnX(), info.getSpawnY(), info.getSpawnZ());
	}

	@Override
	public boolean isSurfaceWorld()
	{
		return true;
	}

	@Override
	public boolean canCoordinateBeSpawn(int par1, int par2)
	{
		return false;
	}

	@Override
	public float calculateCelestialAngle(long par1, float par3)
	{
		return 0.5F;
	}

	@Override
	public boolean canRespawnHere()
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean doesXZShowFog(int par1, int par2)
	{
		return true;
	}

	@Override
	public String getDimensionName()
	{
		return "Nother";
	}

	@Override
	public Vec3 getFogColor(float par1, float par2)
	{
		return Vec3.createVectorHelper(0.20000000298023224D, 0.029999999329447746D, 0.029999999329447746D);
	}
	
	@Override
	public ChunkCoordinates getEntrancePortalLocation()
	{
		return getSpawnPoint();
	}
	@Override
	public boolean canSnowAt(int x, int y, int z, boolean checkLight)
	{
		return false;
	}
	@Override
	public boolean canDoRainSnowIce(Chunk chunk)
	{
		return false;
	}
	@Override
	public boolean canBlockFreeze(int x, int y, int z, boolean byWater)
	{
		return false;
	}
    @SideOnly(Side.CLIENT)
    public boolean isSkyColored()
    {
        return false;
    }

}
