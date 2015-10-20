package com.bioxx.tfc.Items.Tools;

import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.bioxx.tfc.Core.TFC_Achievements;
import com.bioxx.tfc.TileEntities.TEAnvil;
import com.bioxx.tfc.api.TFCBlocks;
import com.bioxx.tfc.api.Crafting.AnvilManager;
import com.bioxx.tfc.api.Enums.EnumDamageType;
import com.bioxx.tfc.api.Enums.EnumItemReach;
import com.bioxx.tfc.api.Enums.EnumSize;
import com.bioxx.tfc.api.Interfaces.ICausesDamage;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

public class ItemHammer extends ItemTerraTool implements ICausesDamage
{
	private static final Set<Block> BLOCKS = Sets.newHashSet(new Block[] {});
	private float damageVsEntity;

	public ItemHammer(ToolMaterial e, float damage)
	{
		super(0, e, BLOCKS);
		this.damageVsEntity = damage;
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		Block id2 = player.worldObj.getBlock(x, y, z);
		int meta2 = player.worldObj.getBlockMetadata(x, y, z);

		if(id2 == TFCBlocks.stoneIgEx || id2 == TFCBlocks.stoneIgIn)
		{
			if(side == 1)
			{
				world.setBlock(x, y, z, TFCBlocks.anvil);
				player.triggerAchievement(TFC_Achievements.achAnvil);
				TEAnvil te = (TEAnvil) world.getTileEntity(x, y, z);
				if(te == null)
					world.setTileEntity(x, y, z, new TEAnvil());
				if(te != null)
				{
					te.stonePair[0] = Block.getIdFromBlock(id2);
					te.stonePair[1] = meta2;
					te.validate();
				}
				//world.markBlockForUpdate(x, y, z);
				return true;
			}
		}
		return false;
	}

	@Override
	public EnumSize getSize(ItemStack is)
	{
		return EnumSize.SMALL;
	}

	@Override
	public EnumDamageType getDamageType()
	{
		return EnumDamageType.CRUSHING;
	}

	@Override
	public Multimap getAttributeModifiers(ItemStack is)
	{
		Multimap multimap = HashMultimap.create();
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Tool modifier", getWeaponDamage(is), 0));
		return multimap;
	}

	public double getWeaponDamage(ItemStack is)
	{
		return Math.floor(damageVsEntity + (damageVsEntity * AnvilManager.getDamageBuff(is)));
	}

	@Override
	public int getMaxDamage(ItemStack is)
	{
		return (int) Math.floor(getMaxDamage() + (getMaxDamage() * AnvilManager.getDurabilityBuff(is)));
	}

	@Override
	public EnumItemReach getReach(ItemStack is)
	{
		return EnumItemReach.MEDIUM;
	}
	
    @Override
	public boolean canHarvestBlock(Block block, ItemStack itemStack)
    {
        return checkBlock(block);
    }
	
    public boolean onBlockStartBreak(final ItemStack itemstack, final int x, final int y, final int z, final EntityPlayer player) {
        if (this.checkBlock(player.worldObj.getBlock(x, y, z)))
        {
	        if (this.checkNeighbours(player.worldObj, x, y, z) || player.capabilities.isCreativeMode) {
	            return false;
        }
	       player.worldObj.markBlockForUpdate(x, y, z);
           return true;
        }
        return false;

    }
    @Override
    public float getDigSpeed(final ItemStack stack, final Block block, final int meta) {
        float digSpeed = super.getDigSpeed(stack, block, meta);
        if (this.checkBlock(block)) {
            digSpeed += stack.getMaxDamage() / 2;
        }
        return digSpeed;
    }
    
	private boolean checkNeighbours(final World world, final int x, final int y, final int z) {
        return world.getBlock(x, y, z).getUnlocalizedName().toLowerCase().contains("glass") || 
        		(this.checkBlock(world.getBlock(x + 1, y, z)) && 
				this.checkBlock(world.getBlock(x - 1, y, z)) && 
				this.checkBlock(world.getBlock(x, y + 1, z)) && 
				this.checkBlock(world.getBlock(x, y - 1, z)) && 
				this.checkBlock(world.getBlock(x, y, z + 1)) && 
				this.checkBlock(world.getBlock(x, y, z - 1)));
	}
    
    private boolean checkBlock(final Block block) {
        final String checkBlockName = block.getUnlocalizedName().toLowerCase();
        return checkBlockName.contains("brick") || checkBlockName.contains("smooth") || checkBlockName.contains("glass") || block.getMaterial() == Material.air;
    }
}
