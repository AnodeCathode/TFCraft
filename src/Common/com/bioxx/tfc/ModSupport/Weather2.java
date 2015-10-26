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
	
	public void load(){
		
	}
	
	@Method(modid="weather2")
	public boolean isRainingOnCoord(World worldObj, int xCoord, int yCoord, int zCoord) 
	{
		int dim = worldObj.provider.dimensionId;
		WeatherManagerServer wms = ServerTickHandler.lookupDimToWeatherMan.get(dim);
	
		StormObject storm = wms.getClosestStorm(Vec3.createVectorHelper(xCoord, StormObject.layers.get(0), zCoord), 64, -1, true);
		if (storm !=null) 
		{
			return true;

		}
		return false;	
	}

}
