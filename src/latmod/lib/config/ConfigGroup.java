package latmod.lib.config;

import java.lang.reflect.*;
import java.util.Map;

import com.google.gson.*;

import latmod.lib.*;
import latmod.lib.util.FinalIDObject;

public final class ConfigGroup extends FinalIDObject implements Cloneable
{
	public final FastList<ConfigEntry> entries;
	private String displayName = null;
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
	
	public ConfigGroup addAll(Class<?> c)
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
		
		return this;
	}
	
	public ConfigGroup setName(String s)
	{ displayName = s; return this; }
	
	public String getDisplayName()
	{ return displayName == null ? LMStringUtils.firstUppercase(ID) : displayName; }
	
	public String getFullID()
	{
		if(!isValid()) return null;
		StringBuilder sb = new StringBuilder();
		sb.append(parentList.ID);
		sb.append('.');
		sb.append(ID);
		return sb.toString();
	}
	
	public boolean isValid()
	{ return ID != null && parentList != null && parentList.ID != null; }
	
	public ConfigGroup clone()
	{
		ConfigGroup g = new ConfigGroup(ID);
		g.displayName = displayName;
		g.entries.addAll(entries);
		return g;
	}
	
	public static class Serializer implements JsonSerializer<ConfigGroup>, JsonDeserializer<ConfigGroup>
	{
		public JsonElement serialize(ConfigGroup src, Type typeOfSrc, JsonSerializationContext context)
		{
			if(src == null) return null;
			
			JsonObject o = new JsonObject();
			
			for(ConfigEntry e : src.entries)
			{
				if(!e.isExcluded())
				{
					e.onPreLoaded();
					o.add(e.ID, context.serialize(e.getJson()));
				}
			}
			
			return o;
		}
		
		public ConfigGroup deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			if(json.isJsonNull()) return null;
			ConfigGroup g = new ConfigGroup("");
			
			JsonObject o = json.getAsJsonObject();
			
			for(Map.Entry<String, JsonElement> e : o.entrySet())
			{
				ConfigEntry entry = new ConfigEntryJsonElement(e.getKey());
				
				if(!e.getValue().isJsonNull())
				{
					entry.setJson(e.getValue());
					entry.onPostLoaded();
				}
				
				g.add(entry);
			}
			
			return g;
		}
	}
}