package latmod.lib.config;

import com.google.gson.*;

import latmod.lib.*;

public class ConfigEntryString extends ConfigEntry
{
	private String value;
	
	public ConfigEntryString(String id, String def)
	{ super(id, PrimitiveType.STRING); value = def; }
	
	public void set(String o)
	{ value = o == null ? "" : o; }
	
	public String get()
	{ return value; }
	
	public final void setJson(JsonElement o)
	{ set(o.getAsString()); }
	
	public final JsonElement getJson()
	{ return new JsonPrimitive(get()); }
	
	void write(ByteIOStream io)
	{ io.writeString(get()); }
	
	void read(ByteIOStream io)
	{ set(io.readString()); }
}