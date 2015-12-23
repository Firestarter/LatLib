package latmod.lib.config;

import com.google.gson.*;

import latmod.lib.*;

public class ConfigEntryColor extends ConfigEntry
{
	private int value;
	private boolean alpha;
	
	public ConfigEntryColor(String id, int def, boolean a)
	{
		super(id, PrimitiveType.INT);
		alpha = a;
		set(def);
		updateDefault();
	}
	
	private int format(int col)
	{ if(!alpha) return col | 0xFF000000; return col; }
	
	public void set(int v)
	{ value = format(v); }
	
	public int get()
	{ return value; }
	
	public final void setJson(JsonElement o)
	{ set(format(o.getAsInt())); }
	
	public final JsonElement getJson()
	{ return new JsonPrimitive(get()); }
	
	public void write(ByteIOStream io)
	{ io.writeInt(get()); }
	
	public void read(ByteIOStream io)
	{ set(format(io.readInt())); }
	
	public String getAsString()
	{ return LMColorUtils.getHex(get()); }
	
	public int getAsInt()
	{ return get(); }
}