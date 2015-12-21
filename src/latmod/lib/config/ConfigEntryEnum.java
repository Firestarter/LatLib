package latmod.lib.config;

import com.google.gson.*;

import latmod.lib.*;
import latmod.lib.util.EnumEnabled;

@SuppressWarnings("all")
public class ConfigEntryEnum<E extends Enum<E>> extends ConfigEntry implements IClickableConfigEntry // EnumTypeAdapterFactory
{
	public final Class<E> enumClass;
	public final FastList<E> values;
	private E value;
	
	public ConfigEntryEnum(String id, Class<E> c, E[] val, E def, boolean addNull)
	{
		super(id, PrimitiveType.ENUM);
		enumClass = c;
		values = new FastList<E>();
		values.setWeakIndexing();
		if(addNull) values.add(null);
		values.addAll(val);
		set(def);
		updateDefault();
	}
	
	public static ConfigEntryEnum<EnumEnabled> enabledWithNull(String id, EnumEnabled def)
	{ return new ConfigEntryEnum<EnumEnabled>(id, EnumEnabled.class, EnumEnabled.VALUES, def, true); }
	
	public void set(Object o)
	{ value = (E)o; }
	
	public E get()
	{ return value; }
	
	public static <T extends Enum<?>> String getName(T e)
	{ return e == null ? "-" : e.name().toLowerCase(); }
	
	public int getIndex()
	{ return values.indexOf(get()); }
	
	private E fromString(String s)
	{
		try
		{
			Object[] o1 = enumClass.getEnumConstants();
			if(o1 == null) return null;
			
			for(int i = 0; i < o1.length; i++)
			{
				Enum<?> e = (Enum<?>)o1[i];
				if(e.name().equalsIgnoreCase(s))
				{ return (E)e; }
			}
		}
		catch(Exception e)
		{ e.printStackTrace(); }
		
		return null;
	}
	
	public final void setJson(JsonElement o, JsonDeserializationContext c)
	{ set(fromString(o.getAsString())); }
	
	public final JsonElement getJson(JsonSerializationContext c)
	{ return new JsonPrimitive(getName(get())); }
	
	public String getValue()
	{ return getName(get()); }
	
	public void write(ByteIOStream io)
	{ io.writeUTF(getName(get())); }
	
	public void read(ByteIOStream io)
	{ fromString(io.readUTF()); }
	
	public void writeExtended(ByteIOStream io)
	{
		io.writeUTF(getName(get()));
		io.writeByte(values.size());
		for(int i = 0; i < values.size(); i++)
			io.writeUTF(getName(values.get(i)));
	}
	
	public void onClicked()
	{ set(values.get((getIndex() + 1) % values.size())); }
}