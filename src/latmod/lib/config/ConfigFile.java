package latmod.lib.config;

import latmod.lib.*;
import latmod.lib.util.IDObject;

import java.io.File;

public class ConfigFile extends IDObject implements IConfigFile
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
	
	public final ConfigGroup getGroup()
	{ return configGroup; }
	
	public final void add(ConfigEntry e)
	{ if(e != null) configGroup.add(e, false); }
	
	public void load()
	{
		ConfigGroup g = (ConfigGroup) LMJsonUtils.fromJsonFile(file, ConfigGroup.class);
		if(g != null) configGroup.loadFromGroup(g);
		save();
	}
	
	public void save()
	{
		try { LMFileUtils.save(file, toJsonString(true)); }
		catch(Exception e) { e.printStackTrace(); }
	}
	
	public final String toJsonString(boolean pretty)
	{ return LMJsonUtils.toJson(LMJsonUtils.getGson(pretty), configGroup); }
}