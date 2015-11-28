package latmod.lib.config;

import com.google.gson.*;

import latmod.lib.*;

public class ConfigEntryBool extends ConfigEntry implements IClickableConfigEntry
{
	private boolean value;
	
	public ConfigEntryBool(String id, boolean def)
	{ super(id, PrimitiveType.BOOLEAN); set(def); }
	
	public void set(boolean v)
	{ value = v; }
	
	public boolean get()
	{ return value; }
	
	public final void setJson(JsonElement o)
	{ set(o.getAsBoolean()); }
	
	public final JsonElement getJson()
	{ return new JsonPrimitive(get()); }
	
	public String getValue()
	{ return get() ? "true" : "false"; }
	
	void write(ByteIOStream io)
	{ io.writeBoolean(get()); }
	
	void read(ByteIOStream io)
	{ set(io.readBoolean()); }
	
	public void onClicked()
	{ set(!get()); }
}