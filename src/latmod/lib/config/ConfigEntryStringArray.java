package latmod.lib.config;

import java.util.List;

import latmod.lib.*;

public class ConfigEntryStringArray extends ConfigEntry
{
	private String[] value;
	
	public ConfigEntryStringArray(String id, String[] def)
	{ super(id, PrimitiveType.STRING_ARRAY); set(def); }
	
	public void set(String[] o)
	{ value = o == null ? new String[0] : o; }
	
	public String[] get()
	{ return value; }
	
	public void setJson(Object o)
	{
		if(o instanceof List<?>)
			set(((List<?>)o).toArray(new String[0]));
		else set((String[])o);
	}
	
	public Object getJson()
	{ return get(); }
	
	void write(ByteIOStream io)
	{
		value = get();
		io.writeUShort(value.length);
		for(int i = 0; i < value.length; i++)
			io.writeString(value[i]);
	}
	
	void read(ByteIOStream io)
	{
		value = new String[io.readUShort()];
		for(int i = 0; i < value.length; i++)
			value[i] = io.readString();
		set(value);
	}
}