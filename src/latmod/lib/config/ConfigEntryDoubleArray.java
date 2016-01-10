package latmod.lib.config;

import com.google.gson.*;
import latmod.lib.PrimitiveType;

import java.io.*;

public class ConfigEntryDoubleArray extends ConfigEntry
{
	private static final double[] emptyDoubleArray = new double[0];
	
	public double[] defValue;
	private double[] value;
	
	public ConfigEntryDoubleArray(String id, double[] def)
	{
		super(id, PrimitiveType.DOUBLE_ARRAY);
		set(def);
		defValue = def == null ? emptyDoubleArray : def;
	}
	
	public void set(double[] o)
	{ value = o == null ? emptyDoubleArray : o; }
	
	public double[] get()
	{ return value; }
	
	public final void setJson(JsonElement o)
	{
		JsonArray a = o.getAsJsonArray();
		value = new double[a.size()];
		for(int i = 0; i < value.length; i++)
			value[i] = a.get(i).getAsFloat();
		set(value.clone());
	}
	
	public final JsonElement getJson()
	{
		JsonArray a = new JsonArray();
		value = get();
		for(int i = 0; i < value.length; i++)
			a.add(new JsonPrimitive(value[i]));
		return a;
	}
	
	public void write(DataOutput io) throws Exception
	{
		value = get();
		io.writeShort(value.length);
		for(int i = 0; i < value.length; i++)
			io.writeDouble(value[i]);
	}
	
	public void read(DataInput io) throws Exception
	{
		value = new double[io.readUnsignedShort()];
		for(int i = 0; i < value.length; i++)
			value[i] = io.readFloat();
		set(value);
	}
	
	public void writeExtended(DataOutput io) throws Exception
	{
		write(io);
		io.writeShort(defValue.length);
		for(int i = 0; i < defValue.length; i++)
			io.writeDouble(defValue[i]);
	}
	
	public void readExtended(DataInput io) throws Exception
	{
		read(io);
		defValue = new double[io.readUnsignedShort()];
		for(int i = 0; i < value.length; i++)
			defValue[i] = io.readFloat();
	}
	
	public String getAsString()
	{
		value = get();
		
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		sb.append(' ');
		
		if(value.length > 0)
		{
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
		}
		
		sb.append(']');
		return sb.toString();
	}
	
	public String[] getAsStringArray()
	{
		value = get();
		String[] s = new String[value.length];
		for(int i = 0; i < s.length; i++)
			s[i] = Double.toString(value[i]);
		return s;
	}
	
	public int[] getAsIntArray()
	{
		value = get();
		int[] a = new int[value.length];
		for(int i = 0; i < a.length; i++)
			a[i] = (int) value[i];
		return a;
	}
	
	public double[] getAsDoubleArray()
	{ return value; }
	
	public String getDefValue()
	{
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		sb.append(' ');
		
		if(defValue.length > 0)
		{
			for(int i = 0; i < defValue.length; i++)
			{
				sb.append(defValue[i]);
				
				if(i != defValue.length - 1)
				{
					sb.append(',');
					sb.append(' ');
				}
			}
			
			sb.append(' ');
		}
		
		sb.append(']');
		return sb.toString();
	}
}