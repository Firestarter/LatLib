package latmod.lib.config;

import com.google.gson.*;
import latmod.lib.*;

import java.lang.reflect.*;
import java.util.*;

public final class ConfigGroup extends ConfigEntry
{
	private final FastMap<String, ConfigEntry> entries;
	private String displayName = null;
	public IConfigFile parentFile = null;
	
	public ConfigGroup(String s)
	{
		super(s, PrimitiveType.MAP);
		entries = new FastMap<String, ConfigEntry>();
	}

	public Collection<ConfigEntry> entries()
	{ return entries.values(); }

	public FastMap<String, ConfigEntry> entryMap()
	{ return entries; }
	
	public IConfigFile getParentFile()
	{
		if(parentFile != null) return parentFile;
		else if(parentGroup != null) return parentGroup.getParentFile();
		else return null;
	}
	
	public void add(ConfigEntry e)
	{
		if(e != null)
		{
			entries.put(e.ID, e);
			e.parentGroup = this;
		}
	}

	public ConfigGroup addAll(Class<?> c)
	{ return addAll(c, null); }
	
	public ConfigGroup addAll(Class<?> c, Object obj)
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
						ConfigEntry entry = (ConfigEntry)f[i].get(obj);
						if(entry != null && entry != this) add(entry);
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
	
	public ConfigGroup clone()
	{ return clone(ID, true); }
	
	public ConfigGroup clone(String id, boolean newEntries)
	{
		ConfigGroup g = new ConfigGroup((id == null || id.isEmpty()) ? ID : id);
		g.displayName = displayName;
		for(int i = 0; i < entries.size(); i++)
			g.add(newEntries ? entries.get(i).clone() : entries.get(i));
		return g;
	}
	
	public final void setJson(JsonElement o0)
	{
		if(o0 == null || !o0.isJsonObject()) return;
		
		entries.clear();
		
		JsonObject o = o0.getAsJsonObject();
		
		for(Map.Entry<String, JsonElement> e : o.entrySet())
		{
			ConfigEntry entry = new ConfigEntryJsonElement(e.getKey());
			
			if(!e.getValue().isJsonNull())
			{
				entry.setJson(e.getValue());
				entry.onPostLoaded();
			}
			
			add(entry);
		}
	}
	
	public final JsonElement getJson()
	{
		JsonObject o = new JsonObject();
		
		for(ConfigEntry e : entries.values())
		{
			if(!e.isExcluded())
			{
				e.onPreLoaded();
				o.add(e.ID, e.getJson());
			}
		}
		
		return o;
	}
	
	public String getAsString()
	{ return ">"; }
	
	public void write(ByteIOStream io)
	{
		io.writeShort(entries.size());
		for(ConfigEntry e : entries.values())
		{
			e.onPreLoaded();
			io.writeByte(e.type.ordinal());
			io.writeUTF(e.ID);
			e.write(io);
		}
	}
	
	public void read(ByteIOStream io)
	{
		int s = io.readUnsignedShort();
		entries.clear();
		for(int i = 0; i < s; i++)
		{
			int type = io.readUnsignedByte();
			String id = io.readUTF();
			ConfigEntry e = ConfigEntry.getEntry(PrimitiveType.VALUES[type], id);
			e.read(io);
			add(e);
		}
	}
	
	public void writeExtended(ByteIOStream io)
	{
		io.writeUTF(displayName);
		io.writeShort(entries.size());
		for(ConfigEntry e : entries.values())
		{
			e.onPreLoaded();
			io.writeByte(e.type.ordinal());
			io.writeUTF(e.ID);
			e.writeExtended(io);
			io.writeUTF(e.info);
			io.writeUTF(e.defaultValue);
		}
	}
	
	public void readExtended(ByteIOStream io)
	{
		displayName = io.readUTF();
		int s = io.readUnsignedShort();
		entries.clear();
		for(int i = 0; i < s; i++)
		{
			int type = io.readUnsignedByte();
			String id = io.readUTF();
			ConfigEntry e = ConfigEntry.getEntry(PrimitiveType.VALUES[type], id);
			e.readExtended(io);
			e.info = io.readUTF();
			e.defaultValue = io.readUTF();
			add(e);
		}
	}
	
	public static class Serializer implements JsonSerializer<ConfigGroup>, JsonDeserializer<ConfigGroup>
	{
		public JsonElement serialize(ConfigGroup src, Type typeOfSrc, JsonSerializationContext context)
		{
			if(src == null) return null;
			return src.getJson();
		}
		
		public ConfigGroup deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			if(json.isJsonNull() || !json.isJsonObject()) return null;
			ConfigGroup g = new ConfigGroup("");
			g.setJson(json.getAsJsonObject());
			return g;
		}
	}
	
	public void sort()
	{
		entries.sort();
		
		for(ConfigEntry e : entries.values())
		{
			ConfigGroup g = e.getAsGroup();
			if(g != null) g.sort();
		}
	}
	
	public int loadFromGroup(ConfigGroup l)
	{
		if(l == null || l.entries.isEmpty()) return 0;
		
		int result = 0;
		
		for(ConfigEntry e1 : l.entries.values())
		{
			ConfigEntry e0 = entries.get(e1.ID);
			
			if(e0 != null)
			{
				if(e0.getAsGroup() != null)
				{
					ConfigGroup g1 = new ConfigGroup(e1.ID);
					g1.setJson(e1.getJson());
					result += e0.getAsGroup().loadFromGroup(g1);
				}
				else
				{
					try
					{
						//System.out.println("Value " + e1.getFullID() + " set from " + e0.getJson() + " to " + e1.getJson());
						e0.setJson(e1.getJson());
						e0.onPostLoaded();
						result++;
					}
					catch(Exception ex)
					{
						System.err.println("Can't set value " + e1.getJson() + " for '" + e0.parentGroup.ID + "." + e0.ID + "' (type:" + e0.type + ")");
						System.err.println(ex.toString());
					}
				}
			}
		}
		
		return result;
	}
	
	public boolean hasKey(Object key)
	{ return entries.containsKey(LMUtils.getID(key)); }
	
	public ConfigEntry getEntry(Object key)
	{ return entries.get(LMUtils.getID(key)); }
	
	public ConfigGroup getGroup(Object key)
	{
		ConfigEntry e = getEntry(key);
		return (e == null) ? null : e.getAsGroup();
	}
	
	public FastList<ConfigGroup> getGroups()
	{
		FastList<ConfigGroup> list = new FastList<ConfigGroup>();
		for(ConfigEntry e : entries)
		{
			ConfigGroup g = e.getAsGroup();
			if(g != null) list.add(g);
		}
		return list;
	}
	
	public ConfigGroup getAsGroup()
	{ return this; }
	
	public int getTotalEntryCount()
	{
		int count = 0;
		
		for(int i = 0; i < entries.size(); i++)
		{
			ConfigGroup g = entries.get(i).getAsGroup();
			if(g == null) count++;
			else count += g.getTotalEntryCount();
		}
		
		return count;
	}
	
	public int getDepth()
	{ return (parentGroup == null) ? 0 : (parentGroup.getDepth() + 1); }
}