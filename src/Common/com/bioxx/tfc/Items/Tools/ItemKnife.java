package com.bioxx.tfc.Items.Tools;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bioxx.tfc.TFCBlocks;
import com.bioxx.tfc.TFCItems;
import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.TileEntities.TEFoodPrep;
import com.bioxx.tfc.api.Enums.EnumDamageType;
import com.bioxx.tfc.api.Enums.EnumItemReach;
import com.bioxx.tfc.api.Enums.EnumSize;
import com.bioxx.tfc.api.Tools.IKnife;

public class ItemKnife extends ItemWeapon implements IKnife
{
	public ItemKnife(ToolMaterial e, float damage)
	{
		super(e, damage);
		this.setMaxDamage(e.getMaxUses());
		this.damageType = EnumDamageType.PIERCING;
	}

	@Override
	public EnumSize getSize(ItemStack is)
	{
		return EnumSize.SMALL;
	}

	@Override
	public boolean canStack()
	{
		return false;
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float HitX, float HitY, float HitZ)
	{
		Block id = world.getBlock(x, y, z);
		if(!world.isRemote && id != TFCBlocks.ToolRack && id != TFCBlocks.ToolRack2)
		{
			int hasBowl = -1;

			for(int i = 0; i < 36 && hasBowl == -1;i++)
			{
				if(entityplayer.inventory.mainInventory[i] != null && entityplayer.inventory.mainInventory[i].getItem() == TFCItems.PotteryBowl && entityplayer.inventory.mainInventory[i].getItemDamage() == 1)
					hasBowl = i;
			}

			Material mat = world.getBlock(x, y, z).getMaterial();

			if(side == 1 && id.isSideSolid(world, x, y, z, ForgeDirection.UP) &&!TFC_Core.isSoil(id) && !TFC_Core.isWater(id) && world.isAirBlock(x, y + 1, z) && hasBowl != -1 &&
					(mat == Material.wood || mat == Material.rock || mat == Material.iron))
			{
				world.setBlock(x, y + 1, z, TFCBlocks.FoodPrep);
				TEFoodPrep te = (TEFoodPrep) world.getTileEntity(x, y + 1, z);
				if(te != null)
				{
					te.setInventorySlotContents(7, entityplayer.inventory.mainInventory[hasBowl]);
					entityplayer.inventory.mainInventory[hasBowl] = null;
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void addExtraInformation(ItemStack is, EntityPlayer player, List arraylist)
	{
		if (TFC_Core.showShiftInformation()) 
		{
			arraylist.add(StatCollector.translateToLocal("gui.Help"));
			arraylist.add(StatCollector.translateToLocal("gui.Knife.Inst0"));
			arraylist.add(StatCollector.translateToLocal("gui.Knife.Inst1"));
			arraylist.add(StatCollector.translateToLocal("gui.Knife.Inst2"));
		}
		else
		{
			arraylist.add(StatCollector.translateToLocal("gui.ShowHelp"));
		}
	}

	@Override
	public EnumItemReach getReach(ItemStack is){
		return EnumItemReach.SHORT;
	}

}