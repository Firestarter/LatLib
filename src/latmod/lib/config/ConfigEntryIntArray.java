package latmod.lib.config;

import java.util.List;

import latmod.lib.*;

public class ConfigEntryIntArray extends ConfigEntry
{
	private int[] value;
	
	public ConfigEntryIntArray(String id, int[] def)
	{ super(id, PrimitiveType.INT_ARRAY); set(def); }
	
	public void set(int[] o)
	{ value = o == null ? new int[0] : o; }
	
	public int[] get()
	{ return value; }
	
	public void setJson(Object o)
	{
		if(o instanceof List<?>)
			setJson(((List<?>)o).toArray(new Integer[0]));
		else set(Converter.toInts((Integer[])o));
	}
	
	public Object getJson()
	{ return Converter.fromInts(get()); }
	
	void write(ByteIOStream io)
	{
		value = get();
		io.writeUShort(value.length);
		for(int i = 0; i < value.length; i++)
			io.writeInt(value[i]);
	}
	
	void read(ByteIOStream io)
	{
		value = new int[io.readUShort()];
		for(int i = 0; i < value.length; i++)
			value[i] = io.readInt();
		set(value);
	}
}