package latmod.lib;

import com.google.gson.JsonElement;
import latmod.lib.json.IJsonObject;

import java.util.*;

public class IntList implements Iterable<Integer>, Cloneable, IJsonObject
{
	private int defVal = -1;
	private int array[];
	private int size;
	
	public IntList(int i)
	{ array = new int[i]; }
	
	public IntList()
	{ this(0); }
	
	public IntList(int[] ai)
	{
		if(ai != null && ai.length > 0)
		{
			size = ai.length;
			array = new int[size];
			System.arraycopy(ai, 0, array, 0, size);
		}
		else
		{
			size = 0;
			array = new int[0];
		}
	}
	
	public int size()
	{ return size; }
	
	public void clear()
	{
		if(size > 0)
		{
			size = 0;
			array = new int[0];
		}
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
	{ return (index >= 0 && index < size) ? array[index] : defVal; }
	
	public int indexOf(int value)
	{
		for(int i = 0; i < size; i++)
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
	
	public List<Integer> toList()
	{
		ArrayList<Integer> l = new ArrayList<>();
		if(size == 0) return l;
		for(int i = 0; i < size; i++)
			l.add(array[i]);
		return l;
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
		IntList l = new IntList(size);
		System.arraycopy(array, 0, l.array, 0, size);
		l.size = size;
		return l;
	}
	
	public static IntList asList(int... values)
	{
		IntList l = new IntList(values.length);
		l.addAll(values);
		return l;
	}
	
	public void setJson(JsonElement e)
	{
		clear();
		addAll(LMJsonUtils.fromArray(e));
	}
	
	public JsonElement getJson()
	{
		return LMJsonUtils.toArray(array);
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
}