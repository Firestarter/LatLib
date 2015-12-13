package latmod.lib.config;

import com.google.gson.*;

import latmod.lib.*;
import latmod.lib.util.DoubleBounds;

public class ConfigEntryDouble extends ConfigEntry
{
	private double value;
	private DoubleBounds bounds;
	
	public ConfigEntryDouble(String id, DoubleBounds b)
	{
		super(id, PrimitiveType.DOUBLE);
		bounds = (b == null) ? new DoubleBounds(0D) : b;
		set(bounds.defValue);
		updateDefault();
	}
	
	public void set(double v)
	{ value = bounds.getVal(v); }
	
	public double get()
	{ return value; }
	
	public final void setJson(JsonElement o, JsonDeserializationContext c)
	{ set(o.getAsDouble()); }
	
	public final JsonElement getJson(JsonSerializationContext c)
	{ return new JsonPrimitive(get()); }
	
	public String getValue()
	{ return Double.toString(get()); }
	
	public void write(ByteIOStream io)
	{ io.writeDouble(get()); }
	
	public void read(ByteIOStream io)
	{ set(io.readDouble()); }
	
	public void writeExtended(ByteIOStream io)
	{
		write(io);
		io.writeDouble(bounds.minValue);
		io.writeDouble(bounds.maxValue);
	}
	
	public void readExtended(ByteIOStream io)
	{
		read(io);
		double min = io.readDouble();
		double max = io.readDouble();
		bounds = new DoubleBounds(bounds.defValue, min, max);
	}
	
	public String getMinValue()
	{ return MathHelperLM.formatDouble(bounds.minValue); }
	
	public String getMaxValue()
	{ return MathHelperLM.formatDouble(bounds.maxValue); }
}