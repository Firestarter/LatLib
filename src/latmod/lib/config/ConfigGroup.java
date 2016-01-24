package latmod.lib.config;

import com.google.gson.*;
import latmod.lib.*;

import java.lang.reflect.Field;
import java.util.*;

public class ConfigGroup extends ConfigEntry
{
	private Map<String, ConfigEntry> entryMap0;
	private String displayName;
	public IConfigFile parentFile;
	
	public ConfigGroup(String s)
	{
		super(s);
		setFlag(FLAG_CANT_ADD, true);
	}
	
	public PrimitiveType getType()
	{ return PrimitiveType.MAP; }
	
	public Map<String, ConfigEntry> entryMap()
	{
		if(entryMap0 == null) entryMap0 = new HashMap<>();
		return entryMap0;
	}
	
	public final List<ConfigEntry> entries()
	{ return LMMapUtils.values(entryMap(), null); }
	
	public IConfigFile getParentFile()
	{
		if(parentFile != null) return parentFile;
		else if(parentGroup != null) return parentGroup.getParentFile();
		else return null;
	}
	
	public ConfigGroup add(ConfigEntry e, boolean copy)
	{
		if(e != null)
		{
			if(copy)
			{
				ConfigEntry e1 = e.clone();
				entryMap().put(e1.ID, e1);
				e1.parentGroup = this;
			}
			else
			{
				entryMap().put(e.ID, e);
				e.parentGroup = this;
			}
		}
		
		return this;
	}
	
	public ConfigGroup addAll(Class<?> c, Object obj, boolean copy)
	{
		try
		{
			Field f[] = c.getDeclaredFields();
			
			if(f != null && f.length > 0) for(int i = 0; i < f.length; i++)
			{
				try
				{
					f[i].setAccessible(true);
					if(ConfigEntry.class.isAssignableFrom(f[i].getType()))
					{
						ConfigEntry entry = (ConfigEntry) f[i].get(obj);
						if(entry != null && entry != this) add(entry, copy);
					}
				}
				catch(Exception e1) { }
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return this;
	}
	
	public ConfigGroup setName(String s)
	{
		displayName = s;
		return this;
	}
	
	public String getDisplayName()
	{ return displayName == null ? LMStringUtils.firstUppercase(ID) : displayName; }
	
	public ConfigEntry clone()
	{
		ConfigGroup g = new ConfigGroup(ID);
		g.displayName = displayName;
		for(ConfigEntry e : entryMap().values())
			g.add(e, true);
		return g;
	}
	
	public final void setJson(JsonElement o0)
	{
		if(o0 == null || !o0.isJsonObject()) return;
		
		entryMap().clear();
		
		JsonObject o = o0.getAsJsonObject();
		
		for(Map.Entry<String, JsonElement> e : o.entrySet())
		{
			ConfigEntry entry = new ConfigEntryJsonElement(e.getKey());
			
			if(!e.getValue().isJsonNull())
			{
				entry.setJson(e.getValue());
				entry.onPostLoaded();
			}
			
			add(entry, false);
		}
	}
	
	public final JsonElement getJson()
	{
		JsonObject o = new JsonObject();
		
		for(ConfigEntry e : entries())
		{
			if(!e.getFlag(FLAG_EXCLUDED))
			{
				e.onPreLoaded();
				o.add(e.ID, e.getJson());
			}
		}
		
		return o;
	}
	
	public String getAsString()
	{ return getJson().toString(); }
	
	public String[] getAsStringArray()
	{ return LMListUtils.toStringArray(entries()); }
	
	public boolean getAsBoolean()
	{ return !entryMap().isEmpty(); }
	
	public int getAsInt()
	{ return entryMap().size(); }
	
	public void write(ByteIOStream io)
	{
		io.writeShort(entryMap().size());
		for(ConfigEntry e : entryMap().values())
		{
			e.onPreLoaded();
			io.writeByte(e.getType().ordinal());
			io.writeUTF(e.ID);
			e.write(io);
		}
	}
	
	public void read(ByteIOStream io)
	{
		int s = io.readUnsignedShort();
		entryMap().clear();
		for(int i = 0; i < s; i++)
		{
			int type = io.readUnsignedByte();
			String id = io.readUTF();
			ConfigEntry e = ConfigEntry.getEntry(PrimitiveType.VALUES[type], id);
			e.read(io);
			add(e, false);
		}
	}
	
	public void writeExtended(ByteIOStream io)
	{
		io.writeUTF(displayName);
		
		io.writeShort(entryMap().size());
		for(ConfigEntry e : entryMap().values())
		{
			e.onPreLoaded();
			io.writeByte(e.getType().ordinal());
			io.writeUTF(e.ID);
			io.writeByte(e.flags);
			e.writeExtended(io);
			io.writeUTF(e.info);
		}
	}
	
	public void readExtended(ByteIOStream io)
	{
		displayName = io.readUTF();
		int s = io.readUnsignedShort();
		entryMap().clear();
		for(int i = 0; i < s; i++)
		{
			int type = io.readUnsignedByte();
			String id = io.readUTF();
			ConfigEntry e = ConfigEntry.getEntry(PrimitiveType.VALUES[type], id);
			e.flags = io.readByte();
			e.readExtended(io);
			e.info = io.readUTF();
			add(e, false);
		}
	}
	
	public int loadFromGroup(ConfigGroup l)
	{
		if(l == null || l.entryMap().isEmpty()) return 0;
		
		int result = 0;
		
		for(ConfigEntry e1 : l.entryMap().values())
		{
			ConfigEntry e0 = entryMap().get(e1.ID);
			
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
						System.err.println("Can't set value " + e1.getJson() + " for '" + e0.parentGroup.ID + "." + e0.ID + "' (type:" + e0.getType() + ")");
						System.err.println(ex.toString());
					}
				}
			}
		}
		
		if(result > 0) onLoadedFromGroup(l);
		return result;
	}
	
	protected void onLoadedFromGroup(ConfigGroup l)
	{
	}
	
	public boolean hasKey(Object key)
	{ return entryMap().containsKey(LMUtils.getID(key)); }
	
	public ConfigEntry getEntry(Object key)
	{ return entryMap().get(LMUtils.getID(key)); }
	
	public ConfigGroup getGroup(Object key)
	{
		ConfigEntry e = getEntry(key);
		return (e == null) ? null : e.getAsGroup();
	}
	
	public List<ConfigGroup> getGroups()
	{
		ArrayList<ConfigGroup> list = new ArrayList<>();
		for(ConfigEntry e : entryMap().values())
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
		
		for(int i = 0; i < entryMap().size(); i++)
		{
			ConfigGroup g = entryMap().get(i).getAsGroup();
			if(g == null) count++;
			else count += g.getTotalEntryCount();
		}
		
		return count;
	}
	
	public int getDepth()
	{ return (parentGroup == null) ? 0 : (parentGroup.getDepth() + 1); }
	
	public ConfigGroup generateSynced(boolean copy)
	{
		ConfigGroup out = new ConfigGroup(ID);
		
		for(ConfigEntry e : entryMap().values())
		{
			if(e.getFlag(FLAG_SYNC)) out.add(e, copy);
			else if(e.getAsGroup() != null)
			{
				ConfigGroup g = e.getAsGroup().generateSynced(copy);
				if(!g.entryMap().isEmpty()) out.add(g, false);
			}
		}
		
		return out;
	}
}