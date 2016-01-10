package latmod.lib.config;

import com.google.gson.*;
import latmod.lib.*;

import java.io.*;

public class ConfigEntryColor extends ConfigEntry
{
	public final LMColor value;
	public final LMColor defValue;
	
	public ConfigEntryColor(String id, int def)
	{
		super(id, PrimitiveType.INT);
		value = new LMColor(def);
		defValue = new LMColor(def);
	}
	
	public final void setJson(JsonElement o)
	{ value.setRGB(o.getAsInt()); }
	
	public final JsonElement getJson()
	{ return new JsonPrimitive(value.color()); }
	
	public void write(DataOutput io) throws Exception
	{ io.writeInt(value.color()); }
	
	public void read(DataInput io) throws Exception
	{ value.setRGB(io.readInt()); }
	
	public void writeExtended(DataOutput io) throws Exception
	{
		write(io);
		io.writeInt(defValue.color());
	}
	
	public void readExtended(DataInput io) throws Exception
	{
		read(io);
		defValue.setRGB(io.readInt());
	}
	
	public String getAsString()
	{ return value.toString(); }
	
	public int getAsInt()
	{ return value.color(); }
	
	public String getDefValue()
	{ return defValue.toString(); }
}