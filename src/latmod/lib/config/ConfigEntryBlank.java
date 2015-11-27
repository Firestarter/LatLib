package latmod.lib.config;

import com.google.gson.JsonElement;

import latmod.lib.*;

public class ConfigEntryBlank extends ConfigEntry
{
	public ConfigEntryBlank(String id)
	{
		super(id, PrimitiveType.NULL);
		serialize = false;
	}
	
	public final void setJson(JsonElement o)
	{ }
	
	public final JsonElement getJson()
	{ return null; }
	
	void write(ByteIOStream io)
	{ }
	
	void read(ByteIOStream io)
	{ }
}