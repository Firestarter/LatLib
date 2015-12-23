package latmod.lib.config;

import com.google.gson.*;

import latmod.lib.*;

public class ConfigEntryBool extends ConfigEntry implements IClickableConfigEntry
{
	private boolean value;
	
	public ConfigEntryBool(String id, boolean def)
	{
		super(id, PrimitiveType.BOOLEAN);
		set(def);
		updateDefault();
	}
	
	public void set(boolean v)
	{ value = v; }
	
	public boolean get()
	{ return value; }
	
	public final void setJson(JsonElement o)
	{ set(o.getAsBoolean()); }
	
	public final JsonElement getJson()
	{ return new JsonPrimitive(get()); }
	
	public void write(ByteIOStream io)
	{ io.writeBoolean(get()); }
	
	public void read(ByteIOStream io)
	{ set(io.readBoolean()); }
	
	public void onClicked()
	{ set(!get()); }
	
	public String getAsString()
	{ return get() ? "true" : "false"; }
	
	public boolean getAsBoolean()
	{ return get(); }
	
	public int getAsInt()
	{ return get() ? 1 : 0; }
	
	public double getAsDouble()
	{ return get() ? 1D : 0D; }
}