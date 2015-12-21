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
	
	public ConfigEntryStringArray(String id, String... def)
	{
		super(id, PrimitiveType.STRING_ARRAY);
		value = new FastList<String>();
		
		if(def != null && def.length > 0)
			set(new FastList<String>(def));
		
		updateDefault();
	}
	
	public void set(FastList<String> o)
	{
		value.clear();
		value.addAll(o);
	}
	
	public FastList<String> get()
	{ return value; }
	
	public final void setJson(JsonElement o, JsonDeserializationContext c)
	{
		JsonArray a = o.getAsJsonArray();
		value.clear();
		for(int i = 0; i < a.size(); i++)
			value.add(a.get(i).getAsString());
		set(value.clone());
	}
	
	public final JsonElement getJson(JsonSerializationContext c)
	{
		JsonArray a = new JsonArray();
		value = get();
		for(int i = 0; i < value.size(); i++)
			a.add(new JsonPrimitive(value.get(i)));
		return a;
	}
	
	public String getValue()
	{ return get().toString(); }
	
	public void write(ByteIOStream io)
	{
		value = get();
		io.writeShort(value.size());
		for(int i = 0; i < value.size(); i++)
			io.writeUTF(value.get(i));
	}
	
	public void read(ByteIOStream io)
	{
		value.clear();
		int s = io.readUnsignedShort();
		for(int i = 0; i < s; i++)
			value.add(io.readUTF());
		set(value.clone());
	}
}