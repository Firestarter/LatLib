package latmod.lib.config;

import latmod.lib.*;
import latmod.lib.util.FloatBounds;

public class ConfigEntryFloat extends ConfigEntry
{
	private float value;
	private final FloatBounds bounds;
	
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
	
	public void setJson(Object o)
	{ set(((Number)o).floatValue()); }
	
	public Object getJson()
	{ return Float.valueOf(get()); }
	
	void write(ByteIOStream io)
	{ io.writeFloat(get()); }
	
	void read(ByteIOStream io)
	{ set(io.readFloat()); }
}