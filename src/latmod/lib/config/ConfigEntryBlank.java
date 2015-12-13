package latmod.lib.config;

import com.google.gson.*;

import latmod.lib.*;

public class ConfigEntryBlank extends ConfigEntry implements IClickableConfigEntry
{
	public ConfigEntryBlank(String id)
	{ super(id, PrimitiveType.NULL); }
	
	public final void setJson(JsonElement o, JsonDeserializationContext c)
	{ }
	
	public final JsonElement getJson(JsonSerializationContext c)
	{ return null; }
	
	public String getValue()
	{ return "edit"; }
	
	public void write(ByteIOStream io)
	{ }
	
	public void read(ByteIOStream io)
	{ }
	
	public void onClicked()
	{
	}
}