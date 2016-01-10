package latmod.lib.config;

import com.google.gson.*;
import latmod.lib.PrimitiveType;

import java.io.*;

public class ConfigEntryBool extends ConfigEntry implements IClickableConfigEntry
{
	public boolean defValue;
	private boolean value;
	
	public ConfigEntryBool(String id, boolean def)
	{
		super(id, PrimitiveType.BOOLEAN);
		set(def);
		defValue = def;
	}
	
	public void set(boolean v)
	{ value = v; }
	
	public boolean get()
	{ return value; }
	
	public final void setJson(JsonElement o)
	{ set(o.getAsBoolean()); }
	
	public final JsonElement getJson()
	{ return new JsonPrimitive(get()); }
	
	public void write(DataOutput io) throws Exception
	{ io.writeBoolean(get()); }
	
	public void read(DataInput io) throws Exception
	{ set(io.readBoolean()); }
	
	public void writeExtended(DataOutput io) throws Exception
	{
		write(io);
		io.writeBoolean(defValue);
	}
	
	public void readExtended(DataInput io) throws Exception
	{
		read(io);
		defValue = io.readBoolean();
	}
	
	public void onClicked()
	{ set(!get()); }
	
	public String getAsString()
	{ return get() ? "true" : "false"; }
	
	public boolean getAsBoolean()
	{ return get(); }
	
	public int getAsInt()
	{ return get() ? 1 : 0; }
	
	public double getAsDouble()
	{ return get() ? 1D : 0D; }
	
	public String getDefValue()
	{ return defValue ? "true" : "false"; }
}