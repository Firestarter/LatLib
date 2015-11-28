package latmod.lib.config;

import com.google.gson.JsonElement;

import latmod.lib.*;

public class ConfigEntryBlank extends ConfigEntry implements IClickableConfigEntry
{
	public ConfigEntryBlank(String id)
	{ super(id, PrimitiveType.NULL); }
	
	public final void setJson(JsonElement o)
	{ }
	
	public final JsonElement getJson()
	{ return null; }
	
	public String getValue()
	{ return "edit"; }
	
	void write(ByteIOStream io)
	{ }
	
	void read(ByteIOStream io)
	{ }
	
	public void onClicked()
	{
	}
}