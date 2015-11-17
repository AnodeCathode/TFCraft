package com.bioxx.tfc.WorldGen.Nother.Structure;


import com.bioxx.tfc.Entities.Mobs.EntityBlazeTFC;
import com.bioxx.tfc.Entities.Mobs.EntityPigZombieTFC;
import com.bioxx.tfc.Entities.Mobs.EntitySkeletonTFC;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.gen.structure.MapGenNetherBridge;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



/**
 * Created by Mike on 11/1/2015.
 */
public class MapGenFortressTFC extends MapGenStructure {
    private List spawnList = new ArrayList();

    public MapGenFortressTFC()
    {

        this.spawnList.add(new SpawnListEntry(EntityBlazeTFC.class, 7, 2, 3));
        this.spawnList.add(new SpawnListEntry(EntityPigZombieTFC.class, 5, 4, 4));
        this.spawnList.add(new SpawnListEntry(EntitySkeletonTFC.class, 10, 4, 4));

    }
public String func_143025_a()
    {
        return "NotherFortress";
    }

    public List getSpawnList()
    {
        return this.spawnList;
    }

    protected boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_)
    {
        int k = p_75047_1_ >> 4;
        int l = p_75047_2_ >> 4;
        this.rand.setSeed((long)(k ^ l << 4) ^ this.worldObj.getSeed());
        this.rand.nextInt();
        return this.rand.nextInt(3) != 0 ? false : (p_75047_1_ != (k << 4) + 4 + this.rand.nextInt(8) ? false : p_75047_2_ == (l << 4) + 4 + this.rand.nextInt(8));

    }

    protected StructureStart getStructureStart(int p_75049_1_, int p_75049_2_)
    {
        return new MapGenFortressTFC.Start(this.worldObj, this.rand, p_75049_1_, p_75049_2_);
    }

    public static class Start extends StructureStart
        {

            public Start() {}

            public Start(World p_i2040_1_, Random rand, int p_i2040_3_, int p_i2040_4_)
            {
                super(p_i2040_3_, p_i2040_4_);
                StructureFortressPieces.Start start = new StructureFortressPieces.Start(rand, (p_i2040_3_ << 4) + 2, (p_i2040_4_ << 4) + 2);
                this.components.add(start);
                start.buildComponent(start, this.components, rand);
                ArrayList arraylist = start.field_74967_d;

                while (!arraylist.isEmpty())
                {
                    int k = rand.nextInt(arraylist.size());
                    StructureComponent structurecomponent = (StructureComponent)arraylist.remove(k);
                    structurecomponent.buildComponent(start, this.components, rand);
                }

                this.updateBoundingBox();
                this.setRandomHeight(p_i2040_1_, rand, 148, 190);
            }
        }


}
