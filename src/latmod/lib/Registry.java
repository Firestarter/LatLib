package latmod.lib;

public class Registry<E>
{
	public class Entry implements Comparable<Entry>
	{
		public final String key;
		public final E value;
		public int ID;
		
		public Entry(String k, E v)
		{ key = k; value = v; }
		
		public String toString()
		{ return key; }
		
		public boolean equals(Object o)
		{ return o != null && (o == this || o == key || key.equals(o.toString())); }
		
		public int hashCode()
		{ return key.hashCode(); }
		
		public int compareTo(Entry o)
		{ return key.compareToIgnoreCase(o.key); }
	}
	
	public final boolean shortKeys;
	public final FastList<Entry> entries;
	
	public Registry(boolean b)
	{
		shortKeys = b;
		entries = new FastList<Entry>();
	}
	
	public void sort()
	{ entries.sort(null); }
	
	public String[] getKeys()
	{ return entries.toStringArray(); }
	
	public FastList<E> getValues(boolean allowMultipleValues)
	{
		FastList<E> values = new FastList<E>();
		
		for(int i = 0; i < entries.size(); i++)
		{
			if(allowMultipleValues) values.add(entries.get(i).value);
			else
			{
				E val = entries.get(i).value;
				if(!values.contains(val)) values.add(val);
			}
		}
		
		return values;
	}
	
	public E register(String key, E value)
	{
		Entry e = new Entry(key, value);
		int index = entries.indexOf(key);
		if(index == -1) entries.add(e);
		else entries.set(index, e);
		return value;
	}
	
	public Entry get(String key)
	{ return entries.getObj(key); }
	
	public Entry getFromValue(E value)
	{
		for(int i = 0; i < entries.size(); i++)
		{
			Entry e = entries.get(i);
			if(e.value == value) return e;
		}
		
		for(int i = 0; i < entries.size(); i++)
		{
			Entry e = entries.get(i);
			if(e.value.equals(value)) return e;
		}
		
		return null;
	}
	
	public Entry getFromID(int ID)
	{
		if(ID <= 0) return null;
		
		for(int i = 0; i < entries.size(); i++)
		{
			Entry e = entries.get(i);
			if(e.ID == ID) return e;
		}
		
		return null;
	}
	
	public int writeToIO(ByteIOStream io)
	{
		int s = entries.size();
		
		io.writeBoolean(shortKeys);
		if(shortKeys) io.writeShort(s);
		else io.writeInt(s);
		
		for(int i = 0; i < s; i++)
		{
			Entry e = entries.get(i);
			io.writeUTF(e.key);
			if(shortKeys) io.writeShort(e.ID);
			else io.writeInt(e.ID);
		}
		
		return s;
	}
	
	public int readFromIO(ByteIOStream io)
	{
		int r = 0;
		
		boolean sk = io.readBoolean();
		int s = sk ? io.readUnsignedShort() : io.readInt();
		
		for(int i = 0; i < s; i++)
		{
			String key = io.readUTF();
			int id = sk ? io.readUnsignedShort() : io.readInt();
			
			Entry e = get(key);
			if(e != null)
			{
				e.ID = id;
				r++;
			}
		}
		
		return r;
	}
}