package latmod.lib;

import java.util.Map;

public class MapEntry<K, V> implements Map.Entry<K, V>
{
	private K key;
	private V val;
	
	public MapEntry(K k, V v)
	{ key = k; val = v; }
	
	public K getKey()
	{ return key; }
	
	public V getValue()
	{ return val; }
	
	public V setValue(V value)
	{ val = value; return val; }
	
	@SuppressWarnings("rawtypes")
	public boolean equals(Object o)
	{ return o != null && (o == this || key == o || ((o instanceof Map.Entry) ? ((Map.Entry)o).getKey().equals(getKey()) : getKey().equals(o))); }
	
	public String toString()
	{ return key.toString(); }
	
	public int hashCode()
	{ return key.hashCode(); }
}