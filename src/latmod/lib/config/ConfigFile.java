package latmod.lib.config;

import java.io.File;

import latmod.lib.*;
import latmod.lib.util.IDObject;

public final class ConfigFile extends IDObject implements IConfigFile
{
	public final File file;
	public final ConfigList configList;
	
	public ConfigFile(String id, File f)
	{
		super(id);
		configList = new ConfigList();
		configList.setID(id);
		configList.groups = new FastList<ConfigGroup>();
		configList.parentFile = this;
		file = LMFileUtils.newFile(f);
	}
	
	public ConfigList getList()
	{ return configList; }
	
	public void add(ConfigGroup g)
	{ configList.add(g); }
	
	public void load()
	{
		ConfigList list = (ConfigList)LMJsonUtils.fromJsonFile(file, ConfigList.class);
		if(list != null)
		{
			list.setID(ID);
			if(configList.loadFromList(list)) save();
		}
	}
	
	public void save()
	{
		try { LMFileUtils.save(file, toJsonString(true)); }
		catch(Exception e) { e.printStackTrace(); }
	}
	
	public final String toJsonString(boolean pretty)
	{
		configList.sort();
		String s = LMJsonUtils.toJson(LMJsonUtils.getGson(pretty), configList);
		return s;
	}
}