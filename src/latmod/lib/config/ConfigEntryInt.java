package latmod.lib.config;

import com.google.gson.*;

import latmod.lib.*;
import latmod.lib.util.IntBounds;

public class ConfigEntryInt extends ConfigEntry
{
	private int value;
	private IntBounds bounds;
	
	public ConfigEntryInt(String id, IntBounds b)
	{
		super(id, PrimitiveType.INT);
		bounds = (b == null) ? new IntBounds(0) : b;
		set(bounds.defValue);
		updateDefault();
	}
	
	public void set(int v)
	{ value = bounds.getVal(v); }
	
	public int get()
	{ return value; }
	
	public void add(int i)
	{ set(get() + i); }
	
	public final void setJson(JsonElement o, JsonDeserializationContext c)
	{ set((o == null || o.isJsonNull()) ? bounds.defValue : o.getAsInt()); }
	
	public final JsonElement getJson(JsonSerializationContext c)
	{ return new JsonPrimitive(get()); }
	
	public String getValue()
	{ return Integer.toString(get()); }
	
	public void write(ByteIOStream io)
	{ io.writeInt(get()); }
	
	public void read(ByteIOStream io)
	{ set(io.readInt()); }
	
	public void writeExtended(ByteIOStream io)
	{
		write(io);
		io.writeInt(bounds.minValue);
		io.writeInt(bounds.maxValue);
	}
	
	public void readExtended(ByteIOStream io)
	{
		read(io);
		int min = io.readInt();
		int max = io.readInt();
		bounds = new IntBounds(bounds.defValue, min, max);
	}
	
	public String getMinValue()
	{
		if(bounds.minValue == Integer.MIN_VALUE) return null;
		else return Integer.toString(bounds.minValue);
	}
	
	public String getMaxValue()
	{
		if(bounds.maxValue == Integer.MAX_VALUE) return null;
		else return Integer.toString(bounds.maxValue);
	}
}