package latmod.lib;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.*;

public enum PrimitiveType
{
	NULL("nil", Object.class),
	BOOLEAN("bool", Boolean.class),
	STRING("str", String.class),
	INT("num_i", Integer.class),
	BYTE("num_b", Byte.class),
	SHORT("num_s", Short.class),
	LONG("num_l", Long.class),
	FLOAT("num_f", Float.class),
	DOUBLE("num_d", Double.class),
	
	STRING_ARRAY("str_a", String[].class),
	INT_ARRAY("num_i_a", int[].class),
	FLOAT_ARRAY("num_f_a", float[].class),
	DOUBLE_ARRAY("num_d_a", double[].class),
	
	; public static final PrimitiveType[] VALUES = values();
	
	public final String ID;
	public final Class<?> typeClass;
	
	public final boolean isBoolean;
	public final boolean isString;
	public final boolean isNumber;
	public final boolean isArray;
	
	PrimitiveType(String s, Class<?> c)
	{
		ID = s;
		typeClass = c;
		
		isBoolean = c == Boolean.class;
		isString = c == String.class;
		isNumber = s.startsWith("num_");
		isArray = s.endsWith("_a");
	}
	
	public static PrimitiveType get(String s)
	{
		for(PrimitiveType t : values())
			if(t.ID.equals(s)) return t;
		return null;
	}

	public static boolean isNull(PrimitiveType type)
	{ return type == null || type == NULL; }
	
	public static class Serializer extends TypeAdapter<PrimitiveType>
	{
		public PrimitiveType read(JsonReader in) throws IOException
		{
			if(in.peek() == JsonToken.NULL)
			{ in.nextNull(); return null; }
			return get(in.nextString());
		}
		
		public void write(JsonWriter out, PrimitiveType value) throws IOException
		{
			if(value == null) out.nullValue();
			else out.value(value.ID);
		}
	}
}