package latmod.lib.config;

import latmod.lib.*;

class ConfigEntryObject extends ConfigEntry
{
	private Object value = null;
	
	ConfigEntryObject(String id)
	{ super(id, PrimitiveType.STRING); }
	
	void setJson(Object o)
	{ value = o; }
	
	Object getJson()
	{ return value; }
	
	void write(ByteIOStream io)
	{ }
	
	void read(ByteIOStream io)
	{ value = null; }
}