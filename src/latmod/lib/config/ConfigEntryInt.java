package latmod.lib.config;

import latmod.lib.*;
import latmod.lib.util.IntBounds;

public class ConfigEntryInt extends ConfigEntry
{
	private int value;
	private final IntBounds bounds;
	
	public ConfigEntryInt(String id, IntBounds b)
	{
		super(id, PrimitiveType.INT);
		bounds = (b == null) ? new IntBounds(0) : b;
		set(bounds.defValue);
	}
	
	public void set(int v)
	{ value = bounds.getVal(v); }
	
	public int get()
	{ return value; }
	
	public void add(int i)
	{ set(get() + i); }
	
	void setJson(Object o)
	{ set(o == null ? bounds.defValue : ((Number)o).intValue()); }
	
	Object getJson()
	{ return Integer.valueOf(get()); }
	
	void write(ByteIOStream io)
	{ io.writeInt(get()); }
	
	void read(ByteIOStream io)
	{ set(io.readInt()); }
}