package latmod.lib;
import java.util.*;

/** Made by LatvianModder */
@SuppressWarnings("all")
public class FastList<E> implements Iterable<E>, List<E> //ArrayList
{
	private E[] objects;
	private int initSize;
	private int incr;
	private int size = 0;
	private boolean isLocked = false;
	private boolean weakIndexing = false;
	
	public FastList(int init, int inc)
	{
		initSize = init;
		incr = MathHelperLM.clampInt(inc, 1, 100);
		objects = (E[])new Object[initSize];
	}
	
	public FastList(int init)
	{ this(init, 5); }
	
	public FastList()
	{ this(10); }
	
	public FastList(E... o)
	{ this((o == null) ? 10 : o.length); addAll(o); }
	
	public FastList<E> blankCopy()
	{ return new FastList<E>(initSize, incr); }
	
	public FastList<E> setLocked()
	{ isLocked = true; return this; }
	
	public FastList<E> setWeakIndexing()
	{ if(!isLocked) weakIndexing = true; return this; }
	
	public boolean isLocked()
	{ return isLocked; }
	
	public int hashCode()
	{
		if(size == 0 || objects[0] == null) return 0;
		if(size == 1) return objects[0].hashCode();
		int h = 0;
		for(int i = 0; i < size; i++)
			h = h * 31 + LMUtils.hashCodeOf(objects[i]);
		return h;
	}
	
	public boolean equals(Object o)
	{
		if(o == null) return false;
		else if(o == this) return true;
		else if(o instanceof List)
		{
			List<?> l = (List<?>)o;
			if(l.size() != size) return false;
			else
			{
				for(int i = 0; i < size; i++)
					if(!LMUtils.areObjectsEqual(l.get(i), objects[i], true))
						return false;
				return true;
			}
		}
		return false;
	}
	
	private void expand(int i)
	{
		if(i <= 0) return;
		E[] o = (E[])new Object[objects.length + i];
		System.arraycopy(objects, 0, o, 0, size);
		objects = o;
	}
	
	public boolean add(E e)
	{
		if(isLocked) return false;
		if(size == objects.length) expand(incr);
		objects[size] = e;
		size++;
		return true;
	}
	
	public E set(int i, E e)
	{ if(!isLocked) objects[i] = e; return e; }
	
	public E get(int i)
	{ return (i >= 0 && i < size) ? objects[i] : null; }
	
	public E remove(int i)
	{
		if(size == 0 || i == -1 || isLocked) return null;
		E e0 = get(i);
		size--;
		for(int j = i; j < size; j++)
			objects[j] = objects[j + 1];
		objects[size] = null;
		return e0;
	}
	
	public boolean remove(Object o)
	{ return removeObj(o); }
	
	public boolean removeObj(Object o)
	{
		if(size == 0 || o == null || isLocked) return false;
		int i = indexOf(o);
		if(i != -1) remove(i);
		return i != -1;
	}
	
	public void removeAll(int... i)
	{
		if(size == 0 || i == null || i.length == 0 || isLocked) return;
		for(int j = 0; j < i.length; j++)
			remove(i[j]);
	}
	
	public int indexOf(Object o)
	{
		if(size == 0 || (!weakIndexing && o == null)) return -1;
		for(int i = 0; i < size; i++)
			if(objects[i] == o) return i;
		if(weakIndexing) return -1;
		for(int i = 0; i < size; i++)
			if(objects[i] != null && objects[i].equals(o)) return i;
		return -1;
	}
	
	public E getObj(Object o)
	{ int i = indexOf(o); return (i == -1) ? null : get(i); }
	
	public int size()
	{ return size; }
	
	public void clear()
	{
		if(size == 0 || isLocked) return;
		for(int i = 0; i < size; i++)
			objects[i] = null;
		size = 0;
	}
	
	public Iterator<E> iterator()
	{ return listIterator(); }
	
	public Object[] toArray()
	{
		Object[] e = new Object[size];
		if(size == 0) return e;
		System.arraycopy(objects, 0, e, 0, size);
		return e;
	}
	
	public <E> E[] toArray(E[] a)
	{
		if(size == 0) return (E[])new Object[0];
		else if(a != null && a.length == size)
		{
			System.arraycopy(objects, 0, a, 0, size);
			return a;
		}
		
		return (E[]) Arrays.copyOf(objects, size, a.getClass());
	}
	
	public String[] toStringArray()
	{
		String[] s = new String[size];
		for(int i = 0; i < size; i++)
			s[i] = String.valueOf(objects[i]);
		return s;
	}
	
	public boolean removeAll(Collection<?> list)
	{ if(list != null && size > 0 && !isLocked) for(Object e : list) remove(e); return true; }
	
	public boolean contains(Object e)
	{ return indexOf(e) != -1; }
	
	public FastList<E> clone()
	{
		FastList<E> l = blankCopy();
		l.cloneFrom(this);
		return l;
	}
	
