package latmod.core.util;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class LMJsonUtils
{
	private static Gson gson = null;
	private static Gson gson_pretty = null;
	public static boolean jsonPrettyPrinting = false;
	private static final FastMap<Class<?>, Object> typeAdapters = new FastMap<Class<?>, Object>();
	
	public static final void register(Class<?> c, Object o)
	{
		typeAdapters.put(c, o);
		gson = null;
	}
	
	static
	{
		register(IntList.class, new IntList.Serializer());
		register(IntMap.class, new IntMap.Serializer());
		register(UUID.class, new UUIDTypeAdapterLM());
	}
	
	public static Gson getGson()
	{
		if(gson == null || gson_pretty == null)
		{
			GsonBuilder gb = new GsonBuilder();
			
			for(int i = 0; i < typeAdapters.size(); i++)
				gb.registerTypeAdapter(typeAdapters.keys.get(i), typeAdapters.values.get(i));
			
			gson = gb.create();
			gb.setPrettyPrinting();
			gson_pretty = gb.create();
		}
		
		return jsonPrettyPrinting ? gson_pretty : gson;
	}
	
	public static <T> T fromJson(String s, Type t)
	{
		if(s == null || s.length() < 2) return null;
		return getGson().fromJson(s, t);
	}
	
	public static <T> T fromJsonFile(File f, Type t)
	{
		if(!f.exists()) return null;
		try { return fromJson(LMStringUtils.toString(new FileInputStream(f)), t); }
		catch(Exception e) { e.printStackTrace(); return null; }
	}
	
	public static String toJson(Object o)
	{
		if(o == null) return null;
		return getGson().toJson(o);
	}
	
	public static boolean toJsonFile(File f, Object o)
	{
		jsonPrettyPrinting = true;
		String s = toJson(o);
		jsonPrettyPrinting = false;
		if(s == null) return false;
		
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(LMFileUtils.newFile(f)));
			writer.write(s);
			writer.close();
			return true;
		}
		catch(Exception e)
		{ e.printStackTrace(); }
		return false;
	}
	
	public static <K, V> Type getMapType(Type K, Type V)
	{ return new TypeToken<Map<K, V>>() {}.getType(); }
	
	public static <E> Type getListType(Type E)
	{ return new TypeToken<List<E>>() {}.getType(); }
	
	public static String formatJson(String s, boolean array)
	{
		if(s == null) return null;
		s = s.trim();
		
		char c0 = array ? '[' : '{';
		char c1 = array ? ']' : '}';
		
		if(s.length() < 2) return c0 + "" + c1;
		StringBuilder sb = new StringBuilder();
		if(s.charAt(0) != c0) sb.append(c0);
		sb.append(s);
		if(s.charAt(s.length() - 1) != c1) sb.append(c1);
		return sb.toString();
	}
}