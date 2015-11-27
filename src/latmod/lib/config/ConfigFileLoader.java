package latmod.lib.config;

import latmod.lib.*;

public class ConfigFileLoader
{
	public void load(ConfigFile f)
	{
		ConfigList list = (ConfigList)LMJsonUtils.fromJsonFile(f.file, ConfigList.class);
		list.setID(f.ID);
		if(f.configList.loadFromList(list)) save(f);
	}
	
	public void save(ConfigFile f)
	{
		try { LMFileUtils.save(f.file, f.toJsonString(true)); }
		catch(Exception e) { e.printStackTrace(); }
	}
}