	public void cloneFrom(FastList<E> l)
	{
		size = l.size;
		if(objects.length < size) objects = (E[])new Object[size];
		System.arraycopy(l.objects, 0, objects, 0, size);
	}
	
	public void sort(Comparator<? super E> c)
	{
		if(isLocked || size < 2) return;
		if(c == null) Arrays.sort(objects, 0, size);
		else Arrays.sort(objects, 0, size, c);
	}
	
	public FastList<E> sortToNew(Comparator<? super E> c)
	{
		FastList<E> l = clone();
		l.sort(c);
		return l;
	}
	
	public String toString()
	{
		if(size == 0) return "[ ]";
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		sb.append(' ');
		
		for(int i = 0; i < size; i++)
		{
			sb.append(String.valueOf(objects[i]));
			
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
	
	private class FastListIterator implements ListIterator<E>
	{
		private int pos = 0;
		
		public boolean hasNext()
		{ return pos < size; }
		
		public E next()
		{ E e = get(pos); pos++; return e; }
		
		public void remove()
		{ FastList.this.remove(pos); }
		
		public boolean hasPrevious()
		{ return pos > 0; }
		
		public E previous()
		{ E e = get(pos - 1); pos--; return e; }
		
		public int nextIndex()
		{ return pos + 1; }
		
		public int previousIndex()
		{ return pos - 1; }
		
		public void set(E e)
		{ FastList.this.set(pos, e); }
		
		public void add(E e)
		{ FastList.this.add(e); }
	}
	
	public boolean isEmpty()
	{ return size <= 0; }
	
	public boolean containsAll(Collection<?> c)
	{ for(Object o : c) if(!contains(o))
	return false; return true; }
	
	public boolean containsAny(Collection<?> c)
	{ for(Object o : c) if(contains(o))
	return true; return false; }
	
	public boolean addAll(Collection<? extends E> l)
	{
		if(l != null && l.size() > 0 && !isLocked)
		{
			if(l instanceof FastList)
			{
				FastList list = (FastList)l;
				expand(Math.max(incr, list.size));
				System.arraycopy(list.objects, 0, objects, size, list.size);
				size += list.size;
			}
			else
			{
				Iterator<? extends E> itr = l.iterator();
				while(itr.hasNext()) add(itr.next());
			}
			
			return true;
		}
		
		return false;
	}
	
	public void addAll(Object[] e)
	{
		if(e != null && e.length > 0 && !isLocked)
		{
			int s = e.length;
			expand(Math.max(incr, s));
			System.arraycopy(e, 0, objects, size, s);
			size += s;
		}
	}
	
	public boolean addAll(int index, Collection<? extends E> c)
	{ throw new UnsupportedOperationException("addAllWithIndex"); }
	
	public boolean retainAll(Collection<?> c)
	{ throw new UnsupportedOperationException("retainAll"); }
	
	public void add(int i, E e)
	{ add(e); }
	
	public int lastIndexOf(Object o)
	{ return -1; }
	
	public ListIterator<E> listIterator()
	{ return new FastListIterator(); }
	
	public ListIterator<E> listIterator(int i)
	{ FastListIterator it = new FastListIterator();
	it.pos = i; return it; }
	
	public FastList<E> subList(int fromIndex, int toIndex)
	{
		if(fromIndex < 0 || toIndex < 0 || fromIndex > size || toIndex > size) return null;
		FastList<E> al = blankCopy();
		if(fromIndex == 0 && toIndex == size)
		{ al.addAll(this); return al; }
		for(int i = fromIndex; i < toIndex; i++)
			al.add(objects[i]);
		//System.arraycopy(objects, fromIndex, al.objects, 0, toIndex - fromIndex);
		return al;
	}
	
	public boolean trim(int t)
	{
		if(size > t && !isLocked)
		{
			while(size > t) { remove(t); t--; }
			return true;
		}

		return false;
	}
	
	public FastList<E> flip()
	{
		FastList<E> al1 = blankCopy();
		if(size == 0) return al1;
		for(int i = size - 1; i >= 0; i--)
		al1.add(get(i)); return al1;
	}
	
	public boolean allObjectsEquals(E e)
	{
		if(e == null) return (size > 0) ? allObjectsEquals(get(0)) : false;
		
		for(int i = 0; i < size; i++)
		{
			if(objects[i] != null && !objects[i].equals(e))
				return false;
		}
		
		return true;
	}
	
	public static <T> FastList<T> asList(Collection<T> c)
	{ FastList<T> l = new FastList<T>(); l.addAll(c); return l; }
	
	public void removeNullValues()
	{
		if(isLocked) return;
		E[] e0 = (E[])objects.clone();
		int size0 = size;
		
		clear();
		
		for(int i = 0; i < size0; i++)
			if(e0[i] != null) add(e0[i]);
	}
	
	public void removeAll(IntList l)
	{
		if(isLocked) return;
		for(int i = 0; i < l.size(); i++)
			remove(l.get(i));
	}
}