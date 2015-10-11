package latmod.lib.config;

import java.io.File;

import latmod.lib.*;
import latmod.lib.util.IDObject;

public final class ConfigFile extends IDObject
{
	public final File file;
	public final ConfigList configList;
	public final boolean canEdit;
	public ConfigFileLoader loader;
	
	public ConfigFile(String id, File f, boolean edit)
	{
		super(id);
		configList = new ConfigList();
		configList.setID(id);
		configList.groups = new FastList<ConfigGroup>();
		configList.parentFile = this;
		file = LMFileUtils.newFile(f);
		canEdit = edit;
		loader = new ConfigFileLoader();
	}
	
	public String toString()
	{ return configList.toString(); }
	
	public void add(ConfigGroup g)
	{ configList.add(g); }
	
	public void load()
	{ loader.load(this); }
	
	public void save()
	{ loader.save(this); }
	
	public String toJsonString(boolean pretty)
	{
		configList.sort();
		LMJsonUtils.setPretty(pretty);
		String s = LMJsonUtils.toJson(configList);
		LMJsonUtils.setPretty(false);
		return s;
	}
	
	public ConfigFile clone()
	{
		ConfigFile f = new ConfigFile(toString(), file, canEdit);
		f.configList.groups = configList.groups.clone();
		return f;
	}
}