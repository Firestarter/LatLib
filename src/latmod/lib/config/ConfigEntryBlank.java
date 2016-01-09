package latmod.lib.config;

import com.google.gson.JsonElement;
import latmod.lib.PrimitiveType;

import java.io.*;

public class ConfigEntryBlank extends ConfigEntry implements IClickableConfigEntry
{
	public ConfigEntryBlank(String id)
	{ super(id, PrimitiveType.NULL); }
	
	public final void setJson(JsonElement o)
	{ }
	
	public final JsonElement getJson()
	{ return null; }
	
	public String getAsString()
	{ return "edit"; }
	
	public void write(DataOutput io) throws Exception
	{ }
	
	public void read(DataInput io) throws Exception
	{ }
	
	public void onClicked()
	{
	}

	public String getDefValue()
	{ return null; }
}