package latmod.lib.config;

import latmod.lib.*;

class ConfigEntryObject extends ConfigEntry
{
	private Object value = null;
	
	ConfigEntryObject(String id)
	{ super(id, PrimitiveType.STRING); }
	
	public void setJson(Object o)
	{ value = o; }
	
	public Object getJson()
	{ return value; }
	
	void write(ByteIOStream io)
	{ }
	
	void read(ByteIOStream io)
	{ value = null; }
}