package latmod.lib.config;

import com.google.gson.*;

import latmod.lib.*;

public class ConfigEntryEnumExtended extends ConfigEntry implements IClickableConfigEntry
{
	public final FastList<String> values;
	public String value;
	
	public ConfigEntryEnumExtended(String id)
	{
		super(id, PrimitiveType.ENUM);
		values = new FastList<String>();
	}
	
	public int getIndex()
	{ return values.indexOf(value); }
	
	public final void setJson(JsonElement o, JsonDeserializationContext c)
	{ value = o.getAsString(); }
	
	public final JsonElement getJson(JsonSerializationContext c)
	{ return new JsonPrimitive(value); }
	
	public String getValue()
	{ return value; }
	
	public void write(ByteIOStream io)
	{ io.writeString(value); }
	
	public void read(ByteIOStream io)
	{ value = io.readString(); }
	
	public void readExtended(ByteIOStream io)
	{
		value = io.readString();
		values.clear();
		int s = io.readUByte();
		for(int i = 0; i < s; i++)
			values.add(io.readString());
	}
	
	public void onClicked()
	{ value = values.get((getIndex() + 1) % values.size()); }
}