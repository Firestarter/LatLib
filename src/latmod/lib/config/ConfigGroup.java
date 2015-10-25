package latmod.lib.config;


import java.lang.reflect.Field;

import latmod.lib.FastList;
import latmod.lib.util.FinalIDObject;

public final class ConfigGroup extends FinalIDObject
{
	public final FastList<ConfigEntry> entries;
	
	public ConfigList parentList = null;
	
	public ConfigGroup(String s)
	{
		super(s);
		entries = new FastList<ConfigEntry>();
	}
	
	public void add(ConfigEntry e)
	{
		if(e != null && !entries.contains(e))
		{ entries.add(e); e.parentGroup = this; }
	}

	public void addAll(Class<?> c)
	{
		try
		{
			Field f[] = c.getDeclaredFields();
			
			if(f != null && f.length > 0)
			for(int i = 0; i < f.length; i++)
			{
				try
				{
					f[i].setAccessible(true);
					if(ConfigEntry.class.isAssignableFrom(f[i].getType()))
					{
						ConfigEntry entry = (ConfigEntry)f[i].get(null);
						if(entry != null) add(entry);
					}
				}
				catch(Exception e1) { }
			}
		}
		catch(Exception e)
		{ e.printStackTrace(); }
	}
}