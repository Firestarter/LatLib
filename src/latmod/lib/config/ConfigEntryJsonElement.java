package latmod.lib.config;

import com.google.gson.*;

import latmod.lib.*;

class ConfigEntryJsonElement extends ConfigEntry
{
	private JsonElement value;
	
	ConfigEntryJsonElement(String id)
	{
		super(id, PrimitiveType.NULL);
		setHidden();
		setExcluded();
	}
	
	public final void setJson(JsonElement o, JsonDeserializationContext c)
	{ value = o; }
	
	public final JsonElement getJson(JsonSerializationContext c)
	{ return value; }
	
	public String getValue()
	{ return String.valueOf(value); }
	
	public void write(ByteIOStream io)
	{ }
	
	public void read(ByteIOStream io)
	{ }
}