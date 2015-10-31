package com.bioxx.tfc.WorldGen;

import java.util.Random;

import com.bioxx.tfc.WorldGen.MapGen.MapGenCavesTFC;
import com.bioxx.tfc.WorldGen.MapGen.MapGenRavineTFC;
import com.bioxx.tfc.WorldGen.MapGen.MapGenRiverRavine;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
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
		// TODO Auto-generated constructor stub
	}

}
