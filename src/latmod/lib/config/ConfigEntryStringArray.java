package latmod.lib.config;

import com.google.gson.*;

import latmod.lib.*;

public class ConfigEntryStringArray extends ConfigEntry
{
	private FastList<String> value;
	
	public ConfigEntryStringArray(String id, FastList<String> def)
	{
		super(id, PrimitiveType.STRING_ARRAY);
		value = new FastList<String>();
		set(def);
		updateDefault();
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
	
	public String getValue()
	{ return get().toString(); }
	
	void write(ByteIOStream io)
	{
		value = get();
		io.writeUShort(value.size());
		for(int i = 0; i < value.size(); i++)
			io.writeString(value.get(i));
	}
	
	void read(ByteIOStream io)
	{
		value.clear();
		int s = io.readUShort();
		for(int i = 0; i < s; i++)
			value.add(io.readString());
		set(value.clone());
	}
}