package latmod.lib.config;

import com.google.gson.*;

import latmod.lib.*;

public class ConfigEntryIntArray extends ConfigEntry
{
	private IntList value;
	
	public ConfigEntryIntArray(String id, IntList def)
	{
		super(id, PrimitiveType.INT_ARRAY);
		value = new IntList();
		set(def);
		updateDefault();
	}
	
	public void set(IntList l)
	{
		value.clear();
		value.addAll(l);
	}
	
	public IntList get()
	{ return value; }
	
	public final void setJson(JsonElement o, JsonDeserializationContext c)
	{
		JsonArray a = o.getAsJsonArray();
		value.clear();
		for(int i = 0; i < a.size(); i++)
			value.add(a.get(i).getAsInt());
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
			io.writeInt(value.get(i));
	}
	
	public void read(ByteIOStream io)
	{
		value.clear();
		int s = io.readUnsignedShort();
		for(int i = 0; i < s; i++)
			value.add(io.readInt());
		set(value.clone());
	}
}