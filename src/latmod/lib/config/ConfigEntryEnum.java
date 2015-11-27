package latmod.lib.config;

import com.google.gson.*;

import latmod.lib.*;

public class ConfigEntryEnum<E extends Enum<?>> extends ConfigEntry // EnumTypeAdapterFactory
{
	public final Class<E> enumClass;
	private E value;
	
	public ConfigEntryEnum(String id, Class<E> c, E def)
	{ super(id, PrimitiveType.ENUM); enumClass = c; value = def; }
	
	@SuppressWarnings("unchecked")
	public void set(Object o)
	{ value = (E)o; }
	
	public E get()
	{ return value; }
	
	public final void setJson(JsonElement o)
	{ set(fromString(o.getAsString())); }
	
	@SuppressWarnings("unchecked")
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
	
	public final JsonElement getJson()
	{ return new JsonPrimitive(get().name().toLowerCase()); }
	
	void write(ByteIOStream io)
	{ io.writeString(get().name()); }
	
	void read(ByteIOStream io)
	{ fromString(io.readString()); }
}