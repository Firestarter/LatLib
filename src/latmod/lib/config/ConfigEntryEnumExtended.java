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
	
	public final void setJson(JsonElement o)
	{ value = o.getAsString(); }
	
	public final JsonElement getJson()
	{ return new JsonPrimitive(value); }
	
	public void write(ByteIOStream io)
	{ io.writeUTF(value); }
	
	public void read(ByteIOStream io)
	{ value = io.readUTF(); }
	
	public void readExtended(ByteIOStream io)
	{
		value = io.readUTF();
		values.clear();
		int s = io.readUnsignedByte();
		for(int i = 0; i < s; i++)
			values.add(io.readUTF());
	}
	
	public void onClicked()
	{ value = values.get((getIndex() + 1) % values.size()); }
	
	public String getAsString()
	{ return value; }
	
	public boolean getAsBoolean()
	{ return value != null; }
	
	public int getAsInt()
	{ return getIndex(); }
}