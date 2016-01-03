package latmod.lib;
import java.util.*;

/** Made by LatvianModder */
public class FastList<E> extends ArrayList<E> implements Set<E>
{
	private static final long serialVersionUID = 1L;

	public FastList(int init)
	{ super(init); }
	
	public FastList()
	{ super(); }
	
	@SuppressWarnings("all")
	public FastList(E... o)
	{ this((o == null) ? 10 : o.length); addAll(o); }

	public FastList(Collection<E> c)
	{
		this((c != null && !c.isEmpty()) ? c.size() : 10);
		if(c != null && !c.isEmpty()) addAll(c);
	}
	
	public int hashCode()
	{
		int s = size();
		if(s == 0 || get(0) == null) return 0;
		if(s == 1) return get(0).hashCode();
		int h = 0;
		for(int i = 0; i < s; i++)
			h = h * 31 + LMUtils.hashCodeOf(get(i));
		return h;
	}
	
	public boolean equals(Object o)
	{
		if(o == null) return false;
		else if(o == this) return true;
		else if(o instanceof List)
		{
			List<?> l = (List<?>)o;
			if(l.size() != size()) return false;
			else
			{
				for(int i = 0; i < size(); i++)
					if(!LMUtils.areObjectsEqual(l.get(i), get(i), true))
						return false;
				return true;
			}
		}
		return false;
	}
	
	public void removeAll(int... i)
	{
		if(size() == 0 || i == null || i.length == 0) return;
		for(int j = 0; j < i.length; j++)
			remove(i[j]);
	}
	
	public int indexOf(Object o)
	{
		int s = size();
		if(s == 0) return -1;
		for(int i = 0; i < s; i++)
			if(get(i) == o) return i;
		for(int i = 0; i < s; i++)
			if(get(i) != null && get(i).equals(o)) return i;
		return -1;
	}
	
	public E getObj(Object o)
	{ int i = indexOf(o); return (i == -1) ? null : get(i); }
	
	public boolean remove(Object o)
	{ int index = indexOf(o); return (index >= 0) && remove(index) != null; }
	
	public String[] toStringArray()
	{
		String[] s = new String[size()];
		for(int i = 0; i < s.length; i++)
			s[i] = String.valueOf(get(i));
		return s;
	}
	
	public int[] toHashCodeArray()
	{
		int[] s = new int[size()];
		for(int i = 0; i < s.length; i++)
			s[i] = LMUtils.hashCodeOf(get(i));
		return s;
	}
	
	public FastList<E> clone()
	{
		FastList<E> l = new FastList<>();
		l.cloneFrom(this);
		return l;
	}
	
	public void cloneFrom(Collection<E> l)
	{ clear(); addAll(l); }
	
	public FastList<E> sortToNew(Comparator<? super E> c)
	{
		FastList<E> l = clone();
		Collections.sort(l, c);
		return l;
	}
	
	public String toString()
	{
		int size = size();
		if(size == 0) return "[ ]";
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		sb.append(' ');
		
		for(int i = 0; i < size; i++)
		{
			sb.append(get(i));
			
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

	public boolean containsAny(Collection<?> c)
	{
		for(Object o : c) if(contains(o))
			return true;
		return false;
	}
	
	@SuppressWarnings("all")
	public void addAll(Object[] e)
	{
		if(e != null && e.length > 0)
			addAll(Arrays.asList((E[])e));
	}
	
	public boolean trim(int t)
	{
		if(size() > t)
		{
			while(size() > t) { remove(t); t--; }
			return true;
		}
		
		return false;
	}
	
	public FastList<E> flip()
	{
		int s = size();
		if(s == 0) return this;
		FastList<E> al1 = new FastList<>();
		al1.ensureCapacity(s);
		for(int i = 0; i < s; i++)
			al1.add(get(s - i - 1));
		return al1;
	}
	
	public boolean allObjectsEquals(Object e)
	{
		if(e == null) return (size() > 0) ? allObjectsEquals(get(0)) : false;
		
		for(int i = 0; i < size(); i++)
		{
			if(!LMUtils.areObjectsEqual(e, get(i), true))
				return false;
		}
		
		return true;
	}
	
	public void removeNullValues()
	{
		for(int i = size() - 1; i >= 0; i--)
			if(get(i) == null) remove(i);
	}
	
	public void removeAll(IntList l)
	{
		for(int i = 0; i < l.size(); i++)
			remove(l.get(i));
	}

	public void removeAll(RemoveFilter<E> f)
	{
		if(f == null) clear();
		else
		{
			for(int i = size() - 1; i >= 0; i--)
			{ if(f.remove(get(i))) remove(i); }
		}
	}
}