package com.bioxx.tfc.WorldGen;

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
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;



public class TFCBiomeGenHell extends TFCBiome {

	@SuppressWarnings("unchecked")
	public TFCBiomeGenHell(int par1) {
		super(par1);
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityGhastTFC.class, 50, 4, 4));
        this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityPigZombieTFC.class, 100, 4, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySpiderTFC.class, 5, 1, 1));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityZombieTFC.class, 10, 2, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySkeletonTFC.class, 8, 1, 1));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityCreeperTFC.class, 3, 1, 2));
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySlimeTFC.class, 8, 1, 2));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityEndermanTFC.class, 1, 1, 2));
        
		
	}
	/**
     * takes temperature, returns color
     */
    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float p_76731_1_)
    {
        return 0;
    }



}
