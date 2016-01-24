package latmod.lib.json;

import com.google.gson.*;
import latmod.lib.ByteIOStream;

import java.util.*;

/**
 * Created by LatvianModder on 23.01.2016.
 */
public class JsonElementIO
{
	public static final byte ID_NULL = 0;
	public static final byte ID_ARRAY = 1;
	public static final byte ID_OBJECT = 2;
	public static final byte ID_P_STRING = 3;
	public static final byte ID_P_BOOL = 4;
	public static final byte ID_P_BYTE = 5;
	public static final byte ID_P_SHORT = 6;
	public static final byte ID_P_INT = 7;
	public static final byte ID_P_LONG = 8;
	public static final byte ID_P_FLOAT = 9;
	public static final byte ID_P_DOUBLE = 10;
	
	private static final String[] names = {"null", "array", "object", "string", "bool", "byte", "short", "int", "long", "float", "double"};
	
	public static String getName(byte id)
	{ return names[id]; }
	
	public static byte getID(JsonElement e)
	{
		if(e == null || e.isJsonNull()) return ID_NULL;
		else if(e.isJsonArray()) return ID_ARRAY;
		else if(e.isJsonObject()) return ID_OBJECT;
		else
		{
			JsonPrimitive p = e.getAsJsonPrimitive();
			
			if(p.isString()) return ID_P_STRING;
			else if(p.isBoolean()) return ID_P_BOOL;
			else
			{
				Number n = p.getAsNumber();
				
				System.out.println(n.getClass());
				
				if(n instanceof Integer) return ID_P_INT;
				else if(n instanceof Byte) return ID_P_BYTE;
				else if(n instanceof Short) return ID_P_SHORT;
				else if(n instanceof Long) return ID_P_LONG;
				else if(n instanceof Float) return ID_P_FLOAT;
				else if(n instanceof Double) return ID_P_DOUBLE;
				else return ID_NULL;
			}
		}
	}
	
	public static JsonElement read(ByteIOStream io)
	{
		byte id = io.readByte();
		
		if(id == ID_NULL) return JsonNull.INSTANCE;
		else if(id == ID_ARRAY)
		{
			JsonArray a = new JsonArray();
			int s = io.readInt();
			
			for(int i = 0; i < s; i++)
				a.add(read(io));
			
			return a;
		}
		else if(id == ID_OBJECT)
		{
			JsonObject o = new JsonObject();
			int s = io.readInt();
			
			for(int i = 0; i < s; i++)
			{
				String key = io.readUTF();
				o.add(key, read(io));
			}
			
			return o;
		}
		else if(id == ID_P_STRING) return new JsonPrimitive(io.readUTF());
		else if(id == ID_P_BOOL) return new JsonPrimitive(io.readBoolean());
		else
		{
			Number n = new Integer(0);
			if(id == ID_P_BYTE) n = Byte.valueOf(io.readByte());
			else if(id == ID_P_SHORT) n = Short.valueOf(io.readShort());
			else if(id == ID_P_INT) n = Integer.valueOf(io.readInt());
			else if(id == ID_P_LONG) n = Long.valueOf(io.readLong());
			else if(id == ID_P_FLOAT) n = Float.valueOf(io.readFloat());
			else if(id == ID_P_DOUBLE) n = Double.valueOf(io.readDouble());
			return new JsonPrimitive(n);
		}
	}
	
	public static void read(ByteIOStream io, IJsonSet i)
	{ i.setJson(read(io)); }
	
	public static void write(ByteIOStream io, JsonElement e)
	{
		System.out.println(getName(getID(e)));
		
		if(e == null || e.isJsonNull()) io.writeByte(ID_NULL);
		else if(e.isJsonArray())
		{
			io.writeByte(ID_ARRAY);
			
			JsonArray a = e.getAsJsonArray();
			int s = a.size();
			io.writeInt(s);
			
			for(int i = 0; i < s; i++)
				write(io, a.get(i));
		}
		else if(e.isJsonObject())
		{
			io.writeByte(ID_OBJECT);
			
			Set<Map.Entry<String, JsonElement>> set = e.getAsJsonObject().entrySet();
			io.writeInt(set.size());
			
			for(Map.Entry<String, JsonElement> entry : set)
			{
				io.writeUTF(entry.getKey());
				write(io, entry.getValue());
			}
		}
		else
		{
			JsonPrimitive p = e.getAsJsonPrimitive();
			
			if(p.isString())
			{
				io.writeByte(ID_P_STRING);
				io.writeUTF(p.getAsString());
			}
			else if(p.isBoolean())
			{
				io.writeByte(ID_P_BOOL);
				io.writeBoolean(p.getAsBoolean());
			}
			else
			{
				Number n = p.getAsNumber();
				
				System.out.println(n.getClass());
				
				if(n instanceof Integer)
				{
					io.writeByte(ID_P_INT);
					io.writeInt(n.intValue());
				}
				else if(n instanceof Byte)
				{
					io.writeByte(ID_P_BYTE);
					io.writeByte(n.byteValue());
				}
				else if(n instanceof Short)
				{
					io.writeByte(ID_P_SHORT);
					io.writeShort(n.shortValue());
				}
				else if(n instanceof Long)
				{
					io.writeByte(ID_P_LONG);
					io.writeLong(n.longValue());
				}
				else if(n instanceof Float)
				{
					io.writeByte(ID_P_FLOAT);
					io.writeFloat(n.floatValue());
				}
				else if(n instanceof Double)
				{
					io.writeByte(ID_P_DOUBLE);
					io.writeDouble(n.doubleValue());
				}
				else io.writeByte(ID_NULL);
			}
		}
	}
	
	public static void write(ByteIOStream io, IJsonGet i)
	{ write(io, i.getJson()); }
}
