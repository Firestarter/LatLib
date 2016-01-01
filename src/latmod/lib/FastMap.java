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
			int i = 0;
			for(Entry<K, V> e : entrySet())
			{
				sb.append(e.getKey());
				sb.append(':');
				sb.append(' ');
				sb.append(e.getValue());

				if(i != s - 1)
				{
					sb.append(',');
					sb.append(' ');
				}
				i++;
			}
			
			sb.append(' ');
		}
		
		sb.append('}');
		return sb.toString();
	}
	
	public String[] getKeyStringArray()
	{
		String[] s = new String[size()];
		if(s.length == 0) return s;
		int i = -1;
		for(Object o : keySet())
			s[++i] = String.valueOf(o);
		return s;
	}

	public String[] getValueStringArray()
	{
		String[] s = new String[size()];
		if(s.length == 0) return s;
		int i = -1;
		for(Object o : values())
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

	public List<Entry<K, V>> sortedEntryList(Comparator<? super Entry<K, V>> c)
	{
		FastList<Entry<K, V>> list = new FastList<>();

		if(c == null) c = new Comparator<Entry<K, V>>()
		{
			public int compare(Entry<K, V> o1, Entry<K, V> o2)
			{ return ((Comparable)o1.getKey()).compareTo(o2.getKey()); }
		};

		list.addAll(entrySet());
		Collections.sort(list, c);
		return list;
	}

	public List<V> values(Comparator<Entry<K, V>> c)
	{
		FastList<V> list = new FastList<>();
		for(Entry<K, V> entry : sortedEntryList(c))
			list.add(entry.getValue());
		return list;
	}

	public Comparator<Entry<K, V>> byKeyNames(final boolean ignoreCase)
	{
		return new Comparator<Entry<K, V>>()
		{
			public int compare(Entry<K, V> o1, Entry<K, V> o2)
			{
				if(ignoreCase) return String.valueOf(o1.getKey()).compareToIgnoreCase(String.valueOf(o2.getKey()));
				else return String.valueOf(o1.getKey()).compareTo(String.valueOf(o2.getKey()));
			}
		};
	}

	public Comparator<Entry<K, V>> byKeyNumbers()
	{
		return new Comparator<Entry<K, V>>()
		{
			public int compare(Entry<K, V> o1, Entry<K, V> o2)
			{
				Number n1 = ((Number)o1.getKey());
				Number n2 = ((Number)o1.getKey());
				return Long.compare((n1 == null) ? 0L : n1.longValue(), (n2 == null) ? 0L : n2.longValue());
			}
		};
	}
}