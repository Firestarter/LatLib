package latmod.core.util;

import java.io.IOException;
import java.util.*;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.*;

public class EnumSerializerLM implements TypeAdapterFactory
{
	@SuppressWarnings("all")
	public TypeAdapter create(Gson gson, TypeToken type)
	{
		Class<?> c = type.getRawType();
		
		if (!c.isEnum()) return null;
		else
		{
			final HashMap<String, Object> map = new HashMap<String, Object>();
			Object[] o = c.getEnumConstants();
			
			for (int i = 0; i < o.length; i++)
				map.put(lowerCaseName(o[i]), o[i]);
			
			return new TypeAdapter()
			{
				public void write(JsonWriter out, Object value) throws IOException
				{
					if (value == null) out.nullValue();
					else out.value(lowerCaseName(value));
				}
				
				public Object read(JsonReader in) throws IOException
				{
					if (in.peek() == JsonToken.NULL) { in.nextNull(); return null; }
					else
					{
						String s = in.nextString();
						if(s != null) s = s.toLowerCase();
						return map.get(s);
					}
				}
			};
		}
	}
	
	public static String lowerCaseName(Object o)
	{ return o instanceof Enum ? ((Enum<?>)o).name().toLowerCase(Locale.US) : o.toString().toLowerCase(Locale.US); }
}