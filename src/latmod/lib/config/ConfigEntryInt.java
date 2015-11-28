package latmod.lib.config;

import com.google.gson.*;

import latmod.lib.*;
import latmod.lib.util.IntBounds;

public class ConfigEntryInt extends ConfigEntry
{
	private int value;
	public final IntBounds bounds;
	
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
	
	public final void setJson(JsonElement o)
	{ set((o == null || o.isJsonNull()) ? bounds.defValue : o.getAsInt()); }
	
	public final JsonElement getJson()
	{ return new JsonPrimitive(get()); }
	
	public String getValue()
	{ return Integer.toString(get()); }
	
	void write(ByteIOStream io)
	{ io.writeInt(get()); }
	
	void read(ByteIOStream io)
	{ set(io.readInt()); }
}