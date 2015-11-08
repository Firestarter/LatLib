package latmod.lib.config;

import com.google.gson.JsonElement;

import latmod.lib.*;

class ConfigEntryJsonElement extends ConfigEntry
{
	private JsonElement value;
	
	ConfigEntryJsonElement(String id)
	{ super(id, PrimitiveType.STRING); }
	
	public final void setJson(JsonElement o)
	{ value = o; }
	
	public final JsonElement getJson()
	{ return value; }
	
	void write(ByteIOStream io)
	{ }
	
	void read(ByteIOStream io)
	{ }
}