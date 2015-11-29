package latmod.lib.config;

import com.google.gson.*;

import latmod.lib.*;
import latmod.lib.util.FloatBounds;

public class ConfigEntryFloat extends ConfigEntry
{
	private float value;
	private FloatBounds bounds;
	
	public ConfigEntryFloat(String id, FloatBounds b)
	{
		super(id, PrimitiveType.FLOAT);
		bounds = (b == null) ? new FloatBounds(0F) : b;
		set(bounds.defValue);
		updateDefault();
	}
	
	public void set(float v)
	{ value = bounds.getVal(v); }
	
	public float get()
	{ return value; }
	
	public final void setJson(JsonElement o)
	{ set(o.getAsFloat()); }
	
	public final JsonElement getJson()
	{ return new JsonPrimitive(get()); }
	
	public String getValue()
	{ return Float.toString(get()); }
	
	void write(ByteIOStream io)
	{ io.writeFloat(get()); }
	
	void read(ByteIOStream io)
	{ set(io.readFloat()); }
	
	void writeExtended(ByteIOStream io)
	{
		write(io);
		io.writeFloat(bounds.minValue);
		io.writeFloat(bounds.maxValue);
	}
	
	void readExtended(ByteIOStream io)
	{
		read(io);
		float min = io.readFloat();
		float max = io.readFloat();
		bounds = new FloatBounds(bounds.defValue, min, max);
	}
	
	public String getMinValue()
	{ return MathHelperLM.formatDouble(bounds.minValue); }
	
	public String getMaxValue()
	{ return MathHelperLM.formatDouble(bounds.maxValue); }
}