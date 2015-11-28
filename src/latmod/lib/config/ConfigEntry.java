package latmod.lib.config;

import com.google.gson.JsonElement;

import latmod.lib.*;
import latmod.lib.util.FinalIDObject;

public abstract class ConfigEntry extends FinalIDObject
{
	public final PrimitiveType type;
	private boolean isHidden = false;
	private boolean isExcluded = false;
	private boolean sync = false;
	public ConfigEntryInfo info;
	
	public ConfigGroup parentGroup = null;
	
	ConfigEntry(String id, PrimitiveType t)
	{ super(id); type = t; }
	
	public abstract void setJson(JsonElement o);
	public abstract JsonElement getJson();
	public abstract String getValue();
	abstract void write(ByteIOStream io);
	abstract void read(ByteIOStream io);
	
	void writeExtended(ByteIOStream io)
	{ write(io); }
	
	void readExtended(ByteIOStream io)
	{ read(io); }
	
	public static ConfigEntry getEntry(PrimitiveType t, String id)
	{
		if(t == null) return null;
		else if(t == PrimitiveType.NULL) return new ConfigEntryBlank(id);
		else if(t == PrimitiveType.BOOLEAN) return new ConfigEntryBool(id, false);
		else if(t == PrimitiveType.FLOAT) return new ConfigEntryFloat(id, null);
		else if(t == PrimitiveType.FLOAT_ARRAY) return new ConfigEntryFloatArray(id, null);
		else if(t == PrimitiveType.INT) return new ConfigEntryInt(id, null);
		else if(t == PrimitiveType.INT_ARRAY) return new ConfigEntryIntArray(id, null);
		else if(t == PrimitiveType.STRING) return new ConfigEntryString(id, null);
		else if(t == PrimitiveType.STRING_ARRAY) return new ConfigEntryStringArray(id, null);
		else if(t == PrimitiveType.ENUM) return new ConfigEntryEnumExtended(id);
		else if(t == PrimitiveType.COLOR) return new ConfigEntryColor(id, 0, false);
		return null;
	}
	
	public void onPreLoaded()
	{
	}
	
	public void onPostLoaded()
	{
	}
	
	public String getFullID()
	{
		if(!isValid()) return null;
		StringBuilder sb = new StringBuilder();
		sb.append(parentGroup.parentList.ID);
		sb.append('.');
		sb.append(parentGroup.ID);
		sb.append('.');
		sb.append(ID);
		return sb.toString();
	}
	
	public boolean isValid()
	{ return ID != null && parentGroup != null && parentGroup.isValid(); }
	
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
	{ info = new ConfigEntryInfo(this, s); return (E)this; }
}