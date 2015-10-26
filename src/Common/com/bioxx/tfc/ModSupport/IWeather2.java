package com.bioxx.tfc.ModSupport;

import net.minecraft.world.World;

public interface IWeather2 {
	public void load();
	public boolean  isRainingOnCoord(World worldObj, int xCoord, int yCoord, int zCoord);
}
