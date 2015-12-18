package latmod.lib;
import java.util.*;

/** Made by LatvianModder */
public class FastMap<K, V> implements Iterable<V>, Map<K, V>
{
	public final FastList<K> keys;
	public final FastList<V> values;
	private boolean nullRemoves = true;
	
	private FastMap(List<K> k, List<V> v)
	{ keys = FastList.asList(k); values = FastList.asList(v); }
	
	public FastMap(int init)
	{ this(new FastList<K>(init), new FastList<V>(init)); }
	
	public FastMap()
	{ this(new FastList<K>(), new FastList<V>()); }
	
	public FastMap<K, V> allowNullValues()
	{ nullRemoves = false; return this; }
	
	public int size()
	{ return values.size(); }
	
	public V get(Object o)
	{
		int i = keys.indexOf(o);
		return i == -1 ? null : values.get(i);
	}
	
	public K getKey(Object o)
	{
		int i = values.indexOf(o);
		return i == -1 ? null : keys.get(i);
	}
	
	public V put(K k, V v)
	{
		if(k == null) return null;
		
		int i = keys.indexOf(k);
		if(i != -1)
		{
			V v0 = values.get(i);
			
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
			
			return v0;
		}
		else
		{
			keys.add(k);
			values.add(v);
			return null;
		}
	}
	
	public V remove(Object k)
	{
		int i = keys.indexOf(k);
		if(i != -1)
		{
			V v = values.get(i);
			keys.remove(i);
			values.remove(i);
			return v;
		}
		
		return null;
	}
	
	public K removeValue(V v)
	{
		int i = values.indexOf(v);
		if(i != -1)
		{
			K k = keys.get(i);
			keys.remove(i);
			values.remove(i);
			return k;
		}
		
		return null;
	}
	
	public void clear()
	{ if(!isEmpty()) { keys.clear(); values.clear(); } }
	
	public void removeAllKeys(FastList<? extends K> al)
	{
		for(int i = 0; i < al.size(); i++)
			remove(al.get(i));
	}
	
	public void removeAllValues(FastList<? extends V> al)
	{
		for(int i = 0; i < al.size(); i++)
			removeValue(al.get(i));
	}
	
	public boolean isEmpty()
	{ return keys.isEmpty(); }
	
	public int putAll(FastMap<K, V> map)
	{
		if(map == null || map.isEmpty()) return 0;
		for(int i = 0; i < map.size(); i++)
			put(map.keys.get(i), map.values.get(i));
		return map.size();
	}
	
	public Iterator<V> iterator()
	{ return values.iterator(); }
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		sb.append(' ');
		
		for(int i = 0; i < size(); i++)
		{
			sb.append(keys.get(i));
			sb.append(':');
			sb.append(' ');
			sb.append(values.get(i));
			
			if(i != size() - 1)
			{
				sb.append(',');
				sb.append(' ');
			}
		}
		
		sb.append(' ');
		sb.append('}');
		return sb.toString();
	}
	
	public FastMap<V, K> inverse()
	{
		FastMap<V, K> map = new FastMap<V, K>();
		map.keys.addAll(values);
		map.values.addAll(keys);
		return map;
	}
	
	public HashMap<K, V> toHashMap()
	{
		HashMap<K, V> map = new HashMap<K, V>();
		for(int i = 0; i < size(); i++)
			map.put(keys.get(i), values.get(i));
		return map;
	}

	public void sortFromKeyStrings(final boolean ignoreCase)
	{
		if(size() < 2) return;
		
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
		if(size() < 2) return;
		
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
	
	public Set<java.util.Map.Entry<K, V>> entrySet()
	{
		FastList<java.util.Map.Entry<K, V>> l = new FastList<java.util.Map.Entry<K, V>>();
		for(int i = 0; i < size(); i++)
			l.add(new MapEntry<K, V>(keys.get(i), values.get(i)));
		return l;
	}
	
	public static <K1, V1> FastMap<K1, V1> fromMap(Map<K1, V1> map)
	{
		FastMap<K1, V1> map1 = new FastMap<K1, V1>();
		map1.putAll(map); return map1;
	}
	
	public FastMap<K, V> getLocked()
	{ return new FastMap<K, V>(); }
	
	public boolean containsKey(Object key)
	{ return keys.contains(key); }
	
	public boolean containsValue(Object value)
	{ return values.contains(value); }
	
	public void putAll(Map<? extends K, ? extends V> map)
	{
		if(map == null || map.isEmpty()) return;
		Iterator<? extends K> itrK = map.keySet().iterator();
		Iterator<? extends V> itrV = map.values().iterator();
		while(itrK.hasNext() && itrV.hasNext())
			put(itrK.next(), itrV.next());
	}
	
	public Set<K> keySet()
	{ return keys; }
	
	public Collection<V> values()
	{ return values; }
}