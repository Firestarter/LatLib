package latmod.lib.config;

import latmod.lib.PrimitiveType;
import latmod.lib.util.*;

public class ConfigEntryInfo
{
	public final ConfigEntry entry;
	public String info = null;
	public String def = null;
	public String min = null;
	public String max = null;
	
	public ConfigEntryInfo(ConfigEntry e, String s)
	{
		entry = e;
		info = s;
		
		if(e instanceof ConfigEntryInt)
		{
			IntBounds b = ((ConfigEntryInt)e).bounds;
			def = Integer.toString(b.defValue);
			min = Integer.toString(b.minValue);
			max = Integer.toString(b.maxValue);
		}
		else if(e instanceof ConfigEntryFloat)
		{
			FloatBounds b = ((ConfigEntryFloat)e).bounds;
			def = Float.toString(b.defValue);
			min = Float.toString(b.minValue);
			max = Float.toString(b.maxValue);
		}
		else if(!PrimitiveType.isNull(e.type)) def = String.valueOf(e.getJson());
	}
}