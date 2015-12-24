package latmod.lib.config;

import latmod.lib.*;
import latmod.lib.util.IDObject;

import java.io.File;

public final class ConfigFile extends IDObject implements IConfigFile
{
	public final File file;
	public final ConfigGroup configGroup;
	
	public ConfigFile(ConfigGroup group, File f)
	{
		super(group.ID);
		configGroup = group;
		configGroup.parentFile = this;
		file = LMFileUtils.newFile(f);
	}
	
	public ConfigFile(String id, File f)
	{ this(new ConfigGroup(id), f); }
	
	public ConfigGroup getGroup()
	{ return configGroup; }
	
	public void add(ConfigEntry e)
	{ if(e != null) configGroup.add(e); }
	
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