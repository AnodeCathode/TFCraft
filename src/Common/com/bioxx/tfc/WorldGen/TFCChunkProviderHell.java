package com.bioxx.tfc.WorldGen;

import java.util.Arrays;
import java.util.Random;

import com.bioxx.tfc.Chunkdata.ChunkData;
import com.bioxx.tfc.Core.TFC_Climate;
import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.WorldGen.MapGen.MapGenCavesTFC;
import com.bioxx.tfc.WorldGen.MapGen.MapGenRavineTFC;
import com.bioxx.tfc.WorldGen.MapGen.MapGenRiverRavine;
import com.bioxx.tfc.api.TFCBlocks;
import com.bioxx.tfc.api.Constant.Global;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.NoiseGeneratorOctaves;

public class TFCChunkProviderHell extends TFCChunkProviderGenerate {
	
	/** RNG. */
	private Random rand;

	/** A NoiseGeneratorOctaves used in generating terrain */
	private NoiseGeneratorOctaves noiseGen1;

	/** A NoiseGeneratorOctaves used in generating terrain */
	private NoiseGeneratorOctaves noiseGen2;

	/** A NoiseGeneratorOctaves used in generating terrain */
	private NoiseGeneratorOctaves noiseGen3;

	/** A NoiseGeneratorOctaves used in generating terrain */
	private NoiseGeneratorOctaves noiseGen4;

	/** A NoiseGeneratorOctaves used in generating terrain */
	public NoiseGeneratorOctaves noiseGen6;
	public NoiseGeneratorOctaves mobSpawnerNoise;

	/** Reference to the World object. */
	private World worldObj;

	/** Holds the overall noise array used in chunk generation */
	private double[] noiseArray;
	private double[] stoneNoise = new double[256];

	/** The biomes that are used to generate the chunk */
	private BiomeGenBase[] biomesForGeneration;

	private DataLayer[] rockLayer1;
	private DataLayer[] rockLayer2;
	private DataLayer[] rockLayer3;
	private DataLayer[] evtLayer;
	private DataLayer[] rainfallLayer;
	private DataLayer[] stabilityLayer;
	private DataLayer[] drainageLayer;


	private Block[] idsTop;
	private Block[] idsBig;
	private byte[] metaBig;

	/** A double array that hold terrain noise from noiseGen3 */
	private double[] noise3;

	/** A double array that hold terrain noise */
	private double[] noise1;

	/** A double array that hold terrain noise from noiseGen2 */
	private double[] noise2;

	/** A double array that hold terrain noise from noiseGen5 */
	//private double[] noise5;

	/** A double array that holds terrain noise from noiseGen6 */
	private double[] noise6;

	/**
	 * Used to store the 5x5 parabolic field that is used during terrain generation.
	 */
	private float[] parabolicField;

	private int[] seaLevelOffsetMap = new int[256];
	private int[] chunkHeightMap = new int[256];

	private MapGenCavesTFC caveGen = new MapGenCavesTFC();
	private MapGenRavineTFC surfaceRavineGen = new MapGenRavineTFC(125, 30);//surface
	private MapGenRavineTFC ravineGen = new MapGenRavineTFC(20, 50);//deep
	private MapGenRiverRavine riverRavineGen = new MapGenRiverRavine();
	
