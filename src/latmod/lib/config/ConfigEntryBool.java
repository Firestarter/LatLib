package latmod.lib.config;

import latmod.lib.*;

public class ConfigEntryBool extends ConfigEntry
{
	private boolean value;
	
	public ConfigEntryBool(String id, boolean def)
	{ super(id, PrimitiveType.BOOLEAN); set(def); }
	
	public void set(boolean v)
	{ value = v; }
	
	public boolean get()
	{ return value; }
	
	public void setJson(Object o)
	{ set(((Boolean)o).booleanValue()); }
	
	public Object getJson()
	{ return Boolean.valueOf(get()); }
	
	void write(ByteIOStream io)
	{ io.writeBoolean(get()); }
	
	void read(ByteIOStream io)
	{ set(io.readBoolean()); }
}