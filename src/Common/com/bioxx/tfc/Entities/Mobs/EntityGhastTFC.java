package com.bioxx.tfc.Entities.Mobs;

import com.bioxx.tfc.Entities.EntityLargeFireballTFC;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import com.bioxx.tfc.Core.TFC_MobData;


public class EntityGhastTFC extends EntityGhast
{
	/** The explosion radius of spawned fireballs. */
	private int explosionStrength = 4;
	private Entity targetedEntity;
	/** Cooldown time between target loss and new target aquirement. */
	private int aggroCooldown;


	public EntityGhastTFC(World par1World)
	{
		super(par1World);

	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(TFC_MobData.GHAST_HEALTH);//MaxHealth
	}
	protected void updateEntityActionState()
	{
		if (!this.worldObj.isRemote && this.worldObj.difficultySetting == EnumDifficulty.PEACEFUL)
		{
			this.setDead();
		}

		this.despawnEntity();
		this.prevAttackCounter = this.attackCounter;
		double d0 = this.waypointX - this.posX;
		double d1 = this.waypointY - this.posY;
		double d2 = this.waypointZ - this.posZ;
		double d3 = d0 * d0 + d1 * d1 + d2 * d2;

		if (d3 < 1.0D || d3 > 3600.0D)
		{
			this.waypointX = this.posX + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
			this.waypointY = this.posY + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
			this.waypointZ = this.posZ + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
		}

		if (this.courseChangeCooldown-- <= 0)
		{
			this.courseChangeCooldown += this.rand.nextInt(5) + 2;
			d3 = (double) MathHelper.sqrt_double(d3);

			if (this.isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, d3))
			{
				this.motionX += d0 / d3 * 0.1D;
				this.motionY += d1 / d3 * 0.1D;
				this.motionZ += d2 / d3 * 0.1D;
			}
			else
			{
				this.waypointX = this.posX;
				this.waypointY = this.posY;
				this.waypointZ = this.posZ;
			}
		}

		if (this.targetedEntity != null && this.targetedEntity.isDead)
		{
			this.targetedEntity = null;
		}

		if (this.targetedEntity == null || this.aggroCooldown-- <= 0)
		{
			this.targetedEntity = this.worldObj.getClosestVulnerablePlayerToEntity(this, 100.0D);

			if (this.targetedEntity != null)
			{
				this.aggroCooldown = 20;
			}
		}

		double d4 = 64.0D;

		if (this.targetedEntity != null && this.targetedEntity.getDistanceSqToEntity(this) < d4 * d4)
		{
			double d5 = this.targetedEntity.posX - this.posX;
			double d6 = this.targetedEntity.boundingBox.minY + (double)(this.targetedEntity.height / 2.0F) - (this.posY + (double)(this.height / 2.0F));
			double d7 = this.targetedEntity.posZ - this.posZ;
			this.renderYawOffset = this.rotationYaw = -((float)Math.atan2(d5, d7)) * 180.0F / (float)Math.PI;

			if (this.canEntityBeSeen(this.targetedEntity))
			{
				if (this.attackCounter == 10)
				{
					this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1007, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
				}

				++this.attackCounter;

				if (this.attackCounter == 20)
				{
					this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1008, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
					EntityLargeFireballTFC entitylargefireball = new EntityLargeFireballTFC(this.worldObj, this, d5, d6, d7) ;
					entitylargefireball.field_92057_e = this.explosionStrength;
					double d8 = 4.0D;
					Vec3 vec3 = this.getLook(1.0F);
					entitylargefireball.posX = this.posX + vec3.xCoord * d8;
					entitylargefireball.posY = this.posY + (double)(this.height / 2.0F) + 0.5D;
					entitylargefireball.posZ = this.posZ + vec3.zCoord * d8;
					this.worldObj.spawnEntityInWorld(entitylargefireball);
					this.attackCounter = -40;
				}
			}
			else if (this.attackCounter > 0)
			{
				--this.attackCounter;
			}
		}
		else
		{
			this.renderYawOffset = this.rotationYaw = -((float)Math.atan2(this.motionX, this.motionZ)) * 180.0F / (float)Math.PI;

			if (this.attackCounter > 0)
			{
				--this.attackCounter;
			}
		}

		if (!this.worldObj.isRemote)
		{
			byte b1 = this.dataWatcher.getWatchableObjectByte(16);
			byte b0 = (byte)(this.attackCounter > 10 ? 1 : 0);

			if (b1 != b0)
			{
				this.dataWatcher.updateObject(16, Byte.valueOf(b0));
			}
		}
	}
	/**
	 * True if the ghast has an unobstructed line of travel to the waypoint.
	 */
	private boolean isCourseTraversable(double p_70790_1_, double p_70790_3_, double p_70790_5_, double p_70790_7_)
	{
		double d4 = (this.waypointX - this.posX) / p_70790_7_;
		double d5 = (this.waypointY - this.posY) / p_70790_7_;
		double d6 = (this.waypointZ - this.posZ) / p_70790_7_;
		AxisAlignedBB axisalignedbb = this.boundingBox.copy();

		for (int i = 1; (double)i < p_70790_7_; ++i)
		{
			axisalignedbb.offset(d4, d5, d6);

			if (!this.worldObj.getCollidingBoundingBoxes(this, axisalignedbb).isEmpty())
			{
				return false;
			}
		}

		return true;
	}

}
