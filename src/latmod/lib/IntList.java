package latmod.lib;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.*;

public class IntList implements Iterable<Integer>, Cloneable // Improve this // FastList
{
	private final int init;
	private int defVal = -1;
	private int array[];
	private int size;
	
	public IntList(int i)
	{
		init = i;
		array = new int[init];
	}
	
	public IntList()
	{ this(5); }
	
	public IntList(int[] ai)
	{
		init = ai.length;
		size = ai.length;
		array = ai.clone();
	}
	
	public int size()
	{ return size; }
	
	public void clear()
	{
		size = 0;
		array = new int[init];
	}
	
	public void setDefVal(int value)
	{ defVal = value; }
	
	public void expand(int s)
	{
		if(size + s > array.length)
		{
			int ai[] = new int[size + Math.max(s, 10)];
			System.arraycopy(array, 0, ai, 0, size);
			array = ai;
		}
	}
	
	public void add(int value)
	{
		expand(1);
		array[size] = value;
		size++;
	}
	
	public void addAll(int... values)
	{
		if(values != null && values.length > 0)
		{
			expand(values.length);
			System.arraycopy(values, 0, array, size, values.length);
			size += values.length;
		}
	}
	
	public void addAll(IntList l)
	{
		if(l != null && l.size > 0)
		{
			expand(l.size);
			System.arraycopy(l.array, 0, array, size, l.size);
			size += l.size;
		}
	}
	
	public int get(int index)
	{ return (index >= 0 && index < size()) ? array[index] : defVal; }
	
	public int indexOf(int value)
	{
		for(int i = 0; i < size(); i++)
			if(array[i] == value) return i;
		return -1;
	}
	
	public boolean contains(int value)
	{ return indexOf(value) != -1; }
	
	public int removeKey(int key)
	{
		if(key < 0 || key >= size) return defVal;
		int rem = get(key);
		size--;
		for(int j = key; j < size; j++)
			array[j] = array[j + 1];
		return rem;
	}
	
	public int removeValue(int value)
	{ return removeKey(indexOf(value)); }
	
	public void set(int i, int value)
	{ array[i] = value; }
	
	public boolean isEmpty()
	{ return size <= 0; }
	
	public int[] toArray()
	{ return toArray(null); }
	
	public int[] toArray(int[] a)
	{
		if(a == null || a.length != size) a = new int[size];
		if(size > 0) System.arraycopy(array, 0, a, 0, size);
		return a;
	}
	
	public void sort()
	{ Arrays.sort(array, 0, size); }
	
	public int[] toSortedArray()
	{
		int[] a = toArray();
		Arrays.sort(a);
		return a;
	}
	
	public int hashCode()
	{
		int h = 0;
		for(int i = 0; i < size; i++)
			h = h * 31 + array[i];
		return h;
	}
	
	public String toString()
	{
		if(size == 0) return "[ ]";
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		sb.append(' ');
		
		for(int i = 0; i < size; i++)
		{
			sb.append(array[i]);
			
			if(i != size - 1)
			{
				sb.append(',');
				sb.append(' ');
			}
		}
		
		sb.append(' ');
		sb.append(']');
		return sb.toString();
	}
	
	public Iterator<Integer> iterator()
	{ return new IntIterator(array); }
	
	public IntList clone()
	{
		IntList l = new IntList(init);
		l.array = array.clone();
		l.size = size;
		return l;
	}
	
	public static IntList asList(int... values)
	{
		IntList l = new IntList(values.length);
		l.addAll(values);
		return l;
	}
	
	public static class IntIterator implements Iterator<Integer>
	{
		public final int[] values;
		public int pos = -1;
		
		public IntIterator(int[] v)
		{ values = v; }
		
		public boolean hasNext()
		{ return pos < values.length; }
		
		public Integer next()
		{ return Integer.valueOf(values[++pos]); }
	}
	
	public static class Serializer implements JsonDeserializer<IntList>, JsonSerializer<IntList>
	{
		public JsonElement serialize(IntList src, Type typeOfSrc, JsonSerializationContext context)
		{
			JsonArray o = new JsonArray();
			for(int i = 0; i < src.size; i++)
				o.add(new JsonPrimitive(src.array[i]));
			return o;
		}
		
		public IntList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			if(json.isJsonNull()) return null;
			
			IntList list = new IntList();
			JsonArray o = json.getAsJsonArray();
			for(int i = 0; i < o.size(); i++)
				list.add(o.get(i).getAsInt());
			return list;
		}
	}
}