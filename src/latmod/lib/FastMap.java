package latmod.lib;
import java.util.*;

/** Made by LatvianModder */
public class FastMap<K, V> extends HashMap<K, V> implements Iterable<V>
{
	private static final long serialVersionUID = 1L;
	
	private boolean nullRemoves = true;
	
	public FastMap(int init, float loadFactor)
	{ super(init, loadFactor); }
	
	public FastMap(int init)
	{ super(init); }
	
	public FastMap()
	{ super(); }
	
	public FastMap<K, V> allowNullValues()
	{ nullRemoves = false; return this; }
	
	public V put(K k, V v)
	{
		if(v == null && nullRemoves)
		{
			V v0 = get(k);
			remove(k);
			return v0;
		}
		
		return super.put(k, v);
	}
	
	public void removeAllKeys(List<? extends K> al)
	{
		for(int i = 0; i < al.size(); i++)
			remove(al.get(i));
	}
	
	public Iterator<V> iterator()
	{ return values().iterator(); }
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		sb.append(' ');
		
		int s = size();
		
		if(s > 0)
		{
			int i = -1;
			for(Entry<K, V> e : entrySet())
			{
				sb.append(e.getKey());
				sb.append(':');
				sb.append(' ');
				sb.append(e.getValue());
				
				if((i++) != s - 1)
				{
					sb.append(',');
					sb.append(' ');
				}
			}
			
			sb.append(' ');
		}
		
		sb.append('}');
		return sb.toString();
	}
	
	public String[] getKeyStringArray()
	{
		String[] s = new String[size()];
		int i = -1;
		for(Object o : keySet())
			s[++i] = String.valueOf(o);
		return s;
	}
	
	public FastMap<V, K> inverse()
	{
		FastMap<V, K> map = new FastMap<V, K>();
		for(Entry<K, V> e : entrySet())
			map.put(e.getValue(), e.getKey());
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
		for(Entry<K, V> e : entrySet())
			list.add(new Obj(e.getKey(), e.getValue()));
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
		for(Entry<K, V> e : entrySet())
			list.add(new Obj(e.getKey(), e.getValue()));
		list.sort(null);
		clear();
		for(int i = 0; i < list.size(); i++)
		{ Obj o = list.get(i); put(o.key, o.val); }
	}
}