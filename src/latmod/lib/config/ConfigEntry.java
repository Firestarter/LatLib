package latmod.lib.config;

import com.google.gson.*;

import latmod.lib.*;
import latmod.lib.util.FinalIDObject;

public abstract class ConfigEntry extends FinalIDObject implements Cloneable
{
	public final PrimitiveType type;
	private boolean isHidden = false;
	private boolean isExcluded = false;
	private boolean sync = false;
	public String info = null;
	public String defaultValue = null;
	
	public ConfigGroup parentGroup = null;
	
	ConfigEntry(String id, PrimitiveType t)
	{ super(id); type = t; }
	
	public abstract void setJson(JsonElement o, JsonDeserializationContext c);
	public abstract JsonElement getJson(JsonSerializationContext c);
	public abstract String getValue();
	public abstract void write(ByteIOStream io);
	public abstract void read(ByteIOStream io);
	
	public void writeExtended(ByteIOStream io)
	{ write(io); }
	
	public void readExtended(ByteIOStream io)
	{ read(io); }
	
	public static ConfigEntry getEntry(PrimitiveType t, String id)
	{
		if(t == null) return null;
		else if(t == PrimitiveType.NULL) return new ConfigEntryBlank(id);
		else if(t == PrimitiveType.MAP) return new ConfigGroup(id);
		else if(t == PrimitiveType.BOOLEAN) return new ConfigEntryBool(id, false);
		else if(t == PrimitiveType.DOUBLE) return new ConfigEntryDouble(id, null);
		else if(t == PrimitiveType.DOUBLE_ARRAY) return new ConfigEntryDoubleArray(id, null);
		else if(t == PrimitiveType.INT) return new ConfigEntryInt(id, null);
		else if(t == PrimitiveType.INT_ARRAY) return new ConfigEntryIntArray(id, null);
		else if(t == PrimitiveType.STRING) return new ConfigEntryString(id, null);
		else if(t == PrimitiveType.STRING_ARRAY) return new ConfigEntryStringArray(id);
		else if(t == PrimitiveType.ENUM) return new ConfigEntryEnumExtended(id);
		else if(t == PrimitiveType.COLOR) return new ConfigEntryColor(id, 0, false);
		return null;
	}
	
	public void onPreLoaded() { }
	public void onPostLoaded() { }
	
	public String getFullID()
	{
		if(!isValid()) return null;
		if(parentGroup == null) return ID;
		StringBuilder sb = new StringBuilder();
		sb.append(parentGroup.getFullID());
		sb.append('.');
		sb.append(ID);
		return sb.toString();
	}
	
	public boolean isValid()
	{ return ID != null && (parentGroup == null || parentGroup.isValid()); }
	
	@SuppressWarnings("unchecked")
	public final <E extends ConfigEntry> E setHidden()
	{ isHidden = true; return (E)this; }
	
	public boolean isHidden()
	{ return isHidden; }
	
	@SuppressWarnings("unchecked")
	public final <E extends ConfigEntry> E setExcluded()
	{ isExcluded = true; return (E)this; }
	
	public boolean isExcluded()
	{ return isExcluded; }
	
	@SuppressWarnings("unchecked")
	public final <E extends ConfigEntry> E sync()
	{ sync = true; return (E)this; }
	
	public boolean shouldSync()
	{ return sync; }
	
	@SuppressWarnings("unchecked")
	public final <E extends ConfigEntry> E setInfo(String s)
	{ info = s; return (E)this; }
	
	public void updateDefault()
	{ try { defaultValue = getValue(); } catch(Exception e) { } }
	
	public String getMinValue() { return null; }
	public String getMaxValue() { return null; }
	
	public ConfigGroup getAsGroup()
	{ return null; }
	
	public ConfigEntry clone()
	{
		ConfigEntry e = ConfigEntry.getEntry(type, ID);
		e.setJson(getJson(LMJsonUtils.serializationContext), LMJsonUtils.deserializationContext);
		return e;
	}
}