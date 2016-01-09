package latmod.lib.config;

import com.google.gson.*;
import latmod.lib.*;

import java.io.*;
import java.util.*;

public class ConfigEntryStringArray extends ConfigEntry
{
	public List<String> defValue;
	private List<String> value;
	
	public ConfigEntryStringArray(String id, List<String> def)
	{
		super(id, PrimitiveType.STRING_ARRAY);
		value = new ArrayList<>();
		set(def);
		defValue = def == null ? new ArrayList<String>() : def;
	}
	
	public ConfigEntryStringArray(String id, String... def)
	{
		super(id, PrimitiveType.STRING_ARRAY);
		value = new ArrayList<>();
		if(def != null && def.length > 0) set(Arrays.asList(def));
		defValue = Arrays.asList(def);
	}
	
	public void set(List<String> o)
	{
		value.clear();
		value.addAll(o);
	}
	
	public List<String> get()
	{ return value; }
	
	public final void setJson(JsonElement o)
	{
		JsonArray a = o.getAsJsonArray();
		value.clear();
		for(int i = 0; i < a.size(); i++)
			value.add(a.get(i).getAsString());
		set(LMListUtils.clone(value));
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
		set(LMListUtils.clone(value));
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
	{ return LMListUtils.toStringArray(get()); }
	
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