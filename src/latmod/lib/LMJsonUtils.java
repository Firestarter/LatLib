package latmod.lib;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import latmod.lib.config.ConfigGroup;
import latmod.lib.json.*;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Type for Lists: new TypeToken<List<E>>() {}.getType()
 */
public class LMJsonUtils
{
	private static Gson gson = null;
	private static Gson gson_pretty = null;
	private static final HashMap<Class<?>, Object> typeAdapters = new HashMap<>();
	private static final ArrayList<TypeAdapterFactory> typeAdapterFactories = new ArrayList<>();
	private static boolean inited = false;
	public static JsonDeserializationContext deserializationContext;
	public static JsonSerializationContext serializationContext, prettySerializationContext;
	
	public static final void register(Class<?> c, Object o)
	{
		typeAdapters.put(c, o);
		gson = null;
		gson_pretty = null;
	}
	
	public static final void registerFactory(TypeAdapterFactory f)
	{
		typeAdapterFactories.add(f);
		gson = null;
		gson_pretty = null;
	}
	
	public static Gson getGson(boolean pretty)
	{
		if(!inited)
		{
			inited = true;
			
			register(IntList.class, new IntList.Serializer());
			register(IntMap.class, new IntMap.Serializer());
			register(UUID.class, new UUIDTypeAdapterLM());
			register(ConfigGroup.class, new ConfigGroup.Serializer());
			register(PrimitiveType.class, new PrimitiveType.Serializer());
			register(Color.class, new ColorSerializerLM());
			register(Time.class, new Time.Serializer());
			registerFactory(new EnumSerializerLM());
		}
		
		if(gson == null || gson_pretty == null)
		{
			GsonBuilder gb = new GsonBuilder();
			
			for(Map.Entry<Class<?>, Object> e : typeAdapters.entrySet())
				gb.registerTypeAdapter(e.getKey(), e.getValue());
			
			for(int i = 0; i < typeAdapterFactories.size(); i++)
				gb.registerTypeAdapterFactory(typeAdapterFactories.get(i));
			
			//System.out.println(typeAdapters + " + " + typeAdapterFactories);
			
			gson = gb.create();
			gb.setPrettyPrinting();
			gson_pretty = gb.create();
			
			deserializationContext = new JsonDeserializationContext()
			{
				@SuppressWarnings("unchecked")
				public <T> T deserialize(JsonElement json, Type typeOfT) throws JsonParseException
				{ return (T) gson.fromJson(json, typeOfT); }
			};
			
			serializationContext = new JsonSerializationContext()
			{
				public JsonElement serialize(Object src)
				{ return gson.toJsonTree(src); }
				
				public JsonElement serialize(Object src, Type typeOfSrc)
				{ return gson.toJsonTree(src, typeOfSrc); }
			};
			
			prettySerializationContext = new JsonSerializationContext()
			{
				public JsonElement serialize(Object src)
				{ return gson_pretty.toJsonTree(src); }
				
				public JsonElement serialize(Object src, Type typeOfSrc)
				{ return gson_pretty.toJsonTree(src, typeOfSrc); }
			};
		}
		
		return pretty ? gson_pretty : gson;
	}
	
	public static <T> T fromJson(Gson gson, String s, Type t)
	{
		if(s == null) return null;
		return gson.fromJson(s, t);
	}
	
	public static <T> T fromJsonFile(Gson gson, File f, Type t)
	{
		if(f == null || !f.exists()) return null;
		try
		{
			FileReader reader = new FileReader(f);
			T obj = gson.fromJson(reader, t);
			reader.close();
			return obj;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static String toJson(Gson gson, Object o)
	{
		if(o == null) return null;
		return gson.toJson(o);
	}
	
	public static boolean toJsonFile(Gson gson, File f, Object o)
	{
		if(o == null) return false;
		
		try
		{
			FileWriter writer = new FileWriter(LMFileUtils.newFile(f));
			gson.toJson(o, o.getClass(), new JsonWriter(writer));
			writer.close();
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
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
	
	public static JsonElement getJsonElement(Reader json)
	{ return (json == null) ? JsonNull.INSTANCE : new JsonParser().parse(json); }
	
	public static JsonElement getJsonElement(File json)
	{
		try
		{
			if(json == null || !json.exists()) return JsonNull.INSTANCE;
			Reader reader = new FileReader(json);
			JsonElement e = getJsonElement(reader);
			reader.close();
			return e;
		}
		catch(Exception ex) { }
		return JsonNull.INSTANCE;
	}
	
	// -- //
	
	public static JsonArray toArray(int[] ai)
	{
		if(ai == null) return null;
		JsonArray a = new JsonArray();
		if(ai.length == 0) return a;
		for(int i = 0; i < ai.length; i++)
			a.add(new JsonPrimitive(ai[i]));
		return a;
	}
	
	public static int[] fromArray(JsonElement e)
	{
		if(e == null || e.isJsonNull() || !e.isJsonArray()) return null;
		JsonArray a = e.getAsJsonArray();
		int[] ai = new int[a.size()];
		if(ai.length == 0) return ai;
		for(int i = 0; i < ai.length; i++)
			ai[i] = a.get(i).getAsInt();
		return ai;
	}
}