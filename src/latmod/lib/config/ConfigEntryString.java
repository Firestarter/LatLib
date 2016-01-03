package latmod.lib.config;

import com.google.gson.*;
import latmod.lib.PrimitiveType;

import java.io.*;

public class ConfigEntryString extends ConfigEntry
{
	public String defValue;
	private String value;
	
	public ConfigEntryString(String id, String def)
	{
		super(id, PrimitiveType.STRING);
		set(def);
		defValue = def == null ? "" : def;
	}
	
	public void set(String o)
	{ value = o == null ? "" : o; }
	
	public String get()
	{ return value; }
	
	public final void setJson(JsonElement o)
	{ set(o.getAsString()); }
	
	public final JsonElement getJson()
	{ return new JsonPrimitive(get()); }
	
	public void write(DataOutput io) throws Exception
	{ io.writeUTF(get()); }

	public void read(DataInput io) throws Exception
	{ set(io.readUTF()); }

	public void writeExtended(DataOutput io) throws Exception
	{
		write(io);
		io.writeUTF(defValue);
	}

	public void readExtended(DataInput io) throws Exception
	{
		read(io);
		defValue = io.readUTF();
	}
	
	public String getAsString()
	{ return get(); }
	
	public boolean getAsBoolean()
	{ return get().equals("true"); }
	
	public int getAsInt()
	{ return Integer.parseInt(get()); }
	
	public double getAsDouble()
	{ return Double.parseDouble(get()); }

	public String getDefValue()
	{ return defValue; }
}