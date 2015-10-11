package latmod.lib.config;

import latmod.lib.*;

public class ConfigFileLoader
{
	public void load(ConfigFile f)
	{
		f.configList.loadFromList((ConfigList)LMJsonUtils.fromJsonFile(f.file, ConfigList.class));
		save(f);
	}
	
	public void save(ConfigFile f)
	{
		try { LMFileUtils.save(f.file, f.toJsonString(true)); }
		catch(Exception e) { e.printStackTrace(); }
	}
}