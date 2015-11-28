package latmod.lib.config;

import com.google.gson.JsonElement;

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
	
	public final void setJson(JsonElement o)
	{ value = o; }
	
	public final JsonElement getJson()
	{ return value; }
	
	public String getValue()
	{ return String.valueOf(value); }
	
	void write(ByteIOStream io)
	{ }
	
	void read(ByteIOStream io)
	{ }
}