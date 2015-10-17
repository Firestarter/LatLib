package latmod.lib.config;

import java.util.List;

import latmod.lib.*;

public class ConfigEntryFloatArray extends ConfigEntry
{
	private float[] value;
	
	public ConfigEntryFloatArray(String id, float[] def)
	{
		super(id, PrimitiveType.FLOAT_ARRAY);
		set(def);
	}
	
	public void set(float[] o)
	{ value = o == null ? new float[0] : o; }
	
	public float[] get()
	{ return value; }
	
	public void setJson(Object o)
	{
		if(o instanceof Double[])
		{
			Double[] d = (Double[])o;
			float[] f = new float[d.length];
			for(int i = 0; i < d.length; i++)
				f[i] = d[i].floatValue();
			set(f);
		}
		else if(o instanceof List<?>)
		{
			setJson(((List<?>)o).toArray(new Float[0]));
		}
		else set(Converter.toFloats((Float[])o));
	}
	
	public Object getJson()
	{ return Converter.fromFloats(get()); }
	
	void write(ByteIOStream io)
	{
		value = get();
		io.writeUShort(value.length);
		for(int i = 0; i < value.length; i++)
			io.writeFloat(value[i]);
	}
	
	void read(ByteIOStream io)
	{
		value = new float[io.readUShort()];
		for(int i = 0; i < value.length; i++)
			value[i] = io.readFloat();
		set(value);
	}
}