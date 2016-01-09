package latmod.lib.config;

import com.google.gson.*;
import latmod.lib.*;
import latmod.lib.util.DoubleBounds;

import java.io.*;

public class ConfigEntryDouble extends ConfigEntry
{
	private double value;
	private DoubleBounds bounds;
	
	public ConfigEntryDouble(String id, DoubleBounds b)
	{
		super(id, PrimitiveType.DOUBLE);
		bounds = (b == null) ? new DoubleBounds(0D) : b;
		set(bounds.defValue);
	}
	
	public void set(double v)
	{ value = bounds.getVal(v); }
	
	public double get()
	{ return value; }

	public void add(double v)
	{ set(get() + v); }
	
	public final void setJson(JsonElement o)
	{ set(o.getAsDouble()); }
	
	public final JsonElement getJson()
	{ return new JsonPrimitive(get()); }
	
	public void write(DataOutput io) throws Exception
	{ io.writeDouble(get()); }

	public void read(DataInput io) throws Exception
	{ set(io.readDouble()); }

	public void writeExtended(DataOutput io) throws Exception
	{
		write(io);
		io.writeDouble(bounds.defValue);
		io.writeDouble(bounds.minValue);
		io.writeDouble(bounds.maxValue);
	}

	public void readExtended(DataInput io) throws Exception
	{
		read(io);
		double def = io.readDouble();
		double min = io.readDouble();
		double max = io.readDouble();
		bounds = new DoubleBounds(def, min, max);
	}

	public String getMinValue()
	{
		if(bounds.minValue == Double.NEGATIVE_INFINITY) return null;
		return MathHelperLM.formatDouble(bounds.minValue);
	}
	
	public String getMaxValue()
	{
		if(bounds.minValue == Double.POSITIVE_INFINITY) return null;
		return MathHelperLM.formatDouble(bounds.maxValue);
	}
	
	public String getAsString()
	{ return Double.toString(get()); }
	
	public int getAsInt()
	{ return (int) get(); }
	
	public double getAsDouble()
	{ return get(); }

	public String getDefValue()
	{ return Double.toString(bounds.defValue); }
}