	public TFCChunkProviderHell(World par1World, long par2, boolean par4) {
		super(par1World, par2, par4);
		this.worldObj = par1World;
		this.rand = new Random(par2);
		this.noiseGen1 = new NoiseGeneratorOctaves(this.rand, 4);
		this.noiseGen2 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noiseGen3 = new NoiseGeneratorOctaves(this.rand, 8);
		this.noiseGen4 = new NoiseGeneratorOctaves(this.rand, 4);
		this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 2);
		this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 1);
		this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);

		this.idsTop = new Block[32768];
		this.idsBig = new Block[16*16*256];
		this.metaBig = new byte[16*16*256];	
	
	}
	@Override
	public Chunk provideChunk(int chunkX, int chunkZ)
	{
		this.rand.setSeed(chunkX * 341873128712L + chunkZ * 132897987541L);

		//	To reduce GC churn, we allocate these arrays once in the
		//	constructor and then just clear them to all zeroes before each
		//	use.
		//
		Arrays.fill(idsTop, null);
		Arrays.fill(idsBig, null);
		Arrays.fill(metaBig, (byte)0);

		this.generateTerrainHigh(chunkX, chunkZ, idsTop);

		biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(biomesForGeneration, chunkX * 16-1, chunkZ * 16-1, 18, 18);
		if (TFC_Climate.getCacheManager(worldObj) != null)
		{
			rockLayer1 = TFC_Climate.getCacheManager(worldObj).loadRockLayerGeneratorData(rockLayer1, chunkX * 16, chunkZ * 16, 16, 16, 0);
			rockLayer2 = TFC_Climate.getCacheManager(worldObj).loadRockLayerGeneratorData(rockLayer2, chunkX * 16, chunkZ * 16, 16, 16, 1);
			rockLayer3 = TFC_Climate.getCacheManager(worldObj).loadRockLayerGeneratorData(rockLayer3, chunkX * 16, chunkZ * 16, 16, 16, 2);
			evtLayer = TFC_Climate.getCacheManager(worldObj).loadEVTLayerGeneratorData(evtLayer, chunkX * 16, chunkZ * 16, 16, 16);
			rainfallLayer = TFC_Climate.getCacheManager(worldObj).loadRainfallLayerGeneratorData(rainfallLayer, chunkX * 16, chunkZ * 16, 16, 16);
			stabilityLayer = TFC_Climate.getCacheManager(worldObj).loadStabilityLayerGeneratorData(stabilityLayer, chunkX * 16, chunkZ * 16, 16, 16);
			drainageLayer = TFC_Climate.getCacheManager(worldObj).loadDrainageLayerGeneratorData(drainageLayer, chunkX * 16, chunkZ * 16, 16, 16);
		}

		seaLevelOffsetMap = new int[256];

		replaceBlocksForBiomeHigh(chunkX, chunkZ, idsTop, rand, idsBig, metaBig);
		replaceBlocksForBiomeLow(chunkX, chunkZ, rand, idsBig, metaBig);

		caveGen.generate(this, this.worldObj, chunkX, chunkZ, idsBig, metaBig);
		surfaceRavineGen.generate(this, this.worldObj, chunkX, chunkZ, idsBig, metaBig);//surface
		ravineGen.generate(this, this.worldObj, chunkX, chunkZ, idsBig, metaBig);//deep
		riverRavineGen.generate(this, this.worldObj, chunkX, chunkZ, idsBig, metaBig);

		Chunk chunk = new Chunk(this.worldObj, idsBig, metaBig, chunkX, chunkZ);
		byte[] abyte1 = chunk.getBiomeArray();

		for (int x = 0; x < 16; ++x)
		{
			for (int z = 0; z < 16; ++z)
			{
				abyte1[x*z] = (byte)getBiome(x, z).biomeID;
			}
		}
		chunk.setBiomeArray(abyte1);

		ChunkData data = new ChunkData(chunk).createNew(worldObj, chunkX, chunkZ);
		data.heightmap = seaLevelOffsetMap;
		data.rainfallMap = this.rainfallLayer;
		TFC_Core.getCDM(worldObj).addData(chunk, data);
		//chunk.heightMap = chunkHeightMap;
		chunk.generateSkylightMap();
		return chunk;
	}
	
	private void replaceBlocksForBiomeHigh(int chunkX, int chunkZ, Block[] idsTop, Random rand, Block[] idsBig, byte[] metaBig)
	{
		int seaLevel = 16;
		int worldHeight = 256;
		int indexOffset = 128;
		double var6 = 0.03125D;
		stoneNoise = noiseGen4.generateNoiseOctaves(stoneNoise, chunkX * 16, chunkZ * 16, 0, 16, 16, 1, var6 * 4.0D, var6 * 1.0D, var6 * 4.0D);
		boolean[] cliffMap = new boolean[256];
		for (int xCoord = 0; xCoord < 16; ++xCoord)
		{
			for (int zCoord = 0; zCoord < 16; ++zCoord)
			{
				int arrayIndex = xCoord + zCoord * 16;
				int arrayIndexDL = zCoord + xCoord * 16;
				int arrayIndex2 = xCoord+1 + zCoord+1 * 16;
				TFCBiome biome = (TFCBiome)getBiome(xCoord,zCoord);
				DataLayer rock1 = rockLayer1[arrayIndexDL] == null ? DataLayer.GRANITE : rockLayer1[arrayIndexDL];
				DataLayer rock2 = rockLayer2[arrayIndexDL] == null ? DataLayer.GRANITE : rockLayer2[arrayIndexDL];
				DataLayer rock3 = rockLayer3[arrayIndexDL] == null ? DataLayer.GRANITE : rockLayer3[arrayIndexDL];
				//DataLayer evt = evtLayer[arrayIndexDL] == null ? DataLayer.EVT_0_125 : evtLayer[arrayIndexDL];
				float rain = rainfallLayer[arrayIndexDL] == null ? DataLayer.RAIN_125.floatdata1 : rainfallLayer[arrayIndexDL].floatdata1;
				DataLayer drainage = drainageLayer[arrayIndexDL] == null ? DataLayer.DRAINAGE_NORMAL : drainageLayer[arrayIndexDL];
				int var12 = (int)(stoneNoise[arrayIndex2] / 3.0D + 6.0D);
				int var13 = -1;

				Block surfaceBlock = TFC_Core.getTypeForGrassWithRain(rock1.data1, rain);
				Block subSurfaceBlock = TFC_Core.getTypeForDirtFromGrass(surfaceBlock);

				float bioTemp = TFC_Climate.getBioTemperature(worldObj, chunkX * 16 + xCoord, chunkZ * 16 + zCoord);
				int h = 0;
				if(TFC_Core.isBeachBiome(getBiome(xCoord-1, zCoord).biomeID) || TFC_Core.isBeachBiome(getBiome(xCoord+1, zCoord).biomeID) || 
						TFC_Core.isBeachBiome(getBiome(xCoord, zCoord+1).biomeID) || TFC_Core.isBeachBiome(getBiome(xCoord, zCoord-1).biomeID))
				{
					if(!TFC_Core.isBeachBiome(getBiome(xCoord, zCoord).biomeID))
						cliffMap[arrayIndex] = true;
				}
				for (int height = 127; height >= 0; --height)
				{
					int indexBig = (arrayIndex) * worldHeight + height + indexOffset;
					int index = (arrayIndex) * 128 + height;
					//metaBig[indexBig] = 0;
					float temp = TFC_Climate.adjustHeightToTemp(height, bioTemp);
					if(TFC_Core.isBeachBiome(biome.biomeID) && height > seaLevel+h && idsTop[index] == Blocks.stone)
					{
						idsTop[index] = Blocks.air;
						if(h == 0)
							h = (height-16)/4;
					}
					if(idsBig[indexBig] == null)
					{
						idsBig[indexBig] = idsTop[index];
						if(indexBig+1 < idsBig.length && TFC_Core.isSoilOrGravel(idsBig[indexBig+1]) && idsBig[indexBig] == Blocks.air)
						{
							for(int upCount = 1; TFC_Core.isSoilOrGravel(idsBig[indexBig+upCount]); upCount++)
							{idsBig[indexBig+upCount] = Blocks.air;}
						}
					}

					if (idsBig[indexBig] == Blocks.stone)
					{
						if(seaLevelOffsetMap[arrayIndex] == 0 && height-16 >= 0)
							seaLevelOffsetMap[arrayIndex] = height-16;

						if(chunkHeightMap[arrayIndex] == 0)
							chunkHeightMap[arrayIndex] = height+indexOffset;

						convertStone(indexOffset+height, arrayIndex, indexBig, idsBig, metaBig, rock1, rock2, rock3);

						//First we check to see if its a cold desert
						if(rain < 125 && temp < 1.5f)
						{
							surfaceBlock = TFC_Core.getTypeForSand(rock1.data1);
							subSurfaceBlock = TFC_Core.getTypeForSand(rock1.data1);
						}
						//Next we check for all other warm deserts
						else if(rain < 125 && biome.heightVariation < 0.5f && temp > 20f)
						{
							surfaceBlock = TFC_Core.getTypeForSand(rock1.data1);
							subSurfaceBlock = TFC_Core.getTypeForSand(rock1.data1);
						}

						if(biome == TFCBiome.BEACH || biome == TFCBiome.OCEAN || biome == TFCBiome.DEEP_OCEAN)
						{
							subSurfaceBlock = surfaceBlock = TFC_Core.getTypeForSand(rock1.data1);
						}
						else if(biome == TFCBiome.GRAVEL_BEACH)
						{
							subSurfaceBlock = surfaceBlock = TFC_Core.getTypeForGravel(rock1.data1);
						}

						if (var13 == -1)
						{
							//The following makes dirt behave nicer and more smoothly, instead of forming sharp cliffs.
							int arrayIndexx = xCoord > 0 ? xCoord - 1 + (zCoord * 16) : -1;
							int arrayIndexX = xCoord < 15 ? xCoord + 1 + (zCoord * 16) : -1;
							int arrayIndexz = zCoord > 0? xCoord + ((zCoord-1) * 16):-1;
							int arrayIndexZ = zCoord < 15? xCoord + ((zCoord+1) * 16):-1;
							int var12Temp = var12;
							for(int counter = 1; counter < var12Temp / 3; counter++)
							{
								if(arrayIndexx >= 0 && seaLevelOffsetMap[arrayIndex]-(3*counter) > seaLevelOffsetMap[arrayIndexx])
								{
									seaLevelOffsetMap[arrayIndex]--;
									var12--;
									height--;
									indexBig = (arrayIndex) * worldHeight + height + indexOffset;
									index = (arrayIndex) * 128 + height;
								}
								else if(arrayIndexX >= 0 && seaLevelOffsetMap[arrayIndex]-(3*counter) > seaLevelOffsetMap[arrayIndexX])
								{
									seaLevelOffsetMap[arrayIndex]--;
									var12--;
									height--;
									indexBig = (arrayIndex) * worldHeight + height + indexOffset;
									index = (arrayIndex) * 128 + height;
								}
								else if(arrayIndexz >= 0 && seaLevelOffsetMap[arrayIndex]-(3*counter) > seaLevelOffsetMap[arrayIndexz])
								{
									seaLevelOffsetMap[arrayIndex]--;
									var12--;
									height--;
									indexBig = (arrayIndex) * worldHeight + height + indexOffset;
									index = (arrayIndex) * 128 + height;
								}
								else if(arrayIndexZ >= 0 && seaLevelOffsetMap[arrayIndex]-(3*counter) > seaLevelOffsetMap[arrayIndexZ])
								{
									seaLevelOffsetMap[arrayIndex]--;
									var12--;
									height--;
									indexBig = (arrayIndex) * worldHeight + height + indexOffset;
									index = (arrayIndex) * 128 + height;
								}
							}
							var13 = (int) (var12 * (1d - Math.max(Math.min((height - 16) / 80d, 1), 0)));

							//Set soil below water
							for(int c = 1; c < 3; c++)
							{
								if(indexBig + c < idsBig.length && 
										idsBig[indexBig + c] != surfaceBlock &&
										idsBig[indexBig + c] != subSurfaceBlock &&
										idsBig[indexBig + c] != TFCBlocks.saltWaterStationary &&
										idsBig[indexBig + c] != TFCBlocks.freshWaterStationary &&
										idsBig[indexBig + c] != TFCBlocks.hotWater)
								{
									idsBig[indexBig + c] = Blocks.air;
									//metaBig[indexBig + c] = 0;
									if(indexBig + c + 1 < idsBig.length && idsBig[indexBig + c + 1] == TFCBlocks.saltWaterStationary)
									{
										idsBig[indexBig + c] = subSurfaceBlock;
										metaBig[indexBig + c] = (byte)TFC_Core.getSoilMeta(rock1.data1);
									}
								}
							}

							//Determine the soil depth based on world height
							int dirtH = Math.max(8-((height + 96 - Global.SEALEVEL) / 16), 0);

							if(var13 > 0)
							{
								if (height >= seaLevel - 1 && index+1 < idsTop.length && idsTop[index + 1] != TFCBlocks.saltWaterStationary && dirtH > 0)
								{
									idsBig[indexBig] = surfaceBlock;
									metaBig[indexBig] = (byte)TFC_Core.getSoilMeta(rock1.data1);


									for(int c = 1; c < dirtH && !TFC_Core.isMountainBiome(biome.biomeID) && 
											biome != TFCBiome.HIGH_HILLS && biome != TFCBiome.HIGH_HILLS_EDGE && !cliffMap[arrayIndex]; c++)
									{
										int offsetHeight = height - c;
										int newIndexBig = (arrayIndex) * worldHeight + offsetHeight + indexOffset;
										idsBig[newIndexBig] = subSurfaceBlock;
										metaBig[newIndexBig] = (byte)TFC_Core.getSoilMeta(rock1.data1);

										if(c > 1+(5-drainage.data1))
										{
											idsBig[newIndexBig] = TFC_Core.getTypeForGravel(rock1.data1);
											metaBig[newIndexBig] = (byte)TFC_Core.getSoilMeta(rock1.data1);
										}
									}
								}
							}
						}

						if (height > seaLevel - 2 && height < seaLevel && idsTop[index + 1] == TFCBlocks.saltWaterStationary ||
							height < seaLevel && idsTop[index + 1] == TFCBlocks.saltWaterStationary)
						{
							if (biome != TFCBiome.SWAMPLAND) // Most areas have gravel and sand bottoms
							{
								if (idsBig[indexBig] != TFC_Core.getTypeForSand(rock1.data1) && rand.nextInt(5) != 0)
								{
									idsBig[indexBig] = TFC_Core.getTypeForGravel(rock1.data1);
									metaBig[indexBig] = (byte)TFC_Core.getSoilMeta(rock1.data1);
								}
							}
							else // Swamp biomes have bottoms that are mostly dirt
							{
								if (idsBig[indexBig] != TFC_Core.getTypeForGravel(rock1.data1))
								{
									idsBig[indexBig] = TFC_Core.getTypeForDirt(rock1.data1);
									metaBig[indexBig] = (byte) TFC_Core.getSoilMeta(rock1.data1);
								}
							}
						}
					}
					else if(idsTop[index] == TFCBlocks.saltWaterStationary && biome != TFCBiome.OCEAN && biome != TFCBiome.DEEP_OCEAN && biome != TFCBiome.BEACH && biome != TFCBiome.GRAVEL_BEACH)
					{
						idsBig[indexBig] = TFCBlocks.freshWaterStationary;
					}
				}
			}
		}

		/*for (int x = 0; x < 16; ++x)
		{
			for (int z = 0; z < 16; ++z)
			{
				int arrayIndex = getIndex(x, z);
				int arrayIndex2 = getIndex(x+1, z+1);

			}
		}*/
	}

	/*private int getIndex(int x, int z)
	{
		return x + z * 16;
	}*/

	private void replaceBlocksForBiomeLow(int par1, int par2, Random rand, Block[] idsBig, byte[] metaBig)
	{
		for (int xCoord = 0; xCoord < 16; ++xCoord)
		{
			for (int zCoord = 0; zCoord < 16; ++zCoord)
			{
				int arrayIndex = xCoord + zCoord * 16;
				int arrayIndexDL = zCoord + xCoord * 16;
				DataLayer rock1 = rockLayer1[arrayIndexDL];
				DataLayer rock2 = rockLayer2[arrayIndexDL];
				DataLayer rock3 = rockLayer3[arrayIndexDL];
				DataLayer stability = stabilityLayer[arrayIndexDL];
				TFCBiome biome = (TFCBiome) getBiome(xCoord, zCoord);

				for (int height = 127; height >= 0; --height)
				{
					//int index = ((arrayIndex) * 128 + height);
					int indexBig = (arrayIndex) * 256 + height;
					metaBig[indexBig] = 0;

					if (height <= 1 + (seaLevelOffsetMap[arrayIndex] / 3) + this.rand.nextInt(3))
					{
						idsBig[indexBig] = Blocks.bedrock;
					}
					else if(idsBig[indexBig] == null)
					{
						convertStone(height, arrayIndex, indexBig, idsBig, metaBig, rock1, rock2, rock3);
						if(TFC_Core.isBeachBiome(biome.biomeID) || TFC_Core.isOceanicBiome(biome.biomeID))
						{
							if(idsBig[indexBig+1] == TFCBlocks.saltWaterStationary)
							{
								idsBig[indexBig] = TFC_Core.getTypeForSand(rock1.data1);
								metaBig[indexBig] = (byte)TFC_Core.getSoilMeta(rock1.data1);
								idsBig[indexBig-1] = TFC_Core.getTypeForSand(rock1.data1);
								metaBig[indexBig-1] = (byte)TFC_Core.getSoilMeta(rock1.data1);
							}
						}
					}

					if (height <= 6 && stability.data1 == 1 && idsBig[indexBig] == Blocks.air)
					{
						idsBig[indexBig] = TFCBlocks.lava;
						metaBig[indexBig] = 0; 
						if(idsBig[indexBig+1] != TFCBlocks.lava && rand.nextBoolean())
						{
							idsBig[indexBig+1] = TFCBlocks.lava;
							metaBig[indexBig+1] = 0; 
						}
					}
				}
			}
		}
	}
	private BiomeGenBase getBiome(int x, int z)
	{
		return this.biomesForGeneration[z + 1 + (x + 1) * 18];
	}

}
