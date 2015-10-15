package latmod.lib.config;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.*;

import latmod.lib.*;
import latmod.lib.util.IDObject;

public final class ConfigList extends IDObject
{
	public FastList<ConfigGroup> groups;
	public ConfigFile parentFile = null;
	
	public ConfigList()
	{ super(null); }
	
	public void add(ConfigGroup g)
	{
		if(g != null && !groups.contains(g))
		{ groups.add(g); g.parentList = this; }
	}
	
	public void loadFromList(ConfigList l)
	{
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
						if(e0 != null) e0.setJson(e1.getJson());
					}
				}
			}
		}
	}
	
	public int writeToIO(ByteIOStream io)
	{
		int count = 0;
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
				e.write(io);
				count++;
			}
		}
		
		return count;
	}
	
	public static ConfigList readFromIO(ByteIOStream io)
	{
		ConfigList list = new ConfigList();
		list.groups = new FastList<ConfigGroup>();
		
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
					e.read(io);
					gr.add(e);
					e.onPostLoaded();
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
					e.onPreLoaded();
					o1.add(e.toString(), context.serialize(e.getJson()));
				}
				
				o.add(g.toString(), o1);
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
					ConfigEntry entry = new ConfigEntryObject(e1.getKey());
					if(!e1.getValue().isJsonNull())
					{
						entry.setJson((Object)context.deserialize(e1.getValue(), Object.class));
						entry.onPostLoaded();
					}
					g.add(entry);
				}
				
				c.groups.add(g);
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
		return null;
	}
}