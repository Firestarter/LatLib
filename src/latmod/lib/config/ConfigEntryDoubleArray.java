package latmod.lib.config;

import com.google.gson.*;

import latmod.lib.*;

public class ConfigEntryDoubleArray extends ConfigEntry
{
	private float[] value;
	
	public ConfigEntryDoubleArray(String id, float[] def)
	{
		super(id, PrimitiveType.DOUBLE_ARRAY);
		set(def);
		updateDefault();
	}
	
	public void set(float[] o)
	{ value = o == null ? new float[0] : o; }
	
	public float[] get()
	{ return value; }
	
	public final void setJson(JsonElement o, JsonDeserializationContext c)
	{
		JsonArray a = o.getAsJsonArray();
		value = new float[a.size()];
		for(int i = 0; i < value.length; i++)
			value[i] = a.get(i).getAsFloat();
		set(value.clone());
	}
	
	public final JsonElement getJson(JsonSerializationContext c)
	{
		JsonArray a = new JsonArray();
		value = get();
		for(int i = 0; i < value.length; i++)
			a.add(new JsonPrimitive(value[i]));
		return a;
	}
	
	public String getValue()
	{
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		sb.append(' ');
		
		for(int i = 0; i < value.length; i++)
		{
			sb.append(value[i]);
			
			if(i != value.length - 1)
			{
				sb.append(',');
				sb.append(' ');
			}
		}
		
		sb.append(' ');
		sb.append(']');
		return sb.toString();
	}
	
	public void write(ByteIOStream io)
	{
		value = get();
		io.writeShort(value.length);
		for(int i = 0; i < value.length; i++)
			io.writeFloat(value[i]);
	}
	
	public void read(ByteIOStream io)
	{
		value = new float[io.readUnsignedShort()];
		for(int i = 0; i < value.length; i++)
			value[i] = io.readFloat();
		set(value);
	}
}