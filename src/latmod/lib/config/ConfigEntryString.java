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
	
	public final void setJson(JsonElement o, JsonDeserializationContext c)
	{ set(o.getAsString()); }
	
	public final JsonElement getJson(JsonSerializationContext c)
	{ return new JsonPrimitive(get()); }
	
	public String getValue()
	{ return get(); }
	
	public void write(ByteIOStream io)
	{ io.writeUTF(get()); }
	
	public void read(ByteIOStream io)
	{ set(io.readUTF()); }
}