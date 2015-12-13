package latmod.lib.config;

import java.io.File;

import latmod.lib.*;
import latmod.lib.util.IDObject;

public final class ConfigFile extends IDObject implements IConfigFile
{
	public final File file;
	public final ConfigGroup configGroup;
	
	public ConfigFile(String id, File f)
	{
		super(id);
		configGroup = new ConfigGroup(id);
		configGroup.parentFile = this;
		file = LMFileUtils.newFile(f);
	}
	
	public ConfigGroup getGroup()
	{ return configGroup; }
	
	public void add(ConfigGroup g)
	{ configGroup.add(g); }
	
	public void load()
	{
		ConfigGroup g = (ConfigGroup)LMJsonUtils.fromJsonFile(file, ConfigGroup.class);
		if(g != null) configGroup.loadFromGroup(g);
		save();
	}
	
	public void save()
	{
		try { LMFileUtils.save(file, toJsonString(true)); }
		catch(Exception e) { e.printStackTrace(); }
	}
	
	public final String toJsonString(boolean pretty)
	{
		configGroup.sort(null);
		String s = LMJsonUtils.toJson(LMJsonUtils.getGson(pretty), configGroup);
		return s;
	}
}