package latmod.lib.config;

import com.google.gson.*;
import latmod.lib.*;

import java.io.*;

public class ConfigEntryStringArray extends ConfigEntry
{
	public FastList<String> defValue;
	private FastList<String> value;
	
	public ConfigEntryStringArray(String id, FastList<String> def)
	{
		super(id, PrimitiveType.STRING_ARRAY);
		value = new FastList<>();
		set(def);
		defValue = def == null ? new FastList<String>() : def;
	}
	
	public ConfigEntryStringArray(String id, String... def)
	{
		super(id, PrimitiveType.STRING_ARRAY);
		value = new FastList<>();
		if(def != null && def.length > 0)
			set(new FastList<>(def));
		defValue = new FastList<>(def);
	}
	
	public void set(FastList<String> o)
	{
		value.clear();
		value.addAll(o);
	}
	
	public FastList<String> get()
	{ return value; }
	
	public final void setJson(JsonElement o)
	{
		JsonArray a = o.getAsJsonArray();
		value.clear();
		for(int i = 0; i < a.size(); i++)
			value.add(a.get(i).getAsString());
		set(value.clone());
	}
	
	public final JsonElement getJson()
	{
		JsonArray a = new JsonArray();
		value = get();
		for(int i = 0; i < value.size(); i++)
			a.add(new JsonPrimitive(value.get(i)));
		return a;
	}
	
	public void write(DataOutput io) throws Exception
	{
		value = get();
		io.writeShort(value.size());
		for(int i = 0; i < value.size(); i++)
			io.writeUTF(value.get(i));
	}

	public void read(DataInput io) throws Exception
	{
		value.clear();
		int s = io.readUnsignedShort();
		for(int i = 0; i < s; i++)
			value.add(io.readUTF());
		set(value.clone());
	}

	public void writeExtended(DataOutput io) throws Exception
	{
		write(io);
		io.writeShort(defValue.size());
		for(int i = 0; i < defValue.size(); i++)
			io.writeUTF(defValue.get(i));
	}

	public void readExtended(DataInput io) throws Exception
	{
		read(io);
		int s = io.readUnsignedShort();
		defValue.clear();
		for(int i = 0; i < s; i++)
			defValue.add(io.readUTF());
	}
	
	public String getAsString()
	{ return get().toString(); }
	
	public String[] getAsStringArray()
	{ return get().toStringArray(); }
	
	public boolean getAsBoolean()
	{ return !get().isEmpty(); }
	
	public int[] getAsIntArray()
	{
		value = get();
		int[] ai = new int[value.size()];
		for(int i = 0; i < ai.length; i++)
			ai[i] = Integer.parseInt(value.get(i));
		return ai;
	}
	
	public double[] getAsDoubleArray()
	{
		value = get();
		double[] ai = new double[value.size()];
		for(int i = 0; i < ai.length; i++)
			ai[i] = Double.parseDouble(value.get(i));
		return ai;
	}

	public String getDefValue()
	{ return defValue.toString(); }
}