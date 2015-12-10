package latmod.lib;
import java.util.*;

/** Made by LatvianModder */
@SuppressWarnings("all")
public class FastList<E> extends ArrayList<E>
{
	private boolean isLocked = false;
	private boolean weakIndexing = false;
	
	public FastList(int init)
	{ super(init); }
	
	public FastList()
	{ this(10); }
	
	public FastList(E... o)
	{ this((o == null) ? 10 : o.length); addAll(o); }
	
	public FastList<E> setLocked()
	{ isLocked = true; return this; }
	
	public FastList<E> setWeakIndexing()
	{ if(!isLocked) weakIndexing = true; return this; }
	
	public boolean isLocked()
	{ return isLocked; }
	
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
	
	public boolean add(E e)
	{
		if(isLocked) return false;
		return super.add(e);
	}
	
	public E set(int i, E e)
	{ return isLocked ? e : super.set(i, e); }
	
	public E remove(int i)
	{
		if(size() == 0 || i < 0 || isLocked) return null;
		return super.remove(i);
	}
	
	public boolean remove(Object o)
	{ return (size() == 0 || o == null || isLocked) ? false : super.remove(o); }
	
	public void removeAll(int... i)
	{
		if(size() == 0 || i == null || i.length == 0 || isLocked) return;
		for(int j = 0; j < i.length; j++)
			remove(i[j]);
	}
	
	public int indexOf(Object o)
	{
		if(size() == 0 || (!weakIndexing && o == null)) return -1;
		for(int i = 0; i < size(); i++)
			if(get(i) == o) return i;
		if(weakIndexing) return -1;
		for(int i = 0; i < size(); i++)
			if(get(i) != null && get(i).equals(o)) return i;
		return -1;
	}
	
	public E getObj(Object o)
	{ int i = indexOf(o); return (i == -1) ? null : get(i); }
	
	public void clear()
	{
		if(size() == 0 || isLocked) return;
		super.clear();
	}
	
	public String[] toStringArray()
	{
		String[] s = new String[size()];
		for(int i = 0; i < s.length; i++)
			s[i] = String.valueOf(get(i));
		return s;
	}
	
	public boolean removeAll(Collection<?> list)
	{ return (list != null && size() > 0 && !isLocked) ? super.removeAll(list) : false; }
	
	
	public FastList<E> clone()
	{
		FastList<E> l = new FastList<E>();
		l.cloneFrom(this);
		if(isLocked) l.setLocked();
		return l;
	}
	
	public void cloneFrom(FastList<E> l)
	{ clear(); addAll(l); }
	
	public void sort(Comparator<? super E> c)
	{
		if(isLocked || size() < 2) return;
		super.sort(c);
	}
	
	public FastList<E> sortToNew(Comparator<? super E> c)
	{
		FastList<E> l = clone();
		l.sort(c);
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
			sb.append(String.valueOf(get(i)));
			
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
	{ for(Object o : c) if(contains(o))
	return true; return false; }
	
	public void addAll(Object[] e)
	{
		if(e != null && e.length > 0 && !isLocked)
			addAll(Arrays.asList((E[])e));
	}
	
	public boolean addAll(int index, Collection<? extends E> c)
	{ throw new UnsupportedOperationException("addAllWithIndex"); }
	
	public boolean retainAll(Collection<?> c)
	{ throw new UnsupportedOperationException("retainAll"); }
	
	public void add(int i, E e)
	{ if(!isLocked) super.add(i, e); }
	
	public boolean trim(int t)
	{
		if(size() > t && !isLocked)
		{
			while(size() > t) { remove(t); t--; }
			return true;
		}

		return false;
	}
	
	public FastList<E> flip()
	{
		FastList<E> al1 = new FastList<E>();
		if(size() == 0) return al1;
		for(int i = size() - 1; i >= 0; i--)
		al1.add(get(i)); return al1;
	}
	
	public boolean allObjectsEquals(E e)
	{
		if(e == null) return (size() > 0) ? allObjectsEquals(get(0)) : false;
		
		for(int i = 0; i < size(); i++)
		{
			if(!LMUtils.areObjectsEqual(e, get(i), true))
				return false;
		}
		
		return true;
	}
	
	public static <T> FastList<T> asList(Collection<T> c)
	{ FastList<T> l = new FastList<T>(); l.addAll(c); return l; }
	
	public void removeNullValues()
	{
		if(isLocked) return;
		for(int i = size() - 1; i >= 0; i--)
			if(get(i) == null) remove(i);
	}
	
	public void removeAll(IntList l)
	{
		if(isLocked) return;
		for(int i = 0; i < l.size(); i++)
			remove(l.get(i));
	}
}