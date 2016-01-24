package latmod.lib;

import com.google.gson.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Type for Lists: new TypeToken<List<E>>() {}.getType()
 */
public class LMJsonUtils
{
	private static Gson gson = null;
	private static Gson gson_pretty = null;
	public static JsonDeserializationContext deserializationContext;
	public static JsonSerializationContext serializationContext, prettySerializationContext;
	
	public static Gson getGson(boolean pretty)
	{
		if(gson == null || gson_pretty == null)
		{
			GsonBuilder gb = new GsonBuilder();
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
	
	public static String toJson(Gson gson, JsonElement e)
	{
		if(e == null) return null;
		return gson.toJson(e);
	}
	
	public static boolean toJsonFile(Gson gson, File f, JsonElement o)
	{
		if(o == null) return false;
		
		try
		{
			String s = toJson(gson, o);
			LMFileUtils.save(f, s);
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static String toJson(JsonElement o)
	{ return toJson(getGson(false), o); }
	
	public static boolean toJsonFile(File f, JsonElement o)
	{ return toJsonFile(getGson(true), f, o); }
	
	public static JsonElement fromJson(String json)
	{ return (json == null || json.isEmpty()) ? JsonNull.INSTANCE : new JsonParser().parse(json); }
	
	public static JsonElement fromJson(Reader json)
	{ return (json == null) ? JsonNull.INSTANCE : new JsonParser().parse(json); }
	
	public static JsonElement fromJson(File json)
	{
		try
		{
			if(json == null || !json.exists()) return JsonNull.INSTANCE;
			BufferedReader reader = new BufferedReader(new FileReader(json));
			JsonElement e = fromJson(reader);
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
	
	public static JsonObject join(JsonObject... o)
	{
		JsonObject o1 = new JsonObject();
		
		for(int i = 0; i < o.length; i++)
		{
			if(o[i] != null)
			{
				for(Map.Entry<String, JsonElement> e : o[i].entrySet())
					o1.add(e.getKey(), e.getValue());
			}
		}
		
		return o1;
	}
	
	public static JsonArray join(JsonArray... a)
	{
		JsonArray a1 = new JsonArray();
		
		for(int i = 0; i < a.length; i++)
		{
			if(a[i] != null)
			{
				for(int j = 0; j < a[i].size(); j++)
					a1.add(a[i].get(j));
			}
		}
		
		return a1;
	}
}