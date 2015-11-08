package latmod.lib.config;

import com.google.gson.JsonElement;

import latmod.lib.*;
import latmod.lib.util.FinalIDObject;

public abstract class ConfigEntry extends FinalIDObject
{
	public final PrimitiveType type;
	
	public ConfigGroup parentGroup = null;
	
	ConfigEntry(String id, PrimitiveType t)
	{ super(id); type = t; }
	
	public abstract void setJson(JsonElement o);
	public abstract JsonElement getJson();
	abstract void write(ByteIOStream io);
	abstract void read(ByteIOStream io);
	
	public static ConfigEntry getEntry(PrimitiveType t, String id)
	{
		if(PrimitiveType.isNull(t)) return null;
		else if(t == PrimitiveType.BOOLEAN) return new ConfigEntryBool(id, false);
		else if(t == PrimitiveType.FLOAT) return new ConfigEntryFloat(id, null);
		else if(t == PrimitiveType.FLOAT_ARRAY) return new ConfigEntryFloatArray(id, null);
		else if(t == PrimitiveType.INT) return new ConfigEntryInt(id, null);
		else if(t == PrimitiveType.INT_ARRAY) return new ConfigEntryIntArray(id, null);
		else if(t == PrimitiveType.STRING) return new ConfigEntryString(id, null);
		else if(t == PrimitiveType.STRING_ARRAY) return new ConfigEntryStringArray(id, null);
		return null;
	}
	
	public void onPreLoaded()
	{
	}
	
	public void onPostLoaded()
	{
	}
}