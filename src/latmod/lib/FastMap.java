package latmod.lib;
import java.util.*;

/** Made by LatvianModder */
public class FastMap<K, V> implements Iterable<V>
{
	public FastList<K> keys;
	public FastList<V> values;
	private boolean nullRemoves = true;
	
	public FastMap(int init, int inc)
	{
		keys = new FastList<K>(init, inc);
		values = new FastList<V>(init, inc);
	}
	
	public FastMap<K, V> setLocked()
	{ keys.setLocked(); values.setLocked(); return this; }
	
	public FastMap<K, V> setNullRemoves(boolean b)
	{ nullRemoves = b; return this; }
	
	public FastMap(int init)
	{ this(init, 5); }
	
	public FastMap()
	{ this(10); }
	
	public int size()
	{ return values.size(); }
	
	public V get(Object o)
	{ int i = keys.indexOf(o);
	return (i == -1) ? null : values.get(i); }
	
	public K getKey(Object o)
	{ int i = values.indexOf(o);
	return (i == -1) ? null : keys.get(i); }
	
	public boolean put(K k, V v)
	{
		if(k == null || keys.isLocked()) return false;
		
		int i = keys.indexOf(k);
		if(i != -1)
		{
			if(nullRemoves && v == null)
			{
				keys.remove(i);
				values.remove(i);
			}
			else
			{
				keys.set(i, k);
				values.set(i, v);
			}
			
			return false;
		}
		else
		{
			if(nullRemoves && v == null)
				return false;
			
			keys.add(k);
			values.add(v);
			return true;
		}
	}
	
	public boolean remove(K k)
	{
		if(keys.isLocked()) return false;
		int i = keys.indexOf(k);
		if(i != -1)
		{
			keys.remove(i);
			values.remove(i);
			return true;
		}
		
		return false;
	}
	
	public boolean removeValue(V v)
	{
		if(keys.isLocked()) return false;
		int i = values.indexOf(v);
		if(i != -1)
		{
			keys.remove(i);
			values.remove(i);
			return true;
		}
		
		return false;
	}
	
	public boolean clear()
	{
		if(keys.isLocked() || isEmpty()) return false;
		keys.clear();
		values.clear();
		return true;
	}
	
	public FastMap<K, V> clone()
	{
		FastMap<K, V> map1 = new FastMap<K, V>();
		map1.keys = keys.clone();
		map1.values = values.clone();
		if(keys.isLocked()) map1.setLocked();
		return map1;
	}
	
	public void removeAllKeys(FastList<? extends K> al)
	{
		if(keys.isLocked()) return;
		for(int i = 0; i < al.size(); i++)
			remove(al.get(i));
	}
	
	public void removeAllValues(FastList<? extends V> al)
	{
		if(keys.isLocked()) return;
		for(int i = 0; i < al.size(); i++)
			removeValue(al.get(i));
	}
	
	public boolean isEmpty()
	{ return keys.isEmpty(); }
	
	public int putAll(FastMap<K, V> map)
	{
		if(map.isEmpty() || keys.isLocked()) return 0;
		Iterator<K> itrK = map.keys.iterator();
		Iterator<V> itrV = map.values.iterator();
		while(itrK.hasNext() && itrV.hasNext())
			put(itrK.next(), itrV.next());
		return map.size();
	}
	
	public int putAll(Map<K, V> map)
	{
		if(map.isEmpty() || keys.isLocked()) return 0;
		Iterator<K> itrK = map.keySet().iterator();
		Iterator<V> itrV = map.values().iterator();
		while(itrK.hasNext() && itrV.hasNext())
			put(itrK.next(), itrV.next());
		return map.size();
	}

	public Iterator<V> iterator()
	{ return values.iterator(); }
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("{ ");
		
		for(int i = 0; i < size(); i++)
		{
			sb.append(keys.get(i));
			sb.append(": ");
			sb.append(values.get(i));
			
			if(i != size() - 1)
			sb.append(", ");
		}
		
		sb.append(" }");
		return sb.toString();
	}
	
	public FastMap<V, K> inverse()
	{
		FastMap<V, K> map = new FastMap<V, K>();
		map.keys.addAll(values);
		map.values.addAll(keys);
		return map;
	}

	public void sortFromKeyStrings(final boolean ignoreCase)
	{
		if(keys.isLocked() || keys.isEmpty()) return;
		
		class Obj implements Comparable<Obj>
		{
			public final K key;
			public final V val;
			
			public Obj(K k, V v)
			{ key = k; val = v; }
			
			public int compareTo(Obj o)
			{
				if(!ignoreCase) return key.toString().compareTo(o.key.toString());
				return key.toString().compareToIgnoreCase(o.key.toString());
			}
		}
		
		FastList<Obj> list = new FastList<Obj>();
		for(int i = 0; i < size(); i++)
			list.add(new Obj(keys.get(i), values.get(i)));
		list.sort(null);
		clear();
		for(int i = 0; i < list.size(); i++)
		{ Obj o = list.get(i); put(o.key, o.val); }
	}
	
	public void sortFromKeyNums()
	{
		if(keys.isLocked() || keys.isEmpty()) return;
		
		class Obj implements Comparable<Obj>
		{
			public final K key;
			public final V val;
			public final long keyLong;
			
			public Obj(K k, V v)
			{ key = k; val = v; keyLong = ((Number)k).longValue(); }
			
			public int compareTo(Obj o)
			{ return Long.compare(keyLong, o.keyLong); }
		}
		
		FastList<Obj> list = new FastList<Obj>();
		for(int i = 0; i < size(); i++)
			list.add(new Obj(keys.get(i), values.get(i)));
		list.sort(null);
		clear();
		for(int i = 0; i < list.size(); i++)
		{ Obj o = list.get(i); put(o.key, o.val); }
	}
	
	/*public static class Serializer implements JsonDeserializer<FastMap>, JsonSerializer<FastMap>
	{
		public JsonElement serialize(FastMap src, Type typeOfSrc, JsonSerializationContext context)
		{
			com.google.gson.GsonBuilder
			JsonObject o = new JsonObject();
			for(int i = 0; i < src.size(); i++)
				o.add(context.serialize(src.keys.get(i)), context.serialize(src.values.get(i)));
			return o;
		}
		
		public FastMap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			FastMap map = new IntMap();
			JsonObject o = json.getAsJsonObject();
			
			for(Map.Entry<String, JsonElement> e : o.entrySet())
			{
				Integer i = MathHelperLM.decode(e.getKey());
				if(i != null) map.put(i.intValue(), e.getValue().getAsInt());
			}
			
			return map;
		}
	}
	
	public static class TypeSerializer implements TypeAdapterFactory
	{
		public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type)
		{
			return null;
		}
	}*/
}