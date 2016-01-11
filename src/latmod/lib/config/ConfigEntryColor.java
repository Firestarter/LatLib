package latmod.lib.config;

import com.google.gson.*;
import latmod.lib.*;

public class ConfigEntryColor extends ConfigEntry
{
	public final LMColor value;
	public final LMColor defValue;
	
	public ConfigEntryColor(String id, int def)
	{
		super(id);
		value = new LMColor(def);
		defValue = new LMColor(def);
	}
	
	public PrimitiveType getType()
	{ return PrimitiveType.COLOR; }
	
	public final void setJson(JsonElement o)
	{ value.setRGB(o.getAsInt()); }
	
	public final JsonElement getJson()
	{ return new JsonPrimitive(value.color()); }
	
	public void write(ByteIOStream io)
	{ io.writeInt(value.color()); }
	
	public void read(ByteIOStream io)
	{ value.setRGB(io.readInt()); }
	
	public void writeExtended(ByteIOStream io)
	{
		write(io);
		io.writeInt(defValue.color());
	}
	
	public void readExtended(ByteIOStream io)
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