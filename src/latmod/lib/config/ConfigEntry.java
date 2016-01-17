package latmod.lib.config;

import com.google.gson.JsonElement;
import latmod.lib.*;
import latmod.lib.util.FinalIDObject;

public abstract class ConfigEntry extends FinalIDObject implements Cloneable, IJsonObject
{
	public String info = null;
	public byte flags = 0;
	public static final int FLAG_HIDDEN = 1;
	public static final int FLAG_EXCLUDED = 2;
	public static final int FLAG_SYNC = 3;
	public static final int FLAG_CANT_EDIT = 4;
	
	public ConfigGroup parentGroup = null;
	
	ConfigEntry(String id)
	{ super(id); }
	
	public abstract PrimitiveType getType();
	public abstract void setJson(JsonElement o);
	public abstract JsonElement getJson();
	public abstract void write(ByteIOStream io);
	public abstract void read(ByteIOStream io);
	
	public void writeExtended(ByteIOStream io)
	{ write(io); }
	
	public void readExtended(ByteIOStream io)
	{ read(io); }
	
	public final String getPrettyJsonString(boolean pretty)
	{ return LMJsonUtils.toJson(LMJsonUtils.getGson(pretty), getJson()); }
	
	public static ConfigEntry getEntry(PrimitiveType t, String id)
	{
		if(t == null) return null;
		else if(t == PrimitiveType.NULL) return new ConfigEntryBlank(id);
		else if(t == PrimitiveType.MAP) return new ConfigGroup(id);
		else if(t == PrimitiveType.BOOLEAN) return new ConfigEntryBool(id, false);
		else if(t == PrimitiveType.DOUBLE) return new ConfigEntryDouble(id, null);
		else if(t == PrimitiveType.DOUBLE_ARRAY) return new ConfigEntryDoubleArray(id, null);
		else if(t == PrimitiveType.INT) return new ConfigEntryInt(id, null);
		else if(t == PrimitiveType.INT_ARRAY) return new ConfigEntryIntArray(id, (IntList) null);
		else if(t == PrimitiveType.STRING) return new ConfigEntryString(id, null);
		else if(t == PrimitiveType.STRING_ARRAY) return new ConfigEntryStringArray(id);
		else if(t == PrimitiveType.ENUM) return new ConfigEntryEnumExtended(id);
		else if(t == PrimitiveType.COLOR) return new ConfigEntryColor(id, 0);
		return null;
	}
	
	public void onPreLoaded() { }
	
	public void onPostLoaded() { }
	
	public String getFullID()
	{
		if(!isValid()) return null;
		if(parentGroup == null) return ID;
		return parentGroup.getFullID() + '.' + ID;
	}
	
	public boolean isValid()
	{ return ID != null && (parentGroup == null || parentGroup.isValid()); }
	
	public final boolean getFlag(int f)
	{ return Bits.getBit(flags, f); }
	
	@SuppressWarnings("unchecked")
	public final <E extends ConfigEntry> E setFlag(int f, boolean b)
	{
		flags = Bits.setBit(flags, f, b);
		return (E) this;
	}
	
	public final <E extends ConfigEntry> E setHidden()
	{ return setFlag(FLAG_HIDDEN, true); }
	
	public final <E extends ConfigEntry> E setExcluded()
	{ return setFlag(FLAG_EXCLUDED, true); }
	
	public final <E extends ConfigEntry> E sync()
	{ return setFlag(FLAG_SYNC, true); }
	
	public final <E extends ConfigEntry> E setCantEdit()
	{ return setFlag(FLAG_CANT_EDIT, true); }
	
	@SuppressWarnings("unchecked")
	public final <E extends ConfigEntry> E setInfo(String s)
	{
		info = s;
		return (E) this;
	}
	
	public String getDefValue() { return getAsString(); }
	
	public String getMinValue() { return null; }
	
	public String getMaxValue() { return null; }
	
	public ConfigEntry clone()
	{
		ConfigEntry e = ConfigEntry.getEntry(getType(), ID);
		e.setJson(getJson());
		return e;
	}
	
	public int compareTo(Object o)
	{
		int i = Boolean.compare(getAsGroup() != null, ((ConfigEntry) o).getAsGroup() != null);
		return (i == 0) ? super.compareTo(o) : i;
	}
	
	public final String toString()
	{ return getAsString(); }
	
	public ConfigGroup getAsGroup()
	{ return null; }
	
	public abstract String getAsString();
	
	public String[] getAsStringArray()
	{ return new String[] {getAsString()}; }
	
	public boolean getAsBoolean()
	{ return false; }
	
	public int getAsInt()
	{ return 0; }
	
	public double getAsDouble()
	{ return 0D; }
	
	public int[] getAsIntArray()
	{ return new int[] {getAsInt()}; }
	
	public double[] getAsDoubleArray()
	{ return new double[] {getAsDouble()}; }
}