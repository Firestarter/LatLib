package latmod.lib.config;

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
}