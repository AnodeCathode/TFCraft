package com.bioxx.tfc.ModSupport;

import cpw.mods.fml.common.Optional.Method;
import weather2.ServerTickHandler;
import weather2.weathersystem.WeatherManagerServer;
import weather2.weathersystem.storm.StormObject;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class Weather2 implements IWeather2
{

	public Weather2() {
		
		
		
	}

	
	@Method(modid="weather2")
	public boolean isRainingOnCoord(World worldObj, int xCoord, int yCoord, int zCoord) 
	{
		int dim = worldObj.provider.dimensionId;
		WeatherManagerServer wms = ServerTickHandler.lookupDimToWeatherMan.get(dim);
		Vec3 startVec3 = Vec3.createVectorHelper(xCoord, yCoord, zCoord);
		StormObject storm = wms.getClosestStorm(startVec3, 300, -1, true);
		
		if (storm !=null && storm.levelWater > storm.levelWaterStartRaining) 
		{
			double radius = (double) storm.size / 2;
			Vec3 location = storm.pos;
			if (startVec3.distanceTo(location) < radius)
			{
				return true;				
			}
		}
		return false;	
	}



}
