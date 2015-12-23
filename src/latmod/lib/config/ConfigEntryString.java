package latmod.lib.config;

import com.google.gson.*;

import latmod.lib.*;

public class ConfigEntryString extends ConfigEntry
{
	private String value;
	
	public ConfigEntryString(String id, String def)
	{
		super(id, PrimitiveType.STRING);
		set(def);
		updateDefault();
	}
	
	public void set(String o)
	{ value = o == null ? "" : o; }
	
	public String get()
	{ return value; }
	
	public final void setJson(JsonElement o)
	{ set(o.getAsString()); }
	
	public final JsonElement getJson()
	{ return new JsonPrimitive(get()); }
	
	public void write(ByteIOStream io)
	{ io.writeUTF(get()); }
	
	public void read(ByteIOStream io)
	{ set(io.readUTF()); }
	
	public String getAsString()
	{ return get(); }
	
	public boolean getAsBoolean()
	{ return get().equals("true"); }
	
	public int getAsInt()
	{ return Integer.parseInt(get()); }
	
	public double getAsDouble()
	{ return Double.parseDouble(get()); }
}