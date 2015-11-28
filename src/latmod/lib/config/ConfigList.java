package latmod.lib.config;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.*;

import latmod.lib.*;
import latmod.lib.util.IDObject;

public final class ConfigList extends IDObject implements Cloneable
{
	public FastList<ConfigGroup> groups;
	public IConfigFile parentFile = null;
	private String displayName = null;
	
	public ConfigList()
	{ super(null); }
	
	public void add(ConfigGroup g)
	{
		if(g != null && !groups.contains(g))
		{ groups.add(g); g.parentList = this; }
	}
	
	public boolean loadFromList(ConfigList l)
	{
		if(l == null || l.groups.isEmpty()) return false;
		
		boolean result = false;
		
		if(l != null && l.groups != null && !l.groups.isEmpty())
		{
			for(int i = 0; i < l.groups.size(); i++)
			{
				ConfigGroup g1 = l.groups.get(i);
				ConfigGroup g0 = groups.getObj(g1);
				
				if(g0 != null)
				{
					for(int j = 0; j < g1.entries.size(); j++)
					{
						ConfigEntry e1 = g1.entries.get(j);
						ConfigEntry e0 = g0.entries.getObj(e1);
						if(e0 != null && !e0.isExcluded())
						{
							try
							{
								//System.out.println("Value " + e1.getFullID() + " set from " + e0.getJson() + " to " + e1.getJson());
								e0.setJson(e1.getJson());
								e0.onPostLoaded();
								result = true;
							}
							catch(Exception ex)
							{
								System.err.println("Can't set value " + e1.getJson() + " for '" + e0.parentGroup.toString() + "." + e0.toString() + "' (type:" + e0.type + ")");
								System.err.println(ex.toString());
							}
						}
					}
				}
			}
		}
		
		return result;
	}
	
	public int writeToIO(ByteIOStream io, boolean extended)
	{
		int count = 0;
		if(extended) io.writeString(displayName);
		io.writeUShort(groups.size());
		
		for(int i = 0; i < groups.size(); i++)
		{
			ConfigGroup g = groups.get(i);
			io.writeString(g.toString());
			io.writeUShort(g.entries.size());
			
			for(int j = 0; j < g.entries.size(); j++)
			{
				ConfigEntry e = g.entries.get(j);
				e.onPreLoaded();
				io.writeString(e.toString());
				io.writeUByte(e.type.ordinal());
				if(extended)
				{
					e.writeExtended(io);
					io.writeString((e.info != null && e.info.info != null) ? e.info.info : null);
				}
				else e.write(io);
				count++;
			}
		}
		
		return count;
	}
	
	public static ConfigList readFromIO(ByteIOStream io, boolean extended)
	{
		ConfigList list = new ConfigList();
		list.groups = new FastList<ConfigGroup>();
		
		if(extended) list.displayName = io.readString();
		int gs = io.readUShort();
		
		for(int i = 0; i < gs; i++)
		{
			String gid = io.readString();
			int es = io.readUShort();
			
			ConfigGroup gr = new ConfigGroup(gid);
			
			for(int j = 0; j < es; j++)
			{
				String eid = io.readString();
				PrimitiveType type = PrimitiveType.VALUES[io.readUByte()];
				ConfigEntry e = ConfigEntry.getEntry(type, eid);
				
				if(e != null)
				{
					if(extended)
					{
						e.readExtended(io);
						e.setInfo(io.readString());
					}
					else e.read(io);
					gr.add(e);
				}
			}
			
			list.add(gr);
		}
		
		return list;
	}
	
	// Json Serializer //
	
	public static class Serializer implements JsonSerializer<ConfigList>, JsonDeserializer<ConfigList>
	{
		public JsonElement serialize(ConfigList src, Type typeOfSrc, JsonSerializationContext context)
		{
			if(src == null) return null;
			
			JsonObject o = new JsonObject();
			
			for(ConfigGroup g : src.groups)
			{
				JsonObject o1 = new JsonObject();
				
				for(ConfigEntry e : g.entries)
				{
					if(!e.isExcluded())
					{
						e.onPreLoaded();
						o1.add(e.toString(), context.serialize(e.getJson()));
					}
				}
				
				if(!o1.entrySet().isEmpty()) o.add(g.toString(), o1);
			}
			
			return o;
		}
		
		public ConfigList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			if(json.isJsonNull()) return null;
			ConfigList c = new ConfigList();
			c.groups = new FastList<ConfigGroup>();
			
			JsonObject o = json.getAsJsonObject();
			
			for(Map.Entry<String, JsonElement> e : o.entrySet())
			{
				ConfigGroup g = new ConfigGroup(e.getKey());
				JsonObject o1 = e.getValue().getAsJsonObject();
				
				for(Map.Entry<String, JsonElement> e1 : o1.entrySet())
				{
					ConfigEntry entry = new ConfigEntryJsonElement(e1.getKey());
					
					if(!e1.getValue().isJsonNull())
					{
						entry.setJson(e1.getValue());
						entry.onPostLoaded();
					}
					g.add(entry);
				}
				
				c.add(g);
			}
			
			return c;
		}
	}
	
	public int totalEntryCount()
	{
		int count = 0;
		for(int i = 0; i < groups.size(); i++)
			count += groups.get(i).entries.size();
		return count;
	}
	
	public void sort()
	{
		groups.sort(null);
		for(int i = 0; i < groups.size(); i++)
			groups.get(i).entries.sort(null);
	}
	
	public ConfigList clone()
	{
		ConfigList list1 = new ConfigList();
		list1.setID(ID);
		list1.parentFile = parentFile;
		list1.displayName = displayName;
		for(ConfigGroup g : groups) list1.add(g.clone());
		return list1;
	}
	
	public ConfigList setName(String s)
	{ displayName = s; return this; }
	
	public String getDisplayName()
	{ return displayName == null ? ID : displayName; }
}