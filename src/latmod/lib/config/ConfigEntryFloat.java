package latmod.lib.config;

import com.google.gson.*;

import latmod.lib.*;
import latmod.lib.util.FloatBounds;

public class ConfigEntryFloat extends ConfigEntry
{
	private float value;
	public final FloatBounds bounds;
	
	public ConfigEntryFloat(String id, FloatBounds b)
	{
		super(id, PrimitiveType.FLOAT);
		bounds = (b == null) ? new FloatBounds(0F) : b;
		set(bounds.defValue);
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
}