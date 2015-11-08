package latmod.lib;

import java.awt.Color;
import java.io.*;
import java.lang.reflect.Type;
import java.util.UUID;

import com.google.gson.*;

import latmod.lib.config.ConfigList;
import latmod.lib.json.*;

/** Type for Lists: new TypeToken<List<E>>() {}.getType() */
public class LMJsonUtils
{
	private static Gson gson = null;
	private static Gson gson_pretty = null;
	private static final FastMap<Class<?>, Object> typeAdapters = new FastMap<Class<?>, Object>();
	private static final FastList<TypeAdapterFactory> typeAdapterFactories = new FastList<TypeAdapterFactory>();
	private static boolean inited = false;
	
	public static final void register(Class<?> c, Object o)
	{ typeAdapters.put(c, o); gson = null; gson_pretty = null; }
	
	public static final void registerFactory(TypeAdapterFactory f)
	{ typeAdapterFactories.add(f); gson = null; gson_pretty = null; }
	
	public static Gson getGson(boolean pretty)
	{
		if(!inited)
		{
			inited = true;
			
			register(IntList.class, new IntList.Serializer());
			register(IntMap.class, new IntMap.Serializer());
			register(UUID.class, new UUIDTypeAdapterLM());
			register(ConfigList.class, new ConfigList.Serializer());
			register(PrimitiveType.class, new PrimitiveType.Serializer());
			register(Color.class, new ColorSerializerLM());
			register(Time.class, new Time.Serializer());
			registerFactory(new EnumSerializerLM());
		}
		
		if(gson == null || gson_pretty == null)
		{
			GsonBuilder gb = new GsonBuilder();
			
			for(int i = 0; i < typeAdapters.size(); i++)
				gb.registerTypeAdapter(typeAdapters.keys.get(i), typeAdapters.values.get(i));
			
			for(int i = 0; i < typeAdapterFactories.size(); i++)
				gb.registerTypeAdapterFactory(typeAdapterFactories.get(i));
			
			//System.out.println(typeAdapters + " + " + typeAdapterFactories);
			
			gson = gb.create();
			gb.setPrettyPrinting();
			gson_pretty = gb.create();
		}
		
		return pretty ? gson_pretty : gson;
	}
	
	public static <T> T fromJson(Gson gson, String s, Type t)
	{ if(s == null) return null; return gson.fromJson(s, t); }
	
	public static <T> T fromJsonFile(Gson gson, File f, Type t)
	{
		if(!f.exists()) return null;
		try { return fromJson(gson, LMStringUtils.readString(new FileInputStream(f)), t); }
		catch(Exception e) { e.printStackTrace(); return null; }
	}
	
	public static String toJson(Gson gson, Object o)
	{ if(o == null) return null; return gson.toJson(o); }
	
	public static boolean toJsonFile(Gson gson, File f, Object o)
	{
		String s = toJson(gson, o);
		if(s == null) return false;
		
		try
		{
			LMFileUtils.save(f, s);
			return true;
		}
		catch(Exception e)
		{ e.printStackTrace(); }
		
		return false;
	}
	
	public static <T> T fromJson(String s, Type t)
	{ return fromJson(getGson(false), s, t); }
	
	public static <T> T fromJsonFile(File f, Type t)
	{ return fromJsonFile(getGson(true), f, t); }
	
	public static String toJson(Object o)
	{ return toJson(getGson(false), o); }
	
	public static boolean toJsonFile(File f, Object o)
	{ return toJsonFile(getGson(true), f, o); }
	
	public static JsonElement getJsonElement(String json)
	{ return (json == null || json.isEmpty()) ? JsonNull.INSTANCE : new JsonParser().parse(json); }
}