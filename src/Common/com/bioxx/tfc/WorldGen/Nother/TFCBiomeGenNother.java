package com.bioxx.tfc.WorldGen.Nother;

import com.bioxx.tfc.TerraFirmaCraft;
import com.bioxx.tfc.Entities.Mobs.EntityCreeperTFC;
import com.bioxx.tfc.Entities.Mobs.EntityEndermanTFC;
import com.bioxx.tfc.Entities.Mobs.EntityGhastTFC;
import com.bioxx.tfc.Entities.Mobs.EntityPigZombieTFC;
import com.bioxx.tfc.Entities.Mobs.EntitySkeletonTFC;
import com.bioxx.tfc.Entities.Mobs.EntitySlimeTFC;
import com.bioxx.tfc.Entities.Mobs.EntitySpiderTFC;
import com.bioxx.tfc.Entities.Mobs.EntityZombieTFC;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.biome.BiomeGenBase;




public class TFCBiomeGenNother extends BiomeGenBase {
	
	protected int biomeColor;
	public BiomeDecoratorTFCNother theBiomeDecorator;
	
	public static TFCBiomeGenNother[] biomeList = new TFCBiomeGenNother[256];
	
	public static final TFCBiomeGenNother NOTHER = new TFCBiomeGenNother(40).setBiomeName("Nother").setBiomeColor(0x69dfa0);

	@SuppressWarnings("unchecked")
	public TFCBiomeGenNother(int par1) {
		super(par1);
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableMonsterList.add(new SpawnListEntry(EntityGhastTFC.class, 50, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityPigZombieTFC.class, 100, 4, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySpiderTFC.class, 5, 1, 1));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityZombieTFC.class, 10, 2, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySkeletonTFC.class, 8, 1, 1));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityCreeperTFC.class, 3, 1, 2));
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySlimeTFC.class, 8, 1, 2));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityEndermanTFC.class, 1, 1, 2));
		biomeList[par1] = this;
		this.theBiomeDecorator = this.createBiomeDecorator();     
		
	}
	/**
     * takes temperature, returns color
     */
    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float p_76731_1_)
    {
        return 0;
    }
	@Override
	public TFCBiomeGenNother setBiomeName(String par1Str)
	{
		this.biomeName = par1Str;
		return this;
	}
	
	public TFCBiomeGenNother setBiomeColor(int c)
	{
		biomeColor = c;
		return this;
	}
	
	public int getBiomeColor()
	{
		return biomeColor;
	}
	
	/**
	 * Allocate a new BiomeDecorator for this BiomeGenBase
	 */
	@Override
	public BiomeDecoratorTFCNother createBiomeDecorator()
	{
		return new BiomeDecoratorTFCNother();
	}
	
	/* 
	* return the biome specified by biomeID, or 0 (ocean) if out of bounds
	*/
	public static TFCBiomeGenNother getBiome(int id)
	{
		if(biomeList[id] == null)
		{
			TerraFirmaCraft.LOG.warn("Biome ID is null: " + id);
		}
		if (id >= 0 && id <= biomeList.length && biomeList[id] != null)
		{
			return biomeList[id];
		}
		else
		{
			TerraFirmaCraft.LOG.warn("Biome ID is out of bounds: " + id + ", defaulting to NOTHER");
			return NOTHER;
		}
	}



}
